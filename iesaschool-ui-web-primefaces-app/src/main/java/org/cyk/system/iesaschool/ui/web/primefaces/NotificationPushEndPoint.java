package org.cyk.system.iesaschool.ui.web.primefaces;

import java.io.Serializable;

import lombok.Getter;

import org.cyk.ui.api.UIManager;
import org.cyk.ui.web.primefaces.push.AbstractNotificationPushEndPoint;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;

@PushEndpoint("/{"+UIManager.PUSH_CHANNEL_VAR+"}/{"+UIManager.PUSH_RECEIVER_VAR+"}") @Singleton
public class NotificationPushEndPoint extends AbstractNotificationPushEndPoint implements Serializable {
    
	private static final long serialVersionUID = 3545052196460272570L;
	
	@Getter @PathParam(UIManager.PUSH_CHANNEL_VAR) protected String channel;
	@Getter @PathParam(UIManager.PUSH_RECEIVER_VAR) protected String receiver;
	
}