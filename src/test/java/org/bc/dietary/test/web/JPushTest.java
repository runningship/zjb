package org.bc.dietary.test.web;

import java.io.IOException;

import org.junit.Test;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

public class JPushTest {
	private static final String masterSecret ="2b575a1dd863f6890d30b25e";
	private static final String appKey ="bfb0717da1ccf51bdb21a811";
	@Test
	public void testPush() throws IOException{
		JPushClient mClient = new JPushClient(masterSecret, appKey);
		PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias("15856985122"))
                //.setNotification(Notification.alert("服务端的通知"))
                .setMessage(Message.content("服务端的消息"))
                .build();
        mClient.sendPush(payload);
	}
}
