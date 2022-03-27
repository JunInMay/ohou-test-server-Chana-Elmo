
package shop.ozip.dev.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentsListRes {
    private Integer allCommentCount;
    private List<GetCommentsListResMainCommentWithRecomment> mainCcomments;
}