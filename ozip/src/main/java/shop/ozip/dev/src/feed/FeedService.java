
package shop.ozip.dev.src.feed;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.feed.model.*;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;
import static shop.ozip.dev.config.BaseResponseStatus.DATABASE_ERROR;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FeedService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedDao feedDao;
    private final FeedProvider feedProvider;
    private final JwtService jwtService;
    private final String fileName;


    @Autowired
    public FeedService(FeedDao feedDao, FeedProvider feedProvider, JwtService jwtService) {
        this.feedDao = feedDao;
        this.feedProvider = feedProvider;
        this.jwtService = jwtService;
        this.fileName = "FeedService";

    }

    // 미디어 피드에 댓글달기
    public PostFeedsMediaFeedsCommentsRes createMediaFeedComment(PostFeedsMediaFeedsCommentsReq postFeedsMediaFeedsCommentsReq) throws BaseException {
        String methodName = "createMediaFeedComment";
        Long userId = jwtService.getUserId();
        // 댓글 달 미디어 피드 존재 여부
        if (!feedDao.checkFeedExistById(postFeedsMediaFeedsCommentsReq.getFeedId())) {
            throw new BaseException(FEED_NOT_EXIST);
        }
        Feed feed = feedDao.getFeedById(postFeedsMediaFeedsCommentsReq.getFeedId());
        if (feed.getIsMediaFeed() != 1) {
            throw new BaseException(IS_NOT_MEDIA_FEED);
        }
        // 답글 달 댓글 존재 여부
        if (postFeedsMediaFeedsCommentsReq.getIsRecomment() == 1){
            if (!feedDao.checkCommentExistById(postFeedsMediaFeedsCommentsReq.getRecommentId())){
                throw new BaseException(RECOMMENT_NOT_EXIST);
            }
            Comment recomment = feedDao.getCommentById(postFeedsMediaFeedsCommentsReq.getRecommentId());
            if (recomment.getFeedId() != postFeedsMediaFeedsCommentsReq.getFeedId()){
                throw new BaseException(POST_RECOMMENT_FEED_NOT_MATCH);
            }
        }
        try{
            return feedDao.createMediaFeedComment(postFeedsMediaFeedsCommentsReq, userId);
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
