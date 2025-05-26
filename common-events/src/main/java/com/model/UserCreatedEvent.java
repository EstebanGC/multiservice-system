package main.java.com.model;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreatedEvent {
    private String userId;
    private String email;
    private Instant createdAt;
}
