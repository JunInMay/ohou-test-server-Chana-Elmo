
package shop.ozip.dev.src.like;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.src.like.LikeDao;
import shop.ozip.dev.src.like.LikeProvider;
import shop.ozip.dev.utils.JwtService;

// Service Create, Update, Delete 의 로직 처리
@Service
public class LikeService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedDao feedDao;
    private final shop.ozip.dev.src.like.LikeDao likeDao;
    private final shop.ozip.dev.src.like.LikeProvider likeProvider;
    private final JwtService jwtService;
    private final String fileName;


    @Autowired
    public LikeService(LikeDao likeDao, LikeProvider likeProvider, JwtService jwtService, FeedDao feedDao) {
        this.likeDao = likeDao;
        this.likeProvider = likeProvider;
        this.jwtService = jwtService;
        this.fileName = "FeedService";
        this.feedDao = feedDao;

    }
}
