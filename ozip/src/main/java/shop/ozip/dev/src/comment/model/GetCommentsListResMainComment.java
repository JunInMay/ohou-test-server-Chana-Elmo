
package shop.ozip.dev.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentsListResMainComment {
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
}