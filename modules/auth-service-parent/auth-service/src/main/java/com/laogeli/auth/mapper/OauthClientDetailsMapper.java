package com.laogeli.auth.mapper;

import com.laogeli.auth.api.module.OauthClientDetails;
import com.laogeli.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Oauth2客户端mapper
 *
 * @author wang
 * @date 2019-12-31
 */
@Mapper
public interface OauthClientDetailsMapper extends CrudMapper<OauthClientDetails> {
}
