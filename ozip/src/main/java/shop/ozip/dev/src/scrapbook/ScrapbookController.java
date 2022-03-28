
package shop.ozip.dev.src.scrapbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.comment.model.PostCommentsMediaFeedsReq;
import shop.ozip.dev.src.comment.model.PostCommentsMediaFeedsRes;
import shop.ozip.dev.src.scrapbook.model.PostBookmarksFeedReq;
import shop.ozip.dev.src.scrapbook.model.PostBookmarksFeedRes;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/bookmarks")
public class ScrapbookController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ScrapbookProvider scrapbookProvider;
    @Autowired
    private final ScrapbookService scrapbookService;
    @Autowired
    private final JwtService jwtService;


    public ScrapbookController(ScrapbookProvider scrapbookProvider, ScrapbookService scrapbookService, JwtService jwtService){
        this.scrapbookProvider = scrapbookProvider;
        this.scrapbookService = scrapbookService;
        this.jwtService = jwtService;
    }


    /*
    피드 북마크 하기 API
    (POST) 127.0.0.1:9000/app/bookmarks/feed
    */
    @ResponseBody
    @PostMapping("/feed")
    public BaseResponse<PostBookmarksFeedRes> postBookmarksFeed(@RequestBody PostBookmarksFeedReq postBookmarksFeedReq) {

        if (postBookmarksFeedReq.getFeedId() == null || postBookmarksFeedReq.getFeedId() == 0) {
            return new BaseResponse<>(EMPTY_BOOKMARK_FEED_ID);
        }

        try{
            PostBookmarksFeedRes postBookmarksFeedRes = scrapbookService.createBookmarkFeed(postBookmarksFeedReq);
            return new BaseResponse<>(postBookmarksFeedRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
