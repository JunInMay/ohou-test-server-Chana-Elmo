
package shop.ozip.dev.src.feed;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.feed.model.*;
import shop.ozip.dev.src.keyword.KeywordDao;
import shop.ozip.dev.src.feed.model.GetFeedsMediasNineRes;
import shop.ozip.dev.utils.Common;


import javax.sql.DataSource;
import java.util.ArrayList;
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
    public List<GetFeedsMediaFeedsResMedia> retrieveMediaFeedMedias(Long userId, Long feedId){
        String retrieveMediaFeedMediasQuery = ""
                + "SELECT media.*, "
                + "       media.feed_id IN (SELECT feed_id "
                + "                         FROM   scrapbook_feed "
                + "                                JOIN scrapbook "
                + "                                  ON scrapbook_feed.scrapbook_id = scrapbook.id "
                + "                         WHERE  user_id = ?) AS is_bookmarked "
                + "FROM   media "
                + "       JOIN feed_having_media "
                + "         ON id = media_id "
                + "WHERE  feed_having_media.feed_id = ?;";
        Object[] retrieveMediaFeedMediasParams = new Object[]{
                userId, feedId
        };
        return this.jdbcTemplate.query(retrieveMediaFeedMediasQuery,
                (rs, rowNum) -> new GetFeedsMediaFeedsResMedia(
                        rs.getLong("id"),
                        rs.getLong("feed_id"),
                        rs.getString("thumbnail_url"),
                        rs.getInt("time"),
                        rs.getString("description"),
                        rs.getString("url"),
                        rs.getLong("media_space_type_id"),
                        rs.getInt("is_photo"),
                        rs.getInt("is_video"),
                        Common.formatTimeStamp(rs.getTimestamp("created_at")),
                        Common.formatTimeStamp(rs.getTimestamp("updated_at")),
                        rs.getInt("is_bookmarked")
                ), retrieveMediaFeedMediasParams);
    }
    
    // 해당 피드에 담긴 사진들 URL리스트만 가져오기
    public List<JustPhotoUrl> getJustPhotoUrlByFeedId(Long feedId){
        String getJustPhotoUrlByFeedIdQuery = ""
                + "SELECT CASE "
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
                + "       end AS url, media.id "
                + "FROM   feed "
                + "       LEFT JOIN feed_having_media "
                + "              ON feed.id = feed_having_media.feed_id "
                + "       LEFT JOIN media "
                + "              ON feed_having_media.media_id = media.id "
                + "WHERE  feed.id = ?";
        return this.jdbcTemplate.query(getJustPhotoUrlByFeedIdQuery,
                (rs, rowNum) -> new JustPhotoUrl(
                        rs.getString("url")
                ), feedId);
    }
    
    // 미디어피드 리스트 조회
    public List<GetFeedsMediaFeedsListRes> retrieveMediaFeedList(Long cursor, Long userId, Integer sort, Integer video, Integer homeType, Integer style) {
        // 최근 인기순
        String sortByBestNewest = "";
        // 역대 인기순
        String sortByBest = "";
        // 최신순
        String sortByNewest = "";
        // 팔로우
        String sortByFollow = "";

        // 가져올 cursor (cursor가 예약어라 standard로 함)
        String standardColumn = "";

        if (sort == 1){
            sortByBestNewest = " AND feed.view_count + Round(feed.created_at/10, 0) < ? ORDER BY standard1 DESC ";
            standardColumn = "standard1";
        } else if (sort == 2) {
            sortByBest = " AND Concat(Lpad(view_count, 8, '0'), Lpad(feed.id, 8, '0')) < ? ORDER BY standard2 DESC ";
            standardColumn = "standard2";
        } else if (sort == 3){
            sortByNewest = " AND (SELECT CONVERT(feed.created_at, signed INTEGER)) < ? ORDER BY standard3 DESC ";
            standardColumn = "standard3";
        } else {
            sortByFollow = ""
                    + "JOIN     follow_user "
                    + "ON       feed.user_id = following_id "
                    + "AND      follow_user.user_id = "+userId.toString()+" ";
            standardColumn = "standard3";
            sortByNewest = " AND (SELECT CONVERT(feed.created_at, signed INTEGER)) < ? ORDER BY standard3 DESC ";
        }

        // 동영상만 필터
        String filterByVideo = "";
        // 주거형태 필터
        String filterByHomeType = "";
        // 스타일 필터
        String filterByStyle = "";

        if (video != 0) {
            filterByVideo = "         and media_feed.is_video=1 ";
        }
        if (homeType != 0) {
            filterByHomeType = "          and media_feed_home_type_id="+ homeType.toString() +" ";
        }
        if (style != 0) {
            filterByStyle = "          and media_feed_style_type_id="+ style.toString() +" ";
        }
        Object[] retrieveMediaFeedListParams = new Object[]{
                userId, userId, cursor
        };
        String retrieveMediaFeedListQuery = ""
                + "SELECT   feed.*, "
                + "          IF (like_count=NULL, 0, like_count)         AS like_count, "
                + "          IF (scrapped_count=NULL, 0, scrapped_count) AS scrapped_count, "
                + "          (EXISTS "
                + "          ( "
                + "                 SELECT * "
                + "                 FROM   follow_user "
                + "                 WHERE  user_id = ? "
                + "                 AND    following_id=user.id )) AS is_followed, "
                + "          user.description as user_description , "
                + "          share_count, "
                + "          IF (comment_count>0, comment_count, 0) AS comment_count, "
                + "          recent_comment_user_profile_image_url, "
                + "          recent_comment_user_nickname, "
                + "          recent_comment_content, "
                + "         CASE "
                + "                  WHEN feed.is_media_feed = 1 "
                + "                  AND "
                + "                           ( "
                + "                                  SELECT media_feed.is_photo "
                + "                                  FROM   media_feed "
                + "                                  WHERE  media_feed.feed_id = feed.id) = 1 THEN "
                + "                           ( "
                + "                                  SELECT url "
                + "                                  FROM   media "
                + "                                  WHERE  media.id = "
                + "                                         ( "
                + "                                                  SELECT   media_id "
                + "                                                  FROM     feed_having_media "
                + "                                                  WHERE    feed_having_media.feed_id = feed.id "
                + "                                                  ORDER BY feed_having_media.created_at "
                + "                                                  LIMIT    1)) "
                + "                  WHEN feed.is_media_feed = 1 "
                + "                  AND "
                + "                           ( "
                + "                                  SELECT media_feed.is_video "
                + "                                  FROM   media_feed "
                + "                                  WHERE  media_feed.feed_id = feed.id) = 1 THEN "
                + "                           ( "
                + "                                  SELECT thumbnail_url "
                + "                                  FROM   media "
                + "                                  WHERE  media.id = "
                + "                                         ( "
                + "                                                SELECT media_id "
                + "                                                FROM   feed_having_media "
                + "                                                WHERE  feed_having_media.feed_id = feed.id "
                + "                                                LIMIT  1)) "
                + "                  WHEN feed.is_photo = 1 THEN "
                + "                           ( "
                + "                                  SELECT url "
                + "                                  FROM   media "
                + "                                  WHERE  media.feed_id = feed.id) "
                + "                  WHEN feed.is_video = 1 THEN "
                + "                           ( "
                + "                                  SELECT thumbnail_url "
                + "                                  FROM   media "
                + "                                  WHERE  media.feed_id = feed.id) "
                + "                  WHEN feed.is_homewarming = 1 THEN feed.thumbnail_url "
                + "                  WHEN feed.is_knowhow = 1 THEN feed.thumbnail_url "
                + "                  ELSE NULL "
                + "         end AS thumbnail, "
                + "         ( "
                + "                SELECT description "
                + "                FROM   media "
                + "                WHERE  media.id = "
                + "                       ( "
                + "                                SELECT   media_id "
                + "                                FROM     feed_having_media "
                + "                                WHERE    feed_having_media.feed_id = feed.id "
                + "                                ORDER BY feed_having_media.created_at "
                + "                                LIMIT    1)) AS description, IF ( "
                + "                                                                   ( "
                + "                                                                   SELECT is_video "
                + "                                                                   FROM   media_feed "
                + "                                                                   WHERE  media_feed.feed_id = feed.id) = 1, "
                + "                                                                 ( "
                + "                                                                        SELECT time "
                + "                                                                        FROM   media "
                + "                                                                        WHERE  media.id = "
                + "                                                                               ( "
                + "                                                                                      SELECT media_id "
                + "                                                                                      FROM   feed_having_media "
                + "                                                                                      WHERE  feed_having_media.feed_id = feed.id "
                + "                                                                                      LIMIT  1)), NULL ) AS video_time, "
                + "         feed.id IN "
                + "         ( "
                + "                SELECT feed_id "
                + "                FROM   scrapbook_feed "
                + "                JOIN   scrapbook "
                + "                ON     scrapbook_feed.scrapbook_id = scrapbook.id "
                + "                WHERE  user_id = ?)                     AS is_bookmarked, "
                + "         feed.view_count + Round(feed.created_at/10, 0) AS standard1, "
                + "Concat(Lpad(view_count, 8, '0'), Lpad(feed.id, 8, '0')) AS standard2, "
                + "      (SELECT CONVERT(feed.created_at, signed INTEGER)) AS standard3 "
                + "FROM     feed "
                + "JOIN     media_feed "
                + "ON       feed.id = media_feed.feed_id "
                + "LEFT JOIN "
                + "          ( "
                + "                   SELECT   Count(*) AS like_count, "
                + "                            feed_id "
                + "                   FROM     like_feed "
                + "                   GROUP BY feed_id) forLikeCount "
                + "ON        feed.id = forLikeCount.feed_id "
                + "LEFT JOIN "
                + "          ( "
                + "                   SELECT   Count(*) AS scrapped_count, "
                + "                            feed_id "
                + "                   FROM     scrapbook_feed "
                + "                   GROUP BY feed_id) forScrappedCount "
                + "ON        feed.id = forScrappedCount.feed_id "
                + "LEFT JOIN "
                + "          ( "
                + "                   SELECT   Count(*) AS comment_count, "
                + "                            feed_id "
                + "                   FROM     comment "
                + "                   GROUP BY feed_id) forCommentCount "
                + "ON        feed.id = forCommentCount.feed_id "
                + "LEFT JOIN "
                + "          ( "
                + "                   SELECT   comment.feed_id , "
                + "                            user.profile_image_url AS recent_comment_user_profile_image_url, "
                + "                            user.nickname          AS recent_comment_user_nickname, "
                + "                            comment.content           recent_comment_content "
                + "                   FROM     comment "
                + "                   JOIN     user "
                + "                   ON       user.id = comment.user_id "
                + "                   WHERE    is_recomment = 0 "
                + "                   GROUP BY feed_id "
                + "                   ORDER BY comment.created_at DESC ) forRecentComment "
                + "ON        forRecentComment.feed_id = feed.id "
                + "JOIN      user "
                + "ON        feed.user_id = user.id "
                + sortByFollow
                + "WHERE    is_media_feed = 1 "
                + filterByVideo
                + filterByHomeType
                + filterByStyle
                + sortByBestNewest
                + sortByBest
                + sortByNewest
                + "LIMIT 5 ";
        String finalStandardColumn = standardColumn;
        List<GetFeedsMediaFeedsListResBase> getFeedsMediaFeedsListResBaseList =  this.jdbcTemplate.query(retrieveMediaFeedListQuery,
                (rs, rowNum) -> new GetFeedsMediaFeedsListResBase(
                        rs.getLong("id"),
                        rs.getInt("like_count"),
                        rs.getInt("scrapped_count"),
                        rs.getInt("is_followed"),
                        rs.getString("user_description"),
                        rs.getInt("share_count"),
                        rs.getInt("comment_count"),
                        rs.getString("recent_comment_user_profile_image_url"),
                        rs.getString("recent_comment_user_nickname"),
                        rs.getString("recent_comment_content"),
                        rs.getString("thumbnail"),
                        rs.getString("description"),
                        rs.getInt("video_time"),
                        rs.getInt("is_bookmarked"),
                        rs.getLong(finalStandardColumn),
                        rs.getInt("is_media_feed")
                ), retrieveMediaFeedListParams);
        String getFeedsMediaFeedsListResPhotoQuery = ""
                + "SELECT feed.id, "
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
                + "       end AS url "
                + "FROM   feed "
                + "       LEFT JOIN feed_having_media "
                + "              ON feed.id = feed_having_media.feed_id "
                + "       LEFT JOIN media "
                + "              ON feed_having_media.media_id = media.id "
                + "WHERE  feed.id = ?";

        List<GetFeedsMediaFeedsListRes> getFeedsMediaFeedsListResList = new ArrayList<>();
        for (int i=0; i<getFeedsMediaFeedsListResBaseList.size();i++ ){
            List<JustPhotoUrl> justPhotoUrlList = getJustPhotoUrlByFeedId(getFeedsMediaFeedsListResBaseList.get(i).getFeedId());
            GetFeedsMediaFeedsListRes getFeedsMediaFeedsListRes = new GetFeedsMediaFeedsListRes(
                    getFeedsMediaFeedsListResBaseList.get(i),
                    justPhotoUrlList);
            getFeedsMediaFeedsListResList.add(getFeedsMediaFeedsListRes);
        }

        return getFeedsMediaFeedsListResList;
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
    
    // 키워드별 핫한 사진 조회
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
                + "where    media.is_photo = 1 "
                + "ORDER BY view_count DESC "
                + "LIMIT    5";
        return this.jdbcTemplate.query(retrieveHotsKeywordMediaListQuery,
                (rs, rowNum) -> new GetFeedsHotsKeywordResMedia(
                        rs.getLong("feed_id"),
                        rs.getString("url"),
                        rs.getString("nickname"),
                        rs.getInt("is_bookmarked")
                ), userId);
    }
    
    // 인기있는 사진 미디어 피드 10장
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
                + "                           LIMIT  1)), (SELECT thumbnail_url "
                + "                                        FROM   media "
                + "                                        WHERE "
                + "               media.id = (SELECT media_id "
                + "                           FROM   feed_having_media "
                + "                           WHERE "
                + "               feed_having_media.feed_id = "
                + "               feed.id "
                + "                           ORDER  BY "
                + "                          feed_having_media.created_at "
                + "                           LIMIT  1))) AS thumbnail, "
                + "               user.nickname, "
                + "               feed.id IN (SELECT feed_id "
                + "                           FROM   scrapbook_feed "
                + "                                  JOIN scrapbook "
                + "                                    ON scrapbook_feed.scrapbook_id = "
                + "                                       scrapbook.id "
                + "                           WHERE  user_id = ?)                         AS "
                + "               is_bookmarked "
                + "        FROM   feed "
                + "               JOIN media_feed "
                + "                 ON media_feed.feed_id = feed.id "
                + "               JOIN user "
                + "                 ON feed.user_id = user.id "
                + "        WHERE  feed.is_media_feed = 1 "
                + "               AND media_feed.is_photo = 1 "
                + "        ORDER  BY feed.view_count DESC "
                + "        LIMIT  10) b "
                + "WHERE  ( @rownum := 0 ) = 0";
        return this.jdbcTemplate.query(retrieveHotsPhotoSectionQuery,
                (rs, rowNum) -> new GetFeedsHotsPhotoRes(
                        rs.getInt("number"),
                        rs.getLong("id"),
                        rs.getString("thumbnail"),
                        rs.getString("nickname"),
                        rs.getInt("is_bookmarked")
                ), userId);
    }

    // 인기있는 비디오 미디어 피드 10장
    public List<GetFeedsHotsVideoRes> retrieveHotsVideoSection(Long userId) {
        String retrieveHotsVideoSectionQuery = ""
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
                + "                           LIMIT  1)), (SELECT thumbnail_url "
                + "                                        FROM   media "
                + "                                        WHERE "
                + "               media.id = (SELECT media_id "
                + "                           FROM   feed_having_media "
                + "                           WHERE "
                + "               feed_having_media.feed_id = "
                + "               feed.id "
                + "                           ORDER  BY "
                + "                          feed_having_media.created_at "
                + "                           LIMIT  1))) AS thumbnail, "
                + "               user.nickname, "
                + "               feed.id IN (SELECT feed_id "
                + "                           FROM   scrapbook_feed "
                + "                                  JOIN scrapbook "
                + "                                    ON scrapbook_feed.scrapbook_id = "
                + "                                       scrapbook.id "
                + "                           WHERE  user_id = ?)                         AS "
                + "               is_bookmarked "
                + "        FROM   feed "
                + "               JOIN media_feed "
                + "                 ON media_feed.feed_id = feed.id "
                + "               JOIN user "
                + "                 ON feed.user_id = user.id "
                + "        WHERE  feed.is_media_feed = 1 "
                + "               AND media_feed.is_video = 1 "
                + "        ORDER  BY feed.view_count DESC "
                + "        LIMIT  10) b "
                + "WHERE  ( @rownum := 0 ) = 0";
        return this.jdbcTemplate.query(retrieveHotsVideoSectionQuery,
                (rs, rowNum) -> new GetFeedsHotsVideoRes(
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


    public List<GetFeedsFollowsListRes> retrieveFeedsFollowsList(Long userId, Long cursor) {
        Object[] retrieveFeedsFollowsListParams = new Object[]{ userId, userId, userId, cursor};
        String retrieveFeedsFollowsListQuery = ""
                + "SELECT "
                + "         CASE "
                + "                  WHEN a.is_video = 1 THEN "
                + "                           ( "
                + "                                  SELECT feed_id "
                + "                                  FROM   media_feed "
                + "                                  WHERE  feed_id = "
                + "                                         ( "
                + "                                                SELECT feed_id "
                + "                                                FROM   feed_having_media "
                + "                                                WHERE  media_id = "
                + "                                                       ( "
                + "                                                              SELECT id "
                + "                                                              FROM   media "
                + "                                                              WHERE  media.feed_id = a.id) "
                + "                                                LIMIT  1)) "
                + "                  ELSE a.id "
                + "         end AS alternative_id, "
                + "         a.*, "
                + "                  Concat(Round(a.created_at/10, 0), Lpad(a.id, 5, \"0\")) AS standard, "
                + "         a.id IN "
                + "         ( "
                + "                SELECT feed_id "
                + "                FROM   scrapbook_feed "
                + "                JOIN   scrapbook "
                + "                ON     scrapbook_feed.scrapbook_id = scrapbook.id "
                + "                WHERE  user_id = ?) AS is_bookmarked "
                + "FROM     ( "
                + "                   SELECT    main.id, "
                + "                             main.is_media_feed, "
                + "                             main.is_photo, "
                + "                             main.is_video, "
                + "                             main.is_homewarming, "
                + "                             main.is_knowhow, "
                + "                             user.profile_image_url, "
                + "                             user.nickname, "
                + "                             ( "
                + "                                      SELECT   description "
                + "                                      FROM     media "
                + "                                      WHERE    media.feed_id = main.id "
                + "                                      ORDER BY media.created_at DESC "
                + "                                      LIMIT    1) AS description, keyword.name AS keyword_name, "
                + "                             CASE "
                + "                                       WHEN main.is_video = 1 THEN "
                + "                                                 ( "
                + "                                                        SELECT time "
                + "                                                        FROM   media "
                + "                                                        WHERE  main.id = media.feed_id) "
                + "                                       ELSE NULL "
                + "                             end AS video_time , "
                + "                             CASE "
                + "                                       WHEN td < 60 THEN Concat(td,\"초 전\") "
                + "                                       WHEN Round(td/60) < 60 THEN Concat(Round(td/60),\"분 전\") "
                + "                                       WHEN Round(Round(td/60)/60) < 24 THEN Concat(Round(Round(td/60)/60),\"시간 전\") "
                + "                                       WHEN Round(Round(Round(td/60)/60)/24) < 7 THEN Concat(Round(Round(Round(td/60)/60)/24),\"일 전\") "
                + "                                       WHEN Round(Round(Round(Round(td/60)/60)/24)/7) < 5 THEN Concat(Round(Round(Round(Round(td/60)/60)/24)/7),\"주 전\") "
                + "                                       WHEN Round(Round(Round(Round(td/60)/60)/24)/30) < 12 THEN Concat(Round(Round(Round(Round(td/60)/60)/24)/30),\"개월 전\") "
                + "                                       ELSE Concat(Round(Round(Round(Round(Round(td/60)/60)/24)/30)/12),\"년 전\") "
                + "                             end AS uploaded_at, "
                + "                             main.view_count, "
                + "                             IF (like_count=NULL, 0, like_count)         AS like_count, "
                + "                             IF (scrapped_count=NULL, 0, scrapped_count) AS scrapped_count, "
                + "                             IF (comment_count>0, comment_count, 0)      AS comment_count, "
                + "                             main.share_count                            AS share_count, "
                + "                             main.created_at "
                + "                   FROM      ( "
                + "                                      SELECT   Row_number() over(partition BY keyword_id ORDER BY feed.created_at DESC) rnum, "
                + "                                               feed.*, "
                + "                                               feed_having_keyword.keyword_id "
                + "                                      FROM     feed "
                + "                                      JOIN     feed_having_keyword "
                + "                                      ON       feed.id = feed_having_keyword.feed_id "
                + "                                      WHERE    feed_having_keyword.keyword_id IN "
                + "                                               ( "
                + "                                                      SELECT keyword_id "
                + "                                                      FROM   follow_keyword "
                + "                                                      WHERE  user_id = ?) "
                + "                                      AND      ( "
                + "                                                        is_photo = 1 "
                + "                                               OR       is_video = 1)) main "
                + "                   JOIN      keyword "
                + "                   ON        keyword.id = main.keyword_id "
                + "                   JOIN      user "
                + "                   ON        main.user_id = user.id "
                + "                   JOIN "
                + "                             ( "
                + "                                    SELECT timestampdiff(second, created_at, now()) AS td, "
                + "                                           id "
                + "                                    FROM   feed) fortd "
                + "                   ON        fortd.id = main.id "
                + "                   LEFT JOIN "
                + "                             ( "
                + "                                      SELECT   count(*) AS like_count, "
                + "                                               feed_id "
                + "                                      FROM     like_feed "
                + "                                      GROUP BY feed_id) forlikecount "
                + "                   ON        main.id = forlikecount.feed_id "
                + "                   LEFT JOIN "
                + "                             ( "
                + "                                      SELECT   count(*) AS scrapped_count, "
                + "                                               feed_id "
                + "                                      FROM     scrapbook_feed "
                + "                                      GROUP BY feed_id) forscrappedcount "
                + "                   ON        main.id = forscrappedcount.feed_id "
                + "                   LEFT JOIN "
                + "                             ( "
                + "                                      SELECT   count(*) AS comment_count, "
                + "                                               feed_id "
                + "                                      FROM     comment "
                + "                                      GROUP BY feed_id) forcommentcount "
                + "                   ON        main.id = forcommentcount.feed_id "
                + "                   WHERE     rnum = 1 "
                + "                   UNION "
                + "                   SELECT    feed.id, "
                + "                             feed.is_media_feed, "
                + "                             feed.is_photo, "
                + "                             feed.is_video, "
                + "                             feed.is_homewarming, "
                + "                             feed.is_knowhow, "
                + "                             user.profile_image_url, "
                + "                             user.nickname, "
                + "                             CASE "
                + "                                       WHEN feed.is_media_feed = 1 THEN "
                + "                                                 ( "
                + "                                                        SELECT description "
                + "                                                        FROM   media "
                + "                                                        WHERE  media.id = "
                + "                                                               ( "
                + "                                                                        SELECT   media_id "
                + "                                                                        FROM     feed_having_media "
                + "                                                                        WHERE    feed_having_media.feed_id = feed.id "
                + "                                                                        ORDER BY feed_having_media.created_at "
                + "                                                                        LIMIT    1)) "
                + "                                       WHEN feed.is_photo = 1 "
                + "                                       OR        feed.is_video THEN "
                + "                                                 ( "
                + "                                                        SELECT description "
                + "                                                        FROM   media "
                + "                                                        WHERE  media.feed_id = feed.id) "
                + "                                       WHEN feed.is_homewarming = 1 THEN "
                + "                                                 ( "
                + "                                                        SELECT concat(description, title) "
                + "                                                        FROM   homewarming_feed "
                + "                                                        WHERE  homewarming_feed.feed_id = feed.id) "
                + "                                       WHEN feed.is_knowhow = 1 THEN "
                + "                                                 ( "
                + "                                                        SELECT concat(description, title) "
                + "                                                        FROM   knowhow_feed "
                + "                                                        WHERE  knowhow_feed.feed_id = feed.id) "
                + "                             end  AS description, "
                + "                             NULL AS keyword_name, "
                + "                             CASE "
                + "                                       WHEN feed.is_video = 1 THEN "
                + "                                                 ( "
                + "                                                        SELECT time "
                + "                                                        FROM   media "
                + "                                                        WHERE  feed.id = media.feed_id) "
                + "                                       ELSE NULL "
                + "                             end AS video_time, "
                + "                             CASE "
                + "                                       WHEN td < 60 THEN concat(td,\"초 전\") "
                + "                                       WHEN round(td/60) < 60 THEN concat(round(td/60),\"분 전\") "
                + "                                       WHEN round(round(td/60)/60) < 24 THEN concat(round(round(td/60)/60),\"시간 전\") "
                + "                                       WHEN round(round(round(td/60)/60)/24) < 7 THEN concat(round(round(round(td/60)/60)/24),\"일 전\") "
                + "                                       WHEN round(round(round(round(td/60)/60)/24)/7) < 5 THEN concat(round(round(round(round(td/60)/60)/24)/7),\"주 전\") "
                + "                                       WHEN round(round(round(round(td/60)/60)/24)/30) < 12 THEN concat(round(round(round(round(td/60)/60)/24)/30),\"개월 전\") "
                + "                                       ELSE concat(round(round(round(round(round(td/60)/60)/24)/30)/12),\"년 전\") "
                + "                             end AS uploaded_at, "
                + "                             feed.view_count, "
                + "                             IF (like_count>0, like_count, 0)         AS like_count, "
                + "                             IF (scrapped_count>0, scrapped_count, 0) AS scrapped_count, "
                + "                             IF (comment_count>0, comment_count, 0)   AS comment_count, "
                + "                             share_count, "
                + "                             feed.created_at "
                + "                   FROM      feed "
                + "                   JOIN "
                + "                             ( "
                + "                                    SELECT timestampdiff(second, created_at, now()) AS td, "
                + "                                           id "
                + "                                    FROM   feed) fortd "
                + "                   ON        fortd.id = feed.id "
                + "                   JOIN      user "
                + "                   ON        user.id = feed.user_id "
                + "                   LEFT JOIN "
                + "                             ( "
                + "                                      SELECT   count(*) AS like_count, "
                + "                                               feed_id "
                + "                                      FROM     like_feed "
                + "                                      GROUP BY feed_id) forlikecount "
                + "                   ON        feed.id = forlikecount.feed_id "
                + "                   LEFT JOIN "
                + "                             ( "
                + "                                      SELECT   count(*) AS scrapped_count, "
                + "                                               feed_id "
                + "                                      FROM     scrapbook_feed "
                + "                                      GROUP BY feed_id) forscrappedcount "
                + "                   ON        feed.id = forscrappedcount.feed_id "
                + "                   LEFT JOIN "
                + "                             ( "
                + "                                      SELECT   count(*) AS comment_count, "
                + "                                               feed_id "
                + "                                      FROM     comment "
                + "                                      GROUP BY feed_id) forcommentcount "
                + "                   ON        feed.id = forcommentcount.feed_id "
                + "                   WHERE     feed.user_id IN "
                + "                             ( "
                + "                                    SELECT following_id "
                + "                                    FROM   follow_user "
                + "                                    WHERE  user_id = ?) "
                + "                   ORDER BY  created_at DESC) a "
                + "       WHERE    concat(round(a.created_at/10, 0), lpad(a.id, 5, \"0\")) < ? "
                + "ORDER BY standard DESC"
                + "       LIMIT    5 ";


        List<GetFeedsFollowsListResBase> getFeedsFollowsListResBasesList = this.jdbcTemplate.query(retrieveFeedsFollowsListQuery,
                (rs, rowNum) -> new GetFeedsFollowsListResBase(
                        rs.getLong("alternative_id"),
                        rs.getInt("is_media_Feed"),
                        rs.getInt("is_photo"),
                        rs.getInt("is_video"),
                        rs.getInt("is_homewarming"),
                        rs.getInt("is_knowhow"),
                        rs.getString("profile_image_url"),
                        rs.getString("nickname"),
                        rs.getString("description"),
                        rs.getString("keyword_name"),
                        rs.getInt("video_time"),
                        rs.getString("uploaded_at"),
                        rs.getInt("view_count"),
                        rs.getInt("like_count"),
                        rs.getInt("scrapped_count"),
                        rs.getInt("comment_count"),
                        rs.getInt("share_count"),
                        rs.getString("created_at"),
                        rs.getLong("standard"),
                        rs.getInt("is_bookmarked")
                ), retrieveFeedsFollowsListParams);
        List<GetFeedsFollowsListRes> getFeedsFollowsListResList = new ArrayList<>();
        for (int i = 0; i < getFeedsFollowsListResBasesList.size(); i++){
            GetFeedsFollowsListRes getFeedsFollowsListRes = new GetFeedsFollowsListRes(
                    getFeedsFollowsListResBasesList.get(i),
                    getJustPhotoUrlByFeedId(getFeedsFollowsListResBasesList.get(i).getFeedId())
            );
            getFeedsFollowsListResList.add(getFeedsFollowsListRes);
        }
        return getFeedsFollowsListResList;
    }
    
    // 집들이 리스트 조회
    public GetFeedsHomewarmingFeedsListRes retrieveHomewarmingFeedList(Long userId, Long cursor, Integer sort, Integer homeType, Integer acreageStart, Integer acreageEnd, Integer budgetStart, Integer budgetEnd, Integer family, Integer style, Integer allColor, Integer wallColor, Integer floorColor, Integer detail, Integer category, Integer subject, int professional) {
        // 최근 인기순
        String sortByBestNewest = "";
        // 역대 인기순
        String sortByBest = "";
        // 최신순
        String sortByNewest = "";
        // 팔로우
        String sortByFollow = "";

        // 가져올 cursor (cursor가 예약어라 standard로 함)
        String standardColumn = "";

        if (sort == 2){
            sortByBestNewest = " AND feed.view_count + Round(feed.created_at/10, 0) < ? ORDER BY standard1 DESC ";
            standardColumn = "standard1";
        } else if (sort == 3) {
            sortByBest = " AND Concat(Lpad(view_count, 8, '0'), Lpad(feed.id, 8, '0')) < ? ORDER BY standard2 DESC ";
            standardColumn = "standard2";
        } else if (sort == 1){
            sortByNewest = " AND (SELECT CONVERT(feed.created_at, signed INTEGER)) < ? ORDER BY standard3 DESC ";
            standardColumn = "standard3";
        } else {
            sortByFollow = ""
                    + "JOIN     follow_user "
                    + "ON       feed.user_id = following_id "
                    + "AND      follow_user.user_id = "+userId.toString()+" ";
            standardColumn = "standard3";
            sortByNewest = " AND (SELECT CONVERT(feed.created_at, signed INTEGER)) < ? ORDER BY standard3 DESC ";
        }

        String filterByHomeType = "";
        String filterByAcreageStart = "";
        String filterByAcreageEnd = "";
        String filterByBudgetStart = "";
        String filterByBudgetEnd = "";
        String filterByFamily = "";
        String filterByStyle = "";
        String filterByAllColor = "";
        String filterByWallColor = "";
        String filterByFloorColor = "";
        String filterByDetail = "";
        String filterByCategory = "";
        String filterBySubject = "";
        String filterByProfessional = "         and homewarming_feed.is_professional != 1 ";
        int[] budgetList = {0, 100, 200, 300, 400, 500, 1000, 2000, 3000, 4000, 1000000000};

        if (homeType != 0) {
            filterByHomeType = "         and homewarming_home_type_id="+homeType+" ";
        }

        filterByAcreageStart = "          and acreage >="+ acreageStart +" ";

        if (acreageEnd != 0) {
            filterByAcreageEnd = "          and acreage <= "+ acreageEnd +" ";
        }
        if (budgetStart > 0 && budgetStart < budgetList.length) {
            filterByBudgetStart = "          and budget >= "+ budgetList[budgetStart-1] +" ";
        }
        if (budgetEnd > 0 && budgetEnd < budgetList.length) {
            filterByBudgetEnd = "          and budget < "+ budgetList[budgetEnd] +" ";
        }
        if (family > 0) {
            filterByFamily = "         and homewarming_family_type_id="+family+" ";
        }
        if (style > 0) {
            filterByStyle = "         and homewarming_style_type_id="+style+" ";
        }
        if (allColor > 0) {
            filterByAllColor = "         and all_color="+allColor+" ";
        }
        if (wallColor > 0) {
            filterByWallColor = "         and wall_color="+wallColor+" ";
        }
        if (floorColor > 0) {
            filterByFloorColor = "         and floor_color="+floorColor+" ";
        }
        if (detail > 0) {
            filterByDetail = "         and homewarming_detail_type_id="+detail+" ";
        }
        if (category > 0) {
            filterByCategory = "         and homewarming_category_type_id="+category+" ";
        }
        if (subject > 0) {
            filterBySubject = "         and homewarming_subject_type_id="+subject+" ";
        }
        if (professional > 0){
            filterByProfessional = "         and homewarming_feed.is_professional = 1 ";
        }

        Object[] retrieveHomewarmingFeedListParams = new Object[]{
                userId, cursor
        };
        String retrieveHomewarmingFeedListQuery = ""
                + "SELECT feed.id, "
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
                + "       end                                                               AS "
                + "       thumbnail, "
                + "       feed.id IN (SELECT feed_id "
                + "                   FROM   scrapbook_feed "
                + "                          JOIN scrapbook "
                + "                            ON scrapbook_feed.scrapbook_id = scrapbook.id "
                + "                   WHERE  user_id = ?)                                   AS "
                + "       is_bookmarked, "
                + "       Concat(homewarming_feed.description, \" \", homewarming_feed.title) AS "
                + "       description, "
                + "       user.profile_image_url, "
                + "       user.nickname, "
                + "       IF (scrapped_count > 0, scrapped_count, 0)                        AS "
                + "       scrapped_count, "
                + "       view_count, "
                + "         feed.view_count + Round(feed.created_at/10, 0) AS standard1, "
                + "Concat(Lpad(view_count, 8, '0'), Lpad(feed.id, 8, '0')) AS standard2, "
                + "      (SELECT CONVERT(feed.created_at, signed INTEGER)) AS standard3 "
                + "FROM     feed "
                + "JOIN     homewarming_feed "
                + "ON       feed.id = homewarming_feed.feed_id "
                + "LEFT JOIN "
                + "          ( "
                + "                   SELECT   Count(*) AS like_count, "
                + "                            feed_id "
                + "                   FROM     like_feed "
                + "                   GROUP BY feed_id) forLikeCount "
                + "ON        feed.id = forLikeCount.feed_id "
                + "LEFT JOIN "
                + "          ( "
                + "                   SELECT   Count(*) AS scrapped_count, "
                + "                            feed_id "
                + "                   FROM     scrapbook_feed "
                + "                   GROUP BY feed_id) forScrappedCount "
                + "ON        feed.id = forScrappedCount.feed_id "
                + "LEFT JOIN "
                + "          ( "
                + "                   SELECT   Count(*) AS comment_count, "
                + "                            feed_id "
                + "                   FROM     comment "
                + "                   GROUP BY feed_id) forCommentCount "
                + "ON        feed.id = forCommentCount.feed_id "
                + "LEFT JOIN "
                + "          ( "
                + "                   SELECT   comment.feed_id , "
                + "                            user.profile_image_url AS recent_comment_user_profile_image_url, "
                + "                            user.nickname          AS recent_comment_user_nickname, "
                + "                            comment.content           recent_comment_content "
                + "                   FROM     comment "
                + "                   JOIN     user "
                + "                   ON       user.id = comment.user_id "
                + "                   WHERE    is_recomment = 0 "
                + "                   GROUP BY feed_id "
                + "                   ORDER BY comment.created_at DESC ) forRecentComment "
                + "ON        forRecentComment.feed_id = feed.id "
                + "JOIN      user "
                + "ON        feed.user_id = user.id "
                + sortByFollow
                + "WHERE    is_homewarming = 1 "
                + filterByHomeType
                + filterByStyle
                + filterByHomeType
                + filterByAcreageStart
                + filterByAcreageEnd
                + filterByBudgetStart
                + filterByBudgetEnd
                + filterByFamily
                + filterByStyle
                + filterByAllColor
                + filterByWallColor
                + filterByFloorColor
                + filterByDetail
                + filterByCategory
                + filterBySubject
                + filterByProfessional
                + sortByBestNewest
                + sortByBest
                + sortByNewest;
        String forLimitQueryTail = "LIMIT 5 ";
        String finalStandardColumn = standardColumn;
        List<GetFeedsHomewarmingFeedsListResFeed> getFeedsHomewarmingFeedsListResFeedList = this.jdbcTemplate.query(retrieveHomewarmingFeedListQuery+forLimitQueryTail,
                (rs, rowNum) -> new GetFeedsHomewarmingFeedsListResFeed(
                        rs.getLong("id"),
                        rs.getString("thumbnail"),
                        rs.getInt("is_bookmarked"),
                        rs.getString("description"),
                        rs.getString("profile_image_url"),
                        rs.getString("nickname"),
                        rs.getInt("scrapped_count"),
                        rs.getInt("view_count"),
                        rs.getLong(finalStandardColumn)
                ), retrieveHomewarmingFeedListParams);
        String forCountQueryHeader = ""
                + "SELECT Count(*) AS count "
                + "FROM   (";
        String forCountQueryTail = ""
                + ") forCount";

        Integer count = this.jdbcTemplate.queryForObject(forCountQueryHeader+retrieveHomewarmingFeedListQuery+forCountQueryTail, Integer.class, retrieveHomewarmingFeedListParams);

        return new GetFeedsHomewarmingFeedsListRes(count, getFeedsHomewarmingFeedsListResFeedList);
    }

    // 노하우 피드 리스트 조회
    public GetFeedsKnowhowFeedsListRes retrieveKnowhowFeedList(Long userId, Long cursor, Integer theme, Integer sort) {
        // 최근 인기순
        String sortByBestNewest = "";
        // 역대 인기순
        String sortByBest = "";
        // 최신순 / 과거순
        String sortByNewestAndOldest = "";
        // 팔로우
        String sortByFollow = "";

        // 가져올 cursor (cursor가 예약어라 standard로 함)
        String standardColumn = "";

        if (sort == 1){
            sortByBestNewest = " AND feed.view_count + Round(feed.created_at/10, 0) < ? ORDER BY standard1 DESC ";
            standardColumn = "standard1";
        } else if (sort == 2) {
            sortByBest = " AND Concat(Lpad(view_count, 8, '0'), Lpad(feed.id, 8, '0')) < ? ORDER BY standard2 DESC ";
            standardColumn = "standard2";
        } else if (sort == 3){
            sortByNewestAndOldest = " AND (SELECT CONVERT(feed.created_at, signed INTEGER)) < ? ORDER BY standard3 DESC ";
            standardColumn = "standard3";
        } else if (sort == 4){
            if (cursor == Long.MAX_VALUE){
                cursor = Long.MIN_VALUE;
            }
            sortByNewestAndOldest = " AND (SELECT CONVERT(feed.created_at, signed INTEGER)) > ? ORDER BY standard3 ASC ";
            standardColumn = "standard3";
        } else {
            sortByFollow = ""
                    + "JOIN     follow_user "
                    + "ON       feed.user_id = following_id "
                    + "AND      follow_user.user_id = "+userId.toString()+" ";
            standardColumn = "standard3";
            sortByNewestAndOldest = " AND (SELECT CONVERT(feed.created_at, signed INTEGER)) < ? ORDER BY standard3 DESC ";
        }

        String filterByThemeType = "";

        if (theme != 0 && theme > 1) {
            filterByThemeType = "         and knowhow_theme_type_id="+theme+" ";
        }

        Object[] retrieveKnowhowFeedListParams = new Object[]{
                userId, cursor
        };
        String retrieveKnowhowFeedListQuery = ""
                + "SELECT CASE "
                + "         WHEN Timestampdiff(day, feed.created_at, Now()) < 3 THEN 1 "
                + "         ELSE 0 "
                + "       end                                                     AS is_new, "
                + "       feed.id, "
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
                + "       end                                                     AS thumbnail, "
                + "       Concat(knowhow_feed.title)                              AS description, "
                + "       user.profile_image_url, "
                + "       user.nickname, "
                + "       view_count, "
                + "       IF (scrapped_count > 0, scrapped_count, 0)              AS scrapped_count "
                + "       , "
                + "       feed.id IN (SELECT feed_id "
                + "                   FROM   scrapbook_feed "
                + "                          JOIN scrapbook "
                + "                            ON scrapbook_feed.scrapbook_id = scrapbook.id "
                + "                   WHERE  user_id = ?)                         AS is_bookmarked, "
                + "         feed.view_count + Round(feed.created_at/10, 0) AS standard1, "
                + "Concat(Lpad(view_count, 8, '0'), Lpad(feed.id, 8, '0')) AS standard2, "
                + "      (SELECT CONVERT(feed.created_at, signed INTEGER)) AS standard3 "
                + "FROM     feed "
                + "JOIN     knowhow_feed "
                + "ON       feed.id = knowhow_feed.feed_id "
                + "LEFT JOIN "
                + "          ( "
                + "                   SELECT   Count(*) AS like_count, "
                + "                            feed_id "
                + "                   FROM     like_feed "
                + "                   GROUP BY feed_id) forLikeCount "
                + "ON        feed.id = forLikeCount.feed_id "
                + "LEFT JOIN "
                + "          ( "
                + "                   SELECT   Count(*) AS scrapped_count, "
                + "                            feed_id "
                + "                   FROM     scrapbook_feed "
                + "                   GROUP BY feed_id) forScrappedCount "
                + "ON        feed.id = forScrappedCount.feed_id "
                + "LEFT JOIN "
                + "          ( "
                + "                   SELECT   Count(*) AS comment_count, "
                + "                            feed_id "
                + "                   FROM     comment "
                + "                   GROUP BY feed_id) forCommentCount "
                + "ON        feed.id = forCommentCount.feed_id "
                + "LEFT JOIN "
                + "          ( "
                + "                   SELECT   comment.feed_id , "
                + "                            user.profile_image_url AS recent_comment_user_profile_image_url, "
                + "                            user.nickname          AS recent_comment_user_nickname, "
                + "                            comment.content           recent_comment_content "
                + "                   FROM     comment "
                + "                   JOIN     user "
                + "                   ON       user.id = comment.user_id "
                + "                   WHERE    is_recomment = 0 "
                + "                   GROUP BY feed_id "
                + "                   ORDER BY comment.created_at DESC ) forRecentComment "
                + "ON        forRecentComment.feed_id = feed.id "
                + "JOIN      user "
                + "ON        feed.user_id = user.id "
                + sortByFollow
                + "WHERE    is_knowhow = 1 "
                + filterByThemeType
                + sortByBestNewest
                + sortByBest
                + sortByNewestAndOldest;
        String forLimitQueryTail = "LIMIT 5 ";
        String finalStandardColumn = standardColumn;
        List<GetFeedsKnowhowFeedsListResFeed> getFeedsKnowhowFeedsListResFeedList = this.jdbcTemplate.query(retrieveKnowhowFeedListQuery+forLimitQueryTail,
                (rs, rowNum) -> new GetFeedsKnowhowFeedsListResFeed(
                        rs.getInt("is_new"),
                        rs.getLong("id"),
                        rs.getString("thumbnail"),
                        rs.getString("description"),
                        rs.getString("profile_image_url"),
                        rs.getString("nickname"),
                        rs.getInt("view_count"),
                        rs.getInt("scrapped_count"),
                        rs.getLong(finalStandardColumn),
                        rs.getInt("is_bookmarked")
                ), retrieveKnowhowFeedListParams);
        String forCountQueryHeader = ""
                + "SELECT Count(*) AS count "
                + "FROM   (";
        String forCountQueryTail = ""
                + ") forCount";

        Integer count = this.jdbcTemplate.queryForObject(forCountQueryHeader+retrieveKnowhowFeedListQuery+forCountQueryTail, Integer.class, retrieveKnowhowFeedListParams);

        return new GetFeedsKnowhowFeedsListRes(count, getFeedsKnowhowFeedsListResFeedList);
    }

    // 미디어 피드 상단에 노출되는 메타데이터
    public GetFeedsMediaFeedsMetaRes retrieveMediaFeedMeta(Long feedId) {
        String retrieveMediaFeedMetaQuery = ""
                + "SELECT media_feed.feed_id, "
                + "       media_feed_acreage_type.name                     AS acreage, "
                + "       media_feed_home_type.name                        AS home, "
                + "       Concat(media_feed_style_type.name, \"스타일\") AS style "
                + "FROM   media_feed "
                + "       LEFT JOIN media_feed_acreage_type "
                + "              ON media_feed_acreage_type.id = "
                + "                 media_feed. media_feed_acreage_type_id "
                + "       LEFT JOIN media_feed_home_type "
                + "              ON media_feed_home_type.id = media_feed. media_feed_home_type_id "
                + "       LEFT JOIN media_feed_style_type "
                + "              ON media_feed_style_type.id = media_feed. media_feed_style_type_id "
                + "WHERE  feed_id = ?;";
        return this.jdbcTemplate.queryForObject(retrieveMediaFeedMetaQuery,
                (rs, rowNum) -> new GetFeedsMediaFeedsMetaRes(
                        rs.getLong("feed_id"),
                        rs.getString("acreage"),
                        rs.getString("home"),
                        rs.getString("style")
                ), feedId);
    }

    // 미디어 피드 하단에 노출되는 유저 정보 및 좋아요, 댓글 등
    public GetFeedsMediaFeedsBottomRes retrieveMediaFeedBottom(Long userId, Long feedId) {
        Object[] retrieveMediaFeedBottomParams = new Object[]{
                userId, feedId
        };
        String retrieveMediaFeedBottomQuery = ""
                + "SELECT user.id                                       AS user_id, "
                + "       user.profile_image_url, "
                + "       user.nickname, "
                + "       user.description                              AS description, "
                + "       ( EXISTS(SELECT * "
                + "                FROM   follow_user "
                + "                WHERE  user_id = ? "
                + "                       AND following_id = user.id) ) AS is_followed, "
                + "       feed.id                                       AS feed_id, "
                + "       IF (like_count > 0, like_count, 0)            AS like_count, "
                + "       IF (scrapped_count > 0, scrapped_count, 0)    AS scrapped_count, "
                + "       IF (comment_count > 0, comment_count, 0)      AS comment_count, "
                + "       view_count "
                + "FROM   feed "
                + "       JOIN user "
                + "         ON user.id = feed.user_id "
                + "       LEFT JOIN (SELECT Count(*) AS like_count, "
                + "                         feed_id "
                + "                  FROM   like_feed "
                + "                  GROUP  BY feed_id) forLikeCount "
                + "              ON feed.id = forLikeCount.feed_id "
                + "       LEFT JOIN (SELECT Count(*) AS scrapped_count, "
                + "                         feed_id "
                + "                  FROM   scrapbook_feed "
                + "                  GROUP  BY feed_id) forScrappedCount "
                + "              ON feed.id = forScrappedCount.feed_id "
                + "       LEFT JOIN (SELECT Count(*) AS comment_count, "
                + "                         feed_id "
                + "                  FROM   comment "
                + "                  GROUP  BY feed_id) forCommentCount "
                + "              ON feed.id = forCommentCount.feed_id "
                + "WHERE  feed.id = ?";
        return this.jdbcTemplate.queryForObject(retrieveMediaFeedBottomQuery,
                (rs, rowNum) -> new GetFeedsMediaFeedsBottomRes(
                        rs.getLong("user_id"),
                        rs.getString("profile_image_url"),
                        rs.getString("nickname"),
                        rs.getString("description"),
                        rs.getInt("is_followed"),
                        rs.getLong("feed_id"),
                        rs.getInt("like_count"),
                        rs.getInt("scrapped_count"),
                        rs.getInt("comment_count"),
                        rs.getInt("view_count")
                ), retrieveMediaFeedBottomParams);
    }

    // 미디어 피드 하단에 노출되는 해당 미디어 피드를 올린 유저가 올린 다른 미디어 피드
    public List<GetFeedsMediaFeedsOthersRes> retrieveMediaFeedOthers(Long feedId) {
        String retrieveMediaFeedOthersQuery = ""
                + "SELECT feed.id, "
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
                + "       end                 AS thumbnail, "
                + "       media_feed.is_photo AS is_media_feed_photo, "
                + "       media_feed.is_video AS is_media_feed_video "
                + "FROM   feed "
                + "       JOIN media_feed "
                + "         ON feed.id = media_feed.feed_id "
                + "WHERE  user_id = (SELECT user_id "
                + "                  FROM   feed "
                + "                  WHERE  feed.id = ?) "
                + "       AND is_media_feed = 1 "
                + "       AND feed.id != ?;";

        Object[] retrieveMediaFeedOthersParams = new Object[]{
                feedId, feedId
        };
        return this.jdbcTemplate.query(retrieveMediaFeedOthersQuery,
                (rs, rowNum) -> new GetFeedsMediaFeedsOthersRes(
                        rs.getLong("id"),
                        rs.getString("thumbnail"),
                        rs.getInt("is_media_feed_photo"),
                        rs.getInt("is_media_feed_video")
                ), retrieveMediaFeedOthersParams);
    }

    public List<GetFeedsScrappedAll> retrieveScrappedAll(Long scrapbookId, Long cursor) {
        Object[] retrieveScrappedAllParams = new Object[]{
                scrapbookId, cursor
        };
        String retrieveScrappedAllQuery = ""
                + "SELECT * "
                + "FROM   (SELECT @rownum := @rownum + 1 AS rownum, "
                + "               b.* "
                + "        FROM   (SELECT CASE "
                + "                         WHEN feed.is_video = 1 THEN (SELECT feed_id "
                + "                                                      FROM   media_feed "
                + "                                                      WHERE "
                + "                         feed_id = (SELECT feed_id "
                + "                                    FROM   feed_having_media "
                + "                                    WHERE  media_id = "
                + "                                           (SELECT id "
                + "                                            FROM   media "
                + "                                            WHERE  media.feed_id = "
                + "                                                   feed.id) "
                + "                                    LIMIT  1)) "
                + "                         ELSE feed.id "
                + "                       end                           AS alternative_id, "
                + "                       feed.is_photo, "
                + "                       feed.is_media_feed, "
                + "                       feed.is_homewarming, "
                + "                       feed.is_knowhow, "
                + "                       IF ((SELECT is_video "
                + "                            FROM   media_feed "
                + "                            WHERE  media_feed.feed_id = feed.id) = 1, (SELECT "
                + "                       time "
                + "                                                                       FROM "
                + "                       media "
                + "                                                                       WHERE "
                + "                       media.id = (SELECT media_id "
                + "                                   FROM   feed_having_media "
                + "                                   WHERE  feed_having_media.feed_id = feed.id "
                + "                                   LIMIT  1)), NULL) AS video_time, "
                + "                       CASE "
                + "                         WHEN feed.is_media_feed = 1 "
                + "                              AND (SELECT media_feed.is_photo "
                + "                                   FROM   media_feed "
                + "                                   WHERE  media_feed.feed_id = feed.id) = 1 THEN "
                + "                         ( "
                + "                         SELECT url "
                + "                         FROM   media "
                + "                         WHERE "
                + "                         media.id = (SELECT media_id "
                + "                                     FROM   feed_having_media "
                + "                                     WHERE  feed_having_media.feed_id = feed.id "
                + "                                     ORDER  BY feed_having_media.created_at "
                + "                                     LIMIT  1)) "
                + "                         WHEN feed.is_media_feed = 1 "
                + "                              AND (SELECT media_feed.is_video "
                + "                                   FROM   media_feed "
                + "                                   WHERE  media_feed.feed_id = feed.id) = 1 THEN "
                + "                         ( "
                + "                         SELECT "
                + "                         thumbnail_url "
                + "                         FROM "
                + "                         media "
                + "                         WHERE "
                + "                         media.id = (SELECT media_id "
                + "                                     FROM   feed_having_media "
                + "                                     WHERE  feed_having_media.feed_id = feed.id "
                + "                                     LIMIT  1)) "
                + "                         WHEN feed.is_photo = 1 THEN (SELECT url "
                + "                                                      FROM   media "
                + "                                                      WHERE "
                + "                         media.feed_id = feed.id) "
                + "                         WHEN feed.is_video = 1 THEN (SELECT thumbnail_url "
                + "                                                      FROM   media "
                + "                                                      WHERE "
                + "                         media.feed_id = feed.id) "
                + "                         WHEN feed.is_homewarming = 1 THEN feed.thumbnail_url "
                + "                         WHEN feed.is_knowhow = 1 THEN feed.thumbnail_url "
                + "                         ELSE NULL "
                + "                       end                           AS thumbnail "
                + "                FROM   scrapbook_feed "
                + "                       JOIN feed "
                + "                         ON scrapbook_feed.feed_id = feed.id "
                + "                WHERE  scrapbook_id = ? "
                + "                ORDER  BY scrapbook_feed.created_at DESC) b "
                + "        WHERE  ( @rownum := 0 ) = 0) main "
                + "WHERE  rownum > ? "
                + "LIMIT  10";
        return this.jdbcTemplate.query(retrieveScrappedAllQuery,
                (rs, rowNum) -> new GetFeedsScrappedAll(
                        rs.getLong("alternative_id"),
                        rs.getInt("is_photo"),
                        rs.getInt("is_media_feed"),
                        rs.getInt("is_homewarming"),
                        rs.getInt("is_knowhow"),
                        rs.getInt("video_time"),
                        rs.getString("thumbnail"),
                        rs.getLong("rownum")
                ), retrieveScrappedAllParams);
    }

    public List<GetFeedsScrappedMediaFeeds> retrieveScrappedMediaFeeds(Long scrapbookId, Long cursor) {

        Object[] retrieveScrappedMediaFeedsParams = new Object[]{
                scrapbookId, cursor
        };
        String retrieveScrappedMediaFeedsQuery = ""
                + "SELECT * "
                + "FROM   (SELECT @rownum := @rownum + 1 AS rownum, "
                + "               b.* "
                + "        FROM   (SELECT CASE "
                + "                         WHEN feed.is_video = 1 THEN (SELECT feed_id "
                + "                                                      FROM   media_feed "
                + "                                                      WHERE "
                + "                         feed_id = (SELECT feed_id "
                + "                                    FROM   feed_having_media "
                + "                                    WHERE  media_id = "
                + "                                           (SELECT id "
                + "                                            FROM   media "
                + "                                            WHERE  media.feed_id = "
                + "                                                   feed.id) "
                + "                                    LIMIT  1)) "
                + "                         ELSE feed.id "
                + "                       end                           AS alternative_id, "
                + "                       feed.is_photo, "
                + "                       feed.is_media_feed, "
                + "                       IF ((SELECT is_video "
                + "                            FROM   media_feed "
                + "                            WHERE  media_feed.feed_id = feed.id) = 1, (SELECT "
                + "                       time "
                + "                                                                       FROM "
                + "                       media "
                + "                                                                       WHERE "
                + "                       media.id = (SELECT media_id "
                + "                                   FROM   feed_having_media "
                + "                                   WHERE  feed_having_media.feed_id = feed.id "
                + "                                   LIMIT  1)), NULL) AS video_time, "
                + "                       CASE "
                + "                         WHEN feed.is_media_feed = 1 "
                + "                              AND (SELECT media_feed.is_photo "
                + "                                   FROM   media_feed "
                + "                                   WHERE  media_feed.feed_id = feed.id) = 1 THEN "
                + "                         ( "
                + "                         SELECT url "
                + "                         FROM   media "
                + "                         WHERE "
                + "                         media.id = (SELECT media_id "
                + "                                     FROM   feed_having_media "
                + "                                     WHERE  feed_having_media.feed_id = feed.id "
                + "                                     ORDER  BY feed_having_media.created_at "
                + "                                     LIMIT  1)) "
                + "                         WHEN feed.is_media_feed = 1 "
                + "                              AND (SELECT media_feed.is_video "
                + "                                   FROM   media_feed "
                + "                                   WHERE  media_feed.feed_id = feed.id) = 1 THEN "
                + "                         ( "
                + "                         SELECT "
                + "                         thumbnail_url "
                + "                         FROM "
                + "                         media "
                + "                         WHERE "
                + "                         media.id = (SELECT media_id "
                + "                                     FROM   feed_having_media "
                + "                                     WHERE  feed_having_media.feed_id = feed.id "
                + "                                     LIMIT  1)) "
                + "                         WHEN feed.is_photo = 1 THEN (SELECT url "
                + "                                                      FROM   media "
                + "                                                      WHERE "
                + "                         media.feed_id = feed.id) "
                + "                         WHEN feed.is_video = 1 THEN (SELECT thumbnail_url "
                + "                                                      FROM   media "
                + "                                                      WHERE "
                + "                         media.feed_id = feed.id) "
                + "                         WHEN feed.is_homewarming = 1 THEN feed.thumbnail_url "
                + "                         WHEN feed.is_knowhow = 1 THEN feed.thumbnail_url "
                + "                         ELSE NULL "
                + "                       end                           AS thumbnail "
                + "                FROM   scrapbook_feed "
                + "                       JOIN feed "
                + "                         ON scrapbook_feed.feed_id = feed.id "
                + "                WHERE  scrapbook_id = ? "
                + "                       AND ( feed.is_media_feed = 1 "
                + "                              OR feed.is_photo = 1 "
                + "                              OR feed.is_video = 1 ) "
                + "                ORDER  BY scrapbook_feed.created_at DESC) b "
                + "        WHERE  ( @rownum := 0 ) = 0) main "
                + "WHERE  rownum > ? "
                + "LIMIT  10 ";
        return this.jdbcTemplate.query(retrieveScrappedMediaFeedsQuery,
                (rs, rowNum) -> new GetFeedsScrappedMediaFeeds(
                        rs.getLong("alternative_id"),
                        rs.getInt("is_photo"),
                        rs.getInt("is_media_feed"),
                        rs.getInt("video_time"),
                        rs.getString("thumbnail"),
                        rs.getLong("rownum")
                ), retrieveScrappedMediaFeedsParams);
    }

    public List<GetFeedsScrappedHomewarmingsFeed> retrieveScrappedHomewarmings(Long scrapbookId, Long cursor) {

        Object[] retrieveScrappedHomewarmingsParams = new Object[]{
                scrapbookId, cursor
        };
        String retrieveScrappedHomewarmingsQuery = ""
                + "SELECT * "
                + "FROM   (SELECT @rownum := @rownum + 1 AS rownum, "
                + "               b.* "
                + "        FROM   (SELECT CASE "
                + "                         WHEN feed.is_video = 1 THEN (SELECT feed_id "
                + "                                                      FROM   media_feed "
                + "                                                      WHERE "
                + "                         feed_id = (SELECT feed_id "
                + "                                    FROM   feed_having_media "
                + "                                    WHERE  media_id = "
                + "                                           (SELECT id "
                + "                                            FROM   media "
                + "                                            WHERE  media.feed_id = "
                + "                                                   feed.id) "
                + "                                    LIMIT  1)) "
                + "                         ELSE feed.id "
                + "                       end "
                + "                       AS "
                + "                               alternative_id, "
                + "                       feed.is_homewarming, "
                + "                       CASE "
                + "                         WHEN feed.is_media_feed = 1 "
                + "                              AND (SELECT media_feed.is_photo "
                + "                                   FROM   media_feed "
                + "                                   WHERE  media_feed.feed_id = feed.id) = 1 THEN "
                + "                         ( "
                + "                         SELECT url "
                + "                         FROM   media "
                + "                         WHERE "
                + "                         media.id = (SELECT media_id "
                + "                                     FROM   feed_having_media "
                + "                                     WHERE  feed_having_media.feed_id = feed.id "
                + "                                     ORDER  BY feed_having_media.created_at "
                + "                                     LIMIT  1)) "
                + "                         WHEN feed.is_media_feed = 1 "
                + "                              AND (SELECT media_feed.is_video "
                + "                                   FROM   media_feed "
                + "                                   WHERE  media_feed.feed_id = feed.id) = 1 THEN "
                + "                         ( "
                + "                         SELECT "
                + "                         thumbnail_url "
                + "                         FROM "
                + "                         media "
                + "                         WHERE "
                + "                         media.id = (SELECT media_id "
                + "                                     FROM   feed_having_media "
                + "                                     WHERE  feed_having_media.feed_id = feed.id "
                + "                                     LIMIT  1)) "
                + "                         WHEN feed.is_photo = 1 THEN (SELECT url "
                + "                                                      FROM   media "
                + "                                                      WHERE "
                + "                         media.feed_id = feed.id) "
                + "                         WHEN feed.is_video = 1 THEN (SELECT thumbnail_url "
                + "                                                      FROM   media "
                + "                                                      WHERE "
                + "                         media.feed_id = feed.id) "
                + "                         WHEN feed.is_homewarming = 1 THEN feed.thumbnail_url "
                + "                         WHEN feed.is_knowhow = 1 THEN feed.thumbnail_url "
                + "                         ELSE NULL "
                + "                       end "
                + "                       AS "
                + "                               thumbnail, "
                + "                       Concat(homewarming_feed.description, \"\", "
                + "                       homewarming_feed.title) AS "
                + "                               title, "
                + "                       forUser.user_id "
                + "                       AS "
                + "                               user_id, "
                + "                       forUser.profile_image_url, "
                + "                       forUser.nickname, "
                + "                       forUser.is_professional "
                + "                FROM   scrapbook_feed "
                + "                       JOIN (SELECT feed.*, "
                + "                                    user.nickname, "
                + "                                    user.profile_image_url, "
                + "                                    user.is_professional "
                + "                             FROM   feed "
                + "                                    JOIN user "
                + "                                      ON feed.user_id = user.id) AS forUser "
                + "                         ON scrapbook_feed.feed_id = forUser.id "
                + "                       JOIN feed "
                + "                         ON scrapbook_feed.feed_id = feed.id "
                + "                       JOIN homewarming_feed "
                + "                         ON scrapbook_feed.feed_id = homewarming_feed.feed_id "
                + "                WHERE  scrapbook_id = ? "
                + "                       AND ( feed.is_homewarming = 1 ) "
                + "                ORDER  BY scrapbook_feed.created_at DESC) b "
                + "        WHERE  ( @rownum := 0 ) = 0) main "
                + "WHERE  rownum > ? "
                + "LIMIT  10 ";
        return this.jdbcTemplate.query(retrieveScrappedHomewarmingsQuery,
                (rs, rowNum) -> new GetFeedsScrappedHomewarmingsFeed(
                        rs.getLong("alternative_id"),
                        rs.getInt("is_homewarming"),
                        rs.getString("thumbnail"),
                        rs.getString("title"),
                        rs.getLong("user_id"),
                        rs.getString("profile_image_url"),
                        rs.getString("nickname"),
                        rs.getInt("is_professional"),
                        rs.getLong("rownum")
                ), retrieveScrappedHomewarmingsParams);
    }

    public List<GetFeedsScrappedKnowhowsFeed> retrieveScrappedKnowhows(Long scrapbookId, Long cursor) {

        Object[] retrieveScrappedKnowhowsParams = new Object[]{
                scrapbookId, cursor
        };
        String retrieveScrappedKnowhowsQuery = ""
                + "SELECT * "
                + "FROM   (SELECT @rownum := @rownum + 1 AS rownum, "
                + "               b.* "
                + "        FROM   (SELECT CASE "
                + "                         WHEN feed.is_video = 1 THEN (SELECT feed_id "
                + "                                                      FROM   media_feed "
                + "                                                      WHERE "
                + "                         feed_id = (SELECT feed_id "
                + "                                    FROM   feed_having_media "
                + "                                    WHERE  media_id = "
                + "                                           (SELECT id "
                + "                                            FROM   media "
                + "                                            WHERE  media.feed_id = "
                + "                                                   feed.id) "
                + "                                    LIMIT  1)) "
                + "                         ELSE feed.id "
                + "                       end                                                   AS "
                + "                       alternative_id, "
                + "                       feed.is_knowhow, "
                + "                       CASE "
                + "                         WHEN feed.is_media_feed = 1 "
                + "                              AND (SELECT media_feed.is_photo "
                + "                                   FROM   media_feed "
                + "                                   WHERE  media_feed.feed_id = feed.id) = 1 THEN "
                + "                         ( "
                + "                         SELECT url "
                + "                         FROM   media "
                + "                         WHERE "
                + "                         media.id = (SELECT media_id "
                + "                                     FROM   feed_having_media "
                + "                                     WHERE  feed_having_media.feed_id = feed.id "
                + "                                     ORDER  BY feed_having_media.created_at "
                + "                                     LIMIT  1)) "
                + "                         WHEN feed.is_media_feed = 1 "
                + "                              AND (SELECT media_feed.is_video "
                + "                                   FROM   media_feed "
                + "                                   WHERE  media_feed.feed_id = feed.id) = 1 THEN "
                + "                         ( "
                + "                         SELECT "
                + "                         thumbnail_url "
                + "                         FROM "
                + "                         media "
                + "                         WHERE "
                + "                         media.id = (SELECT media_id "
                + "                                     FROM   feed_having_media "
                + "                                     WHERE  feed_having_media.feed_id = feed.id "
                + "                                     LIMIT  1)) "
                + "                         WHEN feed.is_photo = 1 THEN (SELECT url "
                + "                                                      FROM   media "
                + "                                                      WHERE "
                + "                         media.feed_id = feed.id) "
                + "                         WHEN feed.is_video = 1 THEN (SELECT thumbnail_url "
                + "                                                      FROM   media "
                + "                                                      WHERE "
                + "                         media.feed_id = feed.id) "
                + "                         WHEN feed.is_homewarming = 1 THEN feed.thumbnail_url "
                + "                         WHEN feed.is_knowhow = 1 THEN feed.thumbnail_url "
                + "                         ELSE NULL "
                + "                       end                                                   AS "
                + "                       thumbnail, "
                + "                       Concat(forKnowhow.description, \"\", forKnowhow.title) AS "
                + "                       title, "
                + "                       forKnowhow.name                                       AS "
                + "                       theme_name "
                + "                FROM   scrapbook_feed "
                + "                       JOIN feed "
                + "                         ON scrapbook_feed.feed_id = feed.id "
                + "                       JOIN (SELECT knowhow_feed.*, "
                + "                                    knowhow_theme_type.name "
                + "                             FROM   knowhow_feed "
                + "                                    JOIN knowhow_theme_type "
                + "                                      ON knowhow_theme_type.id = "
                + "                                         knowhow_feed.knowhow_theme_type_id) AS "
                + "                                          forKnowhow "
                + "                         ON scrapbook_feed.feed_id = forKnowhow.feed_id "
                + "                WHERE  scrapbook_id = ? "
                + "                       AND ( feed.is_knowhow = 1 ) "
                + "                ORDER  BY scrapbook_feed.created_at DESC) b "
                + "        WHERE  ( @rownum := 0 ) = 0) main "
                + "WHERE  rownum > ? "
                + "LIMIT  10 ";
        return this.jdbcTemplate.query(retrieveScrappedKnowhowsQuery,
                (rs, rowNum) -> new GetFeedsScrappedKnowhowsFeed(
                        rs.getLong("alternative_id"),
                        rs.getInt("is_knowhow"),
                        rs.getString("thumbnail"),
                        rs.getString("title"),
                        rs.getString("theme_name"),
                        rs.getLong("rownum")
                ), retrieveScrappedKnowhowsParams);
    }
}
