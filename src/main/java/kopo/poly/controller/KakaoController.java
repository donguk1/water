package kopo.poly.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.oAuth.KakaoOAuth;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class KakaoController {

//    private final KakaoOAuth kakaoOAuth;

    @GetMapping("/kakao")
    public void getKakaoOAuthUrl(HttpServletResponse response) throws IOException {

//        response.sendRedirect(kakaoOAuth.responseUrl());

    }





//    /*  카카오 로그인 api 서비스  */
//    @GetMapping(value = "/auth/kakao/callback")
//    public String kakaoCallback(String code) {
//
//        /*  Post 방식으로 Key=Value 데이터를 요청(카카오)  */
//        RestTemplate rt = new RestTemplate();
//
//        /*  HttpHeader 오브젝트 생성  */
//        HttpHeaders headers =new HttpHeaders();
//        headers.add("Authorization", "Bearer" + oAuthToken.getAccess_token());
//        headers.add("Content-type", "application/x-www-form-urlencoded:charest=utf-8");
//
//        /*  HttpHeader 와 HttpBody 를 하나의 오브젝트에 담기  */
//        HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(headers);
//
//        /*  HttpBody 오브젝트 생성  */
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client", "a9cf415eff39557471ef2d61bda1b541");
//        params.add("ridirect_uri", "http://localhost:11000/auth/kakao/callback");
//        params.add("code", code);
//
//        /*  Http 요청하기 - Post 방식으로 - 그리고 response 변수의 응답 받음  */
//        ResponseEntity<String> response = rt.exchange("http://kapi.kakao.com/v2/user/me", //토큰 발급 요청 주소
//                HttpMethod.POST,    // 요청 메서드
//                kakaoRequest,       // 데이터
//                String.class);      // 응답 받을 타입
//
//        /*  Gson, Json Simple, Obejeck  */
//        ObjectMapper objectMapper =new ObjectMapper();
//        OAuhToken oAuhToken = nulll;
//
//        try {
//
//            oAuhToken = objectMapper.readValues(response.getBody().OAuthToken.class)
//
//        } catch (JsonMappingException e) {
//
//            e.printStackTrace();
//
//        } catch (JsonProcessingException e) {
//
//            e.printStackTrace();
//
//        }
//
//    }


}
