
package shop.ozip.dev.src.scrapbook;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.comment.CommentDao;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.src.scrapbook.model.*;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ScrapbookService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedDao feedDao;
    private final ScrapbookDao scrapbookDao;
    private final ScrapbookProvider scrapbookProvider;
    private final JwtService jwtService;
    private final String fileName;
    private final CommentDao commentDao;


    @Autowired
    public ScrapbookService(ScrapbookDao scrapbookDao, ScrapbookProvider scrapbookProvider, JwtService jwtService, FeedDao feedDao, CommentDao commentDao) {
        this.scrapbookDao = scrapbookDao;
        this.scrapbookProvider = scrapbookProvider;
        this.jwtService = jwtService;
        this.commentDao = commentDao;
        this.fileName = "FeedService";
        this.feedDao = feedDao;

    }

    // 북마크하기 (특히 메인 북마크에)
    public PostBookmarksFeedRes createBookmarkFeed(PostBookmarksFeedReq postBookmarksFeedReq) throws BaseException {
        Long userId = jwtService.getUserId();
        // 북마크할 피드 존재 여부
        if (!feedDao.checkFeedExistById(postBookmarksFeedReq.getFeedId())) {
            throw new BaseException(FEED_NOT_EXIST);
        }
        Scrapbook mainScrapbook = scrapbookDao.getMainScrapbookByUserId(userId);
        // 이미 스크랩된 관계인지
        if (scrapbookDao.checkBookmarkExist(userId, postBookmarksFeedReq.getFeedId())){
            throw new BaseException(ALREADY_SCRAPPED);
        }
        try{
            return scrapbookDao.createBookmarkFeed(mainScrapbook.getId(), postBookmarksFeedReq.getFeedId());
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
    
    // 스크랩북 만들기
    public PostBookmarksRes createScrapbook(PostBookmarksReq postBookmarksReq) throws BaseException{
        Long userId = jwtService.getUserId();
        try{
            return scrapbookDao.createScrapbook(userId, postBookmarksReq.getName(), postBookmarksReq.getDescription());
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
