
package shop.ozip.dev.src.feed;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.feed.model.*;
import shop.ozip.dev.src.keyword.KeywordDao;
import shop.ozip.dev.src.feed.model.GetFeedsMediasNineRes;
import shop.ozip.dev.utils.Common;


import javax.sql.DataSource;
import java.util.List;


@Repository
public class FeedDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource, KeywordDao keywordDao){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    /* 조회하는 메소드명
    DTO 조회하는 메소드명을 get으로 함
    존재 여부를 체크하는 메소드명을 check로 함
    그 외 실제 응답을 만드는 메소드는 retrieve로 함
    */


    // 특정 피드 가져오기
    public Feed getFeedById(Long id){
        String getFeedByIdQuery = ""
                + "SELECT * "
                + "FROM   feed "
                + "WHERE  id = ?;";
        return this.jdbcTemplate.queryForObject(getFeedByIdQuery,
                (rs, rowNum) -> new Feed(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("thumbnail_url"),
                        rs.getInt("view_count"),
                        rs.getInt("share_count"),
                        rs.getInt("is_media_feed"),
                        rs.getInt("is_photo"),
                        rs.getInt("is_video"),
                        rs.getInt("is_homewarming"),
                        rs.getInt("is_qna"),
                        rs.getInt("is_knowhow"),
                        rs.getInt("is_product"),
                        Common.formatTimeStamp(rs.getTimestamp("created_at")),
                        Common.formatTimeStamp(rs.getTimestamp("updated_at"))
                ), id);
    }
    // 특정 피드 존재하는지 체크
    public boolean checkFeedExistById(Long id) {
        String checkFeedExistByIdQuery = ""
                + "SELECT EXISTS (SELECT * "
                + "               FROM   feed "
                + "               WHERE  id = ?) AS exist;";

        return this.jdbcTemplate.queryForObject(checkFeedExistByIdQuery, Boolean.class, id);
    }


    // 특정 미디어 피드 가져오기
    public MediaFeed getMediaFeedByFeedId(Long feedId){
        String getMediaFeedByFeedIdQuery = ""
                + "SELECT * "
                + "FROM   media_feed "
                + "WHERE  feed_id = ?;";
        return this.jdbcTemplate.queryForObject(getMediaFeedByFeedIdQuery,
                (rs, rowNum) -> new MediaFeed(
                        rs.getLong("id"),
                        rs.getLong("feed_id"),
                        rs.getInt("is_photo"),
                        rs.getInt("is_video"),
                        rs.getLong("media_feed_acreage_type_id"),
                        rs.getLong("media_feed_home_type_id"),
                        rs.getLong("media_feed_style_type_id"),
                        Common.formatTimeStamp(rs.getTimestamp("created_at")),
                        Common.formatTimeStamp(rs.getTimestamp("updated_at"))
                ), feedId);
    }
    
    // 특정 미디어 피드에 담긴 미디어(사진)들 가져오기
    public List<Media> getMediaListByFeedId(Long feedId){
        String getMediaListFeedIdQuery = ""
                + "SELECT media.id                  AS id, "
                + "       media.feed_id             AS feed_id, "
                + "       media.description         AS description, "
                + "       media.url                 AS url, "
                + "       media.media_space_type_id AS media_space_type_id, "
                + "       media.is_photo            AS is_photo, "
                + "       media.is_video            AS is_video, "
                + "       media.created_at          AS created_at, "
                + "       media.updated_at          AS updated_at "
                + "FROM   media "
                + "       JOIN feed_having_media "
                + "         ON id = media_id "
                + "WHERE  feed_having_media.feed_id = ?;";
        return this.jdbcTemplate.query(getMediaListFeedIdQuery,
                (rs, rowNum) -> new Media(
                        rs.getLong("id"),
                        rs.getLong("feed_id"),
                        rs.getString("description"),
                        rs.getString("url"),
                        rs.getLong("media_space_type_id"),
                        rs.getInt("is_photo"),
                        rs.getInt("is_video"),
                        Common.formatTimeStamp(rs.getTimestamp("created_at")),
                        Common.formatTimeStamp(rs.getTimestamp("updated_at"))
                ), feedId);
    }

