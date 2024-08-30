package com.hokhanh.web.notification;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hokhanh.web.artist.ArtistClient;
import com.hokhanh.web.beat.BeatClient;
import com.hokhanh.web.customer.CustomerClient;
import com.hokhanh.web.exception.NotificationException;
import com.hokhanh.web.jobType.JobTypeClient;
import com.hokhanh.web.kafka.hire.HireConfirmation;
import com.hokhanh.web.kafka.payment.SuccessfulPaymentNotification;
import com.hokhanh.web.kafka.purchasedBeat.TemporaryPurchasedBeatConfirmation;
import com.hokhanh.web.kafka.purchasedBeat.user.Role;
import com.hokhanh.web.notification.hireConfirmation.HireConfirmationRepository;
import com.hokhanh.web.notification.successfulPaymentNotification.SuccessfulPaymentNotificationRepository;
import com.hokhanh.web.notification.temporaryPurchasedBeatConfirmation.TemporaryPurchasedBeatConfirmationRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotificationService {

	private final NotificationRepository repo;
	private final TemporaryPurchasedBeatConfirmationRepository temporaryPurchasedBeatConfirmationRepository;
	private final SuccessfulPaymentNotificationRepository successfulPaymentNotificationRepository;
	private final HireConfirmationRepository hireConfirmationRepository;
	
	private final ArtistClient artistClient;
	private final BeatClient beatClient;
	private final CustomerClient customerClient;
	private final JobTypeClient jobTypeClient;

	public List<NotificationResponse> findNotificationByArtistIdOrCusgtomerId(String id, Role role, int page) {
		return repo.findByReceiverIdRole(id + " " + role, PageRequest.of(page, 3, Sort.by("notificationTime").descending()))
				.stream()
				.map(notification -> {
					TemporaryPurchasedBeatConfirmation realBeatConfirmation = null;
					HireConfirmation realHireConfirmation = null;
					SuccessfulPaymentNotification realSuccessfulPaymentNotification = null;
					if(notification.getTemporaryPurchasedBeatConfirmationId() != null ) {
						var temporaryPurchasedBeatConfirmation = 
								temporaryPurchasedBeatConfirmationRepository.findById(notification.getTemporaryPurchasedBeatConfirmationId())
								.orElseThrow( () ->
										new NotificationException("Not found Temporary Purchased Beat Confirmation with this id: " 
												+notification.getTemporaryPurchasedBeatConfirmationId()));
						
						var beat = beatClient.findById(temporaryPurchasedBeatConfirmation.getBeatId());
						
						var seller = artistClient.findById(temporaryPurchasedBeatConfirmation.getSellerId());
						
						var purchaser = customerClient.findById(temporaryPurchasedBeatConfirmation.getPurchaserId());
						
						realBeatConfirmation = new TemporaryPurchasedBeatConfirmation( 0,
								beat, seller, purchaser, temporaryPurchasedBeatConfirmation.getPurchaseDate(),
								temporaryPurchasedBeatConfirmation.getPurchasedPrice());
					}else if(notification.getSuccessfullPaymentNotificationId() != null ) {
						var successfullPaymentNotification = 
								successfulPaymentNotificationRepository.findById(notification.getSuccessfullPaymentNotificationId())
								.orElseThrow( () ->
								new NotificationException("Not found Successfull Payment Notification with this id: " 
										+notification.getSuccessfullPaymentNotificationId()));
						
						var seller = artistClient.findById(successfullPaymentNotification.getSellerId());
						
						var purchaser = customerClient.findById(successfullPaymentNotification.getPurchaserId());
						
						realSuccessfulPaymentNotification = new SuccessfulPaymentNotification(successfullPaymentNotification.getPaymentId(),
								successfullPaymentNotification.getAmount(), successfullPaymentNotification.getPaymentMethod(), 
								successfullPaymentNotification.getPaymentCategory(), seller, purchaser, successfullPaymentNotification.getDate());
					}else {
						var hireConfirmation = 
								hireConfirmationRepository.findById(notification.getHireConfirmationID())
								.orElseThrow( () ->
								new NotificationException("Not found Hire Confirmation with this id: " 
										+notification.getHireConfirmationID()));
						
						var expert = artistClient.findById(hireConfirmation.getExpertId());
						
						var hirer = customerClient.findById(hireConfirmation.getHirerId());
						
						var jobType = hireConfirmation.getJobTypeId()!= null ? jobTypeClient.findJobTypeById(hireConfirmation.getJobTypeId())
								:null;
						
						realHireConfirmation = new HireConfirmation(hirer, expert, jobType, hireConfirmation.getStartDate()
								, hireConfirmation.getEndDate()
								, hireConfirmation.getPrice());
					}
					
					return new NotificationResponse(notification.getId(), realBeatConfirmation, realHireConfirmation, realSuccessfulPaymentNotification,
							notification.getType(), notification.getNotificationDate(), notification.getNotificationTime(),
							notification.getReceiverIdRole(), notification.isRead());
				})
				.toList();
	}

	public long countNotificationByArtistIdOrCusgtomerId(String id, Role role) {
		return repo.countByReceiverIdRole(id + " " + role);
	}

	public long countNotificationByArtistIdOrCustomerIdUnRead(String id, Role role) {
		return repo.countByReceiverIdRoleAndIsReadFalse(id + " " + role);
	}

	public void readNotification(List<String> ids) {
		for (String id : ids) {
			Notification notification = this.repo.findById(id)
					.orElseThrow(() -> new NotificationException("Not found notification with this id: "+ id));
			
			if(notification.isRead() == false) {
				notification.setRead(true);
			}
			repo.save(notification);
		}
		
			
	}
	
	
}
