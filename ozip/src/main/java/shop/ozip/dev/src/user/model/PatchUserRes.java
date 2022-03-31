package shop.ozip.dev.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserRes {
    private String profileImageUrl;
    private String nickname;
    private String description;
    private String personalUrl;


}
