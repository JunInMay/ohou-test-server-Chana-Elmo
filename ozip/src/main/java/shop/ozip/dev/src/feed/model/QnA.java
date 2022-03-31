
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QnA {
    private Long id;
    private Long feedId;
    private String title;
    private String content;
    private Integer isNotice;
    private String createdAt;
    private String updatedAt;
    private String status;
}