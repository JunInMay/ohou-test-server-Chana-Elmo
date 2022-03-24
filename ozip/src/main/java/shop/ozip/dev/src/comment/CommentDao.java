
package shop.ozip.dev.src.comment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.comment.model.*;
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
                + "               WHERE  id = ?) AS exist;";

        return this.jdbcTemplate.queryForObject(checkCommentExistByFeedIdQuery, boolean.class, id);
    }


    
    // 미디어 피드에 댓글 달기
    public PostFeedsMediaFeedsCommentsRes createMediaFeedComment(PostFeedsMediaFeedsCommentsReq postFeedsMediaFeedsCommentsReq, Long userId) {
        String createMediaFeedCommentQuery;
        Object[] createMediaFeedCommentParams;
        if (postFeedsMediaFeedsCommentsReq.getIsRecomment() == 1) {
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
                    postFeedsMediaFeedsCommentsReq.getFeedId(),
                    postFeedsMediaFeedsCommentsReq.getContent(),
                    postFeedsMediaFeedsCommentsReq.getRecommentId(),
                    postFeedsMediaFeedsCommentsReq.getIsRecomment()
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
                    postFeedsMediaFeedsCommentsReq.getFeedId(),
                    postFeedsMediaFeedsCommentsReq.getContent()
            };
        }
        this.jdbcTemplate.update(createMediaFeedCommentQuery, createMediaFeedCommentParams);
        String lastInsertIdQuery = "select last_insert_id()";
        Long recentId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,long.class);

        Comment comment = getCommentById(recentId);
        return new PostFeedsMediaFeedsCommentsRes(
                comment.getId(),
                comment.getFeedId(),
                comment.getContent(),
                comment.getRecommentId(),
                comment.getIsRecomment()
        );
    }
}
