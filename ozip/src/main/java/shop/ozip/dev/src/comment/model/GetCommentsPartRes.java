
package shop.ozip.dev.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.comment.model.GetCommentsPartResComment;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentsPartRes {
    private Integer allCommentCount;
    private Integer retrievedCommentCount;
    private List<GetCommentsPartResComment> commentsWithRecomment;
}