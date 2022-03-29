
package shop.ozip.dev.src.comment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.comment.model.*;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/comments")
public class CommentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final shop.ozip.dev.src.comment.CommentProvider commentProvider;
    @Autowired
    private final shop.ozip.dev.src.comment.CommentService commentService;
    @Autowired
    private final JwtService jwtService;




    public CommentController(CommentProvider commentProvider, CommentService commentService, JwtService jwtService){
        this.commentProvider = commentProvider;
        this.commentService = commentService;
        this.jwtService = jwtService;
    }

    /*
    피드에 댓글 달기 API
    (POST) 127.0.0.1:9000/app/comments
    */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostCommentsRes> postFeedsMediaFeedsComments(@RequestBody PostCommentsReq postCommentsReq) {
        postCommentsReq.checkNullIsRecomment();
        // 대댓글 여부와 대댓글 ID 정상 입력 validation
        if (postCommentsReq.getIsRecomment() != 0){
            if (postCommentsReq.getRecommentId() == null) {
                return new BaseResponse<>(AMBIGUOUS_RECOMMENT);
            }
        } else{
            if (postCommentsReq.getRecommentId() != null) {
                return new BaseResponse<>(AMBIGUOUS_RECOMMENT);
            }
        }
        if (postCommentsReq.getContent() == null || postCommentsReq.getContent() == "") {
            return new BaseResponse<>(EMPTY_COMMENT_CONTENT);
        }
        if (postCommentsReq.getFeedId() == null) {
            return new BaseResponse<>(EMPTY_COMMENT_FEED_ID);
        }

        try{
            PostCommentsRes postCommentsRes = commentService.createComment(postCommentsReq);
            return new BaseResponse<>(postCommentsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    피드의 댓글 일부 조회 API
    (GET) 127.0.0.1:9000/app/comments/part/:feedId/:cursor
    */
    @ResponseBody
    @GetMapping("/part/{feedId}")
    public BaseResponse<GetCommentsPartRes> getCommentsPartMediaFeeds(@PathVariable("feedId") Long feedId) {
        if (feedId == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        try{
            GetCommentsPartRes getCommentsPartRes = commentProvider.retrieveCommentsPartMediaFeeds(feedId);
            return new BaseResponse<>(getCommentsPartRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    피드 댓글 리스트 조회 API
    (GET) 127.0.0.1:9000/app/comments/list/:feedId/:cursor
    */
    @ResponseBody
    @GetMapping(value = {"/list/{feedId}", "/list/{feedId}/{cursor}"})
    public BaseResponse<GetCommentsListRes> getCommentsListMediaFeeds(@PathVariable("feedId") Long feedId, @PathVariable(value = "cursor", required = false) Long cursor) {
        if (feedId == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        if (cursor == null){
            cursor = Long.MAX_VALUE;
        }
        try{
            GetCommentsListRes getCommentsListRes = commentProvider.retrieveCommentsListMediaFeeds(feedId, cursor);
            return new BaseResponse<>(getCommentsListRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
