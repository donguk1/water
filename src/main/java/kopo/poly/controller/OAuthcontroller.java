package kopo.poly.controller;


import kopo.poly.service.impl.OAuthService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/oauth")
@Slf4j
public class OAuthcontroller {

    /**
     * 카카오 callback
     * [GET] /oauth/kakao/callback
     */

//    kauth.kakao.com/oauth/authorize?client_id={a9cf415eff39557471ef2d61bda1b541}&redirect_uri={http://localhost:11000/oauth/kakao}&response_type=code

    private final OAuthService oAuthService;


    @RequestMapping(value = "/")
    public String index() {

        log.info(this.getClass().getName() + ".controller / 실행");

        return "index";

    }

    @GetMapping(value = "/login")
    public String login(@RequestParam("code") String code) {

        log.info(this.getClass().getName() + ".controller ");
        log.info("code : " + code);

        return "index";

    }



}
