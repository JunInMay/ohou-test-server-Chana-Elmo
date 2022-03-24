
package shop.ozip.dev.src.keyword;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.src.keyword.model.*;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class KeywordService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedDao feedDao;
    private final KeywordDao keywordDao;
    private final KeywordProvider keywordProvider;
    private final JwtService jwtService;
    private final String fileName;


    @Autowired
    public KeywordService(KeywordDao keywordDao, KeywordProvider keywordProvider, JwtService jwtService, FeedDao feedDao) {
        this.keywordDao = keywordDao;
        this.keywordProvider = keywordProvider;
        this.jwtService = jwtService;
        this.fileName = "FeedService";
        this.feedDao = feedDao;

    }
}
