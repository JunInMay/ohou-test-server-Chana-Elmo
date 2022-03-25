
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsHotsPhotoRes {
    private Integer rowNum;
    private Long feedId;
    private String thumbnailUrl;
    private String nickname;
    private Integer isBookmarked;
}