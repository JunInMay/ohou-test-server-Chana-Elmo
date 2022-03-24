
package shop.ozip.dev.src.scrapbook;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.utils.JwtService;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ScrapbookProvider {

    private final ScrapbookDao scrapbookDao;
    private final JwtService jwtService;
    private final String fileName;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ScrapbookProvider(ScrapbookDao scrapbookDao, JwtService jwtService) {
        this.scrapbookDao = scrapbookDao;
        this.jwtService = jwtService;
        this.fileName = "FeedProvider";
    }

}
