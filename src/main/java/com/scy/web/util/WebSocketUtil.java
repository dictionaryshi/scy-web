package com.scy.web.util;

import com.scy.core.format.MessageUtil;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;

/**
 * WebSocketUtil
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/20.
 */
@Slf4j
public class WebSocketUtil {

    public static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Throwable throwable) {
            log.error(MessageUtil.format("sendMessage error", throwable, "message", message));
        }
    }
}
