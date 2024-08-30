package com.hokhanh.web.notification.successfulPaymentNotification;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SuccessfulPaymentNotificationRepository extends MongoRepository<SuccessfulPaymentNotification, String> {

}
