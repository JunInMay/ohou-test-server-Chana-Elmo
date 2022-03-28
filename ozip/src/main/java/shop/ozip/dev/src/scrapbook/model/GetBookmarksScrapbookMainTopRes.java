
package shop.ozip.dev.src.scrapbook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBookmarksScrapbookMainTopRes {
    private Long scrapbookId;
    private String scrapbookName;
    private String profileImageUrl;
    private String userNickname;
    private Integer allCount;
    private Integer mediaCount;
    private Integer homewarmingCount;
    private Integer knowhowCount;
}