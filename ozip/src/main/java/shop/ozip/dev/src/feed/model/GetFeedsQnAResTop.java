
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsQnAResTop {
    private String title;
    private Long userId;
    private String nickname;
    private String uploadedAt;
    private String content;
}