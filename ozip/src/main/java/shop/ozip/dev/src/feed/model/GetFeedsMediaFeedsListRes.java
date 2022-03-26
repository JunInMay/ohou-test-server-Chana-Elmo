
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsMediaFeedsListRes {
    private GetFeedsMediaFeedsListResBase baseInformation;
    private List<GetFeedsMediaFeedsListResPhoto> photos;
}