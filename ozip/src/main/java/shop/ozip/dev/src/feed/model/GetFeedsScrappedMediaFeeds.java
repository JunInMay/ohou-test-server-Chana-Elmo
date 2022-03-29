
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsScrappedMediaFeeds {
    private Long feedId;
    private Integer isPhoto;
    private Integer isMediaFeed;
    private Integer videoTime;
    private String thumbnailUrl;
    private Long cursor;

}