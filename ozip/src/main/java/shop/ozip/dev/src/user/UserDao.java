
package shop.ozip.dev.src.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.user.model.*;
import shop.ozip.dev.utils.Common;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;

@Repository
public class UserDao {

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
    
    // 이메일 주소로 유저 정보 얻기
    public User getUserByEmail(String email){
        String retrieveUserQuery = ""
                + "SELECT * "
                + "FROM   user "
                + "WHERE  email = ?;";
        String retrieveUserParams = email;


        return this.jdbcTemplate.queryForObject(retrieveUserQuery,
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("provider"),
                        rs.getString("nickname"),
                        rs.getString("profile_image_url"),
                        rs.getString("personal_url"),
                        rs.getString("description"),
                        rs.getInt("point"),
                        rs.getInt("is_deleted"),
                        rs.getInt("is_professional"),
                        rs.getInt("is_provider"),
                        Common.formatTimeStamp(rs.getTimestamp("created_at")),
                        Common.formatTimeStamp(rs.getTimestamp("created_at"))),
                retrieveUserParams);
    }
    // 인덱스로 유저 정보 얻기
    public User getUserById(Long id){
        String retrieveUserQuery = ""
                + "SELECT * "
                + "FROM   user "
                + "WHERE  id = ?;";

        return this.jdbcTemplate.queryForObject(retrieveUserQuery,
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("provider"),
                        rs.getString("nickname"),
                        rs.getString("profile_image_url"),
                        rs.getString("personal_url"),
                        rs.getString("description"),
                        rs.getInt("point"),
                        rs.getInt("is_deleted"),
                        rs.getInt("is_professional"),
                        rs.getInt("is_provider"),
                        Common.formatTimeStamp(rs.getTimestamp("created_at")),
                        Common.formatTimeStamp(rs.getTimestamp("created_at"))),
                id);
    }





    // 유저 1명 조회
    public GetUsersRes getUsers(Long userId){
        String getUserQuery = "select * from user where id = ?";
        Long getUserParams = userId;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUsersRes(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("nickname"),
                        rs.getString("description"),
                        rs.getInt("point")),
                getUserParams);
    }

    

    public Long createUser(PostUsersReq postUsersReq){
        String createUserQuery = "insert into user (email, password, nickname, provider, profile_image_url) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUsersReq.getEmail(), postUsersReq.getPassword(), postUsersReq.getNickname(), postUsersReq.getProvider(), postUsersReq.getProfileImageUrl()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,Long.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from user where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }
    
    // 닉네임 존재하는지 체크
    public int checkNickname(String nickname) {
        String checkNicknameQuery = "select exists(select nickname from user where nickname = ?)";
        String checkNicknameParams = nickname;
        return this.jdbcTemplate.queryForObject(checkNicknameQuery, int.class, checkNicknameParams);
    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public UserPwd getPwd (PostLoginReq postLoginReq){
        String getPwdQuery = ""
                + "SELECT id, "
                + "       password "
                + "FROM   user "
                + "WHERE  email = ?";
        String getPwdParams = postLoginReq.getEmail();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new UserPwd(
                        rs.getLong("id"),
                        rs.getString("password")
                ),
                getPwdParams
                );

    }

    // 특정 피드의 작성자 정보 조회 API
    public GetUsersFeedsRes retrieveUsersFeeds(Long userId, Long feedId) {
        String retrieveUsersFeedsQuery = ""
                + "SELECT user.id, "
                + "       user.profile_image_url                        AS profile_image_url, "
                + "       user.nickname                                 AS nickname, "
                + "       ( EXISTS(SELECT * "
                + "                FROM   follow_user "
                + "                WHERE  user_id = ? "
                + "                       AND following_id = user.id) ) AS is_followed, "
                + "       CASE "
                + "         WHEN td < 60 THEN Concat(td, \"초 전\") "
                + "         WHEN Round(td / 60) < 60 THEN Concat(Round(td / 60), \"분 전\") "
                + "         WHEN Round(Round(td / 60) / 60) < 24 THEN "
                + "         Concat(Round(Round(td / 60) / 60), \"시간 전\") "
                + "         WHEN Round(Round(Round(td / 60) / 60) / 24) < 7 THEN Concat(Round( "
                + "         Round(Round(td / 60) / 60) / 24), \"일 전\") "
                + "         WHEN Round(Round(Round(Round(td / 60) / 60) / 24) / 7) < 5 THEN "
                + "         Concat(Round(Round(Round(Round(td / 60) / 60) / 24) / 7), \"주 전\") "
                + "         WHEN Round(Round(Round(Round(td / 60) / 60) / 24) / 30) < 12 THEN "
                + "         Concat(Round(Round(Round(Round(td / 60) / 60) / 24) / 30), \"개월 전\" "
                + "         ) "
                + "         ELSE Concat(Round(Round(Round(Round(Round(td / 60) / 60) / 24) / 30) / "
                + "                           12), "
                + "              \"년 전\" "
                + "              ) "
                + "       end                                           AS uploaded_at "
                + "FROM   feed "
                + "       JOIN user "
                + "         ON feed.user_id = user.id "
                + "       JOIN (SELECT Timestampdiff(second, created_at, Now()) AS td, "
                + "                    id "
                + "             FROM   feed) forTd "
                + "         ON forTd.id = feed.id "
                + "WHERE  feed.id = ?";
        Object[] retrieveUsersFeedsParams = new Object[]{userId, feedId};

        return this.jdbcTemplate.queryForObject(retrieveUsersFeedsQuery,
                (rs,rowNum)-> new GetUsersFeedsRes(
                        rs.getLong("id"),
                        rs.getString("profile_image_url"),
                        rs.getString("nickname"),
                        rs.getInt("is_followed"),
                        rs.getString("uploaded_at")
                ), retrieveUsersFeedsParams
        );
    }
}
