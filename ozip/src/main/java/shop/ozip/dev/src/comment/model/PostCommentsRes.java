
package shop.ozip.dev.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostCommentsRes {
    private Long id;
    private Long feedId;
    private String content;
    private Long recommentId;
    private Integer isRecomment;
}