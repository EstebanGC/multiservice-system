package com.notificacion.service.service;

import com.notificacion.service.entity.Notification;
import com.notificacion.service.entity.NotificationRequest;
import com.notificacion.service.entity.NotificationResponse;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class NotificationService {

//    private JavaMailSender emailSender;
//
//    public NotificationResponse sendEmail(NotificationRequest request) {
//
//        try {
//            if (request.getRecipient() ==  null || request.getRecipient().isBlank()) {
//                throw new IllegalAccessException("Recipient is required");
//            }
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(request.getRecipient());
//        message.setSubject(request.getSubject());
//        message.setText(request.getContent());
//
//        emailSender.send(message);
//
//        return NotificationResponse.builder()
//                .notificationId(UUID.randomUUID().toString())
//                .status("SENT")
//                .sentAt(LocalDateTime.now())
//                .build();
//
//
//        } catch(Exception e) {
//            System.out.println("Failed to send email" + e);
//            return NotificationResponse.builder()
//                    .status("FAILED")
//                    .sentAt(LocalDateTime.now())
//                    .build();
//        }
//    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(String message) {

        messagingTemplate.convertAndSend("/topic/notifications", message);
    }
}
