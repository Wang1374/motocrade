package com.laogeli.user.controller;

import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.Producer;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.enums.LoginType;
import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.service.RedisService;
import com.laogeli.common.core.web.BaseController;
import com.laogeli.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.awt.image.BufferedImage;

/**
 * 验证码
 *
 * @author wang
 * @date 2019-12-31
 */
@AllArgsConstructor
@Api("生成验证码")
@RestController
@RequestMapping(value = "/v1/code")
public class ValidateCodeController extends BaseController {

    private final Producer producer;

    private final UserService userService;

    private final RedisTemplate redisTemplate;

    private final RedisService redisService;

    /**
     * 生成验证码
     *
     * @param random random
     */
    @ApiOperation(value = "生成验证码", notes = "生成验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "random", value = "随机数", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/{random}")
    public void produceCode(@PathVariable String random, HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        // 生成文字验证码
        String text = producer.createText();
        // 生成图片验证码
        BufferedImage image = producer.createImage(text);
        userService.saveImageCode(random, text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "JPEG", out);
        IOUtils.closeQuietly(out);
    }

    /**
     * 通过随机数获取验证码
     *
     * @param randomStr
     */
    @ApiOperation(value = "获取验证码", notes = "获取验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "random", value = "随机数", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, dataType = "String", paramType = "path")
    })
    @PostMapping("checkGraphCode")
    public ResponseBean<Boolean> checkGraphCode(@RequestParam @NotBlank String randomStr, @RequestParam @NotBlank String code) {
        String key = CommonConstant.DEFAULT_CODE_KEY + LoginType.PWD.getType() + "@" + randomStr;
        if (!redisTemplate.hasKey(key))
            throw new CommonException("图形验证码已过期，请重新获取");
        Object codeObj = redisTemplate.opsForValue().get(key);
        if (codeObj == null)
            throw new CommonException("图形验证码已过期，请重新获取");
        String saveCode = codeObj.toString();
        if (StrUtil.isBlank(saveCode)) {
            redisService.remove(key);
            throw new CommonException("图形验证码已过期，请重新获取");
        }
        if (!StrUtil.equalsIgnoreCase(saveCode, code)) {
            throw new CommonException("图形验证码错误");
        }
        return new ResponseBean<>(true);
    }
}
