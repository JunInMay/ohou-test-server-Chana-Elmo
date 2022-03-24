
package shop.ozip.dev.src.scrapbook;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.scrapbook.model.Scrapbook;
import shop.ozip.dev.utils.Common;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class ScrapbookDao {

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

    // 특정 유저가 스크랩한 피드 개수 가져오기
    public Integer getCountScrapbookFeedByUserId(Long userId){
        String getCountScrapbookFeedByUserIdQuery = ""
                + "SELECT Count(*) "
                + "FROM   scrapbook_feed "
                + "       JOIN scrapbook "
                + "         ON scrapbook_feed.scrapbook_id = scrapbook.id "
                + "WHERE  scrapbook.user_id = ?;";
        return this.jdbcTemplate.queryForObject(
                getCountScrapbookFeedByUserIdQuery,
                Integer.class,
                userId);
    }
}
