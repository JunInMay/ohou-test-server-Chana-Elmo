
package shop.ozip.dev.src.keyword;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
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

    // 특정 키워드의 id를 이름으로 가져옴
    public Long getKeywordIdByName(String name) {
        String checkIdOrZeroKeywordExistByNameQuery = ""
                + "SELECT id "
                + "FROM   keyword "
                + "WHERE  name = ? ";

        return this.jdbcTemplate.queryForObject(checkIdOrZeroKeywordExistByNameQuery, Long.class, name);
    }

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

    // 특정 피드에 달린 키워드 리스트 가져오기
    public List<QnAKeyword> getQnAKeywordListByQnAId(Long QnAId) {
        String getQnAKeywordListByQnAIdQuery = ""
                + "SELECT * "
                + "FROM   qna_having_keyword "
                + "       JOIN qna_keyword "
                + "         ON qna_keyword.id = qna_having_keyword.qna_keyword_id "
                + "WHERE  qna_having_keyword.qna_id = ? ";

        return this.jdbcTemplate.query(getQnAKeywordListByQnAIdQuery,
                (rs, rowNum) -> new QnAKeyword(
                        rs.getLong("id"),
                        rs.getString("name"),
                        Common.formatTimeStamp(rs.getTimestamp("created_at")),
                        Common.formatTimeStamp(rs.getTimestamp("updated_at")),
                        rs.getString("status")
                ), QnAId);
    }

    // 사진에 가장 많이 달린 키워드 조회하기(reffered count 안씀)
    public Keyword getMostRefferedKeywordInPhoto() {
        String getMostRefferedKeywordInPhoto = ""
                + "SELECT keyword.id, "
                + "       keyword.name, "
                + "       keyword.referred_count, "
                + "       keyword.created_at, "
                + "       keyword.updated_at, "
                + "       Count(*) AS count "
                + "FROM   feed_having_keyword "
                + "       JOIN feed "
                + "         ON feed.id = feed_having_keyword.feed_id "
                + "            AND feed.is_photo = 1 "
                + "       JOIN keyword "
                + "         ON feed_having_keyword.keyword_id = keyword.id "
                + "GROUP  BY keyword_id "
                + "ORDER  BY count DESC "
                + "LIMIT  1;";
        return this.jdbcTemplate.queryForObject(getMostRefferedKeywordInPhoto,
                (rs, rowNum) -> new Keyword(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("referred_count"),
                        Common.formatTimeStamp(rs.getTimestamp("created_at")),
                        Common.formatTimeStamp(rs.getTimestamp("updated_at"))
                ));
    }

    // 키워드 만들기
    public Long createKeyword(String name) {
        String createKeywordQuery = ""
                + "INSERT INTO keyword "
                + "            (name) "
                + "VALUES     (?) ";
        this.jdbcTemplate.update(createKeywordQuery, name);
        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,long.class);
    }
    // 키워드 존재 확인
    public Boolean checkKeywordExistByName(String name) {
        String createKeywordQuery = ""
                + "select (EXISTS(select * from keyword where name = ? )) ";
        return this.jdbcTemplate.queryForObject(createKeywordQuery,boolean.class, name);
    }
    // 키워드 존재 확인
    public Boolean checkKeywordExistById(Long keywordId) {
        String createKeywordQuery = ""
                + "select (EXISTS(select * from keyword where id = ? )) ";
        return this.jdbcTemplate.queryForObject(createKeywordQuery,boolean.class, keywordId);
    }

    // 키워드 관계 설정
    public Integer createFeedHavingKeyword(Long feedId, Long keywordId){
        String createFeedHavingKeywordQuery = ""
                + "INSERT INTO feed_having_keyword "
                + "            (feed_id, keyword_id) "
                + "VALUES     (?, ?) ";
        Object[] createFeedHavingKeywordParams = new Object[]{
                feedId, keywordId
        };
        return this.jdbcTemplate.update(createFeedHavingKeywordQuery, createFeedHavingKeywordParams);
    }

}
