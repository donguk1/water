package kopo.poly.controller;


import kopo.poly.service.impl.KakaoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RestController
@AllArgsConstructor
//@RequestMapping(value = "/oauth")
@Slf4j
@Controller
public class Kakaocontroller {

    /**
     * 카카오 callback
     * [GET] /oauth/kakao/callback
     */

//    kauth.kakao.com/oauth/authorize?client_id={a9cf415eff39557471ef2d61bda1b541}&redirect_uri={http://localhost:11000/oauth/kakao}&response_type=code

    private final KakaoService kakaoService;



    /*  다른계정으로 로그인(카카오) 클릭시 실행  */
    @RequestMapping(value = "/kakao/login")
    public String login(@RequestParam("code") String code, HttpSession session) {

        log.info(this.getClass().getName() + ".controller 카카오 로그인 실행");
        log.info("code : " + code);

        /*  accessToken 가져오기  */
        String access_Token = kakaoService.getAccessToken(code);
        String url = "/kakao/login?code=" + code;
        log.info("access_Token : " + access_Token);
        log.info("url : " + url);

        /*  유저 정보 가져오기  */
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(access_Token);
        log.info("userInfo : " + userInfo);


        if(userInfo.get("email") != null) {
            session.setAttribute("userId", userInfo.get("email"));
            session.setAttribute("access_Token", access_Token);
        }

        return "/index";

    }

//    @RequestMapping(value = "/")
//    public String index() {
//        return "index";
//    }
//
//    @RequestMapping(value = "/kakao/login")
//    public String login() {
//        return "/index";
//    }
}
