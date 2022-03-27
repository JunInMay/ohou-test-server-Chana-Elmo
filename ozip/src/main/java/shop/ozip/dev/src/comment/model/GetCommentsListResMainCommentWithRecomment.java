
package shop.ozip.dev.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentsListResMainCommentWithRecomment {
    private Integer mainCommentHaveRecomment;
    private Long mainCommentId;
    private Long mainCommentUserId;
    private String mainCommentProfileImageUrl;
    private String mainCommentNickname;
    private String mainCommentContent;
    private String mainCommentUploadedAt;
    private Integer mainCommentLikeCount;
    private Integer mainCommentIsLiked;
    private Long cursor;
    private List<GetCommentsListResRecomment> recommentList;
}