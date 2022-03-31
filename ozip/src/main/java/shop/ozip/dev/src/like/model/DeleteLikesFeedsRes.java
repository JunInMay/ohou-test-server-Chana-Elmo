
package shop.ozip.dev.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeleteLikesFeedsRes {
    private Long userId;
    private Long feedId;
    private Integer isDeleted;
}