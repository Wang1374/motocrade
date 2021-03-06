package com.laogeli.auth.config;

import com.laogeli.auth.security.CustomTokenConverter;
import com.laogeli.auth.security.CustomTokenServices;
import com.laogeli.auth.security.CustomUserDetailsByNameServiceWrapper;
import com.laogeli.common.security.core.ClientDetailsServiceImpl;
import com.laogeli.common.security.core.CustomUserDetailsService;
import com.laogeli.common.security.exceptions.CustomOauthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * ?????????????????????
 *
 * @author wang
 * @date 2019-12-31
 */
@Configuration
public class CustomAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    /**
     * ???????????????
     */
    private final AuthenticationManager authenticationManager;

    /**
     * redis????????????
     */
    private final RedisConnectionFactory redisConnectionFactory;

    /**
     * ?????????
     */
    private final DataSource dataSource;

    /**
     * key????????????
     */
    private final KeyProperties keyProperties;

    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public CustomAuthorizationServerConfigurer(AuthenticationManager authenticationManager,
                                               RedisConnectionFactory redisConnectionFactory,
                                               DataSource dataSource,
                                               KeyProperties keyProperties,
                                               CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.redisConnectionFactory = redisConnectionFactory;
        this.dataSource = dataSource;
        this.keyProperties = keyProperties;
        this.userDetailsService = userDetailsService;
    }

    /**
     * ???token?????????redis
     *
     * @return TokenStore
     */
    @Bean
    public TokenStore tokenStore() {
         return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * ????????????token??????????????????loadUserByUsername??????????????????tokenService
     * @param endpoints endpoints
     * @return CustomTokenServices
     */
    private CustomTokenServices tokenService(AuthorizationServerEndpointsConfigurer endpoints) {
        CustomTokenServices tokenServices = new CustomTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(true);
        tokenServices.setAccessTokenValiditySeconds(-1);
        tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        tokenServices.setTokenEnhancer(jwtTokenEnhancer());
        addUserDetailsService(tokenServices, userDetailsService);
        return tokenServices;
    }

    /**
     * PreAuthenticatedAuthenticationProvider?????????userDetailsService
     * @param tokenServices tokenServices
     * @param userDetailsService userDetailsService
     */
    private void addUserDetailsService(CustomTokenServices tokenServices, CustomUserDetailsService userDetailsService) {
        if (userDetailsService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new CustomUserDetailsByNameServiceWrapper<>(userDetailsService));
            tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
        }
    }

    /**
     * token??????????????????????????????????????????Token????????????
     *  JwtAccessTokenConverter ???Jwt?????????????????????????????????
     * @return JwtAccessTokenConverter
     */
    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(keyProperties.getKeyStore().getLocation(), keyProperties.getKeyStore().getPassword().toCharArray());
        CustomTokenConverter converter = new CustomTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(keyProperties.getKeyStore().getAlias()));
        return converter;
    }

    /**
     * ??????????????????JdbcClientDetailsService?????????????????????
     *
     * @return ClientDetailsService
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new ClientDetailsServiceImpl(dataSource);
    }

    /**
     * ?????????????????????????????????
     *
     * @param clients clients
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    /**
     * ??????TokenStore???Token??????????????????????????????????????????
     *
     * @param endpoints endpoints
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                // ???token?????????redis
                .tokenStore(tokenStore())
                // ?????????token
                .tokenServices(tokenService(endpoints))
                // token??????
                .tokenEnhancer(jwtTokenEnhancer())
                // ???????????????
                .authenticationManager(authenticationManager)
                // ????????????
                .exceptionTranslator(e -> {
                    if (e instanceof OAuth2Exception) {
                        OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
                        return ResponseEntity
                                .status(oAuth2Exception.getHttpErrorCode())
                                .body(new CustomOauthException(oAuth2Exception.getMessage(), oAuth2Exception.getHttpErrorCode()));
                    } else {
                        throw e;
                    }
                });
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param oauthServer oauthServer
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .passwordEncoder(new BCryptPasswordEncoder())
                // ??????/oauth/token_key???????????????????????????
                .tokenKeyAccess("permitAll()")
                // ??????/oauth/check_token??????????????????????????????
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }
}

