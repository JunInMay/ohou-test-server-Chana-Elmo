package shop.ozip.dev.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserReq {
    private String profileImageUrl;
    private String nickname;
    private String description;
    private String personalUrl;

    public void checkNull(User user){
        if (this.profileImageUrl == null){
            this.setProfileImageUrl(user.getProfileImageUrl());
        }
        if (this.nickname == null){
            this.setNickname(user.getNickname());
        }
        if (this.description == null){
            this.setDescription(user.getDescription());
        }
        if (this.personalUrl == null){
            this.setPersonalUrl(user.getPersonalUrl());
        }
    }
}
