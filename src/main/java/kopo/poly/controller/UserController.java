package kopo.poly.controller;


import kopo.poly.dto.UserDTO;
import kopo.poly.service.IUserService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {

    private final IUserService userService;


    /*  회원가입 화면으로 이동 = "/user/signup"  */
    @GetMapping(value = "/user/signup")
    public String signup() {

        log.info(this.getClass().getName() + ".controller 회원가입 화면 실행");

        return "/user/signup";
    }

    /*  WATEM 회원가입 실행  */
    @PostMapping(value = "/user/insertUser")
    public String insertUser(HttpServletRequest request, ModelMap modelMap) throws Exception {

        log.info(this.getClass().getName() + ".controller 회원가입 실행");

        int res; // 회원가입 결과 /// 1 == 성공 /// 2 == 이미 가입

        String msg = "";
        String url = "";

        /*  회원가입 데이터 저장  */
        UserDTO pDTO = null;

        try {
            /*
             웹(회원정보 입력화면)에서 받는 정보를 String 변수에 저장 시작!
             무조건 웹으로 받은 정보는 DTO 에 저장하기 위해 임시로 String 변수에 저장.

             CmmUtil 함수 : 자바는 null 값이 들어가면 무조건 에러남.
             해당 함수는 들어가있는 문자열이 null 값이면 빈 문자열로 바꿔준다.

             getParameter 함수 : HTTP 요청에 웹페이지 폼을 통해 전송된 데이터 값을 가져오는 함수.
             */

            /*  데이터 선언 및 입력  */
            String id = CmmUtil.nvl(request.getParameter("id"));
            String nick = CmmUtil.nvl(request.getParameter("nick"));
            String pw = CmmUtil.nvl(request.getParameter("pw"));
            String email = CmmUtil.nvl(request.getParameter("email"));
            String pn = CmmUtil.nvl(request.getParameter("pn"));
            String uloc = CmmUtil.nvl(request.getParameter("uloc"));
            String birth = CmmUtil.nvl(request.getParameter("birth"));
            String gender = CmmUtil.nvl(request.getParameter("gender"));
            String oauth = "watem"; // 홈페이지 회원가입
            
            /*  데이터 확인  */
            log.info("id : " + id);
            log.info("nick : " + nick);
            log.info("pw : " + pw);
            log.info("email : " + email);
            log.info("pn : " + pn);
            log.info("uloc : " + uloc);
            log.info("birth : " + birth);
            log.info("gender : " + gender);
            log.info("oauth : " + oauth);

            /*  데이터 저장  */
            pDTO = new UserDTO();
            pDTO.setId(id);
            pDTO.setNick(nick);
            // pw는 절대로 복호화되지 않도록 해시 알고리즘으로 암호화
            pDTO.setPw(EncryptUtil.encHashSHA256(pw));
            pDTO.setEmail(email);
            pDTO.setPn(pn);
            pDTO.setUloc(uloc);
            pDTO.setBirth(birth);
            pDTO.setGender(gender);
            pDTO.setOauth(oauth);

            log.info(String.valueOf(pDTO));

            /*  회원가입  */
            res = userService.insertUser(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if (res == 1) {
                msg = "회원가입되었습니다.";
                url = "/user/login";

            } else if (res == 2) {
                msg = "이미 가입된 아이디입니다.";
                url = "/user/login";

            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";
                url = "/user/signup";

            }

        } catch (Exception e) {

            msg = "회원가입에 실패하였습니다.";
            url = "/user/signup";

            /*  실패 사유 확인용 로그  */
            log.info(e.toString());
            e.printStackTrace();    // Exception 발생 이유와 위치는 어디에서 발생했는지 전체적인 단계 출력

        } finally {

            modelMap.addAttribute("msg", msg);
            modelMap.addAttribute("url", url);

            log.info(this.getClass().getName() + ".controller 회원가입 종료");
        }

        return "/redirect";
    }

    /*  로그인 화면으로 이동 ="/login"  */
    @GetMapping(value = "/user/login")
    public String login() {

        log.info(this.getClass().getName() + ".controller 로그인 화면으로 이동");

        return "/user/login";
    }

    /*  로그인 처리 및 결과창 이동  */
    @PostMapping(value = "/user/loginProc")
    public String loginProc(HttpServletRequest request, ModelMap modelMap, HttpSession session) {

        log.info(this.getClass().getName() + ".controller 로그인 처리 결과 실행");

        String msg = "";
        String url = "";

        /*  회원가입 데이터 저장  */
        UserDTO pDTO = null;

        try {
            /* 화면으로 넘어온 데이터를 Input 태그속 name 값과 매칭시켜 꺼냄.
            잘못돼서 null 값이 들어오면 nvl 함수를 사용해서 비어있는 값을 반환해라 */
            /*  데이터 선언 및 입력  */
            String id = CmmUtil.nvl(request.getParameter("id"));
            String pw = CmmUtil.nvl(request.getParameter("pw"));

            /*  데이터 확인  */
            log.info("user_id : " + id);
            log.info("password : " + pw);

            /*  데이터 저장(비교용)  */
            pDTO = new UserDTO();
            pDTO.setId(id);
            // 비밀번호는 복호화되지 않도록 해시 알고리즘으로 암호화
            pDTO.setPw(EncryptUtil.encHashSHA256(pw));

            /*  로그인을 위한 UserService 호출  */
            UserDTO rDTO = userService.getLogin(pDTO);

            // 로그인 성공시 회원아이디 정보를  session 저장
            if (CmmUtil.nvl(rDTO.getId()).length() > 0) { // 로그인 성공시

                session.setAttribute("SS_ID", id);
                session.setAttribute("SS_NICK", CmmUtil.nvl(rDTO.getNick()));
                session.setAttribute("SS_PW", pw);

                msg = "로그인이 성공했습니다. \n" + rDTO.getNick() + "님 환영합니다.";
                url = "/main";

            } else { // 로그인 실패시

                msg = "로그인이 실패했습니다. \n";
                url = "/user/login";

            }

        } catch (Exception e) { // 로그인이 실패시 사용될 메시지

            msg = "시스템 문제로 로그인이 실패했습니다.";
            url = "/user/login";

            log.info(e.toString());
            e.printStackTrace();    // Exception 발생 이유와 위치는 어디에서 발생했는지 전체적인 단계 출력

        } finally { // 다음 페이지로 넘어갈 정보를 전달

            modelMap.addAttribute("msg", msg);
            modelMap.addAttribute("url", url);

            log.info(this.getClass().getName() + ".controller 로그인 처리 결과 종료");
        }

        return "/redirect";
    }

    /*  회원가입 전 아이디 중복체크(Ajax 사용)  */
    @ResponseBody
    @PostMapping(value = "/user/getUserIdExists")
    public UserDTO getUserIdExists(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".controller 아이디 중복체크 실행");

        /*  데이터 선언 및 입력  */
        String id = CmmUtil.nvl(request.getParameter("id"));

        /*  데이터 확인  */
        log.info("id : " + id);

        /*  데이터 저장  */
        UserDTO pDTO = new UserDTO();
        pDTO.setId(id);

        /*  중복아이디 확인  */
        UserDTO rDTO = Optional.ofNullable(userService.getUserIdExists(pDTO)).orElseGet(UserDTO::new);

        log.info(this.getClass().getName() + ".controller 아이디 중복체크 종료");

        return rDTO;
    }

    /*  회원가입 전 닉네임 중복체크(Ajax 사용)  */
    @ResponseBody
    @PostMapping(value = "/user/getUserNickExists")
    public UserDTO getUserNickExists(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".controller 닉네임 중복체크 실행");

        /*  데이터 선언 및 입력  */
        String nick = CmmUtil.nvl(request.getParameter("nick"));

        /*  데이터 확인  */
        log.info("nick : " + nick);

        /*  데이터 저장  */
        UserDTO pDTO = new UserDTO();
        pDTO.setNick(nick);

        /*  중복닉네임 확인  */
        UserDTO rDTO = Optional.ofNullable(userService.getUserNickExists(pDTO)).orElseGet(UserDTO::new);

        log.info(this.getClass().getName() + ".controller 닉네임 중복체크 종료");

        return rDTO;
    }

    /*  마이페이지로 이동 = "/user/mypage"  */
    @GetMapping(value = "/user/mypage")
    public String mypage(ModelMap modelMap, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".controller 마이페이지로 이동 실행");

        /*  id = P.K  */
        String id = CmmUtil.nvl((String) session.getAttribute("SS_ID"));

        if (id == "") {
            log.info("로그인 정보 없음");

            String msg = "로그인 정보가 없습니다.";
            String url = "/user/login";

            modelMap.addAttribute("msg", msg);
            modelMap.addAttribute("url", url);

            return "/redirect";
        }

        /*  값 전달은 반드시 pDTO 객체 이용 처리  */
        UserDTO pDTO = new UserDTO();
        pDTO.setId(id);

        /*  데이터 확인  */
        log.info("id : " + id);

        /*  마이페이지 가져오기  */
        UserDTO rDTO = Optional.ofNullable(userService.selectUser(pDTO)).orElseGet(UserDTO::new);

        /*  조회된 마이페이지 결과값 넣어주기  */
        modelMap.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".controller 마이페이지 이동 종료");

        return "/user/mypage";

    }

    /*  mypage 수정(회원정보 수정)  */
    @PostMapping(value = "/user/updateUser")
    public String updateUser(HttpSession session, ModelMap modelMap, HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".controller 회원정보 수정 실행");

        String msg = "";
        String url = "";

        try {

            /*  데이터 선언 및 입력  */
            String id = CmmUtil.nvl((String) session.getAttribute("SS_ID"));
            String nick = CmmUtil.nvl(request.getParameter("nick"));
            String email = CmmUtil.nvl(request.getParameter("email"));
            String pn = CmmUtil.nvl(request.getParameter("pn"));
            String uloc = CmmUtil.nvl(request.getParameter("uloc"));

            /*  데이터 확인  */
            log.info("id : " + id);
            log.info("nick : " + nick);
            log.info("ss_nick : " + CmmUtil.nvl((String) session.getAttribute("SS_NICK")));
            log.info("email : " + email);
            log.info("pn : " + pn);
            log.info("uloc : " + uloc);

            /*  데이터 저장  */
            UserDTO pDTO = new UserDTO();
            pDTO.setId(id);
            pDTO.setNick(nick);
            pDTO.setEmail(email);
            pDTO.setPn(pn);
            pDTO.setUloc(uloc);

            /*  개인정보 수정  */
            userService.updateUser(pDTO);

            msg = "수정되었습니다.";
            url = "/user/mypage";

        } catch (Exception e) {

            msg = "수정 실패하였습니다.";
            url = "/user/mypage";

            log.info(e.toString());
            e.printStackTrace();  // Exception 발생 이유와 위치는 어디에서 발생했는지 전체적인 단계 출력

        } finally {

            modelMap.addAttribute("msg", msg);
            modelMap.addAttribute("url", url);

            log.info(this.getClass().getName() + ".controller 회원정보 수정 종료");

        }

        return "/redirect";

    }

    /*  로그아웃  */
    @GetMapping(value = "/logout.do")
    public String logout(HttpServletRequest request, ModelMap modelMap) throws Exception {

        log.info(this.getClass().getName() + ".controller 로그아웃 실행");

        HttpSession session = request.getSession();

        String id = CmmUtil.nvl((String) session.getAttribute("SS_ID"));
        log.info("로그아웃 id : " + id);

        session.invalidate();;

        String msg = "로그아웃 하였습니다.";
        String url = "/main";

        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("url", url);

        return "/redirect";
    }

    /*  아이디 찾기 화면 이동  */
    @GetMapping(value = "/user/find")
    public String findId() {

        log.info(this.getClass().getName() + ".controller 아이디 찾기 화면으로 이동");

        return "/user/find";
    }

    /*  아이디 찾기  */
    @ResponseBody
    @PostMapping(value = "/user/findid")
    public UserDTO findId(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".controller 아이디 찾기 실행");

        // 데이터 입력
        String email = CmmUtil.nvl(request.getParameter("email"));
        String pn = CmmUtil.nvl(request.getParameter("pn"));

        // 데이터 확인
        log.info("email : " + email);
        log.info("pn : " + pn);

        // 데이터 저장
        UserDTO pDTO = new UserDTO();
        pDTO.setEmail(email);
        pDTO.setPn(pn);

        UserDTO rDTO = Optional.ofNullable(userService.findId(pDTO)).orElseGet(UserDTO::new);

        log.info("검색된 아이디 : " + rDTO.getId());

        log.info(this.getClass().getName() + ".controller 아이디 찾기 종료");

        return rDTO;
    }

    /*  임시 비번 설정 및 메일 보내기  */
    @ResponseBody
    @PostMapping(value = "/user/tmpPwEmail")
    public UserDTO tmpPwEmail(HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".controller 임시 비번 설정 및 메일 보내기 실행");

        // 데이터 입력
        String id = CmmUtil.nvl(request.getParameter("id"));
        String email = CmmUtil.nvl(request.getParameter("email"));
        String pn = CmmUtil.nvl(request.getParameter("pn"));

        // 데이터 확인
        log.info("id : " + id);
        log.info("email : " + email);
        log.info("pn : " + pn);

        // 데이터 저장
        UserDTO pDTO = new UserDTO();
        pDTO.setId(id);
        pDTO.setEmail(email);
        pDTO.setPn(pn);

        // 이메일 존재 여부 확인 존재하면 Y값 반환, 없을시 N값 반환
        UserDTO rDTO = Optional.ofNullable(userService.getEmailExists(pDTO)).orElseGet(UserDTO::new);

        // 이메일 없을시(N) 실행
        if (rDTO.getExists_yn().equals("N")) {
            return rDTO;
        }

        // 임시 비번 생성 후 메일 발송
        rDTO = userService.sendEmailPwd(pDTO);

        // 임시 비번 확인용
        log.info("tmpPwd : " + rDTO.getTmpPwd());

        // 데이터 저장(변경할 임시 비번)
        pDTO.setPw(EncryptUtil.encHashSHA256(rDTO.getTmpPwd()));

        // 변경된 임시 비번으로 변경
        userService.updatePw(pDTO);

        return rDTO;
    }

    /*  pw 변경  */
    @PostMapping(value = "/user/loginNewPw")
    public String  loginNewPw(HttpSession session, HttpServletRequest request, ModelMap modelMap) throws Exception {

        log.info(this.getClass().getName() + ".controller 패스워드 변경 실행");

        String msg = "";
        String url = "/user/mypage";

        try {

            // 데이터 입력
            String id = CmmUtil.nvl((String) session.getAttribute("SS_ID"));
            String pw = CmmUtil.nvl(request.getParameter("pw"));
            String newPw = CmmUtil.nvl(request.getParameter("pw1"));
            String newPw2 = CmmUtil.nvl(request.getParameter("pw2"));

            // 데이터 확인
            log.info("id : " + id);
            log.info("pw : " + pw);
            log.info("newPw : " + newPw);
            log.info("newPw2 : " + newPw2);

            // 새 비밀번호 일치 확인
            if (!newPw.equals(newPw2)) {

                log.info("새 비밀번호 불일치");

                msg = "작성하신 새 비밀번호가 일치하지 않습니다.\n다시 입력해주세요.";

                modelMap.addAttribute("msg", msg);
                modelMap.addAttribute("url", url);

                return "/redirect";
            }

            // 현재 사용중인 비번
            if (newPw.equals(pw)) {

                log.info("새 비밀번호가 쓰던 비밀번호와 일치");

                msg = "현재 사용중인 비밀번호입니다.";

                modelMap.addAttribute("msg", msg);
                modelMap.addAttribute("url", url);

                return "/redirect";
            }

            // 데이터 저장
            UserDTO pDTO = new UserDTO();
            pDTO.setId(id);
            pDTO.setPw(EncryptUtil.encHashSHA256(pw));
            pDTO.setNewPw(EncryptUtil.encHashSHA256(newPw));

            // 정보 수정
            userService.loginNewPw(pDTO);

            msg = "수정되었습니다.";

        } catch (Exception e) {

            log.info("에러캐치");

            msg = "수정 실패하였습니다.";

            log.info(e.toString());
            e.printStackTrace();

        } finally {

            modelMap.addAttribute("msg", msg);
            modelMap.addAttribute("url", url);
        }

        return "/redirect";
    }

    /*  회원 탈퇴 실행 로직  */
    @PostMapping(value = "/user/delete")
    public String userDelete(ModelMap modelMap, HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".controller 회원 탈퇴 실행");

        String id = CmmUtil.nvl((String) session.getAttribute("SS_ID"));
        String ssPw = CmmUtil.nvl((String) session.getAttribute("SS_PW"));
        String pw = CmmUtil.nvl(request.getParameter("userDeletePw"));

        log.info("탈퇴할 회원 ID : " + id);
        log.info("세션에 저장된 pw : " + ssPw);
        log.info("입력한 pw : " + pw);

        String msg = "";
        String url = "/main";

        try {

            if (!ssPw.equals(pw)) {

                log.info("비밀번호 불일치");

                msg = "작성하신 비밀번호가 일치하지 않습니다.\n다시 입력해주세요.";
                url = "/user/mypage";

                modelMap.addAttribute("msg", msg);
                modelMap.addAttribute("url", url);

                return "/redirect";
            }

            /*  회원 삭제용 비밀번호 데이터 저장  */
            UserDTO pDTO = new UserDTO();
            pDTO.setId(id);
            pDTO.setPw(EncryptUtil.encHashSHA256(pw));

            /*  회원 삭제용 비즈니스 로직 호출(쿼리문)  */
           userService.deleteUser(pDTO);

            msg = "탈퇴되었습니다.";

            session.invalidate();

        } catch (Exception e) {

            msg = "탈퇴에 실패하였습니다.";

            /*  실패 사유 확인용 로그  */
            log.info(e.toString());
            e.printStackTrace();    // Exception 발생 이유와 위치는 어디에서 발생했는지 전체적인 단계 출력

        } finally {

            modelMap.addAttribute("msg", msg);
            modelMap.addAttribute("url", url);

            log.info(this.getClass().getName() + ".controller 회원 탈퇴 종료");

        }

        return "/redirect";
    }

}
