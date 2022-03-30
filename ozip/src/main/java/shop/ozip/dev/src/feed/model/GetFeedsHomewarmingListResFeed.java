
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsHomewarmingListResFeed {
    private Long feedId;
    private String thumbnailUrl;
    private Integer isBookmarked;
    private String description;
    private String profileImageUrl;
    private String nickname;
    private Integer scrappedCount;
    private Integer viewCount;
    private Long cursor;
}