
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Media {
    private Long id;
    private Long feedId;
    private String description;
    private String url;
    private Long mediaSpaceTypeId;
    private int isPhoto;
    private int isVideo;
    private String createdAt;
    private String updatedAt;
}