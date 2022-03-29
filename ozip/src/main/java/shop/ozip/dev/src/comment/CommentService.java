
package shop.ozip.dev.src.comment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.comment.model.*;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class CommentService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedDao feedDao;
    private final shop.ozip.dev.src.comment.CommentDao commentDao;
    private final shop.ozip.dev.src.comment.CommentProvider commentProvider;
    private final JwtService jwtService;
    private final String fileName;


    @Autowired
    public CommentService(CommentDao commentDao, CommentProvider commentProvider, JwtService jwtService, FeedDao feedDao) {
        this.commentDao = commentDao;
        this.commentProvider = commentProvider;
        this.jwtService = jwtService;
        this.fileName = "FeedService";
        this.feedDao = feedDao;

    }

    // 피드에 댓글달기
    public PostCommentsRes createComment(PostCommentsReq postCommentsReq) throws BaseException {
        String methodName = "createComment";
        Long userId = jwtService.getUserId();
        // 댓글 달 피드 존재 여부
        if (!feedDao.checkFeedExistById(postCommentsReq.getFeedId())) {
            throw new BaseException(FEED_NOT_EXIST);
        }
        // 답글 달 댓글 존재 여부
        if (postCommentsReq.getIsRecomment() == 1){
            if (!commentDao.checkCommentExistById(postCommentsReq.getRecommentId())){
                throw new BaseException(RECOMMENT_NOT_EXIST);
            }
            Comment recomment = commentDao.getCommentById(postCommentsReq.getRecommentId());
            if (recomment.getFeedId() != postCommentsReq.getFeedId()){
                throw new BaseException(POST_RECOMMENT_FEED_NOT_MATCH);
            }
        }
        try{
            return commentDao.createComment(postCommentsReq, userId);
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
