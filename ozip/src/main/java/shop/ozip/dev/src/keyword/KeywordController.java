
package shop.ozip.dev.src.keyword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.feed.model.GetFeedsQnAUserCommentRes;
import shop.ozip.dev.src.keyword.KeywordProvider;
import shop.ozip.dev.src.keyword.KeywordService;
import shop.ozip.dev.src.keyword.model.GetKeywordsRecommendedKeywords;
import shop.ozip.dev.utils.JwtService;

import java.util.List;

@RestController
@RequestMapping("/app/keywords")
public class KeywordController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final shop.ozip.dev.src.keyword.KeywordProvider keywordProvider;
    @Autowired
    private final shop.ozip.dev.src.keyword.KeywordService keywordService;
    @Autowired
    private final JwtService jwtService;


    public KeywordController(KeywordProvider keywordProvider, KeywordService keywordService, JwtService jwtService){
        this.keywordProvider = keywordProvider;
        this.keywordService = keywordService;
        this.jwtService = jwtService;
    }


    /*
    질문과 답변 추천 키워드 조회 API
    (GET) 127.0.0.1:9000/app/keywords/recommended-keywords
    */
    @ResponseBody
    @GetMapping("/recommended-keywords")
    public BaseResponse<List<GetKeywordsRecommendedKeywords>> getKeywordsRecommendedKeywords() {
        try{
            List<GetKeywordsRecommendedKeywords> getKeywordsRecommendedKeywordsList = keywordProvider.retrieveRecommendedKeywords();
            return new BaseResponse<>(getKeywordsRecommendedKeywordsList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
