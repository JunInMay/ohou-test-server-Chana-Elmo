
package shop.ozip.dev.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Follow {
    private Long id;
    private String name;
    private Integer referredCount;
    private String createdAt;
    private String updatedAt;
}