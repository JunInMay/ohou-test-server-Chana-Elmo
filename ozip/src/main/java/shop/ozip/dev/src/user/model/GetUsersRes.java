
package shop.ozip.dev.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUsersRes {
    private Long userId;
    private String email;
    private String nickname;
    private String description;
    private int point;
}
