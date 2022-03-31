
package shop.ozip.dev.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetFollowsFolloweesRes {
    private Long userId;
    private String profileImageUrl;
    private String nickname;
    private String description;
    private Integer isFollowed;
}