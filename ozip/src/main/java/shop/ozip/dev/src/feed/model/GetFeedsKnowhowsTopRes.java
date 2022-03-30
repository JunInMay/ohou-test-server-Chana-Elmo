
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsKnowhowsTopRes {
    private String thumbnail;
    private String theme;
    private String title;
    private String metaImageUrl;
    private String topContent;

}