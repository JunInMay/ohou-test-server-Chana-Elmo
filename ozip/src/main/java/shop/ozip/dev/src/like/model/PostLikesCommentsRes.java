
package shop.ozip.dev.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLikesCommentsRes {
    private Long userId;
    private Long commentId;
    private Integer isLiked;
}