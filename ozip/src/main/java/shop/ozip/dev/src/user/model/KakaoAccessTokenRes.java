package shop.ozip.dev.src.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
// 필드명 바꾸지 말것!!
public class KakaoAccessTokenRes {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String scope;
    private long expires_in;
    private long refresh_token_expires_in;
}
