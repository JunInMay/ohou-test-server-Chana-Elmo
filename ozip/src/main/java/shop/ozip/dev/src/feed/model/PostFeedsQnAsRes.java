
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostFeedsQnAsRes {
    private Long feedId;
    private String title;
    private String content;
    private List<PostFeedsQnAsReqMediaQnA> medias;
    private List<String> keywordIds;


}