
package shop.ozip.dev.src.scrapbook;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.ozip.dev.src.scrapbook.model.*;
import shop.ozip.dev.utils.Common;

import javax.sql.DataSource;
import java.util.List;


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

    // 스크랩북 ID로 가져오기
    public Scrapbook getScrapbookById(Long scrapbookId){
//        getMainScrapbookByUserIdParams
        String getScrapbookByIdQuery = ""
                + "SELECT * "
                + "FROM   scrapbook "
                + "WHERE  id = ? ";

        return this.jdbcTemplate.queryForObject(getScrapbookByIdQuery,
                (rs, rowNum) -> new Scrapbook(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("is_main"),
                        Common.formatTimeStamp(rs.getTimestamp("created_at")),
                        Common.formatTimeStamp(rs.getTimestamp("updated_at"))
                ), scrapbookId);
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
    @Transactional
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
    public DeleteBookmarksFeedRes deleteBookmarkFeed(Long userId, Long feedId) {
        String deleteBookMarkFeedQuery = ""
                + "DELETE scrapbook_feed "
                + "FROM   scrapbook_feed "
                + "       JOIN scrapbook "
                + "         ON scrapbook_id = scrapbook.id "
                + "WHERE  user_id = ? "
                + "       AND feed_id = ?;";
        Object[] deleteBookMarkFeedParams = new Object[]{
                userId, feedId
        };
        int result = this.jdbcTemplate.update(deleteBookMarkFeedQuery, deleteBookMarkFeedParams);

        return new DeleteBookmarksFeedRes(feedId, result);

    }


    @Transactional
    // 스크랩북 만들기
    public PostBookmarksRes createScrapbook(Long userId, String name, String description, Integer isMain) {

        Object[] createScrapbookParams;
        String createScrapbookQuery;
        if (description != null){
            createScrapbookParams = new Object[]{
                    userId, name, isMain, description
            };
            createScrapbookQuery = ""
                    + "INSERT INTO scrapbook "
                    + "            (user_id, "
                    + "             name, "
                    + "             is_main, "
                    + "             description) "
                    + "VALUES     (?, "
                    + "            ?, "
                    + "            ?, "
                    + "            ?);";
        }else {
            createScrapbookParams = new Object[]{
                    userId, name, isMain
            };
            createScrapbookQuery = ""
                    + "INSERT INTO scrapbook "
                    + "            (user_id, "
                    + "             name, "
                    + "             is_main) "
                    + "VALUES     (?, "
                    + "            ?, "
                    + "            ?);";
        }
        this.jdbcTemplate.update(createScrapbookQuery, createScrapbookParams);
        String lastInsertIdQuery = "select last_insert_id()";
        Long recentId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,long.class);

        return new PostBookmarksRes(recentId);
    }



    // 스크랩북 수정하기
    @Transactional
    public PatchBookmarksRes updateScrapbook(PatchBookmarksReq patchBookmarksReq) {

        Object[] createScrapbookParams = new Object[]{
                patchBookmarksReq.getName(),
                patchBookmarksReq.getDescription(),
                patchBookmarksReq.getScrapbookId()
        };
        String createScrapbookQuery = ""
                + "UPDATE scrapbook "
                + "SET    name = ?, "
                + "       description = ? "
                + "WHERE  id = ? ";

        Integer result = this.jdbcTemplate.update(createScrapbookQuery, createScrapbookParams);

        return new PatchBookmarksRes(patchBookmarksReq.getScrapbookId(), patchBookmarksReq.getName(), patchBookmarksReq.getDescription(), result);

    }
    
    // 스크랩북 삭제하기(삭제하고 내부에 속한 피드들은 메인으로 옮기기)
    @Transactional
    public DeleteBookmarksRes deleteScrapbook(DeleteBookmarksReq deleteBookmarksReq, Long mainScrapbookId) {
        Object[] deleteScrapbookParams = new Object[]{
                deleteBookmarksReq.getScrapbookId()
        };
        String deleteScrapbookQuery = ""
                + "DELETE FROM scrapbook "
                + "WHERE  id = ? ";



        Integer result = this.jdbcTemplate.update(deleteScrapbookQuery, deleteScrapbookParams);
        Integer migrateResult = migrateFeedInScrapbook(deleteBookmarksReq.getScrapbookId(), mainScrapbookId);

        return new DeleteBookmarksRes(deleteBookmarksReq.getScrapbookId(), result);
    }

    // 특정 스크랩북에 있는 피드 전체를 다른 스크랩북으로 다 옮기기
    @Transactional
    public Integer migrateFeedInScrapbook(Long fromScrapbookId, Long toScrapbookId) {
        Object[] migrateFeedInScrapbookParams = new Object[]{
                toScrapbookId,
                fromScrapbookId
        };
        String migrateFeedInScrapbookQuery = ""
                + "UPDATE scrapbook_feed "
                + "SET    scrapbook_id = ? "
                + "WHERE  scrapbook_id = ? ";

        Integer result = this.jdbcTemplate.update(migrateFeedInScrapbookQuery, migrateFeedInScrapbookParams);

        return result;
    }


    // 스크랩북 상단 정보 조회
    public GetBookmarksScrapbookTopRes retrieveScrapbookTop(Long scrapbookId) {
        String retrieveScrapbookTopQuery = ""
                + "SELECT main.id, "
                + "       main.name, "
                + "       main.description, "
                + "       user.id as user_id, "
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
                (rs, rowNum) -> new GetBookmarksScrapbookTopRes(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getLong("user_id"),
                        rs.getString("profile_image_url"),
                        rs.getString("nickname"),
                        rs.getInt("all_count"),
                        rs.getInt("media_count"),
                        rs.getInt("homewarming_count"),
                        rs.getInt("knowhow_count")
                ), scrapbookId);
    }

    public List<GetBookmarksScrapbook> retrieveSubScrapbook(Long userId, Long cursor) {
        Object[] retrieveSubScrapbookParams = new Object[]{
                userId, cursor
        };
        String retrieveSubScrapbookQuery = ""
                + "SELECT main.id, "
                + "       main.name, "
                + "       main.description, "
                + "       IF (feed_count > 0, feed_count, 0) AS feed_count, "
                + "       CASE "
                + "         WHEN feed.is_media_feed = 1 "
                + "              AND (SELECT media_feed.is_photo "
                + "                   FROM   media_feed "
                + "                   WHERE  media_feed.feed_id = feed.id) = 1 THEN (SELECT url "
                + "                                                                  FROM   media "
                + "                                                                  WHERE "
                + "         media.id = (SELECT media_id "
                + "                     FROM   feed_having_media "
                + "                     WHERE  feed_having_media.feed_id = feed.id "
                + "                     ORDER  BY feed_having_media.created_at "
                + "                     LIMIT  1)) "
                + "         WHEN feed.is_media_feed = 1 "
                + "              AND (SELECT media_feed.is_video "
                + "                   FROM   media_feed "
                + "                   WHERE  media_feed.feed_id = feed.id) = 1 THEN (SELECT "
                + "         thumbnail_url "
                + "                                                                  FROM   media "
                + "                                                                  WHERE "
                + "         media.id = (SELECT media_id "
                + "                     FROM   feed_having_media "
                + "                     WHERE  feed_having_media.feed_id = feed.id "
                + "                     LIMIT  1)) "
                + "         WHEN feed.is_photo = 1 THEN (SELECT url "
                + "                                      FROM   media "
                + "                                      WHERE  media.feed_id = feed.id) "
                + "         WHEN feed.is_video = 1 THEN (SELECT thumbnail_url "
                + "                                      FROM   media "
                + "                                      WHERE  media.feed_id = feed.id) "
                + "         WHEN feed.is_homewarming = 1 THEN feed.thumbnail_url "
                + "         WHEN feed.is_knowhow = 1 THEN feed.thumbnail_url "
                + "         ELSE NULL "
                + "       end                                AS thumbnail, "
                + "       main.created_at + 0                AS standard "
                + "FROM   (SELECT *, "
                + "               (SELECT feed_id "
                + "                FROM   scrapbook_feed "
                + "                WHERE  scrapbook_feed.scrapbook_id = scrapbook.id "
                + "                ORDER  BY created_at DESC "
                + "                LIMIT  1) AS feed_id "
                + "        FROM   scrapbook) AS main "
                + "       LEFT JOIN feed "
                + "              ON feed.id = main.feed_id "
                + "       LEFT JOIN (SELECT Count(*) AS feed_count, "
                + "                         scrapbook_id "
                + "                  FROM   scrapbook_feed "
                + "                  GROUP  BY scrapbook_id) forFeedCount "
                + "              ON main.id = forFeedCount.scrapbook_id "
                + "WHERE  main.is_main != 1 "
                + "       AND main.user_id = ? "
                + "       AND main.created_at + 0 < ? "
                + "ORDER  BY main.created_at DESC "
                + "LIMIT  10 ";

        return this.jdbcTemplate.query(retrieveSubScrapbookQuery,
                (rs, rowNum) -> new GetBookmarksScrapbook(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("feed_count"),
                        rs.getString("thumbnail"),
                        rs.getLong("standard")
                ), retrieveSubScrapbookParams);
    }


}
