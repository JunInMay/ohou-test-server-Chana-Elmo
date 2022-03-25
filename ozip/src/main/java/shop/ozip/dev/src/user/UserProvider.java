
package shop.ozip.dev.src.user;


import org.springframework.transaction.annotation.Transactional;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponseStatus;
import shop.ozip.dev.src.follow.FollowDao;
import shop.ozip.dev.src.like.LikeDao;
import shop.ozip.dev.src.scrapbook.ScrapbookDao;
import shop.ozip.dev.src.user.model.*;
import shop.ozip.dev.utils.JwtService;
import shop.ozip.dev.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;
    private final String fileName;
    private final FollowDao followDao;
    private final LikeDao likeDao;
    private final ScrapbookDao scrapbookDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService, FollowDao followDao, LikeDao likeDao, ScrapbookDao scrapbookDao) {
        this.userDao = userDao;
        this.jwtService = jwtService;
        this.fileName = "UserProvider";
        this.followDao = followDao;
        this.likeDao = likeDao;
        this.scrapbookDao = scrapbookDao;
    }

//    public List<GetUserRes> getUsers() throws BaseException {
//        try{
//            List<GetUserRes> getUserRes = userDao.getUsers();
//            return getUserRes;
//        }
//        catch (Exception exception) {
//            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
//        }
//    }

//    public List<GetUserRes> getUsersByEmail(String email) throws BaseException{
//        try{
//            List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
//            return getUsersRes;
//        }
//        catch (Exception exception) {
//            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
//        }
//                    }


    public GetUsersRes getUser(Long userId) throws BaseException {
        try {
            GetUsersRes getUsersRes = userDao.getUsers(userId);
            return getUsersRes;
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public int checkNickname(String nickname) throws BaseException {
        try {
            return userDao.checkNickname(nickname);
        } catch (Exception exception) {
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        UserPwd userPwd = null;
        String encryptPwd;

        try {
            userPwd = userDao.getPwd(postLoginReq);
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.FAILED_TO_LOGIN);
        }

        try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.PASSWORD_DECRYPTION_ERROR);
        }

        if(userPwd.getPassword().equals(encryptPwd)){
            Long userId = userPwd.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId,jwt);
        }
        else{
            throw new BaseException(BaseResponseStatus.FAILED_TO_LOGIN);
        }

    }

    // 카카오 로그인
    public PostLoginRes loginKakaoUser(String kakaoId) throws BaseException{
        try {
            User user = userDao.getUserByEmail(kakaoId);
            String jwt = jwtService.createJwt(user.getId());
            return new PostLoginRes(user.getId(), jwt);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 내 정보 가져오기
    @Transactional
    public GetUsersMeRes getUsersMe() throws BaseException{
        String methodName = "getUsersMe";
        Long userId = jwtService.getUserId();
        try {
            User user = userDao.getUserById(userId);

            return new GetUsersMeRes(
                    user.getId(),
                    user.getProfileImageUrl(),
                    user.getEmail(),
                    user.getNickname(),
                    user.getDescription(),
                    followDao.getCountFollowerByUserId(userId),
                    followDao.getCountFollowUserByUserId(userId)+ followDao.getCountFollowKeywordByUserId(userId),
                    likeDao.getCountLikeFeedByUserId(userId),
                    scrapbookDao.getCountScrapbookFeedByUserId(userId)
            );
        } catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


}
