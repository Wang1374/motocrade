package com.laogeli.user.api.module;

import com.laogeli.common.core.persistence.BaseEntity;
import com.laogeli.user.api.constant.AttachmentConstant;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 附件信息
 *
 * @author wang
 * @date 2019-12-31
 */
@Data
public class Attachment extends BaseEntity<Attachment> {

    /**
     * 附件名称
     */
    @NotBlank(message = "附件名称不能为空")
    private String attachName;

    /**
     * 附件大小
     */
    @NotBlank(message = "附件大小不能为空")
    private String attachSize;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 文件ID
     */
    private String fastFileId;

    /**
     * 业务流水号
     */
    @NotBlank(message = "附件业务流水号不能为空")
    private String busiId;

    /**
     * 业务类型
     */
    private String busiType = AttachmentConstant.BUSI_TYPE_NORMAL_ATTACHMENT;

    /**
     * 业务模块
     */
    private String busiModule;

    /**
     * 预览地址
     */
    private String previewUrl;
}
