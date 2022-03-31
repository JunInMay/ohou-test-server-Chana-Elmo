
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsQnASimilarResFeed {
    private Long feedId;
    private Long qnaId;
    private String title;
    private String profileImageUrl;
    private String nickname;
    private String uploadedAt;
    private Integer commentCount;
    private Integer ViewCount;
    private String thumbnailUrl;
    private Long cursor;
}