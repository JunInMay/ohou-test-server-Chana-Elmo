
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFeedsScrappedMainHomewarmingsFeedRes {
    private Long feedId;
    private Integer isHomewarming;
    private String thumbnailUrl;
    private String title;
    private Long userId;
    private String profileImageUrl;
    private String userNickname;
    private Integer isProfessional;
    private Long cursor;

}