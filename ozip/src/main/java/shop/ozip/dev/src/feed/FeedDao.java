
package shop.ozip.dev.src.feed;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.feed.model.*;
import shop.ozip.dev.src.keyword.KeywordDao;
import shop.ozip.dev.src.keyword.model.Keyword;
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

        return this.jdbcTemplate.queryForObject(checkFeedExistByIdQuery, boolean.class, id);
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

}
