
package shop.ozip.dev.src.follow;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.src.follow.FollowDao;
import shop.ozip.dev.utils.JwtService;

//Provider : Read의 비즈니스 로직 처리
@Service
public class FollowProvider {

    private final shop.ozip.dev.src.follow.FollowDao followDao;
    private final JwtService jwtService;
    private final String fileName;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public FollowProvider(FollowDao followDao, JwtService jwtService) {
        this.followDao = followDao;
        this.jwtService = jwtService;
        this.fileName = "FeedProvider";
    }

}
