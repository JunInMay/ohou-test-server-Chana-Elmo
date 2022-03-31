
package shop.ozip.dev.src.keyword.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetKeywordsRecommendedKeywords {
    private Long qnaKeywordId;
    private String name;
    private Integer count;
}