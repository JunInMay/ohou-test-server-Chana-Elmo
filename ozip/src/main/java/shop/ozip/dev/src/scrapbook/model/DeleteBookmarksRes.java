
package shop.ozip.dev.src.scrapbook.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteBookmarksRes {
    private Long scrapbookId;
    private Integer isDeleted;
}