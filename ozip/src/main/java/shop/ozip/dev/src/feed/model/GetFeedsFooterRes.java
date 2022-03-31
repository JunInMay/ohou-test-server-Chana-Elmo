
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.keyword.model.QnAKeyword;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsFooterRes {
    private Integer likeCount;
    private Integer isLiked;
    private Integer scrappedCount;
    private Integer isBookmarked;
    private Integer commentCount;
    private Integer shareCount;
}