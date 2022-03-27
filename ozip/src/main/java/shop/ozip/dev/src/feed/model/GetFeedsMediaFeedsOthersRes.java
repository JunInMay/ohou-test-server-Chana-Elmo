
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsMediaFeedsOthersRes {
    private Long feedId;
    private String thumbnailUrl;
    private Integer isMediaFeedPhoto;
    private Integer isMediaFeedVideo;
}