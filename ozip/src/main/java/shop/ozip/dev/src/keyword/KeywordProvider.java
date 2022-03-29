
package shop.ozip.dev.src.keyword;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.src.keyword.KeywordDao;
import shop.ozip.dev.utils.JwtService;

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

}
