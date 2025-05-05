package com.notificacion.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {

    private String recipient;
    private String message;
}
