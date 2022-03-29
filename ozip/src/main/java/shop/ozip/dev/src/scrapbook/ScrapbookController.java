
package shop.ozip.dev.src.scrapbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.scrapbook.ScrapbookProvider;
import shop.ozip.dev.src.scrapbook.ScrapbookService;
import shop.ozip.dev.src.scrapbook.model.*;
import shop.ozip.dev.utils.JwtService;

import java.util.List;

import static shop.ozip.dev.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/bookmarks")
public class ScrapbookController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final shop.ozip.dev.src.scrapbook.ScrapbookProvider scrapbookProvider;
    @Autowired
    private final shop.ozip.dev.src.scrapbook.ScrapbookService scrapbookService;
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
    피드 북마크 취소하기 API
    (POST) 127.0.0.1:9000/app/bookmarks/feed
    */
    @ResponseBody
    @DeleteMapping("/feed")
    public BaseResponse<DeleteBookmarksFeedRes> deleteBookmarksFeed(@RequestBody DeleteBookmarksFeedReq deleteBookmarksFeedReq) {

        if (deleteBookmarksFeedReq.getFeedId() == null || deleteBookmarksFeedReq.getFeedId() == 0) {
            return new BaseResponse<>(EMPTY_BOOKMARK_FEED_ID_FOR_DELETE);
        }

        try{
            DeleteBookmarksFeedRes deleteBookmarksFeedRes = scrapbookService.deleteBookmarkFeed(deleteBookmarksFeedReq);
            return new BaseResponse<>(deleteBookmarksFeedRes);
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
    스크랩북 내용 수정하기 API
    (PATCH) 127.0.0.1:9000/app/bookmarks
    */
    @ResponseBody
    @PatchMapping("")
    public BaseResponse<PatchBookmarksRes> patchBookmarks(@RequestBody PatchBookmarksReq patchBookmarksReq) {

        if (patchBookmarksReq.getName() == null || patchBookmarksReq.getName() == ""){
            return new BaseResponse<>(EMPTY_BOOKMARK_NAME);
        }
        if (patchBookmarksReq.getDescription().length() > 30){
            return new BaseResponse<>(TOO_LONG_BOOKMARK_DESCRIPTION);
        }

        try{
            PatchBookmarksRes patchBookmarksRes = scrapbookService.updateScrapbook(patchBookmarksReq);
            return new BaseResponse<>(patchBookmarksRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    스크랩북 삭제하기 API
    (DELETE) 127.0.0.1:9000/app/bookmarks
    */
    @ResponseBody
    @DeleteMapping("")
    public BaseResponse<DeleteBookmarksRes> deleteBookmarks(@RequestBody DeleteBookmarksReq deleteBookmarksReq) {

        if (deleteBookmarksReq.getScrapbookId() == null || deleteBookmarksReq.getScrapbookId() == 0){
            return new BaseResponse<>(EMPTY_SCRAPBOOK_ID);
        }

        try{
            DeleteBookmarksRes deleteBookmarksRes = scrapbookService.deleteScrapbook(deleteBookmarksReq);
            return new BaseResponse<>(deleteBookmarksRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /*
    특정 유저의 메인 스크랩북 최상단 정보 조회 API
    (GET) 127.0.0.1:9000/app/bookmarks/scrapbook/main/top/:userId
    */
    @ResponseBody
    @GetMapping("/scrapbooks/main/top/{userId}")
    public BaseResponse<GetBookmarksScrapbookTopRes> getBookmarksScrapbookMainTop(@PathVariable("userId") Long userId) {
        if (userId == null){
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        try{
            Long scrapbookId = 0L;
            GetBookmarksScrapbookTopRes getBookmarksScrapbookTopRes = scrapbookProvider.retrieveScrapbookTop(userId, scrapbookId);
            return new BaseResponse<>(getBookmarksScrapbookTopRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    특정 스크랩북 최상단 정보 조회 API
    (GET) 127.0.0.1:9000/app/bookmarks/scrapbook/top/:scrapbookId
    */
    @ResponseBody
    @GetMapping("/scrapbooks/top/{scrapbookId}")
    public BaseResponse<GetBookmarksScrapbookTopRes> getBookmarksScrapbookTop(@PathVariable("scrapbookId") Long scrapbookId) {
        if (scrapbookId == null){
            return new BaseResponse<>(EMPTY_SCRAPBOOK_ID);
        }
        if (scrapbookId == 0){
            return new BaseResponse<>(WRONG_SCRAPBOOK_ID);
        }
        try{
            GetBookmarksScrapbookTopRes getBookmarksScrapbookTopRes = scrapbookProvider.retrieveScrapbookTop(null, scrapbookId);
            return new BaseResponse<>(getBookmarksScrapbookTopRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    특정 유저가 만든 모든 서브 스크랩북 조회 API
    (GET) 127.0.0.1:9000/app/bookmarks/scrapbook/:userId/:cursor
    */
    @ResponseBody
    @GetMapping(value = {"/scrapbooks/{userId}/{cursor}","/scrapbooks/{userId}"})
    public BaseResponse<List<GetBookmarksScrapbook>> getBookmarksScrapbook(@PathVariable("userId") Long userId, @PathVariable(value = "cursor", required = false) Long cursor) {
        if (userId == null){
            return new BaseResponse<>(USERS_EMPTY_USER_ID);
        }
        if (cursor == null){
            cursor = Long.MAX_VALUE;
        }
        try{
            List<GetBookmarksScrapbook> getBookmarksScrapbookList = scrapbookProvider.retrieveSubScrapbook(userId, cursor);
            return new BaseResponse<>(getBookmarksScrapbookList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
