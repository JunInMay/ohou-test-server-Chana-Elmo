
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.keyword.model.Keyword;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsMediasRes {
    private Long mediaId;
    private Long referredId;
    private Integer isHomewarming;
    private Integer isMediaFeed;
    private String acreage;
    private String hometype;
    private String style;

    private String imageUrl;
    private String description;
    private Integer count;
    private List<Keyword> keywords;
}