
package shop.ozip.dev.src.comment;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.utils.JwtService;

//Provider : Read의 비즈니스 로직 처리
@Service
public class CommentProvider {

    private final CommentDao commentDao;
    private final JwtService jwtService;
    private final String fileName;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CommentProvider(CommentDao commentDao, JwtService jwtService) {
        this.commentDao = commentDao;
        this.jwtService = jwtService;
        this.fileName = "FeedProvider";
    }

}
