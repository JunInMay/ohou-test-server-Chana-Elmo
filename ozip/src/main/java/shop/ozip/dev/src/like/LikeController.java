
package shop.ozip.dev.src.like;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.like.model.*;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/likes" +
        "")
public class LikeController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final shop.ozip.dev.src.like.LikeProvider likeProvider;
    @Autowired
    private final shop.ozip.dev.src.like.LikeService likeService;
    @Autowired
    private final JwtService jwtService;


    public LikeController(LikeProvider likeProvider, LikeService likeService, JwtService jwtService){
        this.likeProvider = likeProvider;
        this.likeService = likeService;
        this.jwtService = jwtService;
    }

    /*
    댓글 좋아요 API
    (POST) 127.0.0.1:9000/app/likes/comments
    */
    @ResponseBody
    @PostMapping("/comments")
    public BaseResponse<PostLikesCommentsRes> postLikesComments(@RequestBody PostLikesCommentsReq postLikesCommentsReq) {
        if(postLikesCommentsReq.getCommentId() == null){
            return new BaseResponse<>(EMPTY_COMMENT_ID);
        }
        try{
            PostLikesCommentsRes postLikesCommentsRes = likeService.createLikesComment(postLikesCommentsReq);
            return new BaseResponse<>(postLikesCommentsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    댓글 좋아요 취소 API
    (DELETE) 127.0.0.1:9000/app/likes/comments
    */
    @ResponseBody
    @DeleteMapping("/comments")
    public BaseResponse<DeleteLikesCommentsRes> deleteLikesComments(@RequestBody DeleteLikesCommentsReq deleteLikesCommentsReq) {
        if(deleteLikesCommentsReq.getCommentId() == null){
            return new BaseResponse<>(EMPTY_COMMENT_ID);
        }
        try{
            DeleteLikesCommentsRes deleteLikesCommentsRes = likeService.deleteLikesComment(deleteLikesCommentsReq);
            return new BaseResponse<>(deleteLikesCommentsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /*
    피드 좋아요 API
    (POST) 127.0.0.1:9000/app/likes/feeds
    */
    @ResponseBody
    @PostMapping("/feeds")
    public BaseResponse<PostLikesFeedsRes> postLikesFeeds(@RequestBody PostLikesFeedsReq postLikesFeedsReq) {
        if(postLikesFeedsReq.getFeedId() == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        try{
            PostLikesFeedsRes postLikesFeedsRes = likeService.createLikesFeed(postLikesFeedsReq);
            return new BaseResponse<>(postLikesFeedsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    피드 좋아요 취소 API
    (DELETE) 127.0.0.1:9000/app/likes/comments
    */
    @ResponseBody
    @DeleteMapping("/feeds")
    public BaseResponse<DeleteLikesFeedsRes> deleteLikesFeeds(@RequestBody DeleteLikesFeedsReq deleteLikesFeedsReq) {
        if(deleteLikesFeedsReq.getFeedId() == null){
            return new BaseResponse<>(EMPTY_FEED_ID);
        }
        try{
            DeleteLikesFeedsRes deleteLikesFeedsRes = likeService.deleteLikesFeed(deleteLikesFeedsReq);
            return new BaseResponse<>(deleteLikesFeedsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
