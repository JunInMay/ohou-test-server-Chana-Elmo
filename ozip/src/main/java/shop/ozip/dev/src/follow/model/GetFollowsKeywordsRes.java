
package shop.ozip.dev.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetFollowsKeywordsRes {
    private Long keywordId;
    private String keywordName;
    private Integer isFollowed;
}