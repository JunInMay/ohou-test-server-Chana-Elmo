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
public class KakaoUserRes {
    private Long id;
    private String connected_at;
    private KakaoUserResProperties properties;
}
