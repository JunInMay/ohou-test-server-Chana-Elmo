
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsHotsKeywordResMedia {
    private Long feedId;
    private String thumbnailUrl;
    private String nickname;
    private Integer isBookmmarked;
}