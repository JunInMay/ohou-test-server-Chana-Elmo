
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsMediaFeedsListRes {
    private Long feedId;
    private String thumbnailUrl;
    private String description;
    private Integer videoTime;
    private Integer isBookmarked;
    private Long cursor;
    private Integer isMediaFeed;
}