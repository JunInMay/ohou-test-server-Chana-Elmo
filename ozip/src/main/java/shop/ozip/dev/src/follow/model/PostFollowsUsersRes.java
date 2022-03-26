
package shop.ozip.dev.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFollowsUsersRes {
    private Long userId;
    private Integer isFollowed;
}