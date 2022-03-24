
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
    public BaseResponse<PostFeedsMediaFeedsCommentsRes> postFeedsMediaFeedsComments(@RequestBody PostFeedsMediaFeedsCommentsReq postFeedsMediaFeedsCommentsReq) {
        postFeedsMediaFeedsCommentsReq.checkNullIsRecomment();
        // 대댓글 여부와 대댓글 ID 정상 입력 validation
        if (postFeedsMediaFeedsCommentsReq.getIsRecomment() != 0){
            if (postFeedsMediaFeedsCommentsReq.getRecommentId() == null) {
                return new BaseResponse<>(AMBIGUOUS_RECOMMENT);
            }
        } else{
            if (postFeedsMediaFeedsCommentsReq.getRecommentId() != null) {
                return new BaseResponse<>(AMBIGUOUS_RECOMMENT);
            }
        }
        if (postFeedsMediaFeedsCommentsReq.getContent() == null || postFeedsMediaFeedsCommentsReq.getContent() == "") {
            return new BaseResponse<>(EMPTY_COMMENT_CONTENT);
        }
        if (postFeedsMediaFeedsCommentsReq.getFeedId() == null) {
            return new BaseResponse<>(EMPTY_COMMENT_FEED_ID);
        }

        try{
            PostFeedsMediaFeedsCommentsRes postFeedsMediaFeedsCommentsRes = commentService.createMediaFeedComment(postFeedsMediaFeedsCommentsReq);
            return new BaseResponse<>(postFeedsMediaFeedsCommentsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
