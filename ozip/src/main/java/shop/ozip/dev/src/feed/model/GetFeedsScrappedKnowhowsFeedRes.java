
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsScrappedKnowhowsFeedRes {
    private Long feedId;
    private Integer isKnowhow;
    private String thumbnailUrl;
    private String title;
    private String themeName;
    private Long cursor;

}