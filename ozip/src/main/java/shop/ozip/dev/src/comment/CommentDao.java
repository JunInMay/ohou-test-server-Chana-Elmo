
package shop.ozip.dev.src.comment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.comment.model.*;
import shop.ozip.dev.src.feed.model.GetFeedsMediaFeedsOthersRes;
import shop.ozip.dev.utils.Common;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class CommentDao {

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

    // 특정 댓글 가져오기
    public Comment getCommentById(Long id){
        String getFeedByIdQuery = ""
                + "SELECT * "
                + "FROM   comment "
                + "WHERE  id = ?;";
        return this.jdbcTemplate.queryForObject(getFeedByIdQuery,
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getLong("feed_id"),
                        rs.getString("content"),
                        rs.getLong("recomment_id"),
                        rs.getInt("is_recomment"),
                        Common.formatTimeStamp(rs.getTimestamp("created_at")),
                        Common.formatTimeStamp(rs.getTimestamp("updated_at"))
                ), id);
    }
    // 특정 댓글 존재하는지 체크
    public boolean checkCommentExistById(Long id) {
        String checkCommentExistByFeedIdQuery = ""
                + "SELECT EXISTS (SELECT * "
                + "               FROM   comment "
                + "               WHERE  id = ?) AS exist ";

        return this.jdbcTemplate.queryForObject(checkCommentExistByFeedIdQuery, boolean.class, id);
    }

    // 특정 피드에 달린 댓글의 총 개수 구하기
    public Integer countCommentByFeedId(Long feedId){
        String countCommentByFeedIdQuery = ""
                + "SELECT Count(*) "
                + "FROM   comment "
                + "WHERE  feed_id = ? ";

        return this.jdbcTemplate.queryForObject(countCommentByFeedIdQuery, Integer.class, feedId);

    }


    
    // 미디어 피드에 댓글 달기
    public PostCommentsMediaFeedsRes createMediaFeedComment(PostCommentsMediaFeedsReq postCommentsMediaFeedsReq, Long userId) {
        String createMediaFeedCommentQuery;
        Object[] createMediaFeedCommentParams;
        if (postCommentsMediaFeedsReq.getIsRecomment() == 1) {
            createMediaFeedCommentQuery = ""
                    + "INSERT INTO comment "
                    + "            (user_id, "
                    + "             feed_id, "
                    + "             content, "
                    + "             recomment_id, "
                    + "             is_recomment) "
                    + "VALUES     (?, "
                    + "            ?, "
                    + "            ?, "
                    + "            ?, "
                    + "            ?)";
            createMediaFeedCommentParams = new Object[]{
                    userId,
                    postCommentsMediaFeedsReq.getFeedId(),
                    postCommentsMediaFeedsReq.getContent(),
                    postCommentsMediaFeedsReq.getRecommentId(),
                    postCommentsMediaFeedsReq.getIsRecomment()
            };
        }
        else {
            createMediaFeedCommentQuery = ""
                    + "INSERT INTO comment "
                    + "            (user_id, "
                    + "             feed_id, "
                    + "             content) "
                    + "VALUES     (?, "
                    + "            ?, "
                    + "            ?)";
            createMediaFeedCommentParams = new Object[]{
                    userId,
                    postCommentsMediaFeedsReq.getFeedId(),
                    postCommentsMediaFeedsReq.getContent()
            };
        }
        this.jdbcTemplate.update(createMediaFeedCommentQuery, createMediaFeedCommentParams);
        String lastInsertIdQuery = "select last_insert_id()";
        Long recentId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,long.class);

        Comment comment = getCommentById(recentId);
        return new PostCommentsMediaFeedsRes(
                comment.getId(),
                comment.getFeedId(),
                comment.getContent(),
                comment.getRecommentId(),
                comment.getIsRecomment()
        );
    }

    // 특정 피드에 달린 일부 댓글 조회
    public List<GetCommentsPartMediaFeedsResComment> retrieveCommentsPart(Long userId, Long feedId) {
        Object[] retrieveCommentsPartParams = new Object[]{
            userId, userId, feedId
        };
        String retrieveCommentsPartQuery = ""
                + "SELECT CASE "
                + "         WHEN EXISTS(SELECT * "
                + "                     FROM   comment "
                + "                     WHERE  recomment_id = A.id) THEN 1 "
                + "         ELSE 0 "
                + "       end                                      AS "
                + "       main_comment_does_have_recomment, "
                + "       A.id                                     AS main_comment_id, "
                + "       A.user_id                                AS main_comment_user_id, "
                + "       user.profile_image_url                   AS "
                + "       main_comment_profile_image_url, "
                + "       user.nickname                            AS main_comment_nickname, "
                + "       A.content                                AS main_comment_content, "
                + "       CASE "
                + "         WHEN td < 60 THEN Concat(td, \"초\") "
                + "         WHEN Round(td / 60) < 60 THEN Concat(Round(td / 60), \"분\") "
                + "         WHEN Round(Round(td / 60) / 60) < 24 THEN "
                + "         Concat(Round(Round(td / 60) / 60), \"시간\") "
                + "         WHEN Round(Round(Round(td / 60) / 60) / 24) < 7 THEN Concat(Round( "
                + "         Round(Round(td / 60) / 60) / 24), \"일\") "
                + "         WHEN Round(Round(Round(Round(td / 60) / 60) / 24) / 7) < 5 THEN "
                + "         Concat(Round(Round(Round(Round(td / 60) / 60) / 24) / 7), \"주\") "
                + "         WHEN Round(Round(Round(Round(td / 60) / 60) / 24) / 30) < 12 THEN "
                + "         Concat(Round(Round(Round(Round(td / 60) / 60) / 24) / 30), \"개월\") "
                + "         ELSE Concat(Round(Round(Round(Round(Round(td / 60) / 60) / 24) / 30) / "
                + "                           12), "
                + "              \"년\") "
                + "       end                                      AS main_comment_uploaded_at, "
                + "       ( EXISTS(SELECT * "
                + "                FROM   like_comment "
                + "                WHERE  user_id = ? "
                + "                       AND comment_id = A.id) ) AS main_comment_is_liked, "
                + "       B.id                                     AS recomment_id, "
                + "       B.user_id                                AS recomment_user_id, "
                + "       B.profile_image_url                      AS recomment_profile_image_url, "
                + "       B.nickname                               AS recomment_nickname, "
                + "       B.content                                AS recomment_content, "
                + "       B.uploaded_at                            AS recomment_uploaded_at, "
                + "       B.is_liked                               AS recomment_is_liked "
                + "FROM   comment A "
                + "       LEFT JOIN (SELECT rA.*, "
                + "                         user.profile_image_url, "
                + "                         user.nickname, "
                + "                         ( EXISTS(SELECT * "
                + "                                  FROM   like_comment "
                + "                                  WHERE  user_id = ? "
                + "                                         AND comment_id = rA.id) ) AS is_liked, "
                + "                         CASE "
                + "                           WHEN td < 60 THEN Concat(td, \"초\") "
                + "                           WHEN Round(td / 60) < 60 THEN "
                + "                           Concat(Round(td / 60), \"분\") "
                + "                           WHEN Round(Round(td / 60) / 60) < 24 THEN "
                + "                           Concat(Round(Round(td / 60) / 60), \"시간\") "
                + "                           WHEN Round(Round(Round(td / 60) / 60) / 24) < 7 THEN "
                + "                           Concat( "
                + "                           Round( "
                + "                           Round(Round(td / 60) / 60) / 24), \"일\") "
                + "                           WHEN Round(Round(Round(Round(td / 60) / 60) / 24) / 7 "
                + "                                ) < 5 "
                + "                                          THEN "
                + "                           Concat(Round(Round(Round(Round(td / 60) / 60) / 24) / "
                + "                                        7), "
                + "                           \"주\") "
                + "                           WHEN Round(Round(Round(Round(td / 60) / 60) / 24) / "
                + "                                      30) < 12 "
                + "                                          THEN "
                + "                           Concat(Round(Round(Round(Round(td / 60) / 60) / 24) / "
                + "                                        30), "
                + "                           \"개월\") "
                + "                           ELSE Concat(Round(Round(Round(Round(Round(td / 60) / "
                + "                                                               60) / "
                + "                                                         24) / 30) / "
                + "                                             12), "
                + "                                \"년\") "
                + "                         end                                       AS "
                + "                         uploaded_at "
                + "                  FROM   comment rA "
                + "                         JOIN (SELECT Timestampdiff(second, created_at, Now()) "
                + "                                      AS td, "
                + "                                      id "
                + "                               FROM   comment) forTd "
                + "                           ON forTd.id = rA.id "
                + "                         JOIN user "
                + "                           ON user.id = rA.user_id "
                + "                  GROUP  BY rA.recomment_id "
                + "                  ORDER  BY rA.created_at DESC "
                + "                  LIMIT  1) B "
                + "              ON B.recomment_id = A.id "
                + "       JOIN user "
                + "         ON user.id = A.user_id "
                + "       JOIN (SELECT Timestampdiff(second, created_at, Now()) AS td, "
                + "                    id "
                + "             FROM   comment) forTd "
                + "         ON forTd.id = A.id "
                + "WHERE  A.feed_id = ? "
                + "       AND A.is_recomment = 0 "
                + "ORDER  BY A.created_at DESC "
                + "LIMIT  5 ";
        return this.jdbcTemplate.query(retrieveCommentsPartQuery,
                (rs, rowNum) -> new GetCommentsPartMediaFeedsResComment(
                        rs.getInt("main_comment_does_have_recomment"),
                        rs.getLong("main_comment_id"),
                        rs.getLong("main_comment_user_id"),
                        rs.getString("main_comment_profile_image_url"),
                        rs.getString("main_comment_nickname"),
                        rs.getString("main_comment_content"),
                        rs.getString("main_comment_uploaded_at"),
                        rs.getInt("main_comment_is_liked"),
                        rs.getLong("recomment_id"),
                        rs.getLong("recomment_user_id"),
                        rs.getString("recomment_profile_image_url"),
                        rs.getString("recomment_nickname"),
                        rs.getString("recomment_content"),
                        rs.getString("recomment_uploaded_at"),
                        rs.getInt("recomment_is_liked")
                ), retrieveCommentsPartParams);


    }
}
