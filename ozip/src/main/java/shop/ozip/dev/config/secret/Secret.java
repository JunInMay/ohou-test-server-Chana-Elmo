package shop.ozip.dev.config.secret;

// TODO: 해당 KEY 값들을 꼭 바꿔서 사용해주세요!
// TODO: .gitignore에 추가하는거 앚지 마세요!
public class Secret {
//    public static String JWT_SECRET_KEY = "UwKYibQQgkW7g-*k.ap9kje-wxBHb9wdXoBT4vnt4P3sJWt-Nu";
    public static String JWT_SECRET_KEY = "eyJhbGciqwjodjiowqdjliakczxm,.c~!@#!~#@!#@)_!$)_#@!)_$)_#@$*%@&!&!OiJIUzUxMiJ9.eyJSb2xlIjoi66y4MWZ4anpjbHZsa0AhQCEiLCJJc3N1ZXIiOiLspIDsnbTsoJUiLCJVc2VybmFtZSI6IuuniO2YuOuniO2YuCDrtoDro6jsvbHsvbEg44WL44WLIiwiZXhwIjoxNjQ2NjYxNDUxLCJpYXQiOjE2NDY2NTg0NTF9.miNNGxrCLD2BnjPtDQ9mB9qjSQLfllQhdgksrnrdlsdlfwnfdmsafhffktrptw123123b9vi8SW-rzcvUYLCb2Cc9G53Jm4Bu09e0v9TjwMQ4k@D6vIB1J2GCk12!@#SyqSzcnXR-uA";
//    public static String USER_INFO_PASSWORD_KEY = "o9pqYVC9-F8_.PEzEiw!L9F6.AYj9jcfVJ*_i.ifXYnyE68kix@Q2dL6rw*bV-rpdZYwcqZG-jPF-fw3CiJyKsfZ778ks-*jnZn";
    public static String USER_INFO_PASSWORD_KEY = "v9pcCJm4pXOYJZCO";
    public static String USER_OAUTH_KAKAO_REST_API_KEY = "84bbf58feb2327b33442efca30f14247";
    
    // OAUTH 인증 주소(변경여지있음)
    private static String KAKAO_CODE_LINK = "https://kauth.kakao.com/oauth/authorize?client_id=84bbf58feb2327b33442efca30f14247&redirect_uri=http://localhost:9000/app/user/kakao&response_type=code";
}
