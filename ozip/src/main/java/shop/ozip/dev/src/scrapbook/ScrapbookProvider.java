
package shop.ozip.dev.src.scrapbook;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.src.feed.model.Feed;
import shop.ozip.dev.src.feed.model.GetFeedsMediaFeedsMetaRes;
import shop.ozip.dev.src.scrapbook.model.GetBookmarksScrapbookMainTopRes;
import shop.ozip.dev.src.scrapbook.model.Scrapbook;
import shop.ozip.dev.src.user.UserDao;
import shop.ozip.dev.utils.JwtService;

import static shop.ozip.dev.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class ScrapbookProvider {

    private final ScrapbookDao scrapbookDao;
    private final JwtService jwtService;
    private final String fileName;
    private final UserDao userDao;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ScrapbookProvider(ScrapbookDao scrapbookDao, JwtService jwtService, UserDao userDao) {
        this.scrapbookDao = scrapbookDao;
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.fileName = "FeedProvider";
    }

    // 스크랩북 상단 유저정보 + 피드 갯수 조회
    public GetBookmarksScrapbookMainTopRes retrieveScrapbookTop(Long userId, Long scrapbookId) throws BaseException {
        String methodName = "retrieveScrapbookTop";
        if (!userDao.checkUserExistById(userId)){
            throw new BaseException(USER_NOT_EXIST);
        }
        if (scrapbookId == 0){
            Scrapbook mainScrapbook = scrapbookDao.getMainScrapbookByUserId(userId);
            scrapbookId = mainScrapbook.getId();
        }
        if (!scrapbookDao.checkScrapbookById(scrapbookId)){
            throw new BaseException(SCRAPBOOK_NOT_EXIST);
        }


        try{
            GetBookmarksScrapbookMainTopRes getBookmarksScrapbookMainTopRes = scrapbookDao.retrieveScrapbookTop(scrapbookId);
            return getBookmarksScrapbookMainTopRes;
        }
        catch (Exception exception) {
            System.out.println("["+ fileName +":"+methodName+"]"+exception.getMessage());
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
