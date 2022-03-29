
package shop.ozip.dev.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.user.UserProvider;
import shop.ozip.dev.src.user.UserService;
import shop.ozip.dev.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseResponseStatus;
import shop.ozip.dev.src.user.model.*;
import shop.ozip.dev.utils.ValidationRegex;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static shop.ozip.dev.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final shop.ozip.dev.src.user.UserProvider userProvider;
    @Autowired
    private final shop.ozip.dev.src.user.UserService userService;
    @Autowired
    private final JwtService jwtService;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /*
    내 기본 정보 조회 API
    (GET) 127.0.0.1:9000/app/users/me
    */
    @ResponseBody
    @GetMapping("/me")
    public BaseResponse<GetUsersMeRes> getUsersMe() {
        try{
            GetUsersMeRes getUsersMeRes = userProvider.getUsersMe();
            return new BaseResponse<>(getUsersMeRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /*
    해당 피드의 작성자 정보 조회 API
    (GET) 127.0.0.1:9000/app/users/feeds/:feedId
    */
    @ResponseBody
    @GetMapping("/feeds/{feedId}")
    public BaseResponse<GetUsersFeedsRes> getUsersFeeds(@PathVariable("feedId") Long feedId) {
        try{
            GetUsersFeedsRes getUsersFeedsRes = userProvider.retrieveUsersFeeds(feedId);
            return new BaseResponse<>(getUsersFeedsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }


    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<GetUsersRes> getUser(@PathVariable("userId") Long userId) {
        // Get Users
        try{
            GetUsersRes getUsersRes = userProvider.getUser(userId);
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUsersRes> createUser(@RequestBody PostUsersReq postUsersReq) {

        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if (postUsersReq.getProvider() == "local") {
            if (postUsersReq.getEmail() == null) {
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_EMAIL);
            }
            //이메일 정규표현
            if (!ValidationRegex.isRegexEmail(postUsersReq.getEmail())) {
                return new BaseResponse<>(BaseResponseStatus.POST_USERS_INVALID_EMAIL);
            }
        }
        try{
            PostUsersRes postUsersRes = userService.createUser(postUsersReq);
            return new BaseResponse<>(postUsersRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }
    // 카카오 로그인
    @ResponseBody
    @GetMapping("/kakao-login")
    public BaseResponse<PostLoginRes> getKakaoLogin() {
        try{
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String accessToken = request.getHeader("Access-Token");
            if (accessToken == null) {
                return new BaseResponse<>(KAKAO_EMPTY_ACCESS_TOKEN);
            }
            String kakaoId = "kakao_"+userService.getKakaoUser(accessToken).getId().toString();
            PostLoginRes userLoginRes = userProvider.loginKakaoUser(kakaoId);
            return new BaseResponse<>(userLoginRes);
        } catch (IOException ioException) {
            return new BaseResponse<>(KAKAO_INVALID_ACCESS_TOKEN);
        }
        catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /*
     * 카카오 로그인 : code 획득용 콜백 메소드
     * [GET] /app/users/kakao
     * */
    @ResponseBody
    @GetMapping("/kakao")
    public BaseResponse<KakaoCallback> kakaoCallback(@RequestParam String code) {
        try {
            System.out.println("[UserController:kakaoCallback] code : " + code);
            String accessToken = userService.getKakaoAccessToken(code);
            userService.getKakaoUser(accessToken);
            KakaoCallback kakaoCallback = new KakaoCallback(accessToken);
            return new BaseResponse<>(kakaoCallback);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        } catch (IOException e) {
            return new BaseResponse<>(KAKAO_LOGIN_FAIL);
        }
    }







    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
//    @ResponseBody
//    @PatchMapping("/{userIdx}")
//    public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user){
//        try {
//            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userIdx != userIdxByJwt){
//                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT);
//            }
//            //같다면 유저네임 변경
//            PatchUserReq patchUserReq = new PatchUserReq(userIdx,user.getUserName());
//            userService.modifyUserName(patchUserReq);
//
//            String result = "";
//        return new BaseResponse<>(result);
//        } catch (BaseException exception) {
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }


}
