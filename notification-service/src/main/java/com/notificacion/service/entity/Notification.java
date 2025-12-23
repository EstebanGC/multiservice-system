package com.notificacion.service.entity;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Long id;
    private String type;
    private String title;
    private String content;
    private LocalDateTime timestamp;
    private boolean read;
}
