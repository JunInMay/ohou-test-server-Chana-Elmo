
package shop.ozip.dev.src.scrapbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.ozip.dev.utils.JwtService;

@RestController
@RequestMapping("/app/comments")
public class ScrapbookController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ScrapbookProvider scrapbookProvider;
    @Autowired
    private final ScrapbookService scrapbookService;
    @Autowired
    private final JwtService jwtService;


    public ScrapbookController(ScrapbookProvider scrapbookProvider, ScrapbookService scrapbookService, JwtService jwtService){
        this.scrapbookProvider = scrapbookProvider;
        this.scrapbookService = scrapbookService;
        this.jwtService = jwtService;
    }


}
