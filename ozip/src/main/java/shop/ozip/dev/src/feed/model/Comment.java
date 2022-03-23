
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Comment {
    private Long id;
    private Long userId;
    private Long feedId;
    private String content;
    private Long recommentId;
    private Integer isRecomment;
    private String createdAt;
    private String updatedAt;


}