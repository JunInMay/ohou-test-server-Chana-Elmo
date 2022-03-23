
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFeedsMediaFeedsCommentsReq {
    private Long feedId;
    private String content;
    private Long recommentId;
    private Integer isRecomment;
    
    public void checkNullIsRecomment() {
        if (this.getIsRecomment() == null) {
            this.setIsRecomment(0);
        }
    }


}