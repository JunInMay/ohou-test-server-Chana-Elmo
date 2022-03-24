
package shop.ozip.dev.src.keyword;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.comment.model.Comment;
import shop.ozip.dev.src.keyword.model.*;
import shop.ozip.dev.utils.Common;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class KeywordDao {

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

    // 특정 피드에 달린 키워드 리스트 가져오기
    public List<Keyword> getKeywordListByFeedId(Long feedId) {
        String getKeywordListByFeedIdQuery = ""
                + "SELECT keyword.id             AS id, "
                + "       keyword.name           AS name, "
                + "       keyword.referred_count AS reffered_count, "
                + "       keyword.created_at     AS created_at, "
                + "       keyword.updated_at     AS updated_at "
                + "FROM   feed_having_keyword "
                + "       JOIN keyword "
                + "         ON feed_having_keyword.keyword_id = keyword.id "
                + "WHERE  feed_having_keyword.feed_id = ?;";
        return this.jdbcTemplate.query(getKeywordListByFeedIdQuery,
                (rs, rowNum) -> new Keyword(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("reffered_count"),
                        Common.formatTimeStamp(rs.getTimestamp("created_at")),
                        Common.formatTimeStamp(rs.getTimestamp("updated_at"))
                ), feedId);
    }
}
