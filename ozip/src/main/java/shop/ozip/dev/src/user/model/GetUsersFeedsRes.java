
package shop.ozip.dev.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUsersFeedsRes {
    private Long userId;
    private String profileImageUrl;
    private String nickname;
    private Integer isFollowed;
    private String uploadedAt;
    private String uploadedDate;
    private Integer isProfessional;
}
