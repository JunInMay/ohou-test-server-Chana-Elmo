
package shop.ozip.dev.src.feed;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.feed.model.*;
import shop.ozip.dev.src.keyword.KeywordDao;
import shop.ozip.dev.utils.JwtService;

import java.util.List;

import static shop.ozip.dev.config.BaseResponseStatus.*;
import static shop.ozip.dev.config.BaseResponseStatus.DATABASE_ERROR;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FeedService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final shop.ozip.dev.src.feed.FeedDao feedDao;
    private final shop.ozip.dev.src.feed.FeedProvider feedProvider;
    private final JwtService jwtService;
    private final String fileName;
    private final KeywordDao keywordDao;


    @Autowired
    public FeedService(FeedDao feedDao, FeedProvider feedProvider, JwtService jwtService, KeywordDao keywordDao) {
        this.feedDao = feedDao;
        this.feedProvider = feedProvider;
        this.jwtService = jwtService;
        this.keywordDao = keywordDao;
        this.fileName = "FeedService";

    }

    // 미디어피드 만들기
    @Transactional
    public PostFeedsMediaFeedsRes createMediaFeed(PostFeedsMediaFeedsReq postFeedsMediaFeedsReq) throws BaseException{
        Long userId = jwtService.getUserId();
        try{
            return feedDao.createMediaFeed(userId, postFeedsMediaFeedsReq);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 사진 업로드하고 미디어피드와 결합하기 + 키워드와 결합하기
    @Transactional
    public PostFeedsMediasPhotoRes createMediaPhoto(PostFeedsMediasPhotoReq postFeedsMediasPhotoReq) throws BaseException {
        Long userId = jwtService.getUserId();
        if (!feedDao.checkFeedExistById(postFeedsMediasPhotoReq.getOwnerFeedId())){
            throw new BaseException(FEED_NOT_EXIST);
        }
        Feed feed = feedDao.getFeedById(postFeedsMediasPhotoReq.getOwnerFeedId());
        if (feed.getIsMediaFeed() != 1){
            throw new BaseException(IS_NOT_MEDIA_FEED);
        }
        if (!feedDao.checkMediaFeedExistByFeedId(postFeedsMediasPhotoReq.getOwnerFeedId())){
            throw new BaseException(MEDIA_FEED_NOT_EXIST);
        }
        MediaFeed mediaFeed = feedDao.getMediaFeedByFeedId(postFeedsMediasPhotoReq.getOwnerFeedId());
        if (mediaFeed.getIsPhoto() != 1){
            throw new BaseException(MEDIA_FEED_NOT_PHOTO);
        }
        if (feed.getUserId() != userId){
            throw new BaseException(NOT_FEED_OWNER);
        }
        try{
            List<String> keywords = postFeedsMediasPhotoReq.getKeywords();
            PostFeedsMediasPhotoRes postFeedsMediasPhotoRes = feedDao.createMediaPhoto(userId, postFeedsMediasPhotoReq);
            for (int i=0; i < keywords.size(); i++){
                Long keywordId;
                if (!keywordDao.checkKeywordExistByName(keywords.get(i))){
                    keywordId = keywordDao.createKeyword(keywords.get(i));
                }  else {
                    keywordId = keywordDao.getKeywordIdByName(keywords.get(i));
                }
                keywordDao.createFeedHavingKeyword(postFeedsMediasPhotoRes.getPhotoFeedId(), keywordId);
            }

            return postFeedsMediasPhotoRes;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 동영상 올리고 미디어피드와 결합 + 키워드와 결합
    public PostFeedsMediasVideoRes createMediaVideo(PostFeedsMediasVideoReq postFeedsMediasVideoReq) throws BaseException {

        Long userId = jwtService.getUserId();
        if (!feedDao.checkFeedExistById(postFeedsMediasVideoReq.getOwnerFeedId())){
            throw new BaseException(FEED_NOT_EXIST);
        }
        Feed feed = feedDao.getFeedById(postFeedsMediasVideoReq.getOwnerFeedId());
        if (feed.getIsMediaFeed() != 1){
            throw new BaseException(IS_NOT_MEDIA_FEED);
        }
        if (!feedDao.checkMediaFeedExistByFeedId(postFeedsMediasVideoReq.getOwnerFeedId())){
            throw new BaseException(MEDIA_FEED_NOT_EXIST);
        }
        MediaFeed mediaFeed = feedDao.getMediaFeedByFeedId(postFeedsMediasVideoReq.getOwnerFeedId());
        if (mediaFeed.getIsPhoto() != 1){
            throw new BaseException(MEDIA_FEED_NOT_PHOTO);
        }
        if (feed.getUserId() != userId){
            throw new BaseException(NOT_FEED_OWNER);
        }
        try{
            List<String> keywords = postFeedsMediasVideoReq.getKeywords();
            PostFeedsMediasVideoRes postFeedsMediasVideoRes = feedDao.createMediaVideo(userId, postFeedsMediasVideoReq);
            for (int i=0; i < keywords.size(); i++){
                Long keywordId;
                if (!keywordDao.checkKeywordExistByName(keywords.get(i))){
                    keywordId = keywordDao.createKeyword(keywords.get(i));
                }  else {
                    keywordId = keywordDao.getKeywordIdByName(keywords.get(i));
                }
                keywordDao.createFeedHavingKeyword(postFeedsMediasVideoRes.getVideoFeedId(), keywordId);
            }

            return postFeedsMediasVideoRes;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
