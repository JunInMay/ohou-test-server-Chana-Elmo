
package shop.ozip.dev.src.comment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.comment.model.*;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.src.feed.model.Feed;
import shop.ozip.dev.utils.JwtService;

import java.util.ArrayList;
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
    public GetCommentsPartRes retrieveCommentsPartMediaFeeds(Long feedId) throws BaseException {
        String methodName = "retrieveCommentsPartMediaFeeds";
        Long userId = jwtService.getUserId();
        if (!feedDao.checkFeedExistById(feedId)) {
            throw new BaseException(FEED_NOT_EXIST);
        }
        Feed feed = feedDao.getFeedById(feedId);
        if (feed.getIsMediaFeed() != 1) {
            throw new BaseException(IS_NOT_MEDIA_FEED);
        }
        try{
            List<GetCommentsPartResComment> getCommentsPartResCommentList = commentDao.retrieveCommentsPart(userId, feedId);
            Integer allCommentCount = commentDao.countCommentByFeedId(feedId);
            Integer retrievedCommentCount = getCommentsPartResCommentList.size();

            for (int i = 0; i < getCommentsPartResCommentList.size(); i++){
                if (getCommentsPartResCommentList.get(i).getMainCommentHaveRecomment() > 0) {
                    retrievedCommentCount += 1;
                }
            }

            return new GetCommentsPartRes(allCommentCount, retrievedCommentCount, getCommentsPartResCommentList);
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 미디어 피드의 댓글 전부 조회
    public GetCommentsListRes retrieveCommentsListMediaFeeds(Long feedId, Long cursor) throws BaseException {

        String methodName = "retrieveCommentsListMediaFeeds";
        Long userId = jwtService.getUserId();
        if (!feedDao.checkFeedExistById(feedId)) {
            throw new BaseException(FEED_NOT_EXIST);
        }
        Feed feed = feedDao.getFeedById(feedId);
        if (feed.getIsMediaFeed() != 1) {
            throw new BaseException(IS_NOT_MEDIA_FEED);
        }
        try{
            List<GetCommentsListResMainComment> getCommentsListResMainCommentList = commentDao.retrieveCommentsListMainComment(feedId, userId, cursor);
            List<GetCommentsListResMainCommentWithRecomment> getCommentsListResMainCommentWithRecommentList = new ArrayList<>();
            for (int i = 0; i < getCommentsListResMainCommentList.size(); i++){
                GetCommentsListResMainComment getCommentsListResMainComment = getCommentsListResMainCommentList.get(i);
                List<GetCommentsListResRecomment> getCommentsListResRecommentList = commentDao.retrieveCommentsListRecomment(feedId, userId, getCommentsListResMainComment.getMainCommentId());
                GetCommentsListResMainCommentWithRecomment getCommentsListResMainCommentWithRecomment = new GetCommentsListResMainCommentWithRecomment(
                        getCommentsListResMainComment.getMainCommentHaveRecomment(),
                        getCommentsListResMainComment.getMainCommentId(),
                        getCommentsListResMainComment.getMainCommentUserId(),
                        getCommentsListResMainComment.getMainCommentProfileImageUrl(),
                        getCommentsListResMainComment.getMainCommentNickname(),
                        getCommentsListResMainComment.getMainCommentContent(),
                        getCommentsListResMainComment.getMainCommentUploadedAt(),
                        getCommentsListResMainComment.getMainCommentLikeCount(),
                        getCommentsListResMainComment.getMainCommentIsLiked(),
                        getCommentsListResMainComment.getCursor(),
                        getCommentsListResRecommentList
                );
                getCommentsListResMainCommentWithRecommentList.add(getCommentsListResMainCommentWithRecomment);
            }

            return new GetCommentsListRes(
                    commentDao.countCommentByFeedId(feedId),
                    getCommentsListResMainCommentWithRecommentList
            );
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
