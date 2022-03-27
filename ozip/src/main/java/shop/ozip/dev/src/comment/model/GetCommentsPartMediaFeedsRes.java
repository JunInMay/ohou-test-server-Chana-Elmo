
package shop.ozip.dev.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentsPartMediaFeedsRes {
    private Integer allCommentCount;
    private Integer retrievedCommentCount;
    private List<GetCommentsPartMediaFeedsResComment> commentsWithRecomment;
}