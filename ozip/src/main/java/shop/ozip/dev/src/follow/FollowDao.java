
package shop.ozip.dev.src.follow;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.follow.model.DeleteFollowsUsersReq;
import shop.ozip.dev.src.follow.model.PostFollowsUsersReq;

import javax.sql.DataSource;


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

    // 팔로우하기
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
}
