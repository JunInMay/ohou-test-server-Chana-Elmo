
package shop.ozip.dev.src.like;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.ozip.dev.utils.JwtService;

@RestController
@RequestMapping("/app/comments")
public class LikeController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final LikeProvider likeProvider;
    @Autowired
    private final LikeService likeService;
    @Autowired
    private final JwtService jwtService;


    public LikeController(LikeProvider likeProvider, LikeService likeService, JwtService jwtService){
        this.likeProvider = likeProvider;
        this.likeService = likeService;
        this.jwtService = jwtService;
    }


}
