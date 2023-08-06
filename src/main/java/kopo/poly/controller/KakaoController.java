package kopo.poly.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.TokenDTO;
import kopo.poly.dto.KakaoDTO;
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

@Slf4j
@Controller
@RequiredArgsConstructor
public class KakaoController {

    private final IUserService userService;

    @Value("${cos.key}")
    private String cosKey;

    /* 카카오 로그인 엑세스 토큰 받기 */
    @GetMapping("/auth/kakao/callback")
    public String kakaoCallback(String code, HttpSession session, ModelMap modelMap) throws Exception { //Data를 리턴해주는 컨트롤러 함수
        
        log.info(this.getClass().getName() + ".controller 카카오 회원가입 및 로그인 실행");

        String msg = "";
        String url = "";
        int res; // 회원가입 결과 /// 1 == 성공 /// 2 == 이미 가입

        // POST 방식으로 key=value 데이터를 요청 (카카오 쪽으로)

        // http 요청을 편하게 할 수 있다. Retrofit2, OkHttp, RestTemplate 라이브러리가 있다.
        RestTemplate rt = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "dd3d60b3f3cd9de73af963a2feb15b30");
        params.add("redirect_url", "http://localhost:11000/auth/kakao/callback");
        params.add("code", code);
        
        log.info("code : " + code);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        // Http 요청하기 - POST 방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
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

        log.info("카카오 엑세스 토큰 : " + tokenDTO.getAccess_token());

        // http 요청을 편하게 할 수 있다. Retrofit2, OkHttp, RestTemplate 라이브러리가 있다.
        RestTemplate rt2 = new RestTemplate();

        // HttpHeader 오브젝트 생성
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", "Bearer " + tokenDTO.getAccess_token());
        headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>>kakaoProfileRequest = new HttpEntity<>(headers2);

        // Http 요청하기 - POST방식으로 - 그리고 response 변수의 응답 받음.
        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoDTO kakaoDTO = null;
        try {
            kakaoDTO = objectMapper2.readValue(response2.getBody(), KakaoDTO.class);
        } catch (JsonMappingException e) {
            log.info(e.toString());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            log.info(e.toString());
            e.printStackTrace();
        }

        log.info(".카카오 아이디(번호) : " + kakaoDTO.getId());
        log.info(".카카오 이메일 : " + kakaoDTO.getKakao_account().getEmail());
        log.info(".카카오 생일 : " + kakaoDTO.getKakao_account().getBirthday());
        log.info(".카카오 성별 : " + kakaoDTO.getKakao_account().getGender());
        log.info(".카카오 닉네임 : " + kakaoDTO.getProperties().getNickname());

        log.info(".서버 아이디 : " + "kakao_" + kakaoDTO.getId());
        log.info(".서버 패스워드 : " + cosKey);

        // 서버 아이디가 DB에 있는지 확인합니다
        log.info("회원가입 전 계정 중복 확인");
        String serverId = "kakao_" + kakaoDTO.getId();
        UserDTO userDTO = userService.getUserById(serverId);

        if (userDTO == null) {
            // 서버 아이디가 DB에 없으므로 회원가입 후 로그인 처리를 수행합니다
            log.info("계정 없으므로 회원 가입 실행");
            UserDTO newUserDTO = new UserDTO();
            newUserDTO.setId(serverId);
            newUserDTO.setNick(kakaoDTO.getProperties().getNickname());
            newUserDTO.setEmail(kakaoDTO.getKakao_account().getEmail());
            newUserDTO.setPw(cosKey); // 새로운 사용자의 적절한 비밀번호로 대체해야 합니다.
            newUserDTO.setOauth("kakao");

            // 추가로 저장할 카카오 정보들도 DTO에 추가합니다
//            newUserDTO.setBirth(kakaoUserInfoDTO.getKakao_account().getBirthday()); /* 년도는 못 가져옴, 그래서 일단 주석처리 */
            newUserDTO.setGender(kakaoDTO.getKakao_account().getGender());

            // 새로운 사용자를 DB에 저장합니다
            res = userService.insertUser(newUserDTO);

            if (res == 1) {
                // 회원가입 성공
                log.info("회원가입 성공");
                session.setAttribute("SS_ID", serverId);
                session.setAttribute("SS_NICK", kakaoDTO.getProperties().getNickname());

                msg = "회원가입에 성공했습니다. \n로그인이 성공했습니다. \n" + kakaoDTO.getProperties().getNickname() + "님 환영합니다.";
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

        log.info(this.getClass().getName() + ".controller 카카오 회원가입 및 로그인 종료");

        return "/redirect";
    }


}

