package shop.ozip.dev.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String provider;
    private String nickname;
    private String profileImageUrl;
    private String personalUrl;
    private String description;
    private Integer point;
    private String status;
    private Integer isProfessional;
    private Integer isSeller;
    private String createdAt;
    private String updatedAt;
}
