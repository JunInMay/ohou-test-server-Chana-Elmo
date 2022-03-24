
package shop.ozip.dev.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostFeedsMediaFeedsCommentsReq {
    private Long feedId;
    private String content;
    private Long recommentId;
    private Integer isRecomment;

    public void checkNullIsRecomment() {
        if (this.getIsRecomment() == null) {
            this.setIsRecomment(Integer.valueOf(0));
        }
    }


}