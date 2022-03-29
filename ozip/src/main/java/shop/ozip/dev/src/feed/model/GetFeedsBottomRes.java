
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsBottomRes {
    private Long userId;
    private String profileImageUrl;
    private String nickname;
    private String description;
    private Integer isFollowed;
    private Long feedId;
    private Integer likeCount;
    private Integer scrappedCount;
    private Integer commentCount;
    private Integer viewCount;
}