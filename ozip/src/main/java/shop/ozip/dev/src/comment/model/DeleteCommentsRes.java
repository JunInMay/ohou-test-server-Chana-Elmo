
package shop.ozip.dev.src.comment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeleteCommentsRes {
    private Long commentId;
    private Integer isDeleted;
}