
package shop.ozip.dev.src.feed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.config.BaseResponseStatus;
import shop.ozip.dev.src.feed.FeedProvider;
import shop.ozip.dev.src.feed.FeedService;
import shop.ozip.dev.src.feed.model.*;
import shop.ozip.dev.utils.JwtService;
import shop.ozip.dev.utils.ValidationRegex;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    public BaseResponse<GetFeedMediaFeedRes> getFeedsMediaFeeds(@PathVariable("feedId") Long feedId) {
        if (feedId == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        try{
            GetFeedMediaFeedRes getFeedMediaFeedRes = feedProvider.retrieveMediaFeed(feedId);
            return new BaseResponse<>(getFeedMediaFeedRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
