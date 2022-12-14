
package shop.ozip.dev.src.user;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.secret.Secret;
import shop.ozip.dev.src.scrapbook.ScrapbookDao;
import shop.ozip.dev.src.scrapbook.model.PostBookmarksRes;
import shop.ozip.dev.src.user.model.*;
import shop.ozip.dev.utils.JwtService;
import shop.ozip.dev.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.ozip.dev.config.BaseResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static shop.ozip.dev.config.BaseResponseStatus.KAKAO_INVALID_ACCESS_TOKEN;
import static shop.ozip.dev.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final shop.ozip.dev.src.user.UserDao userDao;
    private final shop.ozip.dev.src.user.UserProvider userProvider;
    private final JwtService jwtService;
    private final ScrapbookDao scrapbookDao;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService, ScrapbookDao scrapbookDao) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

        this.scrapbookDao = scrapbookDao;
    }

    //POST 회원가입
    public PostUsersRes createUser(PostUsersReq postUsersReq) throws BaseException {


        // 카카오 유저 회원가입일 경우
        if (postUsersReq.getProvider().equals("kakao")) {
            try {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                String accessToken = request.getHeader("Access-Token");
                KakaoUserRes kakaoUserRes = getKakaoUser(accessToken); // 액세스 토큰을 활용해서 카카오 유저의 id 확인
                String kakaoId = kakaoUserRes.getId().toString();
                postUsersReq.setEmail("kakao_"+kakaoId); // 로그인 아이디는 kakao_21380328와 같은 형식
                postUsersReq.setPassword(kakaoId); // 비밀번호는 사실 필요없지만, null값이 들어갈 순 없으므로 일단 kakaoId와 동일하게 세팅
            } catch (IOException ioException){
                throw new BaseException(KAKAO_INVALID_ACCESS_TOKEN);
            }
        }

        //중복
        if (userProvider.checkEmail(postUsersReq.getEmail()) == 1) {
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_EMAIL);
        }

        if (userProvider.checkNickname(postUsersReq.getNickname()) == 1) {
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_NICKNAME);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUsersReq.getPassword());
            postUsersReq.setPassword(pwd);

        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            
            postUsersReq.checkNullProfileImageUrl();
            Long userId = userDao.createUser(postUsersReq);

            // 계정이 생성되었으므로 메인 스크랩북 만들어줌
            PostBookmarksRes postBookmarksRes = scrapbookDao.createScrapbook(userId, "스크랩북", null, 1);
            
            //jwt 발급.
            return new PostUsersRes(userId);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }

    // 유저 정보 수정
    public PatchUserRes updateUser(PatchUserReq patchUserReq) throws BaseException{
        Long userId = jwtService.getUserId();
        if (!userDao.checkUserExistById(userId)){
            throw new BaseException(USER_NOT_EXIST);
        }
        if (userProvider.checkNickname(patchUserReq.getNickname()) == 1) {
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_NICKNAME);
        }
        User user = userDao.getUserById(userId);
        patchUserReq.checkNull(user);
        try{

            int result = userDao.updateUser(userId, patchUserReq);
            user = userDao.getUserById(userId);

            return new PatchUserRes(
                    user.getProfileImageUrl(),
                    user.getNickname(),
                    user.getDescription(),
                    user.getPersonalUrl()
            );

        } catch(Exception exception){
            throw new BaseException(BaseResponseStatus.DATABASE_ERROR);
        }
    }


    
    // 카카오 액세스 토큰 받기
    public String getKakaoAccessToken (String code) throws IOException {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //POST 요청을 위해 기본값이 false인 setDoOutput을 true로
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id="+ Secret.USER_OAUTH_KAKAO_REST_API_KEY); // TODO REST_API_KEY 입력
        sb.append("&redirect_uri="+Secret.KAKAO_REDIRECT_URI); // TODO 인가코드 받은 redirect_uri 입력
//        sb.append("&redirect_uri=http://localhost:9000/app/users/kakao"); // TODO 인가코드 받은 redirect_uri 입력
        sb.append("&code=" + code);
        bw.write(sb.toString());
        bw.flush();
        System.out.println("URI는:"+Secret.KAKAO_REDIRECT_URI);

        //결과 코드가 200이라면 성공
        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);

        //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        System.out.println("response body : " + result);
        ObjectMapper objectMapper = new ObjectMapper();

        KakaoAccessTokenRes kakaoAccessTokenRes = objectMapper.readValue(result, KakaoAccessTokenRes.class);
        System.out.println("액세스 토큰 : "+ kakaoAccessTokenRes.getAccess_token());
        access_Token = kakaoAccessTokenRes.getAccess_token();

        br.close();
        bw.close();

        return access_Token;
    }

    // 카카오 : 액세스토큰으로 카카오 유저정보 받기
    //https://kauth.kakao.com/oauth/authorize?client_id=333c74b180fcfd54b8e90b3a2232a8b2&redirect_uri=http://localhost:9000/app/users/kakao&response_type=code
    public KakaoUserRes getKakaoUser(String token) throws BaseException, IOException {

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        String kakaoId;
        //access_token을 이용하여 사용자 정보 조회
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

        //결과 코드가 200이라면 성공
        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);

        //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }
        System.out.println("response body : " + result);

        ObjectMapper objectMapper = new ObjectMapper();

        KakaoUserRes kakaoUserRes = objectMapper.readValue(result, KakaoUserRes.class);
        System.out.println("카카오 유저 Idx : "+ kakaoUserRes.getId());
        System.out.println("카카오 유저 프로필 이미지 : "+ kakaoUserRes.getProperties().getProfile_image());
        System.out.println("카카오 유저 연결시간 : "+ kakaoUserRes.getConnected_at());
        kakaoId = kakaoUserRes.getId().toString();

        br.close();

        return kakaoUserRes;
    }



}
