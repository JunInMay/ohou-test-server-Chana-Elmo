
package shop.ozip.dev.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentsPartMediaFeedsResComment {
    private Integer mainCommentDoesHaveRecomment;
    private Long mainCommentId;
    private Long mainCommentUserId;
    private String mainCommentProfileImageUrl;
    private String mainCommentNickname;
    private String mainCommentContent;
    private String mainCommentUploadedAt;
    private Integer mainCommentIsLiked;
    private Long recommentId;
    private Long recommentUserId;
    private String recommentProfileImageUrl;
    private String recommentNickname;
    private String recommentContent;
    private String recommentUploadedAt;
    private Integer recommentIsLiked;
}