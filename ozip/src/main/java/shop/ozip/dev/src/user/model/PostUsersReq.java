
package shop.ozip.dev.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static shop.ozip.dev.config.Constant.defaultUserProfileImage;

@Getter
@Setter
@AllArgsConstructor
public class PostUsersReq {
    private String email;
    private String password;
    private String nickname;
    private String provider;
    private String profileImageUrl;

    public void checkNullProfileImageUrl(){
        if (this.getProfileImageUrl() == null){
            this.setProfileImageUrl(defaultUserProfileImage);
        }
    }
}