
package shop.ozip.dev.src.scrapbook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Scrapbook {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private Integer isMain;
    private String createdAt;
    private String updatedAt;
}