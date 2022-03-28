
package shop.ozip.dev.src.text;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.secret.Secret;
import shop.ozip.dev.src.feed.FeedDao;
import shop.ozip.dev.src.like.LikeDao;
import shop.ozip.dev.src.like.LikeProvider;
import shop.ozip.dev.src.text.model.AdultCheckRes;
import shop.ozip.dev.src.text.model.GetTextVerificationRes;
import shop.ozip.dev.utils.JwtService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

// Service Create, Update, Delete 의 로직 처리
@Service
public class TextService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FeedDao feedDao;
    private final shop.ozip.dev.src.like.LikeDao likeDao;
    private final LikeProvider likeProvider;
    private final JwtService jwtService;
    private final String fileName;


    @Autowired
    public TextService(LikeDao likeDao, LikeProvider likeProvider, JwtService jwtService, FeedDao feedDao) {
        this.likeDao = likeDao;
        this.likeProvider = likeProvider;
        this.jwtService = jwtService;
        this.fileName = "FeedService";
        this.feedDao = feedDao;

    }

    // 카카오 : 액세스토큰으로 카카오 유저정보 받기
    //https://kauth.kakao.com/oauth/authorize?client_id=333c74b180fcfd54b8e90b3a2232a8b2&redirect_uri=http://localhost:9000/app/users/kakao&response_type=code
    public GetTextVerificationRes checkProhibitedWord(String text) throws BaseException, IOException {
        String queryString = "?query="+URLEncoder.encode(text, "UTF-8");
        String reqURL = "https://openapi.naver.com/v1/search/adult.json" + queryString;
        String kakaoId;
        //access_token을 이용하여 사용자 정보 조회
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        conn.setRequestProperty("X-Naver-Client-Id", Secret.CHANA_NAVER_APP_ID);
        conn.setRequestProperty("X-Naver-Client-Secret", Secret.CHANA_NAVER_APP_KEY);

        System.out.println(reqURL);

        //결과 코드가 200이라면 성공
        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);

        //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br_before = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println("response body : " + result);

        ObjectMapper objectMapper = new ObjectMapper();

        AdultCheckRes adultCheckRes = objectMapper.readValue(result, AdultCheckRes.class);


        br.close();

        GetTextVerificationRes getTextVerificationRes = new GetTextVerificationRes();
        getTextVerificationRes.setText(text);
        getTextVerificationRes.setResult("appropriate");
        if (Integer.parseInt(adultCheckRes.getAdult())==1){
            getTextVerificationRes.setResult("inappropriate");
        }
        System.out.println("체크한 텍스트 : " + text);
        System.out.println("체크한 결과 : "+ getTextVerificationRes.getResult());



        return getTextVerificationRes;
    }
}
