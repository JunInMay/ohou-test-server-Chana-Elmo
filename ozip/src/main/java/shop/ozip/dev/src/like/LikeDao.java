
package shop.ozip.dev.src.like;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.like.model.DeleteLikesCommentsReq;
import shop.ozip.dev.src.like.model.DeleteLikesFeedsReq;
import shop.ozip.dev.src.like.model.PostLikesCommentsReq;
import shop.ozip.dev.src.like.model.PostLikesFeedsReq;

import javax.sql.DataSource;


@Repository
public class LikeDao {

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

    // 특정 유저가 좋아요한 피드의 수 가져오기
    public Integer getCountLikeFeedByUserId(Long userId){
        String getCountLikeFeedByUserIdQuery = ""
                + "SELECT Count(*) "
                + "FROM   like_feed "
                + "WHERE  user_id = ?;";
        return this.jdbcTemplate.queryForObject(
                getCountLikeFeedByUserIdQuery,
                Integer.class,
                userId);
    }

    // 유저가 특정 댓글 좋아요했는지 체크
    public Boolean checkLikeCommentExist(Long userId, Long commentId){
        String checkLikeCommentExistQuery = ""
                + "SELECT EXISTS (SELECT * "
                + "               FROM   like_comment "
                + "               WHERE  user_id = ? and comment_id = ?) AS exist;";
        Object[] checkLikeCommentExistParams = new Object[]{userId, commentId};

        return this.jdbcTemplate.queryForObject(checkLikeCommentExistQuery, Boolean.class, checkLikeCommentExistParams);
    }
    // 유저가 특정 피드 좋아요했는지 체크
    public Boolean checkLikeFeedExist(Long userId, Long feedId){
        String checkLikeFeedExistQuery = ""
                + "SELECT EXISTS (SELECT * "
                + "               FROM   like_feed "
                + "               WHERE  user_id = ? and feed_id = ?) AS exist;";
        Object[] checkLikeFeedExistParams = new Object[]{userId, feedId};

        return this.jdbcTemplate.queryForObject(checkLikeFeedExistQuery, Boolean.class, checkLikeFeedExistParams);
    }


    // 댓글 좋아요하기
    public Integer createLikesComment(Long userId, PostLikesCommentsReq postLikesCommentsReq) {
        String createLikesCommentQuery = ""
                + "INSERT INTO like_comment "
                + "            (user_id, "
                + "             comment_id) "
                + "VALUES     (?, "
                + "            ?) ";
        Object[] createLikesCommentParams = new Object[]{
                userId,
                postLikesCommentsReq.getCommentId()};

        return this.jdbcTemplate.update(createLikesCommentQuery, createLikesCommentParams);

    }

    // 댓글 좋아요 취소
    public Integer deleteLikesComment(Long userId, DeleteLikesCommentsReq deleteLikesCommentsReq) {
        String deleteLikesCommentQuery = ""
                + "DELETE FROM like_comment "
                + "WHERE  user_id = ? "
                + "       AND comment_id = ? ";
        Object[] deleteLikesCommentParams = new Object[]{
                userId,
                deleteLikesCommentsReq.getCommentId()};

        return this.jdbcTemplate.update(deleteLikesCommentQuery, deleteLikesCommentParams);
    }


    // 피드 좋아요하기
    public Integer createLikesFeed(Long userId, PostLikesFeedsReq postLikesFeedsReq) {
        String createLikesCommentQuery = ""
                + "INSERT INTO like_feed "
                + "            (user_id, "
                + "             feed_id) "
                + "VALUES     (?, "
                + "            ?) ";
        Object[] createLikesCommentParams = new Object[]{
                userId,
                postLikesFeedsReq.getFeedId()};

        return this.jdbcTemplate.update(createLikesCommentQuery, createLikesCommentParams);
    }

    public Integer deleteLikesFeed(Long userId, DeleteLikesFeedsReq deleteLikesFeedsReq) {

        String deleteLikesCommentQuery = ""
                + "DELETE FROM like_feed "
                + "WHERE  user_id = ? "
                + "       AND feed_id = ? ";
        Object[] deleteLikesCommentParams = new Object[]{
                userId,
                deleteLikesFeedsReq.getFeedId()};

        return this.jdbcTemplate.update(deleteLikesCommentQuery, deleteLikesCommentParams);
    }
}
