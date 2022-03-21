package shop.ozip.dev.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String description;
    private int point;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
