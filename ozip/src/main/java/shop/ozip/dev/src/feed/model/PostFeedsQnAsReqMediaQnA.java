
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
public class PostFeedsQnAsReqMediaQnA {
    private String url;
    private String description;

    public void nullCheck(){
        if (this.getDescription()==null){
            this.setDescription("");
        }
    }
}