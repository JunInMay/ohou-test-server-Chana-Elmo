
package shop.ozip.dev.src.follow;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.src.follow.model.*;
import shop.ozip.dev.src.keyword.KeywordDao;
import shop.ozip.dev.src.user.UserDao;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedDao feedDao;
    private final shop.ozip.dev.src.follow.FollowDao followDao;
    private final shop.ozip.dev.src.follow.FollowProvider followProvider;
    private final JwtService jwtService;
    private final String fileName;
    private final UserDao userDao;
    private final KeywordDao keywordDao;


    @Autowired
    public FollowService(FollowDao followDao, FollowProvider followProvider, JwtService jwtService, FeedDao feedDao, UserDao userDao, KeywordDao keywordDao) {
        this.followDao = followDao;
        this.followProvider = followProvider;
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.keywordDao = keywordDao;
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

    public DeleteFollowsUsersRes deleteFollowsUsers(DeleteFollowsUsersReq deleteFollowsUsersReq) throws BaseException{
        Long userId = jwtService.getUserId();
        if (!followDao.checkFollowUserExist(userId, deleteFollowsUsersReq.getUserId())){
            throw new BaseException(DELETE_FOLLOW_NOT_EXIST);
        }
        if (!userDao.checkUserExistById(deleteFollowsUsersReq.getUserId())){
            throw new BaseException(USER_NOT_EXIST);
        }

        try{
            int result = followDao.deleteFollowsUsers(userId, deleteFollowsUsersReq);
            return new DeleteFollowsUsersRes(
                    deleteFollowsUsersReq.getUserId(),
                    result
            );
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 키워드 팔로우하기
    public PostFollowsKeywordsRes createFollowsKeywords(PostFollowsKeywordsReq postFollowsKeywordsReq) throws BaseException{

        Long userId = jwtService.getUserId();
        if (!keywordDao.checkKeywordExistById(postFollowsKeywordsReq.getKeywordId())){
            throw new BaseException(KEYWORD_NOT_EXIST);
        }
        if (followDao.checkFollowKeywordExist(userId, postFollowsKeywordsReq.getKeywordId())){
            throw new BaseException(POST_FOLLOW_ALREADY_FOLLOW);
        }

        try{
            int result = followDao.createFollowsKeywords(userId, postFollowsKeywordsReq);
            return new PostFollowsKeywordsRes(
                    postFollowsKeywordsReq.getKeywordId(),
                    result
            );
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 키워드 언팔로우하기
    public DeleteFollowsKeywordsRes deleteFollowsKeywords(DeleteFollowsKeywordsReq deleteFollowsKeywordsReq) throws BaseException {

        Long userId = jwtService.getUserId();
        if (!keywordDao.checkKeywordExistById(deleteFollowsKeywordsReq.getKeywordId())){
            throw new BaseException(KEYWORD_NOT_EXIST);
        }
        if (!followDao.checkFollowKeywordExist(userId, deleteFollowsKeywordsReq.getKeywordId())){
            throw new BaseException(DELETE_FOLLOW_NOT_EXIST);
        }

        try{
            int result = followDao.deleteFollowsKeywords(userId, deleteFollowsKeywordsReq);
            return new DeleteFollowsKeywordsRes(
                    deleteFollowsKeywordsReq.getKeywordId(),
                    result
            );
        } catch(Exception exception){
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
