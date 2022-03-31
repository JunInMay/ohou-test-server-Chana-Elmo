
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.keyword.model.QnAKeyword;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsQnARes {
    private String title;
    private Long userId;
    private String nickname;
    private String uploadedAt;
    private String content;
    private List<GetFeedsQnAResPhoto> photos;
    private List<QnAKeyword> keywords;
}