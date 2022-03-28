
package shop.ozip.dev.src.scrapbook;


import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.comment.model.Comment;
import shop.ozip.dev.src.comment.model.PostCommentsMediaFeedsRes;
import shop.ozip.dev.src.scrapbook.model.PostBookmarksFeedRes;
import shop.ozip.dev.src.scrapbook.model.Scrapbook;
import shop.ozip.dev.utils.Common;

import javax.sql.DataSource;


@Repository
public class ScrapbookDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    /* 조회하는 메소드명
    DTO 조회하는 메소드명을 get으로 함
    존재 여부를 체크하는 메소드명을 check로 함
    그 외 실제 응답을 만드는 메소드는 retrieve로 함
    */

    // 특정 유저가 스크랩한 피드 개수 가져오기
    public Integer getCountScrapbookFeedByUserId(Long userId){
        String getCountScrapbookFeedByUserIdQuery = ""
                + "SELECT Count(*) "
                + "FROM   scrapbook_feed "
                + "       JOIN scrapbook "
                + "         ON scrapbook_feed.scrapbook_id = scrapbook.id "
                + "WHERE  scrapbook.user_id = ?;";
        return this.jdbcTemplate.queryForObject(
                getCountScrapbookFeedByUserIdQuery,
                Integer.class,
                userId);
    }
    // 유저의 메인 스크랩북 가져오기
    public Scrapbook getMainScrapbookByUserId(Long userId){
//        getMainScrapbookByUserIdParams
        String getMainScrapbookByUserIdQuery = ""
                + "SELECT * "
                + "FROM   scrapbook "
                + "WHERE  user_id = ? "
                + "       AND is_main = 1;";;

        return this.jdbcTemplate.queryForObject(getMainScrapbookByUserIdQuery,
                (rs, rowNum) -> new Scrapbook(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("is_main"),
                        Common.formatTimeStamp(rs.getTimestamp("created_at")),
                        Common.formatTimeStamp(rs.getTimestamp("updated_at"))
                ), userId);
    }
    // 해당 피드를 이미 북마크했는지 체크
    public boolean checkBookmarkExist(Long userId, Long feedId) {
        String checkBookmarkExistQuery = ""
                + "SELECT EXISTS(SELECT user_id, "
                + "                     feed_id "
                + "              FROM   scrapbook_feed "
                + "                     JOIN (SELECT * "
                + "                           FROM   scrapbook) s "
                + "                       ON s.id = scrapbook_feed.scrapbook_id "
                + "              WHERE  user_id = ? "
                + "                     AND feed_id = ?) AS exist;";
        Object[] checkBookmarkExistParams = new Object[]{
                userId, feedId
        };
        return this.jdbcTemplate.queryForObject(checkBookmarkExistQuery, boolean.class, checkBookmarkExistParams);
    }

    
    // 피드 북마크하기
    public PostBookmarksFeedRes createBookmarkFeed(Long scrapbookId, Long feedId) {
        String createBookMarkFeedQuery = ""
                + "INSERT INTO scrapbook_feed "
                + "            (scrapbook_id, "
                + "             feed_id) "
                + "VALUES     (?, "
                + "            ?);";;
        Object[] createBookMarkFeedParams = new Object[]{
                scrapbookId, feedId
        };
        int result = this.jdbcTemplate.update(createBookMarkFeedQuery, createBookMarkFeedParams);


        return new PostBookmarksFeedRes(
                scrapbookId,
                result
        );
    }
}
