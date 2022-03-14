package com.laogeli.auth.service;

import com.laogeli.auth.api.module.OauthClientDetails;
import com.laogeli.auth.mapper.OauthClientDetailsMapper;
import com.laogeli.common.core.service.CrudService;
import org.springframework.stereotype.Service;

/**
 * Oauth2客户端Service
 *
 * @author wang
 * @date 2019-12-31
 */
@Service
public class OauthClientDetailsService extends CrudService<OauthClientDetailsMapper, OauthClientDetails> {
}
