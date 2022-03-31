
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.keyword.model.QnAKeyword;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsQnANoticeRes {
    private Long feedId;
    private Long qnaId;
    private String title;
}