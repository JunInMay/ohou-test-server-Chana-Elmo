
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
    private final shop.ozip.dev.src.feed.FeedProvider feedProvider;
    @Autowired
    private final shop.ozip.dev.src.feed.FeedService feedService;
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
    public BaseResponse<GetFeedsMediaFeedsRes> getFeedsMediaFeeds(@PathVariable("feedId") Long feedId) {
        if (feedId == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        try{
            GetFeedsMediaFeedsRes getFeedsMediaFeedsRes = feedProvider.retrieveMediaFeed(feedId);
            return new BaseResponse<>(getFeedsMediaFeedsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    미디어피드 상단 메타 데이터 조회 API
    (GET) 127.0.0.1:9000/app/feeds/media-feeds/{feedId}/meta
    */
    @ResponseBody
    @GetMapping("/media-feeds/{feedId}/meta")
    public BaseResponse<GetFeedsMediaFeedsMetaRes> getFeedsMediaFeedsMeta(@PathVariable("feedId") Long feedId) {
        if (feedId == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        try{
            GetFeedsMediaFeedsMetaRes getFeedsMediaFeedsMetaRes = feedProvider.retrieveMediaFeedMeta(feedId);
            return new BaseResponse<>(getFeedsMediaFeedsMetaRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    특정 피드의 하단 정보 조회 API
    (GET) 127.0.0.1:9000/app/feeds/{feedId}/bottom
    */
    @ResponseBody
    @GetMapping("/{feedId}/bottom")
    public BaseResponse<GetFeedsBottomRes> getFeedsMediaFeedsBottom(@PathVariable("feedId") Long feedId) {
        if (feedId == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        try{
            GetFeedsBottomRes getFeedsBottomRes = feedProvider.retrieveFeedBottom(feedId);
            return new BaseResponse<>(getFeedsBottomRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    해당 미디어 피드 작성자가 올린 다른 미디어 피드 조회 API
    (GET) 127.0.0.1:9000/app/feeds/media-feeds/:feedId/others
    */
    @ResponseBody
    @GetMapping("/media-feeds/{feedId}/others")
    public BaseResponse<List<GetFeedsMediaFeedsOthersRes>> getFeedsMediaFeedsOthersRes(@PathVariable("feedId") Long feedId) {
        if (feedId == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        try{
            List<GetFeedsMediaFeedsOthersRes> getFeedsMediaFeedsOthersRes = feedProvider.retrieveMediaFeedOthers(feedId);
            return new BaseResponse<>(getFeedsMediaFeedsOthersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    미디어 피드(사진 묶음, 동영상) 조회(정렬 및 필터) API
    (GET) 127.0.0.1:9000/app/feeds/media-feeds/list/:lastValue?sort=&video=&home-type=&style=/
    */
    @ResponseBody
    @GetMapping(value = {"/media-feeds"})
    public BaseResponse<List<GetFeedsMediaFeedsListRes>> getFeedsMediaFeedsList(@RequestParam(value = "cursor", required = false) Long cursor, @RequestParam(value="sort", required=false, defaultValue="1") Integer sort, @RequestParam(value="video", required=false, defaultValue="0") Integer video, @RequestParam(value="home-type", required=false, defaultValue="0") Integer homeType, @RequestParam(value="style", required=false, defaultValue="0") Integer style) {
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
    집들이 피드 조회(정렬 및 필터) API
    (GET) 127.0.0.1:9000/app/feeds/homewarmings/list/:cursor?sort=&home-type=&acreage-start=&acreage-end=&budget-start=&budget-end&family=&style=&all-color=&wall-color=&floor-color=&detail=&category=&subject=
    */
    @ResponseBody
    @GetMapping(value = {"/homewarmings/"})
    public BaseResponse<GetFeedsHomewarmingFeedsListRes> getFeedsHomewarmingsList(@RequestParam(value = "cursor", required = false) Long cursor, @RequestParam(value="sort", required=false, defaultValue="1") Integer sort, @RequestParam(value="home-type", required=false, defaultValue="0") Integer homeType, @RequestParam(value="acreage-start", required=false, defaultValue="0") Integer acreageStart, @RequestParam(value="acreage-end", required=false, defaultValue="0") Integer acreageEnd, @RequestParam(value="budget-start", required=false, defaultValue="0") Integer budgetStart, @RequestParam(value="budget-end", required=false, defaultValue="0") Integer budgetEnd, @RequestParam(value="family", required=false, defaultValue="0") Integer family, @RequestParam(value="style", required=false, defaultValue="0") Integer style, @RequestParam(value="all-color", required=false, defaultValue="0") Integer allColor, @RequestParam(value="wall-color", required=false, defaultValue="0") Integer wallColor, @RequestParam(value="floor-color", required=false, defaultValue="0") Integer floorColor, @RequestParam(value="detail", required=false, defaultValue="0") Integer detail, @RequestParam(value="category", required=false, defaultValue="0") Integer category, @RequestParam(value="subject", required=false, defaultValue="0") Integer subject) {
        // 정렬 적용시 주의
        if (cursor == null){
            cursor = Long.MAX_VALUE;
        }
        try{
            int professional = 0;
            GetFeedsHomewarmingFeedsListRes getFeedsHomewarmingFeedsListRes = feedProvider.retrieveHomewarmingFeedList(cursor, sort, homeType, acreageStart, acreageEnd, budgetStart, budgetEnd, family, style, allColor, wallColor, floorColor, detail, category, subject, professional);
            return new BaseResponse<>(getFeedsHomewarmingFeedsListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    전문가 집들이 피드 조회(정렬 및 필터) API
    (GET) 127.0.0.1:9000/app/feeds/homewarmings/pro/list?sort=&home-type=&acreage-start=&acreage-end=&budget=&family=&style=&all-color=&wall-color=&floor-color=&detail=&category=&subject=
    */
    @ResponseBody
    @GetMapping(value = {"/homewarmings/pro"})
    public BaseResponse<GetFeedsHomewarmingFeedsListRes> getFeedsHomewarmingsProList(@RequestParam(value = "cursor", required = false) Long cursor, @RequestParam(value="sort", required=false, defaultValue="1") Integer sort, @RequestParam(value="home-type", required=false, defaultValue="0") Integer homeType, @RequestParam(value="acreage-start", required=false, defaultValue="0") Integer acreageStart, @RequestParam(value="acreage-end", required=false, defaultValue="0") Integer acreageEnd, @RequestParam(value="budget-start", required=false, defaultValue="0") Integer budgetStart, @RequestParam(value="budget-end", required=false, defaultValue="0") Integer budgetEnd, @RequestParam(value="family", required=false, defaultValue="0") Integer family, @RequestParam(value="style", required=false, defaultValue="0") Integer style, @RequestParam(value="all-color", required=false, defaultValue="0") Integer allColor, @RequestParam(value="wall-color", required=false, defaultValue="0") Integer wallColor, @RequestParam(value="floor-color", required=false, defaultValue="0") Integer floorColor, @RequestParam(value="detail", required=false, defaultValue="0") Integer detail, @RequestParam(value="category", required=false, defaultValue="0") Integer category, @RequestParam(value="subject", required=false, defaultValue="0") Integer subject) {
        // 정렬 적용시 주의
        if (cursor == null){
            cursor = Long.MAX_VALUE;
        }
        try{
            int professional = 1;
            GetFeedsHomewarmingFeedsListRes getFeedsHomewarmingFeedsListRes = feedProvider.retrieveHomewarmingFeedList(cursor, sort, homeType, acreageStart, acreageEnd, budgetStart, budgetEnd, family, style, allColor, wallColor, floorColor, detail, category, subject, professional);
            return new BaseResponse<>(getFeedsHomewarmingFeedsListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    노하우 조회(정렬 및 필터) API
    (GET) 127.0.0.1:9000/app/feeds/knowhows/list/:cursor?sort=&theme=
    */
    @ResponseBody
    @GetMapping(value = {"/knowhows"})
    public BaseResponse<GetFeedsKnowhowFeedsListRes> getFeedsKnowhowFeedsList(@RequestParam(value = "cursor", required = false) Long cursor, @RequestParam(value="sort", required=false, defaultValue="3") Integer sort, @RequestParam(value="theme", required=false, defaultValue="0") Integer theme) {
        // 정렬 적용시 주의
        if (cursor == null){
            cursor = Long.MAX_VALUE;
        }
        try{
            GetFeedsKnowhowFeedsListRes getFeedsKnowhowFeedsListRes = feedProvider.retrieveKnowhowFeedList(cursor, theme, sort);
            return new BaseResponse<>(getFeedsKnowhowFeedsListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /*
    팔로우탭 - 유저가 팔로우한 키워드 + 유저들의 피드 리스트 조회 API
    (GET) 127.0.0.1:9000/app/feeds/follows
    */
    @ResponseBody
    @GetMapping(value = {"/follows"})
    public BaseResponse<List<GetFeedsFollowsListRes>> getFeedsFollowsList(@RequestParam(value = "cursor", required = false) Long cursor) {
        if (cursor == null){
            cursor = Long.MAX_VALUE;
        }
        try{
            List<GetFeedsFollowsListRes> getFeedsFollowsListRes = feedProvider.retrieveFeedsFollowsList(cursor);
            return new BaseResponse<>(getFeedsFollowsListRes);
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


    /*
    특정 스크랩북의 모든 피드 조회 API
    (GET) 127.0.0.1:9000/app/feeds/scrapped/all/:scrapbookId/:cursor
    */
    @ResponseBody
    @GetMapping(value = {"/scrapped/all/{scrapbookId}","/scrapped/all/{scrapbookId}/{cursor}"})
    public BaseResponse<List<GetFeedsScrappedAll>> getFeedsScrappedAll(@PathVariable(value="scrapbookId") Long scrapbookId, @PathVariable(value="cursor", required = false) Long cursor) {
        if (cursor == null){
            cursor = Long.MIN_VALUE;
        }
        try{
            List<GetFeedsScrappedAll> getFeedsScrappedAllList = feedProvider.retrieveScrappedAll(scrapbookId, cursor);
            return new BaseResponse<>(getFeedsScrappedAllList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    메인 스크랩북의 모든 피드 조회 API
    (GET) 127.0.0.1:9000/app/feeds/scrapped/main/all/:userId
    */
    @ResponseBody
    @GetMapping(value = {"/scrapped/main/all/{userId}","/scrapped/main/all/{userId}/{cursor}"})
    public BaseResponse<List<GetFeedsScrappedMainAll>> getFeedsScrappedMainAll(@PathVariable(value="userId") Long userId, @PathVariable(value="cursor", required = false) Long cursor) {
        if (cursor == null){
            cursor = Long.MIN_VALUE;
        }
        try{
            List<GetFeedsScrappedMainAll> getFeedsScrappedMainAllList = feedProvider.retrieveScrappedMainAll(userId, cursor);
            return new BaseResponse<>(getFeedsScrappedMainAllList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    특정 스크랩북의 모든 미디어 관련 피드 조회 API
    (GET) 127.0.0.1:9000/app/feeds/scrapped/media-feeds/:scrapbookId/:cursor
    */
    @ResponseBody
    @GetMapping(value = {"/scrapped/media-feeds/{scrapbookId}","/scrapped/media-feeds/{scrapbookId}/{cursor}"})
    public BaseResponse<List<GetFeedsScrappedMediaFeeds>> getFeedsScrappedMediaFeeds(@PathVariable(value="scrapbookId") Long scrapbookId, @PathVariable(value="cursor", required = false) Long cursor) {
        if (cursor == null){
            cursor = Long.MIN_VALUE;
        }
        try{
            List<GetFeedsScrappedMediaFeeds> getFeedsScrappedMediaFeedsList = feedProvider.retrieveScrappedMediaFeeds(scrapbookId, cursor);
            return new BaseResponse<>(getFeedsScrappedMediaFeedsList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    메인 스크랩북의 모든 미디어 관련 피드 조회 API
    (GET) 127.0.0.1:9000/app/feeds/scrapped/main/media-feeds/:userId/:cursor
    */
    @ResponseBody
    @GetMapping(value = {"/scrapped/main/media-feeds/{userId}","/scrapped/main/media-feeds/{userId}/{cursor}"})
    public BaseResponse<List<GetFeedsScrappedMainMediaFeeds>> getFeedsScrappedMainMediaFeeds(@PathVariable(value="userId") Long userId, @PathVariable(value="cursor", required = false) Long cursor) {
        if (cursor == null){
            cursor = Long.MIN_VALUE;
        }
        try{
            List<GetFeedsScrappedMainMediaFeeds> getFeedsScrappedMainMediaFeedsList = feedProvider.retrieveScrappedMainMediaFeeds(userId, cursor);
            return new BaseResponse<>(getFeedsScrappedMainMediaFeedsList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    특정 스크랩북의 집들이 피드 조회 API
    (GET) 127.0.0.1:9000/app/feeds/scrapped/homewarmings/:scrapbookId/:cursor
    */
    @ResponseBody
    @GetMapping(value = {"/scrapped/homewarmings/{scrapbookId}","/scrapped/homewarmings/{scrapbookId}/{cursor}"})
    public BaseResponse<List<GetFeedsScrappedHomewarmingsFeed>> getFeedsScrappedHomewarmings(@PathVariable(value="scrapbookId") Long scrapbookId, @PathVariable(value="cursor", required = false) Long cursor) {
        if (cursor == null){
            cursor = Long.MIN_VALUE;
        }
        try{
            List<GetFeedsScrappedHomewarmingsFeed> getFeedsScrappedHomewarmingsFeedList = feedProvider.retrieveScrappedHomewarmings(scrapbookId, cursor);
            return new BaseResponse<>(getFeedsScrappedHomewarmingsFeedList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    메인 스크랩북의 집들이 피드 조회 API
    (GET) 127.0.0.1:9000/app/feeds/scrapped/main/homewarmings/:userId/:cursor
    */
    @ResponseBody
    @GetMapping(value = {"/scrapped/main/homewarmings/{userId}","/scrapped/main/homewarmings/{userId}/{cursor}"})
    public BaseResponse<List<GetFeedsScrappedMainHomewarmingsFeed>> getFeedsScrappedMainHomewarmings(@PathVariable(value="userId") Long userId, @PathVariable(value="cursor", required = false) Long cursor) {
        if (cursor == null){
            cursor = Long.MIN_VALUE;
        }
        try{
            List<GetFeedsScrappedMainHomewarmingsFeed> getFeedsScrappedHomewarmingsFeedList = feedProvider.retrieveScrappedMainHomewarmings(userId, cursor);
            return new BaseResponse<>(getFeedsScrappedHomewarmingsFeedList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    
    /*
    특정 스크랩북의 노하우 피드 조회 API
    (GET) 127.0.0.1:9000/app/feeds/scrapped/knowhows/:scrapbookId/:cursor
    */
    @ResponseBody
    @GetMapping(value = {"/scrapped/knowhows/{scrapbookId}","/scrapped/knowhows/{scrapbookId}/{cursor}"})
    public BaseResponse<List<GetFeedsScrappedKnowhowsFeed>> getFeedsScrappedKnowhows(@PathVariable(value="scrapbookId") Long scrapbookId, @PathVariable(value="cursor", required = false) Long cursor) {
        if (cursor == null){
            cursor = Long.MIN_VALUE;
        }
        try{
            List<GetFeedsScrappedKnowhowsFeed> getFeedsScrappedKnowhowsFeedList = feedProvider.retrieveScrappedKnowhows(scrapbookId, cursor);
            return new BaseResponse<>(getFeedsScrappedKnowhowsFeedList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    메인 스크랩북의 노하우 피드 조회 API
    (GET) 127.0.0.1:9000/app/feeds/scrapped/main/knowhows/:userId/:cursor
    */
    @ResponseBody
    @GetMapping(value = {"/scrapped/main/knowhows/{userId}","/scrapped/main/knowhows/{userId}/{cursor}"})
    public BaseResponse<List<GetFeedsScrappedMainKnowhowsFeed>> getFeedsScrappedMainKnowhows(@PathVariable(value="userId") Long userId, @PathVariable(value="cursor", required = false) Long cursor) {
        if (cursor == null){
            cursor = Long.MIN_VALUE;
        }
        try{
            List<GetFeedsScrappedMainKnowhowsFeed> getFeedsScrappedMainKnowhowsFeedList = feedProvider.retrieveScrappedMainKnowhows(userId, cursor);
            return new BaseResponse<>(getFeedsScrappedMainKnowhowsFeedList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    미디어 상세 조회 API
    (GET) 127.0.0.1:9000/app/feeds/medias/:feedId
    */
    @ResponseBody
    @GetMapping("/medias/{feedId}")
    public BaseResponse<GetFeedsMediasRes> getFeedsMedias(@PathVariable("feedId") Long feedId) {
        if (feedId == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        try{
            GetFeedsMediasRes getFeedsMediasRes = feedProvider.retrieveMedia(feedId);
            return new BaseResponse<>(getFeedsMediasRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    유저들의 비슷한 공간 베스트 조회 API
    (GET) 127.0.0.1:9000/app/feeds/medias/similar-space/:feedId
    */
    @ResponseBody
    @GetMapping(value = {"/medias/similar-space/{feedId}", "/medias/similar-space/{feedId}/{cursor}"})
    public BaseResponse<List<GetFeedsMediasSimilarSpaceRes>> getFeedsMediasSimilarSpace(@PathVariable("feedId") Long feedId, @PathVariable(value = "cursor", required = false) Long cursor) {
        if (cursor == null){
            cursor = Long.MAX_VALUE;
        }
        if (feedId == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        try{
            List<GetFeedsMediasSimilarSpaceRes> getFeedsMediasSimilarSpaceRes = feedProvider.retrieveMediasSimilarSpace(feedId, cursor);
            return new BaseResponse<>(getFeedsMediasSimilarSpaceRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
