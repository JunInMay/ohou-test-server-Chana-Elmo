
package shop.ozip.dev.src.feed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.feed.model.*;
import shop.ozip.dev.src.feed.model.GetFeedsMediasNineRes;
import shop.ozip.dev.utils.JwtService;

import java.util.List;

import static shop.ozip.dev.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/feeds")
public class FeedController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FeedProvider feedProvider;
    @Autowired
    private final FeedService feedService;
    @Autowired
    private final JwtService jwtService;




    public FeedController(FeedProvider feedProvider, FeedService feedService, JwtService jwtService){
        this.feedProvider = feedProvider;
        this.feedService = feedService;
        this.jwtService = jwtService;
    }

    /*
    미디어 피드 상세 내용 조회 API
    (GET) 127.0.0.1:9000/app/feeds/media-feeds/{feedId}
    */
    @ResponseBody
    @GetMapping("/media-feeds/{feedId}")
    public BaseResponse<GetFeedsMediaFeedRes> getFeedsMediaFeeds(@PathVariable("feedId") Long feedId) {
        if (feedId == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        try{
            GetFeedsMediaFeedRes getFeedsMediaFeedRes = feedProvider.retrieveMediaFeed(feedId);
            return new BaseResponse<>(getFeedsMediaFeedRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    미디어 피드(사진 묶음, 동영상) 조회(정렬 및 필터) API
    (GET) 127.0.0.1:9000/app/feeds/media-feeds/list/?sort=&video=&home-type=&style=/:lastValue
    */
    @ResponseBody
    @GetMapping(value = {"/media-feeds/list", "/media-feeds/list/{cursor}"})
    public BaseResponse<List<GetFeedsMediaFeedsListRes>> getFeedsMediaFeedsList(@PathVariable(value = "cursor", required = false) Long cursor, @RequestParam(value="sort", required=false, defaultValue="1") Integer sort, @RequestParam(value="video", required=false, defaultValue="0") Integer video, @RequestParam(value="home-type", required=false, defaultValue="0") Integer homeType, @RequestParam(value="style", required=false, defaultValue="0") Integer style) {
        // 정렬 적용시 주의
        if (cursor == null){
            cursor = Long.MAX_VALUE;
        }
        try{
            List<GetFeedsMediaFeedsListRes> getFeedsMediaFeedsListRes = feedProvider.retrieveMediaFeedList(cursor, sort, video, homeType, style);
            return new BaseResponse<>(getFeedsMediaFeedsListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    홈-인기 인기 사진 묶음 TOP 10 API
    (GET) 127.0.0.1:9000/app/feeds/hots/photo
    */
    @ResponseBody
    @GetMapping("/hots/photo")
    public BaseResponse<List<GetFeedsHotsPhotoRes>> getFeedsHotsPhoto() {
        try{
            List<GetFeedsHotsPhotoRes> getFeedsHotsPhotoRes = feedProvider.retrieveHotsPhotoSection();
            return new BaseResponse<>(getFeedsHotsPhotoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    홈-인기 인기 동영상 피드 TOP 10 API
    (GET) 127.0.0.1:9000/app/feeds/hots/photo
    */
    @ResponseBody
    @GetMapping("/hots/video")
    public BaseResponse<List<GetFeedsHotsVideoRes>> getFeedsHotsVideo() {
        try{
            List<GetFeedsHotsVideoRes> getFeedsHotsVideoRes = feedProvider.retrieveHotsVideoSection();
            return new BaseResponse<>(getFeedsHotsVideoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /*
    홈-인기 키워드별 핫한 사진 피드 조회 API
    (GET) 127.0.0.1:9000/app/feeds/hots/keyword
    */
    @ResponseBody
    @GetMapping("/hots/keyword")
    public BaseResponse<GetFeedsHotsKeywordRes> getFeedsHotsKeyword() {
        try{
            GetFeedsHotsKeywordRes getFeedsHotsKeywordRes = feedProvider.retrieveHotsKeywordSection();
            return new BaseResponse<>(getFeedsHotsKeywordRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }




    /*
    홈-인기 : 섹션 1번 조회 API (집들이 4개)
    (GET) 127.0.0.1:9000/app/feeds/hots/1
    */
    @ResponseBody
    @GetMapping("/hots/1")
    public BaseResponse<List<GetFeedsHotsRes>> getFeedsHotsOne() {
        try{
            List<GetFeedsHotsRes> getFeedsHotsRes = feedProvider.retrieveHotsFeedSection(1);
            return new BaseResponse<>(getFeedsHotsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    홈-인기 : 섹션 2번 조회 API (노하우 4개)
    (GET) 127.0.0.1:9000/app/feeds/hots/2
    */
    @ResponseBody
    @GetMapping("/hots/2")
    public BaseResponse<List<GetFeedsHotsRes>> getFeedsHotsTwo() {
        try{
            List<GetFeedsHotsRes> getFeedsHotsRes = feedProvider.retrieveHotsFeedSection(2);
            return new BaseResponse<>(getFeedsHotsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    홈-인기 : 섹션 3번 조회 API (집들이 2개 + 노하우 2개)
    (GET) 127.0.0.1:9000/app/feeds/hots/3
    */
    @ResponseBody
    @GetMapping("/hots/3")
    public BaseResponse<List<GetFeedsHotsRes>> getFeedsHotsThree() {
        try{
            List<GetFeedsHotsRes> getFeedsHotsRes = feedProvider.retrieveHotsFeedSection(3);
            return new BaseResponse<>(getFeedsHotsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    9개 공간별 유저가 가장 최근에 업로드한 미디어(사진,동영상) 조회 API
    (GET) 127.0.0.1:9000/app/users/:userId/medias/nine
    */
    @ResponseBody
    @GetMapping("/medias/nine/{userId}")
    public BaseResponse<List<GetFeedsMediasNineRes>> getFeedsMediasNine(@PathVariable("userId") Long userId) {
        try{
            List<GetFeedsMediasNineRes> getFeedsMediasNineResList = feedProvider.retrieveFeedsMediasNine(userId);
            return new BaseResponse<>(getFeedsMediasNineResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    특정 유저가 올린 미디어 개수 조회
    (GET) 127.0.0.1:9000/app/users/:userId/medias/nine
    */
    @ResponseBody
    @GetMapping("/medias/count/{userId}")
    public BaseResponse<Integer> getFeedsMediasCount(@PathVariable("userId") Long userId) {
        try{
            Integer getFeedsMediasCount = feedProvider.retrieveFeedsMediasCount(userId);
            return new BaseResponse<>(getFeedsMediasCount);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
