
package shop.ozip.dev.src.scrapbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.comment.model.PostCommentsMediaFeedsReq;
import shop.ozip.dev.src.comment.model.PostCommentsMediaFeedsRes;
import shop.ozip.dev.src.feed.model.GetFeedsMediaFeedsMetaRes;
import shop.ozip.dev.src.scrapbook.model.*;
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

    /*
    스크랩북(북마크 폴더) 만들기 API
    (POST) 127.0.0.1:9000/app/bookmarks
    */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostBookmarksRes> postBookmarks(@RequestBody PostBookmarksReq postBookmarksReq) {

        if (postBookmarksReq.getName() == null || postBookmarksReq.getName() == ""){
            return new BaseResponse<>(EMPTY_BOOKMARK_NAME);
        }
        if (postBookmarksReq.getDescription().length() > 30){
            return new BaseResponse<>(TOO_LONG_BOOKMARK_DESCRIPTION);
        }

        try{
            PostBookmarksRes postBookmarksRes = scrapbookService.createScrapbook(postBookmarksReq);
            return new BaseResponse<>(postBookmarksRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /*
    특정 유저의 메인 스크랩북 최상단 정보 조회 API
    (GET) 127.0.0.1:9000/app/bookmarks/scrapbook/main/top/:userId
    */
    @ResponseBody
    @GetMapping("/scrapbook/main/top/{userId}")
    public BaseResponse<GetBookmarksScrapbookMainTopRes> getBookmarksScrapbookMainTop(@PathVariable("userId") Long userId) {
        if (userId == null){
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        try{
            Long scrapbookId = 0L;
            GetBookmarksScrapbookMainTopRes getBookmarksScrapbookMainTopRes = scrapbookProvider.retrieveScrapbookTop(userId, scrapbookId);
            return new BaseResponse<>(getBookmarksScrapbookMainTopRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
