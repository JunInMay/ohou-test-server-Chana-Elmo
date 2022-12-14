
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import shop.ozip.dev.src.feed.model.GetFeedsMediaFeedsResBase;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsMediaFeedsRes {
    private Long feedId;
    private List<GetFeedsMediaFeedsResBase> medias;
}