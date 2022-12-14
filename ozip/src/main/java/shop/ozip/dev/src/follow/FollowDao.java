
package shop.ozip.dev.src.follow;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.ozip.dev.src.follow.model.*;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class FollowDao {

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

    // 특정 유저"를 팔로우 한" 사람의 수 가져오기
    public Integer getCountFollowerByUserId(Long userId){
        String getCountFollowerByUserIdQuery = ""
                + "SELECT Count(*) "
                + "FROM   follow_user "
                + "WHERE  following_id = ?;";
        return this.jdbcTemplate.queryForObject(
                getCountFollowerByUserIdQuery,
                Integer.class,
                userId);
    }
    // 특정 유저"가 팔로우 한" 사람의 수 가져오기
    public Integer getCountFollowUserByUserId(Long userId){
        String getCountFollowerByUserIdQuery = ""
                + "SELECT Count(*) "
                + "FROM   follow_user "
                + "WHERE  user_id = ?;";
        return this.jdbcTemplate.queryForObject(
                getCountFollowerByUserIdQuery,
                Integer.class,
                userId);
    }
    // 특정 유저"가 팔로우 한" 키워드의 수 가져오기
    public Integer getCountFollowKeywordByUserId(Long userId){
        String getCountFollowKeywordByUserIdQuery = ""
                + "SELECT Count(*) "
                + "FROM   follow_keyword "
                + "WHERE  user_id = ?;";
        return this.jdbcTemplate.queryForObject(
                getCountFollowKeywordByUserIdQuery,
                Integer.class,
                userId);
    }

    public Boolean checkFollowUserExist(Long userId, Long followingId){
        String checkFollowUserExistQuery = ""
                + "SELECT EXISTS (SELECT * "
                + "               FROM   follow_user "
                + "               WHERE  user_id = ? and following_id = ?) AS exist;";
        Object[] checkFollowUserExistParams = new Object[]{userId, followingId};

        return this.jdbcTemplate.queryForObject(checkFollowUserExistQuery, Boolean.class, checkFollowUserExistParams);
    }

    public Boolean checkFollowKeywordExist(Long userId, Long keywordId){
        String checkFollowUserExistQuery = ""
                + "SELECT EXISTS (SELECT * "
                + "               FROM   follow_keyword "
                + "               WHERE  user_id = ? and keyword_id = ?) AS exist;";
        Object[] checkFollowUserExistParams = new Object[]{userId, keywordId};

        return this.jdbcTemplate.queryForObject(checkFollowUserExistQuery, Boolean.class, checkFollowUserExistParams);
    }

    // 팔로우하기
    @Transactional
    public int createFollowsUsers(Long userId, PostFollowsUsersReq postFollowsUsersReq) {
        String createFollowsUsersQuery = ""
                + "INSERT INTO follow_user "
                + "            (user_id, "
                + "             following_id) "
                + "VALUES     (?, "
                + "            ?)";
        Object[] createFollowsUsersParams = new Object[]{
                userId,
                postFollowsUsersReq.getUserId()
                };

        return this.jdbcTemplate.update(
                createFollowsUsersQuery,
                createFollowsUsersParams);
    }

    // 언팔로우하기
    @Transactional
    public int deleteFollowsUsers(Long userId, DeleteFollowsUsersReq deleteFollowsUsersReq) {
        String deleteFollowsUsersQuery = ""
                + "DELETE FROM follow_user "
                + "WHERE  user_id = ? "
                + "       AND following_id = ?";
        Object[] deleteFollowsUsersParams = new Object[]{
                userId,
                deleteFollowsUsersReq.getUserId()
        };

        return this.jdbcTemplate.update(
                deleteFollowsUsersQuery,
                deleteFollowsUsersParams);
    }

    // 키워드 팔로우
    @Transactional
    public int createFollowsKeywords(Long userId, PostFollowsKeywordsReq postFollowsKeywordsReq) {

        String createFollowsKeywordsQuery = ""
                + "INSERT INTO follow_keyword "
                + "            (user_id, "
                + "             keyword_id) "
                + "VALUES     (?, "
                + "            ?)";
        Object[] createFollowsKeywordsParams = new Object[]{
                userId,
                postFollowsKeywordsReq.getKeywordId()
        };

        return this.jdbcTemplate.update(
                createFollowsKeywordsQuery,
                createFollowsKeywordsParams);
    }

    @Transactional
    public int deleteFollowsKeywords(Long userId, DeleteFollowsKeywordsReq deleteFollowsKeywordsReq) {
        String deleteFollowsUsersQuery = ""
                + "DELETE FROM follow_keyword "
                + "WHERE  user_id = ? "
                + "       AND keyword_id = ?";
        Object[] deleteFollowsUsersParams = new Object[]{
                userId,
                deleteFollowsKeywordsReq.getKeywordId()
        };

        return this.jdbcTemplate.update(
                deleteFollowsUsersQuery,
                deleteFollowsUsersParams);
    }


    // 특정유저가 팔료우한 키워드 리스트 가져오기
    public List<GetFollowsKeywordsRes> retrieveFollowKeywordList(Long myId, Long userId) {
        String retrieveFollowKeywordListQuery = ""
                + "SELECT keyword.id, "
                + "       Concat(\"#\", keyword.name)                           AS name, "
                + "       ( EXISTS(SELECT * "
                + "                FROM   follow_keyword "
                + "                WHERE  user_id = ? "
                + "                       AND keyword_id = main.keyword_id) ) AS is_followed "
                + "FROM   follow_keyword main "
                + "       JOIN keyword "
                + "         ON main.keyword_id = keyword.id "
                + "WHERE  user_id = ? ";
        Object[] retrieveFollowKeywordListParams = new Object[]{
                myId, userId
        };
        return this.jdbcTemplate.query(retrieveFollowKeywordListQuery,
                (rs, rowNum) -> new GetFollowsKeywordsRes(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("is_followed")
                ), retrieveFollowKeywordListParams);
    }

    // 내가 팔로우한 유저 조회
    public List<GetFollowsFolloweesRes> retrieveFolloweesList(Long userId, Long myId) {
        String retrieveFolloweesListQuery = ""
                + "SELECT user.id, "
                + "       user.profile_image_url, "
                + "       user.nickname, "
                + "       user.description, "
                + "       ( EXISTS(SELECT * "
                + "                FROM   follow_user "
                + "                WHERE  user_id = ? "
                + "                       AND following_id = user.id) ) AS is_followed "
                + "FROM   follow_user "
                + "       JOIN user "
                + "         ON user.id = follow_user.following_id "
                + "WHERE  follow_user.user_id = ? "
                + "ORDER  BY follow_user.created_at ASC;";
        Object[] retrieveFolloweesListParams = new Object[]{
                myId, userId
        };


        return this.jdbcTemplate.query(retrieveFolloweesListQuery,
                (rs, rowNum) -> new GetFollowsFolloweesRes(
                        rs.getLong("id"),
                        rs.getString("profile_image_url"),
                        rs.getString("nickname"),
                        rs.getString("description"),
                        rs.getInt("is_followed")
                ), retrieveFolloweesListParams);
    }

    // 특정 유저를 팔로우하고있는 유저들 조회
    public List<GetFollowsFollowersRes> retrieveFollowersList(Long userId, Long myId) {
        Object[] retrieveFollowersListParams = new Object[]{
                myId, userId
        };
        String retrieveFollowersListQuery = ""
                + "SELECT user.id, "
                + "       user.profile_image_url, "
                + "       user.nickname, "
                + "       user.description, "
                + "       ( EXISTS(SELECT * "
                + "                FROM   follow_user "
                + "                WHERE  user_id = ? "
                + "                       AND following_id = user.id) ) AS is_followed "
                + "FROM   follow_user "
                + "       JOIN user "
                + "         ON user.id = follow_user.user_id "
                + "WHERE  follow_user.following_id = ? "
                + "ORDER  BY follow_user.created_at ASC;";
        return this.jdbcTemplate.query(retrieveFollowersListQuery,
                (rs, rowNum) -> new GetFollowsFollowersRes(
                        rs.getLong("id"),
                        rs.getString("profile_image_url"),
                        rs.getString("nickname"),
                        rs.getString("description"),
                        rs.getInt("is_followed")
                ), retrieveFollowersListParams);
    }
}
