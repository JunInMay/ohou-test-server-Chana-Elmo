
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsScrappedResFeed {
    private Long feedId;
    private String thumbnailUrl;
    private Integer isMediaFeed;
    private Integer isPhoto;
    private Integer isVideo;
    private Integer isHomewarming;
    private Integer isKnowhow;
}