
package shop.ozip.dev.src.feed;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.src.feed.FeedProvider;
import shop.ozip.dev.src.feed.model.*;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;
import static shop.ozip.dev.config.BaseResponseStatus.DATABASE_ERROR;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FeedService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final shop.ozip.dev.src.feed.FeedDao feedDao;
    private final shop.ozip.dev.src.feed.FeedProvider feedProvider;
    private final JwtService jwtService;
    private final String fileName;


    @Autowired
    public FeedService(FeedDao feedDao, FeedProvider feedProvider, JwtService jwtService) {
        this.feedDao = feedDao;
        this.feedProvider = feedProvider;
        this.jwtService = jwtService;
        this.fileName = "FeedService";

    }

}
