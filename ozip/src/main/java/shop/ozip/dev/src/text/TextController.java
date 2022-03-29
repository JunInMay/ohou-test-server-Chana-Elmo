
package shop.ozip.dev.src.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.like.LikeProvider;
import shop.ozip.dev.src.like.LikeService;
import shop.ozip.dev.src.text.TextService;
import shop.ozip.dev.src.text.model.GetTextVerificationRes;
import shop.ozip.dev.src.user.model.KakaoCallback;
import shop.ozip.dev.utils.JwtService;

import java.io.IOException;

import static shop.ozip.dev.config.BaseResponseStatus.KAKAO_LOGIN_FAIL;
import static shop.ozip.dev.config.BaseResponseStatus.NAVER_API_ERROR;

@RestController
@RequestMapping("/app/text")
public class TextController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private final shop.ozip.dev.src.text.TextService textService;
    @Autowired
    private final JwtService jwtService;


    public TextController(LikeProvider likeProvider, TextService textService, JwtService jwtService){
        this.textService = textService;
        this.jwtService = jwtService;
    }

    /*
     * 서드파티 네이버 API - 불건전 워드 검색
     * [GET] /app/users/
     * */
    @ResponseBody
    @GetMapping("/verification/{text}")
    public BaseResponse<GetTextVerificationRes> getTextVerification(@PathVariable String text) {
        try {
            GetTextVerificationRes getTextVerificationRes = textService.checkProhibitedWord(text);
            return new BaseResponse<>(getTextVerificationRes);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        } catch (IOException e) {
            e.printStackTrace();
            return new BaseResponse<>(NAVER_API_ERROR);
        }
    }

}
