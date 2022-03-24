
package shop.ozip.dev.src.like;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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


}
