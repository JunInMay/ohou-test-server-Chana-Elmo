
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsMediaFeedsListRes {
    private Long id;
    private String thumbnail;
    private String description;
    private Integer isBookmarked;
    private Integer isMediaFeed;
    private Integer isPhoto;
    private Integer isVideo;
    private Integer isHomewarming;
    private Integer isKnowhow;
    private Integer isQna;
    private Integer isProduct;
}