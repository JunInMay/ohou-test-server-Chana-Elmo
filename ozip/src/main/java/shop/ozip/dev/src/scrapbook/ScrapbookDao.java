
package shop.ozip.dev.src.scrapbook;


import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.ozip.dev.src.comment.model.Comment;
import shop.ozip.dev.src.comment.model.PostCommentsMediaFeedsRes;
import shop.ozip.dev.src.feed.model.Feed;
import shop.ozip.dev.src.scrapbook.model.GetBookmarksScrapbookMainTopRes;
import shop.ozip.dev.src.scrapbook.model.PostBookmarksFeedRes;
import shop.ozip.dev.src.scrapbook.model.PostBookmarksRes;
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
    // 스크랩북이 존재하는지 체크
    public boolean checkScrapbookById(Long scrapbookId) {
        String checkScrapbookByIdQuery = ""
                + "SELECT EXISTS(SELECT * "
                + "              FROM   scrapbook "
                + "              WHERE  id = ?) AS exist;";
        Object[] checkScrapbookByIdParams = new Object[]{
                scrapbookId
        };
        return this.jdbcTemplate.queryForObject(checkScrapbookByIdQuery, boolean.class, checkScrapbookByIdParams);
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

    @Transactional
    // 스크랩북 만들기
    public PostBookmarksRes createScrapbook(Long userId, String name, String description) {

        Object[] createScrapbookParams;
        String createScrapbookQuery;
        if (description != null){
            createScrapbookParams = new Object[]{
                    userId, name, description
            };
            createScrapbookQuery = ""
                    + "INSERT INTO scrapbook "
                    + "            (user_id, "
                    + "             name, "
                    + "             description) "
                    + "VALUES     (?, "
                    + "            ?, "
                    + "            ?);";
        }else {
            createScrapbookParams = new Object[]{
                    userId, name
            };
            createScrapbookQuery = ""
                    + "INSERT INTO scrapbook "
                    + "            (user_id, "
                    + "             name) "
                    + "VALUES     (?, "
                    + "            ?);";
        }
        this.jdbcTemplate.update(createScrapbookQuery, createScrapbookParams);
        String lastInsertIdQuery = "select last_insert_id()";
        Long recentId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,long.class);

        return new PostBookmarksRes(recentId);
    }


    // 스크랩북 상단 정보 조회
    public GetBookmarksScrapbookMainTopRes retrieveScrapbookTop(Long scrapbookId) {
        String retrieveScrapbookTopQuery = ""
                + "SELECT main.id, "
                + "       main.name, "
                + "       user.profile_image_url, "
                + "       user.nickname, "
                + "       (SELECT Count(*) "
                + "        FROM   scrapbook_feed "
                + "        WHERE  scrapbook_feed.scrapbook_id = main.id) AS all_count, "
                + "       (SELECT Count(*) "
                + "        FROM   scrapbook_feed "
                + "               JOIN feed "
                + "                 ON scrapbook_feed.feed_id = feed.id "
                + "        WHERE  scrapbook_feed.scrapbook_id = main.id "
                + "               AND feed.is_photo = 1 "
                + "                OR feed.is_video = 1 "
                + "                OR feed.is_media_feed = 1)            AS media_count, "
                + "       (SELECT Count(*) "
                + "        FROM   scrapbook_feed "
                + "               JOIN feed "
                + "                 ON scrapbook_feed.feed_id = feed.id "
                + "        WHERE  scrapbook_feed.scrapbook_id = main.id "
                + "               AND feed.is_product = 1)               AS product_count, "
                + "       (SELECT Count(*) "
                + "        FROM   scrapbook_feed "
                + "               JOIN feed "
                + "                 ON scrapbook_feed.feed_id = feed.id "
                + "        WHERE  scrapbook_feed.scrapbook_id = main.id "
                + "               AND is_homewarming = 1)                AS homewarming_count, "
                + "       (SELECT Count(*) "
                + "        FROM   scrapbook_feed "
                + "               JOIN feed "
                + "                 ON scrapbook_feed.feed_id = feed.id "
                + "        WHERE  scrapbook_feed.scrapbook_id = main.id "
                + "               AND is_knowhow = 1)                    AS knowhow_count "
                + "FROM   scrapbook main "
                + "       JOIN user "
                + "         ON main.user_id = user.id "
                + "WHERE  main.id = ? ";
        return this.jdbcTemplate.queryForObject(retrieveScrapbookTopQuery,
                (rs, rowNum) -> new GetBookmarksScrapbookMainTopRes(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("profile_image_url"),
                        rs.getString("nickname"),
                        rs.getInt("all_count"),
                        rs.getInt("media_count"),
                        rs.getInt("homewarming_count"),
                        rs.getInt("knowhow_count")
                ), scrapbookId);
    }
}
