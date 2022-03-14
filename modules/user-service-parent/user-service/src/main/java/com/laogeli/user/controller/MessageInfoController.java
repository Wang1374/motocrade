package com.laogeli.user.controller;

import com.github.pagehelper.PageInfo;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.model.ResponseBean;
import com.laogeli.common.core.utils.PageUtil;
import com.laogeli.user.api.module.MessageInfo;
import com.laogeli.user.mapper.MessageInfoMapper;
import com.laogeli.user.service.MessageInfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 消息信息管理
 *
 * @author wang
 * @Date 2021-02-02 9:37
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/v1/message")
public class MessageInfoController {


    private MessageInfoMapper messageInfoMapper;

    private MessageInfoService messageInfoService;

    /**
     * 新增消息
     *
     * @param messageInfo
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加消息")
    @ApiImplicitParam(name = "messageInfo", value = "消息实体", required = true, dataType = "MessageInfo")
    public ResponseBean<Boolean> insertMessage(@RequestBody MessageInfo messageInfo) {
        return new ResponseBean<>(messageInfoMapper.insert(messageInfo) > 0);
    }


    /**
     * 根据公司id查询消息列表
     *
     * @param corporateIdentify
     * @return
     */
    @GetMapping("getMessage")
    @ApiOperation(value = "根据公司id查询消息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "corporateIdentify", value = "公司id", required = true, dataType = "String")
    })
    public PageInfo<MessageInfo> getMessage(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                            @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                            @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                            @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                            @RequestParam(value = "corporateIdentify", required = false) String corporateIdentify) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setBelongCompaniesId(corporateIdentify);
        return messageInfoService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), messageInfo);
    }

    /**
     * 标记为已读
     *
     * @param messageIds
     * @return
     */
    @PutMapping("updateStatus")
    @ApiOperation(value = "标记为已读")
    @ApiImplicitParam(name = "messageIds", value = "ids", required = true, dataType = "String")
    public ResponseBean<Boolean> updateReadStatus(@RequestParam String messageIds) {
        String[] ids = messageIds.split(",");
        return new ResponseBean<>(messageInfoMapper.updateReadStatus(ids) > 0);
    }

    /**
     * 标记为全部已读
     *
     * @param belongCompaniesId
     * @return
     */
    @PutMapping("updateAllRead/{belongCompaniesId}")
    @ApiOperation(value = "标记为全部已读")
    @ApiImplicitParam(name = "belongCompaniesId", value = "公司id", required = true, dataType = "String")
    public ResponseBean<Boolean> updateAllRead(@PathVariable String belongCompaniesId) {
        return new ResponseBean<>(messageInfoMapper.updateAllRead(belongCompaniesId) > 0);
    }

    /**
     * 根据id删除消息
     *
     * @param messageIds
     * @return
     */
    @PutMapping("deleteMessage")
    @ApiOperation(value = "根据id删除消息")
    @ApiImplicitParam(name = "messageIds", value = "ids", required = true, dataType = "String")
    public ResponseBean<Boolean> deleteMessageByIds(@RequestParam String messageIds) {
        String[] ids = messageIds.split(",");
        return new ResponseBean<>(messageInfoMapper.deleteMessageByIds(ids) > 0);
    }


    /**
     * 根据belongCompaniesId查询未读消息数量
     *
     * @param belongCompaniesId
     * @return
     */
    @GetMapping("getMessageNotRead/{belongCompaniesId}")
    @ApiOperation(value = "根据id删除消息")
    @ApiImplicitParam(name = "belongCompaniesId", value = "belongCompaniesId", required = true, dataType = "String")
    public ResponseBean<Integer> getMessageNotRead(@PathVariable("belongCompaniesId") String belongCompaniesId) {

        return new ResponseBean<>(messageInfoMapper.getMessageNotRead(belongCompaniesId));
    }
}
