
package shop.ozip.dev.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFollowsUsersRes {
    private Long userId;
    private Integer isDeleted;
}