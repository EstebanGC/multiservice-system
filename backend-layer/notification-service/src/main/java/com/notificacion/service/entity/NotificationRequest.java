package com.notificacion.service.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class NotificationRequest {

    private String recipient;
    private String message;
//    private String recipient;
//    private String type;        // EMAIL, SMS, PUSH
//    private String subject;
//    private String content;
//    private Map<String, Object> additionalData;
}
