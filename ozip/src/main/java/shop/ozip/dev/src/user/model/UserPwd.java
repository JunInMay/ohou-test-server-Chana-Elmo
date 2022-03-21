package shop.ozip.dev.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserPwd {
    private Long userId;
    private String password;
}
