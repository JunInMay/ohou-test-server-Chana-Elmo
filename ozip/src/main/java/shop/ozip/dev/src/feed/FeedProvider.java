
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

    private final shop.ozip.dev.src.feed.FeedDao feedDao;
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


    // 미디어 피드 상단에 노출되는 메타 데이터 조회하기
    public GetFeedsMediaFeedsMetaRes retrieveMediaFeedMeta(Long feedId) throws BaseException{
        String methodName = "retrieveMediaFeedMeta";
        Long userId = jwtService.getUserId();
        if (!feedDao.checkFeedExistById(feedId)) {
            throw new BaseException(FEED_NOT_EXIST);
        }
        Feed feed = feedDao.getFeedById(feedId);
        if (feed.getIsMediaFeed() != 1) {
            throw new BaseException(IS_NOT_MEDIA_FEED);
        }
        try{
            GetFeedsMediaFeedsMetaRes getFeedsMediaFeedsMetaRes = feedDao.retrieveMediaFeedMeta(feedId);
            return getFeedsMediaFeedsMetaRes;
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 미디어 피드 하단에 노출되는 유저 + 좋아요, 스크랩, 댓글, 조회 정보
    public GetFeedsMediaFeedsBottomRes retrieveMediaFeedBottom(Long feedId) throws BaseException{
        String methodName = "retrieveMediaFeedBottom";
        Long userId = jwtService.getUserId();
        if (!feedDao.checkFeedExistById(feedId)) {
            throw new BaseException(FEED_NOT_EXIST);
        }
        Feed feed = feedDao.getFeedById(feedId);
        if (feed.getIsMediaFeed() != 1) {
            throw new BaseException(IS_NOT_MEDIA_FEED);
        }
        try{
            GetFeedsMediaFeedsBottomRes getFeedsMediaFeedsBottomRes = feedDao.retrieveMediaFeedBottom(userId, feedId);
            return getFeedsMediaFeedsBottomRes;
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 미디어 피드 하단에 노출되는, 미디어 피드를 유저가 올린 다른 미디어 피드 조회하기
    public List<GetFeedsMediaFeedsOthersRes> retrieveMediaFeedOthers(Long feedId) throws BaseException{
        String methodName = "retrieveMediaFeedOthers";
        if (!feedDao.checkFeedExistById(feedId)) {
            throw new BaseException(FEED_NOT_EXIST);
        }
        Feed feed = feedDao.getFeedById(feedId);
        if (feed.getIsMediaFeed() != 1) {
            throw new BaseException(IS_NOT_MEDIA_FEED);
        }
        try{
            List<GetFeedsMediaFeedsOthersRes> getFeedsMediaFeedsOthersRes = feedDao.retrieveMediaFeedOthers(feedId);
            return getFeedsMediaFeedsOthersRes;
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
    
    // 미디어 피드 상세 내용 조회하기
    public GetFeedsMediaFeedsRes retrieveMediaFeed(Long feedId) throws BaseException{
        String methodName = "retrieveMediaFeed";
        Long userId = jwtService.getUserId();
        if (!feedDao.checkFeedExistById(feedId)) {
            throw new BaseException(FEED_NOT_EXIST);
        }
        Feed feed = feedDao.getFeedById(feedId);
        if (feed.getIsMediaFeed() != 1) {
            throw new BaseException(IS_NOT_MEDIA_FEED);
        }
        try{
            List<GetFeedsMediaFeedsResMedia> getFeedsMediaFeedsResMediaList = feedDao.retrieveMediaFeedMedias(userId, feedId);
            List<GetFeedsMediaFeedsResBase> getFeedsMediaFeedsResBaseList = new ArrayList();
            for (int i = 0; i < getFeedsMediaFeedsResMediaList.size(); i++) {
                GetFeedsMediaFeedsResMedia getFeedsMediaFeedsResMedia = getFeedsMediaFeedsResMediaList.get(i);
                GetFeedsMediaFeedsResBase getFeedsMediaFeedsResBase = new GetFeedsMediaFeedsResBase(getFeedsMediaFeedsResMedia, keywordDao.getKeywordListByFeedId(getFeedsMediaFeedsResMedia.getFeedId()));
                getFeedsMediaFeedsResBaseList.add(getFeedsMediaFeedsResBase);
            }

            MediaFeed mediaFeed = feedDao.getMediaFeedByFeedId(feedId);
            return new GetFeedsMediaFeedsRes(mediaFeed.getFeedId(), getFeedsMediaFeedsResBaseList);
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 미디어 피드 리스트 조회하기
    public List<GetFeedsMediaFeedsListRes> retrieveMediaFeedList(Long cursor, Integer sort, Integer video, Integer homeType, Integer style) throws BaseException {
        String methodName = "retrieveMediaFeedList";
        Long userId = jwtService.getUserId();
        try{
            List<GetFeedsMediaFeedsListRes> getFeedsMediaFeedsListRes = feedDao.retrieveMediaFeedList(cursor, userId, sort, video, homeType, style);
            return getFeedsMediaFeedsListRes;
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //팔로우탭 - 유저가 팔로우한 키워드 + 유저들의 피드 리스트 조회 API
    public List<GetFeedsFollowsListRes> retrieveFeedsFollowsList(Long cursor) throws BaseException{
        String methodName = "retrieveFeedsFollowsList";
        Long userId = jwtService.getUserId();
        try{
            List<GetFeedsFollowsListRes> getFeedsFollowsListRes = feedDao.retrieveFeedsFollowsList(userId, cursor);
            return getFeedsFollowsListRes;
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 집들이 리스트 조회
    public GetFeedsHomewarmingFeedsListRes retrieveHomewarmingFeedList(Long cursor, Integer sort, Integer homeType, Integer acreageStart, Integer acreageEnd, Integer budgetStart, Integer budgetEnd, Integer family, Integer style, Integer allColor, Integer wallColor, Integer floorColor, Integer detail, Integer category, Integer subject, int professional) throws BaseException{
        String methodName = "retrieveMediaFeedList";
        Long userId = jwtService.getUserId();
        try{
            GetFeedsHomewarmingFeedsListRes getFeedsHomewarmingFeedsListRes = feedDao.retrieveHomewarmingFeedList(userId, cursor, sort, homeType, acreageStart, acreageEnd, budgetStart, budgetEnd, family, style, allColor, wallColor, floorColor, detail, category, subject, professional);
            return getFeedsHomewarmingFeedsListRes;
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetFeedsKnowhowFeedsListRes retrieveKnowhowFeedList(Long cursor, Integer theme, Integer sort) throws BaseException {
        String methodName = "retrieveMediaFeedList";
        Long userId = jwtService.getUserId();
        try{
            GetFeedsKnowhowFeedsListRes getFeedsKnowhowFeedsListRes = feedDao.retrieveKnowhowFeedList(userId, cursor, theme, sort);
            return getFeedsKnowhowFeedsListRes;
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

    // 인기있는 동영상 10개
    public List<GetFeedsHotsVideoRes> retrieveHotsVideoSection() throws BaseException{
        String methodName = "retrieveHotsVideoSection";
        Long userId = jwtService.getUserId();
        try{
            List<GetFeedsHotsVideoRes> getFeedsHotsVideoRes = feedDao.retrieveHotsVideoSection(userId);
            return getFeedsHotsVideoRes;
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

    // 해당 스크랩북에 스크랩된 모든 피드 조회
    public List<GetFeedsScrappedAll> retrieveScrappedAll(Long scrapbookId, Long cursor) throws BaseException{
        String methodName = "retrieveScrappedAll";
        try {
            return feedDao.retrieveScrappedAll(scrapbookId, cursor);
        } catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 해당 스크랩북에 스크랩된 미디어 관룐 피드들 조회
    public List<GetFeedsScrappedMediaFeeds> retrieveScrappedMediaFeeds(Long scrapbookId, Long cursor) throws BaseException{
        String methodName = "retrieveScrappedMediaFeeds";
        try {
            return feedDao.retrieveScrappedMediaFeeds(scrapbookId, cursor);
        } catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<GetFeedsScrappedHomewarmingsFeed> retrieveScrappedHomewarmings(Long scrapbookId, Long cursor) throws BaseException{
        String methodName = "retrieveScrappedHomewarming";
        try {
            return feedDao.retrieveScrappedHomewarmings(scrapbookId, cursor);
        } catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<GetFeedsScrappedKnowhowsFeed> retrieveScrappedKnowhows(Long scrapbookId, Long cursor) throws BaseException{
        String methodName = "retrieveScrappedKnowhows";
        try {
            return feedDao.retrieveScrappedKnowhows(scrapbookId, cursor);
        } catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }

    }

    // 특정 유저의 스크랩북의 전체탭
    public List<GetFeedsScrappedMainAll> retrieveScrappedMainAll(Long userId, Long cursor) throws BaseException {
        String methodName = "retrieveScrappedMainAll";
        try {
            return feedDao.retrieveScrappedMainAll(userId, cursor);
        } catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    public List<GetFeedsScrappedMainMediaFeeds> retrieveScrappedMainMediaFeeds(Long userId, Long cursor) throws BaseException {
        String methodName = "retrieveScrappedMainMediaFeeds";
        try {
            return feedDao.retrieveScrappedMainMediaFeeds(userId, cursor);
        } catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }
}
