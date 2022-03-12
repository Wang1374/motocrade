package com.laogeli.auth.service;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.laogeli.auth.api.module.WxSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WxSessionService {

    @Autowired
    private WxMaService wxMaService;

    /**
     * @Desecription: 根据code获取WxSession
     * @Param: code
     * @Param: WxSession
     * @Return: WxSession
     * @Author: yangyu
     * @Date: 2021/8/25 22:13
     */
    public WxSession getSession(String code) {
        WxSession session = null;
        try {
            WxMaJscode2SessionResult result = wxMaService.getUserService().getSessionInfo(code);
            session = new WxSession(result.getOpenid(), result.getSessionKey());
            log.info("获取wx session成功，openId: {}, sessionKey: {}", session.getOpenId(), session.getSessionKey());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return session;
    }

    /**
     * @Desecription: 根据code获取WxSession
     * @Param: code
     * @Param: WxSession
     * @Return: WxSession
     * @Return:
     * @Author: yangyu
     * @Date: 2021/8/25 22:14
     */
    public WxSession code2Session(String code) {
        WxSession session = null;
        try {
            WxMaJscode2SessionResult result = wxMaService.jsCode2SessionInfo(code);
            session = new WxSession(result.getOpenid(), result.getSessionKey());
            log.info("获取wx session成功，openId: {}, sessionKey: {}", session.getOpenId(), session.getSessionKey());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return session;
    }
}
