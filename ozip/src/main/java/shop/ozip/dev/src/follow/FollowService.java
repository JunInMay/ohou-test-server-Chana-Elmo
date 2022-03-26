
package shop.ozip.dev.src.follow;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.src.follow.model.PostFollowsUsersReq;
import shop.ozip.dev.src.follow.model.PostFollowsUsersRes;
import shop.ozip.dev.src.user.UserDao;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedDao feedDao;
    private final FollowDao followDao;
    private final FollowProvider followProvider;
    private final JwtService jwtService;
    private final String fileName;
    private final UserDao userDao;


    @Autowired
    public FollowService(FollowDao followDao, FollowProvider followProvider, JwtService jwtService, FeedDao feedDao, UserDao userDao) {
        this.followDao = followDao;
        this.followProvider = followProvider;
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.fileName = "FeedService";
        this.feedDao = feedDao;

    }

    public PostFollowsUsersRes createFollowsUsers(PostFollowsUsersReq postFollowsUsersReq) throws BaseException {
        Long userId = jwtService.getUserId();
        if (followDao.checkFollowUserExist(userId, postFollowsUsersReq.getUserId())){
            throw new BaseException(POST_FOLLOW_ALREADY_FOLLOW);
        }
        if (!userDao.checkUserExistById(postFollowsUsersReq.getUserId())){
            throw new BaseException(USER_NOT_EXIST);
        }


        try{
            int result = followDao.createFollowsUsers(userId, postFollowsUsersReq);
            return new PostFollowsUsersRes(
                    postFollowsUsersReq.getUserId(),
                    result
            );
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }
}
