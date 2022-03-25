
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


    public GetUsersMediasNineRes retrieveGetUsersMedias(Long userId, Integer type) {
        String queryAttachment = "";
        if (type != 0) {
            queryAttachment = "       AND media_space_type.id = " + type.toString() + " ";
        }

        String retrieveGetUsersMediasQuery =""
                + "SELECT media.url, "
                + "       media_space_type.name, "
                + "       user.id, "
                + "       user.nickname "
                + "FROM   media "
                + "       JOIN feed "
                + "         ON feed.id = media.feed_id "
                + "       JOIN user "
                + "         ON feed.user_id = user.id "
                + "       JOIN media_space_type "
                + "         ON media.media_space_type_id = media_space_type.id "
                + "WHERE  user_id = ? "
                + queryAttachment
                + "ORDER  BY media.created_at DESC "
                + "LIMIT  1";
        String checkGetUsersMediasQuery = "SELECT EXISTS ("+retrieveGetUsersMediasQuery+") AS exist";
        if (!this.jdbcTemplate.queryForObject(checkGetUsersMediasQuery, boolean.class, userId)){
            return new GetUsersMediasNineRes(null, null, null, null);
        }
        GetUsersMediasNineRes getUsersMediasNineRes = this.jdbcTemplate.queryForObject(retrieveGetUsersMediasQuery,
                (rs, rowNum) -> new GetUsersMediasNineRes(
                        rs.getObject("url", String.class),
                        rs.getObject("name", String.class),
                        rs.getObject("id", Long.class),
                        rs.getObject("nickname", String.class)),
                userId);
        return getUsersMediasNineRes;
    }
}
