
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsHotsKeywordRes {
    private String keywordName;
    private List<GetFeedsHotsKeywordResMedia> feeds;

}