
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsMediaFeedsResMedia {
    private Long id;
    private Long feedId;
    private String thumbnailUrl;
    private Integer time;
    private String description;
    private String url;
    private Long mediaSpaceTypeId;
    private int isPhoto;
    private int isVideo;
    private String createdAt;
    private String updatedAt;
    private Integer isBookmarked;
}