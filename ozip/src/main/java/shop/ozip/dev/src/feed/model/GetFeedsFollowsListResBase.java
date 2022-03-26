
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsFollowsListResBase {
    private Long feedId;
    private Integer isMediaFeed;
    private Integer isPhoto;
    private Integer isVideo;
    private Integer isHomewarming;
    private Integer isKnowhow;
    private String profileImageUrl;
    private String nickname;
    private String description;
    private String keywordName;
    private Integer videoTime;
    private String uploadedAt;
    private Integer viewCount;
    private Integer likeCount;
    private Integer scrappedCount;
    private Integer commentCount;
    private Integer shareCount;
    private String createdAt;
    private Long cursor;
    private Integer isBookmarked;

}