
package shop.ozip.dev.src.comment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.comment.model.*;
import shop.ozip.dev.src.feed.model.GetFeedsMediaFeedsOthersRes;
import shop.ozip.dev.utils.JwtService;

import java.util.List;

import static shop.ozip.dev.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/comments")
public class CommentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CommentProvider commentProvider;
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final JwtService jwtService;




    public CommentController(CommentProvider commentProvider, CommentService commentService, JwtService jwtService){
        this.commentProvider = commentProvider;
        this.commentService = commentService;
        this.jwtService = jwtService;
    }

    /*
    미디어 피드 댓글 달기 API
    (POST) 127.0.0.1:9000/app/comments/media-feeds
    */
    @ResponseBody
    @PostMapping("/media-feeds")
    public BaseResponse<PostCommentsMediaFeedsRes> postFeedsMediaFeedsComments(@RequestBody PostCommentsMediaFeedsReq postCommentsMediaFeedsReq) {
        postCommentsMediaFeedsReq.checkNullIsRecomment();
        // 대댓글 여부와 대댓글 ID 정상 입력 validation
        if (postCommentsMediaFeedsReq.getIsRecomment() != 0){
            if (postCommentsMediaFeedsReq.getRecommentId() == null) {
                return new BaseResponse<>(AMBIGUOUS_RECOMMENT);
            }
        } else{
            if (postCommentsMediaFeedsReq.getRecommentId() != null) {
                return new BaseResponse<>(AMBIGUOUS_RECOMMENT);
            }
        }
        if (postCommentsMediaFeedsReq.getContent() == null || postCommentsMediaFeedsReq.getContent() == "") {
            return new BaseResponse<>(EMPTY_COMMENT_CONTENT);
        }
        if (postCommentsMediaFeedsReq.getFeedId() == null) {
            return new BaseResponse<>(EMPTY_COMMENT_FEED_ID);
        }

        try{
            PostCommentsMediaFeedsRes postCommentsMediaFeedsRes = commentService.createMediaFeedComment(postCommentsMediaFeedsReq);
            return new BaseResponse<>(postCommentsMediaFeedsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    미디어 피드 댓글 일부 조회 API
    (GET) 127.0.0.1:9000/app/comments/part/media-feeds/:feedId
    */
    @ResponseBody
    @GetMapping("/part/media-feeds/{feedId}")
    public BaseResponse<GetCommentsPartMediaFeedsRes> getCommentsPartMediaFeeds(@PathVariable("feedId") Long feedId) {
        if (feedId == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        try{
            GetCommentsPartMediaFeedsRes getCommentsPartMediaFeedsRes = commentProvider.retrieveCommentsPartMediaFeeds(feedId);
            return new BaseResponse<>(getCommentsPartMediaFeedsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
