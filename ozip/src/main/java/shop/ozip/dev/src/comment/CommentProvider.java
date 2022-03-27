
package shop.ozip.dev.src.comment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.comment.model.GetCommentsPartMediaFeedsRes;
import shop.ozip.dev.src.comment.model.GetCommentsPartMediaFeedsResComment;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.src.feed.model.Feed;
import shop.ozip.dev.utils.JwtService;

import java.util.List;

import static shop.ozip.dev.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class CommentProvider {

    private final CommentDao commentDao;
    private final JwtService jwtService;
    private final String fileName;


    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final FeedDao feedDao;

    @Autowired
    public CommentProvider(CommentDao commentDao, JwtService jwtService, FeedDao feedDao) {
        this.commentDao = commentDao;
        this.jwtService = jwtService;
        this.feedDao = feedDao;
        this.fileName = "FeedProvider";
    }

    // 피드의 댓글 일부 조회
    public GetCommentsPartMediaFeedsRes retrieveCommentsPartMediaFeeds(Long feedId) throws BaseException {
        String methodName = "retrieveMediaFeedOthers";
        Long userId = jwtService.getUserId();
        if (!feedDao.checkFeedExistById(feedId)) {
            throw new BaseException(FEED_NOT_EXIST);
        }
        Feed feed = feedDao.getFeedById(feedId);
        if (feed.getIsMediaFeed() != 1) {
            throw new BaseException(IS_NOT_MEDIA_FEED);
        }
        try{
            List<GetCommentsPartMediaFeedsResComment> getCommentsPartMediaFeedsResCommentList = commentDao.retrieveCommentsPart(userId, feedId);
            Integer allCommentCount = commentDao.countCommentByFeedId(feedId);
            Integer retrievedCommentCount = getCommentsPartMediaFeedsResCommentList.size();

            for (int i=0; i < getCommentsPartMediaFeedsResCommentList.size(); i++){
                retrievedCommentCount += getCommentsPartMediaFeedsResCommentList.get(i).getMainCommentDoesHaveRecomment();
            }

            return new GetCommentsPartMediaFeedsRes(allCommentCount, retrievedCommentCount, getCommentsPartMediaFeedsResCommentList);
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
