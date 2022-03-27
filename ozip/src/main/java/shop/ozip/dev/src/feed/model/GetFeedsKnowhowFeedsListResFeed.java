
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsKnowhowFeedsListResFeed {
    private Integer isNew;
    private Long feedId;
    private String thumbnailUrl;
    private String description;
    private String profileImageUrl;
    private String nickname;
    private Integer viewCount;
    private Integer scrappedCount;
    private Long cursor;
    private Integer isBookmarked;
}