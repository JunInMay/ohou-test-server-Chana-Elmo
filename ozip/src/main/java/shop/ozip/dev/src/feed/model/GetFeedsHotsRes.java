
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsHotsRes {
    private Long feedId;
    private String thumbnailUrl;
    private String description;
    private String title;
    private Integer isBookmarked;
    private Integer isHomewarming;
    private Integer isKnowhow;

}