
package shop.ozip.dev.src.scrapbook;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.utils.JwtService;

// Service Create, Update, Delete 의 로직 처리
@Service
public class ScrapbookService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedDao feedDao;
    private final ScrapbookDao scrapbookDao;
    private final ScrapbookProvider scrapbookProvider;
    private final JwtService jwtService;
    private final String fileName;


    @Autowired
    public ScrapbookService(ScrapbookDao scrapbookDao, ScrapbookProvider scrapbookProvider, JwtService jwtService, FeedDao feedDao) {
        this.scrapbookDao = scrapbookDao;
        this.scrapbookProvider = scrapbookProvider;
        this.jwtService = jwtService;
        this.fileName = "FeedService";
        this.feedDao = feedDao;

    }
}
