
package shop.ozip.dev.src.scrapbook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchBookmarksFeedRes {
    private Long feedId;
    private Long scrapbookId;
    private Integer isPatched;
}