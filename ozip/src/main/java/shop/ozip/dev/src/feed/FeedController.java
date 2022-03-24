
package shop.ozip.dev.src.feed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.feed.model.*;
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
    미디어 피드 상세 내용 조회 API
    (GET) 127.0.0.1:9000/app/feeds/media-feeds/list?sort=&video=&home-type=&style=&acreage=
    */
    @ResponseBody
    @GetMapping(value = {"/media-feeds/list", "/media-feeds/list/{lastValue}"})
    public BaseResponse<List<GetFeedsMediaFeedsListRes>> getFeedsMediaFeedsList(@PathVariable(value = "lastValue", required = false) Long lastValue) {
        // 정렬 적용시 주의
        if (lastValue == null){
            lastValue = Long.valueOf(0);
        }
        try{
            List<GetFeedsMediaFeedsListRes> getFeedsMediaFeedsListRes = feedProvider.retrieveMediaFeedList(lastValue);
            return new BaseResponse<>(getFeedsMediaFeedsListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