    public List<GetFeedsMediaFeedsListRes> retrieveMediaFeedList(Long lastValue, Long userId) {
        // TODO: 페이지네이션
        String retrieveMediaFeedListQuery = ""
                + "SELECT feed.*, "
                + "       IF((SELECT is_photo "
                + "           FROM   media_feed "
                + "           WHERE  media_feed.feed_id = feed.id) = 1, (SELECT url "
                + "                                                      FROM   media "
                + "                                                      WHERE "
                + "       media.id = (SELECT media_id "
                + "                   FROM   feed_having_media "
                + "                   WHERE  feed_having_media.feed_id = feed.id "
                + "                   ORDER  BY feed_having_media.created_at "
                + "                   LIMIT  1)), 1)      AS thumbnail, "
                + "       IF((SELECT is_photo "
                + "           FROM   media_feed "
                + "           WHERE  media_feed.feed_id = feed.id) = 1, (SELECT description "
                + "                                                      FROM   media "
                + "                                                      WHERE "
                + "       media.id = (SELECT media_id "
                + "                   FROM   feed_having_media "
                + "                   WHERE  feed_having_media.feed_id = feed.id "
                + "                   ORDER  BY feed_having_media.created_at "
                + "                   LIMIT  1)), 1)      AS description, "
                + "       feed.id IN (SELECT feed_id "
                + "                   FROM   scrapbook_feed "
                + "                          JOIN scrapbook "
                + "                            ON scrapbook_feed.scrapbook_id = scrapbook.id "
                + "                   WHERE  user_id = ?) AS is_bookmarked "
                + "FROM   feed "
                + "WHERE  is_media_feed = 1;";

        return this.jdbcTemplate.query(retrieveMediaFeedListQuery,
                (rs, rowNum) -> new GetFeedsMediaFeedsListRes(
                        rs.getLong("id"),
                        rs.getString("thumbnail"),
                        rs.getString("description"),
                        rs.getInt("is_bookmarked"),
                        rs.getInt("is_media_feed"),
                        rs.getInt("is_photo"),
                        rs.getInt("is_video"),
                        rs.getInt("is_homewarming"),
                        rs.getInt("is_knowhow"),
                        rs.getInt("is_qna"),
                        rs.getInt("is_product")
                ), userId);

    }
    
