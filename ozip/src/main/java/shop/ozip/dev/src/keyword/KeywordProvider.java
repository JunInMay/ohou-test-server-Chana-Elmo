
package shop.ozip.dev.src.keyword;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.feed.model.GetFeedsQnASimilarRes;
import shop.ozip.dev.src.feed.model.GetFeedsQnASimilarResFeed;
import shop.ozip.dev.src.keyword.KeywordDao;
import shop.ozip.dev.src.keyword.model.GetKeywordsRecommendedKeywords;
import shop.ozip.dev.src.keyword.model.QnAKeyword;
import shop.ozip.dev.utils.JwtService;

import java.util.ArrayList;
import java.util.List;

import static shop.ozip.dev.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class KeywordProvider {

    private final shop.ozip.dev.src.keyword.KeywordDao keywordDao;
    private final JwtService jwtService;
    private final String fileName;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public KeywordProvider(KeywordDao keywordDao, JwtService jwtService) {
        this.keywordDao = keywordDao;
        this.jwtService = jwtService;
        this.fileName = "FeedProvider";
    }

    public List<GetKeywordsRecommendedKeywords> retrieveRecommendedKeywords() throws BaseException {
        try{
            List<GetKeywordsRecommendedKeywords> getKeywordsRecommendedKeywordsList = keywordDao.retrieveRecommendedKeywords();
            return getKeywordsRecommendedKeywordsList;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
