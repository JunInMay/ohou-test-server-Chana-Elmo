
package shop.ozip.dev.src.keyword;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.src.keyword.KeywordProvider;
import shop.ozip.dev.src.keyword.KeywordService;
import shop.ozip.dev.utils.JwtService;

@RestController
@RequestMapping("/app/comments")
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


}
