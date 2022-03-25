
package shop.ozip.dev.src.feed;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponseStatus;
import shop.ozip.dev.src.feed.model.*;
import shop.ozip.dev.src.keyword.KeywordDao;
import shop.ozip.dev.src.keyword.model.Keyword;
import shop.ozip.dev.src.feed.model.GetFeedsMediasNineRes;
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

    // 인기 섹션 번호별 조회
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

    // 키워드별 핫한 사진 묶음 조회(조회수 많은)
    public GetFeedsHotsKeywordRes retrieveHotsKeywordSection() throws BaseException{
        String methodName = "retrieveHotsKeywordSection";
        Long userId = jwtService.getUserId();
        try{
            List<GetFeedsHotsKeywordResMedia> getFeedsHotsKeywordResMediaList = feedDao.retrieveHotsKeywordMediaList(userId);
            Keyword keyword = keywordDao.getMostRefferedKeywordInPhoto();
            GetFeedsHotsKeywordRes getFeedsHotsKeywordRes = new GetFeedsHotsKeywordRes(keyword.getName(), getFeedsHotsKeywordResMediaList);
            return getFeedsHotsKeywordRes;
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }

    }
    
    // 인기있는 사진 10장
    public List<GetFeedsHotsPhotoRes> retrieveHotsPhotoSection() throws BaseException{
        String methodName = "retrieveHotsPhotoSection";
        Long userId = jwtService.getUserId();
        try{
            List<GetFeedsHotsPhotoRes> getFeedsHotsPhotoRes = feedDao.retrieveHotsPhotoSection(userId);
            return getFeedsHotsPhotoRes;
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
    
    // 유저별 미디어 9개 조회
    @Transactional
    public List<GetFeedsMediasNineRes> retrieveFeedsMediasNine(Long userId) throws BaseException{
        String methodName = "retrieveFeedsMediasNine";
        try {
            List<GetFeedsMediasNineRes> getFeedsMediasNineResList = new ArrayList<>();
            int[] types = {0, 1, 2, 3, 7, 8, 4, 6, 11};
            for (int i = 0; i < 9; i++) {
                getFeedsMediasNineResList.add(feedDao.retrieveFeedsMediasForNine(userId, types[i]));
            }

            return getFeedsMediasNineResList;
        } catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
    
    // 특정 유저가 업로드한 미디어 개수
    public Integer retrieveFeedsMediasCount(Long userId) throws BaseException{
        String methodName = "retrieveFeedsMediasCount";
        try {
            return feedDao.retrieveFeedsMediasCount(userId);
        } catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
