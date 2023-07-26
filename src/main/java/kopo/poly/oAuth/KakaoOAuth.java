package kopo.poly.oAuth;

import org.springframework.beans.factory.annotation.Value;

public class KakaoOAuth {


    @Value("${app.kakao.restApiKey")
    private String resApiKey;

    @Value("${app.kakao.redirectUrl")
    private String kakaoRedirectUrl;

    private final String KAKAO_TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";



    public String responseURL() {

        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" +
                resApiKey + "&redirect_uri=" +
                kakaoRedirectUrl + "&response_type=code";

        return kakaoLoginUrl;

    }


}
