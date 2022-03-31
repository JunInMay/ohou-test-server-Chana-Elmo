
package shop.ozip.dev.src.follow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.feed.model.GetFeedsHotsKeywordRes;
import shop.ozip.dev.src.follow.FollowProvider;
import shop.ozip.dev.src.follow.FollowService;
import shop.ozip.dev.src.follow.model.*;
import shop.ozip.dev.src.user.model.PostUsersReq;
import shop.ozip.dev.utils.JwtService;

@RestController
@RequestMapping("/app/follows")
public class FollowController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final shop.ozip.dev.src.follow.FollowProvider followProvider;
    @Autowired
    private final shop.ozip.dev.src.follow.FollowService followService;
    @Autowired
    private final JwtService jwtService;


    public FollowController(FollowProvider followProvider, FollowService followService, JwtService jwtService){
        this.followProvider = followProvider;
        this.followService = followService;
        this.jwtService = jwtService;
    }

    /*
    유저 팔로우 하기 API
    (POST) 127.0.0.1:9000/app/follows/users
    */
    @ResponseBody
    @PostMapping("/users")
    public BaseResponse<PostFollowsUsersRes> postFollowsUsers(@RequestBody PostFollowsUsersReq postFollowsUsersReq) {
        try{
            PostFollowsUsersRes postFollowsUsersRes = followService.createFollowsUsers(postFollowsUsersReq);
            return new BaseResponse<>(postFollowsUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    유저 언팔로우 하기 API
    (DELETE) 127.0.0.1:9000/app/follows/users
    */
    @ResponseBody
    @DeleteMapping("/users")
    public BaseResponse<DeleteFollowsUsersRes> deleteFollowsUsers(@RequestBody DeleteFollowsUsersReq deleteFollowsUsersReq) {
        try{
            DeleteFollowsUsersRes deleteFollowsUsersRes = followService.deleteFollowsUsers(deleteFollowsUsersReq);
            return new BaseResponse<>(deleteFollowsUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    키워드 팔로우 하기 API

    (POST) 127.0.0.1:9000/app/follows/keywords
    */
    @ResponseBody
    @PostMapping("/keywords")
    public BaseResponse<PostFollowsKeywordsRes> postFollowsKeywords(@RequestBody PostFollowsKeywordsReq postFollowsKeywordsReq) {
        try{
            PostFollowsKeywordsRes postFollowsKeywordsRes = followService.createFollowsKeywords(postFollowsKeywordsReq);
            return new BaseResponse<>(postFollowsKeywordsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /*
    키워드 언팔로우 하기 API
    (DELETE) 127.0.0.1:9000/app/follows/keywords
    */
    @ResponseBody
    @DeleteMapping("/keywords")
    public BaseResponse<DeleteFollowsKeywordsRes> deleteFollowsKeywords(@RequestBody DeleteFollowsKeywordsReq deleteFollowsKeywordsReq) {
        try{
            DeleteFollowsKeywordsRes deleteFollowsKeywordsRes = followService.deleteFollowsKeywords(deleteFollowsKeywordsReq);
            return new BaseResponse<>(deleteFollowsKeywordsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
