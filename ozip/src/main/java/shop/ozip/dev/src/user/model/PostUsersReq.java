
package shop.ozip.dev.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static shop.ozip.dev.config.Constant.DEFAULT_USER_PROFILE_IMAGE;

@Getter
@Setter
@AllArgsConstructor
public class PostUsersReq {
    private String email;
    private String password;
    private String nickname;
    private String provider;
    private String profileImageUrl;
    
    // 회원가입 요청시 프로필 이미지 안들어오면 기본 프사로 바꿈
    public void checkNullProfileImageUrl(){
        if (this.getProfileImageUrl() == null){
            this.setProfileImageUrl(DEFAULT_USER_PROFILE_IMAGE);
        }
    }
}