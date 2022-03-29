
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.feed.model.GetFeedsFollowsListResBase;
import shop.ozip.dev.src.feed.model.JustPhotoUrl;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsFollowsListRes {
    private GetFeedsFollowsListResBase baseInformation;
    private List<JustPhotoUrl> photos;

}