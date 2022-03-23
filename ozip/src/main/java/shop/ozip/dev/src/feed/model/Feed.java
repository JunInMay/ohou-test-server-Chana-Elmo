
package shop.ozip.dev.src.feed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Feed {
    private Long id;
    private Long userId;
    private String thumbnailUrl;
    private Integer viewCount;
    private Integer shareCount;
    private Integer isMediaFeed;
    private Integer isPhoto;
    private Integer isVideo;
    private Integer isHomewarming;
    private Integer isKnowhow;
    private Integer isQna;
    private Integer isProduct;
    private String createdAt;
    private String updatedAt;
}