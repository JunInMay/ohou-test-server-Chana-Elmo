
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.keyword.model.Keyword;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MediaWithKeyword {
    private Media media;
    private List<Keyword> keywords;
}