package kopo.poly.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.NaverDTO;
import kopo.poly.dto.TokenDTO;
import kopo.poly.dto.UserDTO;
import kopo.poly.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class NaverController {

    private final IUserService userService;

    @Value("${cos.key}")
    private String cosKey;

    /* 네이버 로그인 엑세스 토큰 받기*/
    @GetMapping("/auth/naver/callback")
    public String naverCallback(String code, HttpSession session, ModelMap modelMap) throws Exception { //Data를 리턴해주는 컨트롤러 함수

        log.info(this.getClass().getName() + ".controller 네이버 회원가입 및 로그인 실행");

        String msg = "";
        String url = "";
        int res; // 회원가입 결과 /// 1 == 성공 /// 2 == 이미 가입

        // POST 방식으로 key=value 데이터를 요청 (네이버 쪽으로)

        // http 요청을 편하게 할 수 있다. Retrofit2, OkHttp, RestTemplate 라이브러리가 있다.
        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "twt6vN_j4GmLOQgE_RUJ");
        params.add("client_secret", "XDEHfUAMYl");
        params.add("redirect_uri", "http://localhost:11000/auth/naver/callback");
        params.add("code", code);

        log.info("code : " + code);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
                new HttpEntity<>(params, headers);

        // Http 요청하기 - POST 방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );

        // 객체에 담아볼 것이다. Gson, Json Simple, ObjectMapper 라이브러리가 있다.
        ObjectMapper objectMapper = new ObjectMapper();

        TokenDTO tokenDTO = null;

        try {
            tokenDTO = objectMapper.readValue(response.getBody(), TokenDTO.class);
        } catch (JsonMappingException e) {
            log.info(e.toString());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.info(e.toString());
            e.printStackTrace();
        }

        log.info("네이버 엑세스 토큰 : " + tokenDTO.getAccess_token());

        // http 요청을 편하게 할 수 있다. Retrofit2, OkHttp, RestTemplate 라이브러리가 있다.
        RestTemplate rt2 = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + tokenDTO.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>>naverProfileRequest = new HttpEntity<>(headers2);

        // Http 요청하기 - POST방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response2 = rt2.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                naverProfileRequest,
                String.class
        );

        ObjectMapper objectMapper2 = new ObjectMapper();
        NaverDTO naverDTO = null;
        try {
            naverDTO = objectMapper2.readValue(response2.getBody(), NaverDTO.class);
        } catch (JsonMappingException e) {
            log.info(e.toString());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.info(e.toString());
            e.printStackTrace();
        }

        log.info(".네이버 닉네임 : " + naverDTO.getResponse().getNickname());
        log.info(".네이버 아이디(문자) : " + naverDTO.getResponse().getId());
        log.info(".네이버 생일 : " + naverDTO.getResponse().getBirthyear() + "-" + naverDTO.getResponse().getBirthday());
        log.info(".네이버 이메일 : " + naverDTO.getResponse().getEmail());
        log.info(".네이버 폰번호 : " + naverDTO.getResponse().getMobile());
        log.info(".네이버 성별 : " + naverDTO.getResponse().getGender());

        log.info(".서버 아이디 : " + "naver_" + naverDTO.getResponse().getId());
        log.info(".서버 패스워드 : " + cosKey);

        // 서버 아이디가 DB에 있는지 확인합니다
        log.info("회원가입 전 계정 중복 확인");
        String serverId = "naver_" + naverDTO.getResponse().getId();
        UserDTO userDTO = userService.getUserById(serverId);
        
        String gender = naverDTO.getResponse().getGender();
        if (Objects.equals(gender, "F")) {
            gender = "female";
        } else if (Objects.equals(gender, "M")) {
            gender = "male";
        }

        if (userDTO == null) {
            // 서버 아이디가 DB에 없으므로 회원가입 후 로그인 처리를 수행합니다
            log.info("계정 없으므로 회원 가입 실행");
            UserDTO newUserDTO = new UserDTO();
            newUserDTO.setNick(naverDTO.getResponse().getNickname());
            newUserDTO.setId(serverId);
            newUserDTO.setPw(cosKey); // 새로운 사용자의 적절한 비밀번호로 대체해야 합니다.
            newUserDTO.setBirth(naverDTO.getResponse().getBirthyear() + "-" + naverDTO.getResponse().getBirthday());
            newUserDTO.setEmail(naverDTO.getResponse().getEmail());
            newUserDTO.setPn(naverDTO.getResponse().getMobile());
            newUserDTO.setGender(gender);
            newUserDTO.setOauth("naver");

            // 새로운 사용자를 DB에 저장합니다
            res = userService.insertUser(newUserDTO);

            if (res == 1) {
                // 회원가입 성공
                log.info("회원가입 성공");
                session.setAttribute("SS_ID", serverId);
                session.setAttribute("SS_NICK", naverDTO.getResponse().getNickname());

                msg = "회원가입에 성공했습니다. \n로그인이 성공했습니다. \n" + naverDTO.getResponse().getNickname() + "님 환영합니다.";
                url = "/main";
            } else {
                // 회원가입 실패
                log.info("회원가입 실패");
            }
        } else {
            // 서버 아이디가 DB에 이미 존재하므로 로그인 처리를 수행합니다
            log.info("계정 보유로 로그인 실행");

            session.setAttribute("SS_ID", serverId);
            session.setAttribute("SS_NICK", userDTO.getNick());

            msg = "로그인이 성공했습니다. \n" + userDTO.getNick() + "님 환영합니다.";
            url = "/main";
        }

        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("url", url);

        log.info(this.getClass().getName() + ".controller 네이버 회원가입 및 로그인 종료");

        return "/redirect";
    }


}