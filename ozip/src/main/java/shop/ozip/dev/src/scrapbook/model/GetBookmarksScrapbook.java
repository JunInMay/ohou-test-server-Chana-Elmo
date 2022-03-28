
package shop.ozip.dev.src.scrapbook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBookmarksScrapbook {
    private Long scrapbookId;
    private String scrapbookName;
    private String description;
    private Integer feedCount;
    private String thumbnailUrl;
    private Long cursor;
}