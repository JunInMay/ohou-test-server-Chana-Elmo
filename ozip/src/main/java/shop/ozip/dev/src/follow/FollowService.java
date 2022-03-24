
package shop.ozip.dev.src.follow;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.utils.JwtService;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedDao feedDao;
    private final FollowDao followDao;
    private final FollowProvider followProvider;
    private final JwtService jwtService;
    private final String fileName;


    @Autowired
    public FollowService(FollowDao followDao, FollowProvider followProvider, JwtService jwtService, FeedDao feedDao) {
        this.followDao = followDao;
        this.followProvider = followProvider;
        this.jwtService = jwtService;
        this.fileName = "FeedService";
        this.feedDao = feedDao;

    }
}
