
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFeedsMediaFeedsCommentsRes {
    private Long feedId;
    private String content;
    private Long recommentId;
    private Integer isRecomment;
}