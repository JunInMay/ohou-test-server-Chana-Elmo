
package shop.ozip.dev.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentsListResRecomment {
    private Long recommentId;
    private Long recommentUserId;
    private String recommentProfileImageUrl;
    private String recommentNickname;
    private String recommentContent;
    private String recommentUploadedAt;
    private Integer recommentLikeCount;
    private Integer recommentIsLiked;
}