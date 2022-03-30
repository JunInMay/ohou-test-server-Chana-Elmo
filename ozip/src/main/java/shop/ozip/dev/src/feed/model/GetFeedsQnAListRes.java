
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.keyword.model.QnAKeyword;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsQnAListRes {
    private Long feedId;
    private String title;
    private String profileImageUrl;
    private String nickname;
    private String uploadedAt;
    private Integer commentCount;
    private Integer viewCount;
    private String thumbnailUrl;
    private Long cursor;
    private List<QnAKeyword> keywords;
}