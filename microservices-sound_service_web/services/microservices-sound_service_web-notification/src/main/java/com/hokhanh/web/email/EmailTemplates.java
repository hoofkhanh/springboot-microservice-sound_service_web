package com.hokhanh.web.email;

import lombok.Getter;

public enum EmailTemplates {

	TEMPORARY_PURCHASED_BEAT_CONFIRMATION("temporary-purchased-beat-confirmation", "Purchased Beat Confirmation"),
	
	SUCCESSFUL_PAYMENT_NOTIFICATION("successful-payment-notification", "Successful Payment Notification"),
	
	HIRE_CONFIRMATION("hire-confirmation", "Hire Confirmation");
	
	@Getter
	private final String template;
	
	@Getter
	private final String subject;
	
	private EmailTemplates(String template, String subject) {
		this.template = template;
		this.subject = subject;
	}
}
