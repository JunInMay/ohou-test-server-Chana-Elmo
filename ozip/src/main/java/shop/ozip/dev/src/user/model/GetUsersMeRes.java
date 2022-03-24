
package shop.ozip.dev.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUsersMeRes {
    private Long id;
    private String email;
    private String nickname;
    private String description;
    private Integer point;
    private Integer follower;
    private Integer followee;
}
