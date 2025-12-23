package com.notificacion.service.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private String notificationId;
    private String status;
    private LocalDateTime sentAt;
}
