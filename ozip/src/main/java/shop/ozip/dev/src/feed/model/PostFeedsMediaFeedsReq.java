
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostFeedsMediaFeedsReq {
    private Integer isPhoto;
    private Integer isVideo;
    private Long acreageId;
    private Long homeId;
    private Long styleId;
}