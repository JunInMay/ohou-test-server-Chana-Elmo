
package shop.ozip.dev.src.feed.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsMediasNineRes {
    private String thumbnailUrl;
    private String type;
    private Long userId;
    private String userNickname;
}
