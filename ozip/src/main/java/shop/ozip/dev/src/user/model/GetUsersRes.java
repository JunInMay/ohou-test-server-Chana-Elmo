
package shop.ozip.dev.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUsersRes {
    private Long id;
    private String profileImageUrl;
    private String email;
    private String nickname;
    private String description;
    private Integer follower;
    private Integer followee;
    private Integer likeFeed;
    private Integer scrapBookFeeds;
}
