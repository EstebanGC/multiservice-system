package com.notificacion.service.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Lond id; 
    private String type;
    private String title;
    private String content;
    private LocalDateTime timestamp;
    private boolean read;
}
