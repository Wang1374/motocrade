package com.laogeli.user.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.user.api.module.Attachment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 附件mapper
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Mapper
public interface AttachmentMapper extends CrudMapper<Attachment> {

    /**
     * 根据附件id数组查询预览url
     *
     * @param attachment       附件
     * @return List
     */
    List<Map<String, Object>>  findListStringById(Attachment attachment);
}
