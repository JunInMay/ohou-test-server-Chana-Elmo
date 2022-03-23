
package shop.ozip.dev.src.feed;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponseStatus;
import shop.ozip.dev.config.secret.Secret;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.src.feed.FeedProvider;
import shop.ozip.dev.src.feed.model.*;
import shop.ozip.dev.utils.JwtService;
import shop.ozip.dev.utils.SHA256;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static shop.ozip.dev.config.BaseResponseStatus.KAKAO_INVALID_ACCESS_TOKEN;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FeedService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedDao feedDao;
    private final FeedProvider feedProvider;
    private final JwtService jwtService;


    @Autowired
    public FeedService(FeedDao feedDao, FeedProvider feedProvider, JwtService jwtService) {
        this.feedDao = feedDao;
        this.feedProvider = feedProvider;
        this.jwtService = jwtService;

    }
}
