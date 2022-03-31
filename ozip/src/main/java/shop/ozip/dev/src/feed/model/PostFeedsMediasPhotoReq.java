
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostFeedsMediasPhotoReq {
    private Long ownerFeedId;
    private String url;
    private String description;
    private Long spaceId;
    private List<String> keywords;
}