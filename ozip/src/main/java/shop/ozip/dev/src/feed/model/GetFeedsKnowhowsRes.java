
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.keyword.model.Keyword;

import java.security.Key;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsKnowhowsRes {
    private List<GetFeedsKnowhowsResPhoto> photos;
    private List<Keyword> keywords;

}