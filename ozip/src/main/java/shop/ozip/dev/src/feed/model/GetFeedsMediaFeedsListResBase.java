
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsMediaFeedsListResBase {
    private Long feedId;
    private Integer likeCount;
    private Integer scrappedCount;
    private Integer isFollowed;
    private String userDescription;
    private Integer shareCount;
    private Integer commentCount;
    private String recentCommentUserProfileImageUrl;
    private String recentCommentUserNickname;
    private String recentCommentContent;
    private String thumbnailUrl;
    private String description;
    private Integer videoTime;
    private Integer isBookmarked;
    private Long cursor;
    private Integer isMediaFeed;
}