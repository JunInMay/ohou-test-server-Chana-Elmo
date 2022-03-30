
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsHomewarmingSimilarRes {
    private Long feedId;
    private String thumbnailUrl;
    private Integer isBookmarked;
    private String title;
    private String nickname;
    private Integer isProfessional;
    private Integer scrappedCount;
    private Integer viewCount;
    private Long cursor;
}