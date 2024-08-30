package com.hokhanh.web.notification;

import java.util.List;

public record ListNotificationResponse(
	List<NotificationResponse> notifications,
	long total,
	long unReadTotal
) {

}