    // 인기 섹션 1, 2, 3 구하기
    public List<GetFeedsHotsRes> retrieveHotsFeedSection(Long userId, Integer i) {
        String retrieveHotsFeedSectionQuery;
        Object[] retrieveHotsFeedSectionParams;
        if (i == 1) {
            retrieveHotsFeedSectionQuery = ""
                    + "SELECT feed.id                         AS id, "
                    + "       feed.thumbnail_url              AS thumbnail_url, "
                    + "       homewarming_feed.description    AS description, "
                    + "       homewarming_feed.title          AS title, "
                    + "       feed.id IN (SELECT feed_id "
                    + "                   FROM   scrapbook_feed "
                    + "                          JOIN scrapbook "
                    + "                            ON scrapbook_feed.scrapbook_id = scrapbook.id "
                    + "                   WHERE  user_id = ?) AS is_bookmarked, "
                    + "       is_homewarming, "
                    + "       is_knowhow "
                    + "FROM   feed "
                    + "       JOIN homewarming_feed "
                    + "         ON homewarming_feed.feed_id = feed.id "
                    + "WHERE  feed.is_homewarming = 1 "
                    + "LIMIT  4 offset 0;";
            retrieveHotsFeedSectionParams = new Object[]{userId};
        }
        else if (i == 2) {
            retrieveHotsFeedSectionQuery = ""
                    + "SELECT feed.id                         AS id, "
                    + "       feed.thumbnail_url              AS thumbnail_url, "
                    + "       knowhow_feed.description        AS description, "
                    + "       knowhow_feed.title              AS title, "
                    + "       feed.id IN (SELECT feed_id "
                    + "                   FROM   scrapbook_feed "
                    + "                          JOIN scrapbook "
                    + "                            ON scrapbook_feed.scrapbook_id = scrapbook.id "
                    + "                   WHERE  user_id = ?) AS is_bookmarked, "
                    + "       is_homewarming, "
                    + "       is_knowhow "
                    + "FROM   feed "
                    + "       JOIN knowhow_feed "
                    + "         ON knowhow_feed.feed_id = feed.id "
                    + "WHERE  feed.is_knowhow = 1 "
                    + "LIMIT  4 offset 0;";
            retrieveHotsFeedSectionParams = new Object[]{userId};
        }
        else {
            retrieveHotsFeedSectionQuery = ""
                    + "(SELECT feed.id                         AS id, "
                    + "        feed.thumbnail_url              AS thumbnail_url, "
                    + "        homewarming_feed.description    AS description, "
                    + "        homewarming_feed.title          AS title, "
                    + "        feed.id IN (SELECT feed_id "
                    + "                    FROM   scrapbook_feed "
                    + "                           JOIN scrapbook "
                    + "                             ON scrapbook_feed.scrapbook_id = scrapbook.id "
                    + "                    WHERE  user_id = ?) AS is_bookmarked, "
                    + "        is_homewarming, "
                    + "        is_knowhow "
                    + " FROM   feed "
                    + "        JOIN homewarming_feed "
                    + "          ON homewarming_feed.feed_id = feed.id "
                    + " WHERE  feed.is_homewarming = 1 "
                    + " LIMIT  2 offset 4) "
                    + "UNION "
                    + "(SELECT feed.id                         AS id, "
                    + "        feed.thumbnail_url              AS thumbnail_url, "
                    + "        knowhow_feed.description        AS description, "
                    + "        knowhow_feed.title              AS title, "
                    + "        feed.id IN (SELECT feed_id "
                    + "                    FROM   scrapbook_feed "
                    + "                           JOIN scrapbook "
                    + "                             ON scrapbook_feed.scrapbook_id = scrapbook.id "
                    + "                    WHERE  user_id = ?) AS is_bookmarked, "
                    + "        is_homewarming, "
                    + "        is_knowhow "
                    + " FROM   feed "
                    + "        JOIN knowhow_feed "
                    + "          ON knowhow_feed.feed_id = feed.id "
                    + " WHERE  feed.is_knowhow = 1 "
                    + " LIMIT  2 offset 4);";
            retrieveHotsFeedSectionParams = new Object[]{userId, userId};
        }
        return this.jdbcTemplate.query(retrieveHotsFeedSectionQuery,
                (rs, rowNum) -> new GetFeedsHotsRes(
                        rs.getLong("id"),
                        rs.getString("thumbnail_url"),
                        rs.getString("description"),
                        rs.getString("title"),
                        rs.getInt("is_bookmarked"),
                        rs.getInt("is_homewarming"),
                        rs.getInt("is_knowhow")
                ), retrieveHotsFeedSectionParams);
    }

    public List<GetFeedsHotsKeywordResMedia> retrieveHotsKeywordMediaList(Long userId) {
        String retrieveHotsKeywordMediaListQuery = ""
                + "SELECT   media.url, "
                + "         media.feed_id, "
                + "         user.nickname, "
                + "         feed.id IN "
                + "         ( "
                + "                SELECT feed_id "
                + "                FROM   scrapbook_feed "
                + "                JOIN   scrapbook "
                + "                ON     scrapbook_feed.scrapbook_id = scrapbook.id "
                + "                WHERE  user_id = ?) AS is_bookmarked "
                + "FROM     feed "
                + "JOIN "
                + "         ( "
                + "                  SELECT   keyword_id, "
                + "                           Count(*) AS count "
                + "                  FROM     feed_having_keyword "
                + "                  JOIN     feed "
                + "                  ON       feed.id = feed_having_keyword.feed_id "
                + "                  AND      feed.is_photo = 1 "
                + "                  GROUP BY keyword_id "
                + "                  ORDER BY count DESC "
                + "                  LIMIT    1) a "
                + "JOIN     feed_having_keyword "
                + "ON       feed.id = feed_having_keyword.feed_id "
                + "AND      a.keyword_id = feed_having_keyword.keyword_id "
                + "JOIN     user "
                + "ON       feed.user_id = user.id "
                + "JOIN     media "
                + "ON       media.feed_id = feed.id "
                + "order BY view_count DESC "
                + "LIMIT    5;";
        return this.jdbcTemplate.query(retrieveHotsKeywordMediaListQuery,
                (rs, rowNum) -> new GetFeedsHotsKeywordResMedia(
                        rs.getLong("feed_id"),
                        rs.getString("url"),
                        rs.getString("nickname"),
                        rs.getInt("is_bookmarked")
                ), userId);
    }

