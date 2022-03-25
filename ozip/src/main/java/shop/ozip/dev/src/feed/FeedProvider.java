
package shop.ozip.dev.src.feed;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.feed.model.*;
import shop.ozip.dev.src.keyword.KeywordDao;
import shop.ozip.dev.utils.JwtService;

import java.util.ArrayList;
import java.util.List;

import static shop.ozip.dev.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class FeedProvider {

    private final FeedDao feedDao;
    private final JwtService jwtService;
    private final String fileName;
    private final KeywordDao keywordDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FeedProvider(FeedDao feedDao, JwtService jwtService, KeywordDao keywordDao) {
        this.feedDao = feedDao;
        this.jwtService = jwtService;
        this.fileName = "FeedProvider";
        this.keywordDao = keywordDao;
    }
    
    // 미디어 피드 상세 내용 조회하기
    public GetFeedsMediaFeedRes retrieveMediaFeed(Long feedId) throws BaseException{
        String methodName = "retrieveMediaFeed";
        if (!feedDao.checkFeedExistById(feedId)) {
            throw new BaseException(FEED_NOT_EXIST);
        }
        Feed feed = feedDao.getFeedById(feedId);
        if (feed.getIsMediaFeed() != 1) {
            throw new BaseException(IS_NOT_MEDIA_FEED);
        }
        try{
            List<Media> mediaList = feedDao.getMediaListByFeedId(feedId);
            List<MediaWithKeyword> mediaWithKeywordList = new ArrayList();
            for (int i = 0; i < mediaList.size(); i++) {
                Media media = mediaList.get(i);
                MediaWithKeyword mediaWithKeyword = new MediaWithKeyword(media, keywordDao.getKeywordListByFeedId(media.getFeedId()));
                mediaWithKeywordList.add(mediaWithKeyword);
            }

            MediaFeed mediaFeed = feedDao.getMediaFeedByFeedId(feedId);
            return new GetFeedsMediaFeedRes(mediaFeed.getFeedId(), mediaWithKeywordList);
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 미디어 피드 리스트 조회하기
    public List<GetFeedsMediaFeedsListRes> retrieveMediaFeedList(Long lastValue) throws BaseException {
        String methodName = "retrieveMediaFeedList";
        Long userId = jwtService.getUserId();
        try{
            List<GetFeedsMediaFeedsListRes> getFeedsMediaFeedsListRes = feedDao.retrieveMediaFeedList(lastValue, userId);
            return getFeedsMediaFeedsListRes;
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 인기 섹션 1번 조회
    public List<GetFeedsHotsRes> retrieveHotsFeedSection(Integer i) throws BaseException{
        String methodName = "retrieveHotsFeedSectionOne";
        Long userId = jwtService.getUserId();
        try{
            List<GetFeedsHotsRes> getFeedsHotsResList = feedDao.retrieveHotsFeedSection(userId, i);
            return getFeedsHotsResList;
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
