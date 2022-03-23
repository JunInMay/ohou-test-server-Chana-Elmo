
package shop.ozip.dev.src.feed;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.feed.model.*;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class FeedProvider {

    private final FeedDao feedDao;
    private final JwtService jwtService;
    private final String fileName;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FeedProvider(FeedDao feedDao, JwtService jwtService) {
        this.feedDao = feedDao;
        this.jwtService = jwtService;
        this.fileName = "FeedProvider";
    }
    
    // 미디어 피드 상세 내용 조회하기
    public GetFeedMediaFeedRes retrieveMediaFeed(Long feedId) throws BaseException{
        String methodName = "retrieveMediaFeed";
        if (!feedDao.checkFeedExistById(feedId)) {
            throw new BaseException(FEED_NOT_EXIST);
        }
        Feed feed = feedDao.getFeedById(feedId);
        if (feed.getIsMediaFeed() != 1) {
            throw new BaseException(IS_NOT_MEDIA_FEED);
        }
        try{
            return feedDao.retrieveMediaFeed(feedId);
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
