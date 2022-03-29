
package shop.ozip.dev.src.like;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.src.like.LikeDao;
import shop.ozip.dev.utils.JwtService;

//Provider : Read의 비즈니스 로직 처리
@Service
public class LikeProvider {

    private final shop.ozip.dev.src.like.LikeDao likeDao;
    private final JwtService jwtService;
    private final String fileName;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LikeProvider(LikeDao likeDao, JwtService jwtService) {
        this.likeDao = likeDao;
        this.jwtService = jwtService;
        this.fileName = "FeedProvider";
    }

}
