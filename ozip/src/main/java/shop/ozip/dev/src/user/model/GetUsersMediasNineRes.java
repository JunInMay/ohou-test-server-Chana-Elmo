
package shop.ozip.dev.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
public class GetUsersMediasNineRes {
    private String thumbnailUrl;
    private String type;
    private Long userId;
    private String userNickname;
}
