
package shop.ozip.dev.src.follow;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.feed.model.MediaFeed;
import shop.ozip.dev.src.follow.model.*;
import shop.ozip.dev.utils.Common;

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
    public Integer getCountFolloweeByUserId(Long userId){
        String getCountFollowerByUserIdQuery = ""
                + "SELECT Count(*) "
                + "FROM   follow_user "
                + "WHERE  user_id = ?;";
        return this.jdbcTemplate.queryForObject(
                getCountFollowerByUserIdQuery,
                Integer.class,
                userId);
    }


}
