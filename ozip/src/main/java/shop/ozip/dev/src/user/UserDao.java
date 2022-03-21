
package shop.ozip.dev.src.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.ozip.dev.src.user.model.*;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    public List<GetUserRes> getUsers(){
//        String getUsersQuery = "select * from UserInfo";
//        return this.jdbcTemplate.query(getUsersQuery,
//                (rs,rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("userName"),
//                        rs.getString("ID"),
//                        rs.getString("Email"),
//                        rs.getString("password"))
//                );
//    }

//    public List<GetUserRes> getUsersByEmail(String email){
//        String getUsersByEmailQuery = "select * from UserInfo where email =?";
//        String getUsersByEmailParams = email;
//        return this.jdbcTemplate.query(getUsersByEmailQuery,
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("userIdx"),
//                        rs.getString("userName"),
//                        rs.getString("ID"),
//                        rs.getString("Email"),
//                        rs.getString("password")),
//                getUsersByEmailParams);
//    }

    public User retrieveUser(String email){
        String retrieveUserQuery = ""
                + "SELECT * "
                + "FROM   user "
                + "WHERE  email = ?;";
        String retrieveUserParams = email;
        System.out.println(email);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return this.jdbcTemplate.queryForObject(retrieveUserQuery,
                (rs, rowNum) -> new User(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("nickname"),
                        rs.getString("description"),
                        rs.getString("provider"),
                        rs.getInt("point"),
                        dateFormat.format(rs.getTimestamp("created_at")),
                        dateFormat.format(rs.getTimestamp("created_at"))),
                retrieveUserParams);
    }

    public GetUserRes getUser(Long userId){
        String getUserQuery = "select * from user where id = ?";
        Long getUserParams = userId;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("nickname"),
                        rs.getString("description"),
                        rs.getInt("point")),
                getUserParams);
    }

    

    public Long createUser(PostUsersReq postUsersReq){
        String createUserQuery = "insert into user (email, password, nickname, provider) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postUsersReq.getEmail(), postUsersReq.getPassword(), postUsersReq.getNickname(), postUsersReq.getProvider()};
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

}
