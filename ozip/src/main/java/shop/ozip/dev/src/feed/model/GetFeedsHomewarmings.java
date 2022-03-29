
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsHomewarmings {
    private Long photoId;
    private String imageUrl;
    private String isBookmarked;
    private String description;

}