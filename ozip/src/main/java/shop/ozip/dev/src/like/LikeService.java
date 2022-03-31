
package shop.ozip.dev.src.like;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.comment.CommentDao;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.src.like.model.*;
import shop.ozip.dev.src.user.UserDao;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class LikeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedDao feedDao;
    private final shop.ozip.dev.src.like.LikeDao likeDao;
    private final shop.ozip.dev.src.like.LikeProvider likeProvider;
    private final JwtService jwtService;
    private final String fileName;
    private final CommentDao commentDao;
    private final UserDao userDao;


    @Autowired
    public LikeService(LikeDao likeDao, LikeProvider likeProvider, JwtService jwtService, FeedDao feedDao, CommentDao commentDao, UserDao userDao) {
        this.likeDao = likeDao;
        this.likeProvider = likeProvider;
        this.jwtService = jwtService;
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.fileName = "FeedService";
        this.feedDao = feedDao;

    }

    // 댓글 좋아요하기
    public PostLikesCommentsRes createLikesComment(PostLikesCommentsReq postLikesCommentsReq) throws BaseException {

        Long userId = jwtService.getUserId();
        if(!commentDao.checkCommentExistById(postLikesCommentsReq.getCommentId())){
            throw new BaseException(COMMENT_NOT_EXIST);
        }
        if(likeDao.checkLikeCommentExist(userId, postLikesCommentsReq.getCommentId())){
            throw new BaseException(ALREADY_LIKED);
        }

        try {
            Integer result = likeDao.createLikesComment(userId, postLikesCommentsReq);
            return new PostLikesCommentsRes(
                    userId,
                    postLikesCommentsReq.getCommentId(),
                    result
            );
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 댓글 좋아요 취소
    public DeleteLikesCommentsRes deleteLikesComment(DeleteLikesCommentsReq deleteLikesCommentsReq) throws BaseException{
        Long userId = jwtService.getUserId();
        if(!commentDao.checkCommentExistById(deleteLikesCommentsReq.getCommentId())){
            throw new BaseException(COMMENT_NOT_EXIST);
        }
        if(!likeDao.checkLikeCommentExist(userId, deleteLikesCommentsReq.getCommentId())){
            throw new BaseException(NOT_LIKED);
        }

        try {
            Integer result = likeDao.deleteLikesComment(userId, deleteLikesCommentsReq);
            return new DeleteLikesCommentsRes(
                    userId,
                    deleteLikesCommentsReq.getCommentId(),
                    result
            );
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 피드 좋아요
    public PostLikesFeedsRes createLikesFeed(PostLikesFeedsReq postLikesFeedsReq) throws BaseException {


        Long userId = jwtService.getUserId();
        if(!commentDao.checkCommentExistById(postLikesFeedsReq.getFeedId())){
            throw new BaseException(FEED_NOT_EXIST);
        }
        if(likeDao.checkLikeFeedExist(userId, postLikesFeedsReq.getFeedId())){
            throw new BaseException(ALREADY_LIKED);
        }

        try {
            Integer result = likeDao.createLikesFeed(userId, postLikesFeedsReq);
            return new PostLikesFeedsRes(
                    userId,
                    postLikesFeedsReq.getFeedId(),
                    result
            );
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 피드 좋아요 취소
    public DeleteLikesFeedsRes deleteLikesFeed(DeleteLikesFeedsReq deleteLikesFeedsReq) throws BaseException {

        Long userId = jwtService.getUserId();
        if(!commentDao.checkCommentExistById(deleteLikesFeedsReq.getFeedId())){
            throw new BaseException(FEED_NOT_EXIST);
        }
        if(!likeDao.checkLikeFeedExist(userId, deleteLikesFeedsReq.getFeedId())){
            throw new BaseException(NOT_LIKED);
        }

        try {
            Integer result = likeDao.deleteLikesFeed(userId, deleteLikesFeedsReq);
            return new DeleteLikesFeedsRes(
                    userId,
                    deleteLikesFeedsReq.getFeedId(),
                    result
            );
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
