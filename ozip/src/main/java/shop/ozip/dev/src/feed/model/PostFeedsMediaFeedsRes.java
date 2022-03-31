
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostFeedsMediaFeedsRes {
    private Long feedId;
    private Integer isPhoto;
    private Integer isVideo;

}