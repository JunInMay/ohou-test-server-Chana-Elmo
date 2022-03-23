
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MediaFeed {
    private Long id;
    private Long feedId;
    private int isPhoto;
    private int isVideo;
    private Long mediaFeedAcreageTypeId;
    private Long mediaFeedHomeTypeId;
    private Long mediaFeedStyleTypeId;
    private String createdAt;
    private String updatedAt;
}