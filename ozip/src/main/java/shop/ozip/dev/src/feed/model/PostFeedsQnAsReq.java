
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
public class PostFeedsQnAsReq {
    private String title;
    private String content;
    private List<PostFeedsQnAsReqMediaQnA> medias;
    private List<Long> keywordIds;

    public void checkNullContent(){
        if(this.getContent() == null){
            this.setContent("");
        }
    }
}