    public List<GetFeedsHotsPhotoRes> retrieveHotsPhotoSection(Long userId) {
        String retrieveHotsPhotoSectionQuery = ""
                + "SELECT @rownum := @rownum + 1 AS number, "
                + "       b.* "
                + "FROM   (SELECT feed.id, "
                + "               IF((SELECT is_photo "
                + "                   FROM   media_feed "
                + "                   WHERE  media_feed.feed_id = feed.id) = 1, (SELECT url "
                + "                                                              FROM   media "
                + "                                                              WHERE "
                + "               media.id = (SELECT media_id "
                + "                           FROM   feed_having_media "
                + "                           WHERE  feed_having_media.feed_id = feed.id "
                + "                           ORDER  BY feed_having_media.created_at "
                + "                           LIMIT  1)), 1)      AS thumbnail, "
                + "               user.nickname, "
                + "               feed.id IN (SELECT feed_id "
                + "                           FROM   scrapbook_feed "
                + "                                  JOIN scrapbook "
                + "                                    ON scrapbook_feed.scrapbook_id = "
                + "                                       scrapbook.id "
                + "                           WHERE  user_id = ?) AS is_bookmarked "
                + "        FROM   feed "
                + "               JOIN media_feed "
                + "                 ON media_feed.feed_id = feed.id "
                + "               JOIN user "
                + "                 ON feed.user_id = user.id "
                + "        WHERE  feed.is_media_feed = 1 "
                + "               AND media_feed.is_photo = 1 "
                + "        ORDER  BY feed.view_count DESC "
                + "        LIMIT  10) b "
                + "WHERE  ( @rownum := 0 ) = 0;";
        return this.jdbcTemplate.query(retrieveHotsPhotoSectionQuery,
                (rs, rowNum) -> new GetFeedsHotsPhotoRes(
                        rs.getInt("number"),
                        rs.getLong("id"),
                        rs.getString("thumbnail"),
                        rs.getString("nickname"),
                        rs.getInt("is_bookmarked")
                ), userId);
    }

    // 유저별 미디어 9개 조회
    public GetFeedsMediasNineRes retrieveFeedsMediasForNine(Long userId, Integer type) {
        String queryAttachment = "";
        if (type != 0) {
            queryAttachment = "       AND media_space_type.id = " + type.toString() + " ";
        }
        String retrieveFeedsMediasForNineQuery =""
                + "SELECT IF (media.is_photo = 1, media.url, media.thumbnail_url) AS url, "
                + "       media_space_type.name, "
                + "       user.id, "
                + "       user.nickname "
                + "FROM   media "
                + "       JOIN feed "
                + "         ON feed.id = media.feed_id "
                + "       JOIN user "
                + "         ON feed.user_id = user.id "
                + "       JOIN media_space_type "
                + "         ON media.media_space_type_id = media_space_type.id "
                + "WHERE  user_id = ? "
                + queryAttachment
                + "ORDER  BY media.created_at DESC "
                + "LIMIT  1";
        String checkGetUsersMediasQuery = "SELECT EXISTS ("+retrieveFeedsMediasForNineQuery+") AS exist";
        if (!this.jdbcTemplate.queryForObject(checkGetUsersMediasQuery, boolean.class, userId)){
            return new GetFeedsMediasNineRes(null, null, null, null);
        }
        GetFeedsMediasNineRes getFeedsMediasNineRes = this.jdbcTemplate.queryForObject(retrieveFeedsMediasForNineQuery,
                (rs, rowNum) -> new GetFeedsMediasNineRes(
                        rs.getObject("url", String.class),
                        rs.getObject("name", String.class),
                        rs.getObject("id", Long.class),
                        rs.getObject("nickname", String.class)),
                userId);
        return getFeedsMediasNineRes;
    }
    
    // 특정 유저가 업로드한 미디어 개수
    public Integer retrieveFeedsMediasCount(Long userId) {
        String retrieveFeedsMediasCountQuery =""
                + "SELECT Count(*) "
                + "FROM   media "
                + "       JOIN feed "
                + "         ON feed.id = media.feed_id "
                + "WHERE  feed.user_id = ?";
        return this.jdbcTemplate.queryForObject(retrieveFeedsMediasCountQuery, Integer.class, userId);
    }
}
