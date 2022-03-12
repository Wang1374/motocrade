package com.laogeli.user.controller;

import com.laogeli.common.core.exceptions.CommonException;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.user.api.module.Company;
import com.laogeli.user.api.vo.MotorcadeVo;
import com.laogeli.user.mapper.CompanyMapper;
import com.laogeli.user.service.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.*;
/**
 * 企业信息管理
 *
 * @author yangyu
 * @date 2020-06-24
 */
@Slf4j
@AllArgsConstructor
@Api("企业信息管理")
@RestController
@RequestMapping(value = "/v1/company")
public class CompanyController {

    private final CompanyService companyService;

    private final CompanyMapper companyMapper;

    /**
     * 根据企业标识id获取
     *
     * @param id id
     * @return ResponseBean
     */
    @GetMapping("getCompanyById/{id}")
    @ApiOperation(value = "获取企业信息", notes = "根据企业标识id获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "企业标识id", required = true, dataType = "String", paramType = "path")
    public ResponseBean<Company> getUserById(@PathVariable String id) {
        Company company = new Company();
        company.setId(id);
        return new ResponseBean<>(companyService.get(company));
    }

    /**
     * 更新企业信息
     *
     * @param company company
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新企业信息", notes = "根据企业id更新企业信息")
    @ApiImplicitParam(name = "company", value = "企业信息实体Company", required = true, dataType = "Company")
    @Log(value = "更新企业信息", type = 0)
    public ResponseBean<Boolean> updateCompany(@RequestBody Company company) {
        try {
            company.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            return new ResponseBean<>(companyService.update(company) > 0);
        } catch (Exception e) {
            log.error("更新企业信息失败！", e);
            throw new CommonException("更新企业信息失败！");
        }
    }

    /**
     * 查询所有车队
     * @return
     */
    @GetMapping("getMotorcade")
    public List<MotorcadeVo> getMotorcade(){
        return companyMapper.getMotorcade();
    }
}
