package com.hokhanh.web.email;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.hokhanh.web.kafka.hire.HireConfirmation;
import com.hokhanh.web.kafka.payment.SuccessfulPaymentNotification;
import com.hokhanh.web.kafka.purchasedBeat.TemporaryPurchasedBeatConfirmation;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;
	
	@Async
	public void sendPurchasedBeatConfirmation (TemporaryPurchasedBeatConfirmation confirmation) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, 
        		MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom("contact@hoofkhanh.com");
        
        final String templateName = EmailTemplates.TEMPORARY_PURCHASED_BEAT_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("temporaryPurchasedBeat", confirmation.temporaryPurchasedBeatid());
        variables.put("seller", confirmation.seller());
        variables.put("purchaser", confirmation.purchaser());
        variables.put("beat", confirmation.beat());
        variables.put("date", confirmation.purchaseDate());
        variables.put("price", confirmation.purchasedPrice());

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(EmailTemplates.TEMPORARY_PURCHASED_BEAT_CONFIRMATION.getSubject());
        
        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(confirmation.purchaser().user().email());
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
        	e.printStackTrace();
        }
	}
	
	@Async
	public void sendSuccessfulPaymentNotification(SuccessfulPaymentNotification notification) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, 
        		MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom("contact@hoofkhanh.com");
        
        final String templateName = EmailTemplates.SUCCESSFUL_PAYMENT_NOTIFICATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("price", notification.amount());
        variables.put("seller", notification.seller());
        variables.put("purchaser", notification.purchaser());
        variables.put("date", notification.date());

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(EmailTemplates.SUCCESSFUL_PAYMENT_NOTIFICATION.getSubject());
        
        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            List<String> emailAddresses = List.of(
                    notification.purchaser().user().email(),
                    notification.seller().user().email()
                );
            
            messageHelper.setTo(emailAddresses.toArray(new String[0]));
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
        	e.printStackTrace();
        }
	}

	@Async
	public void sendHireConfirmationToMailSoundMakerChecker(HireConfirmation confirmation) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, 
        		MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom("contact@hoofkhanh.com");
        
        final String templateName = EmailTemplates.HIRE_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("hirer", confirmation.hirer());
        variables.put("expert", confirmation.expert());
        variables.put("startDate", confirmation.startDate());
        variables.put("endDate", confirmation.endDate());
        variables.put("price", confirmation.price());
        
        if(confirmation.jobType() != null ) {
        	variables.put("jobType", confirmation.jobType());
        }
        
        
//        biến display đồng ý hoặc không khi có customer muốn expert
        variables.put("hire", "display hire input");

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(EmailTemplates.HIRE_CONFIRMATION.getSubject());
        
        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(confirmation.expert().user().email());
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
        	e.printStackTrace();
        }
	}
	
	@Async
	public void sendHireConfirmationToMailOfHirer(HireConfirmation confirmation) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, 
        		MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        messageHelper.setFrom("contact@hoofkhanh.com");
        
        final String templateName = EmailTemplates.HIRE_CONFIRMATION.getTemplate();

        Map<String, Object> variables = new HashMap<>();
        variables.put("hirer", confirmation.hirer());
        variables.put("expert", confirmation.expert());
        variables.put("startDate", confirmation.startDate());
        variables.put("endDate", confirmation.endDate());
        variables.put("price", confirmation.price());
        
        if(confirmation.jobType() != null ) {
        	variables.put("jobType", confirmation.jobType());
        }
        
        
//        biến để display input thanh toán
        variables.put("payment", "display payment input");

        Context context = new Context();
        context.setVariables(variables);
        messageHelper.setSubject(EmailTemplates.HIRE_CONFIRMATION.getSubject());
        
        try {
            String htmlTemplate = templateEngine.process(templateName, context);
            messageHelper.setText(htmlTemplate, true);

            messageHelper.setTo(confirmation.hirer().user().email());
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
        	e.printStackTrace();
        }
	}
	
	
}
