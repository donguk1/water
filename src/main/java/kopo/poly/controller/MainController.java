package kopo.poly.controller;


import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class MainController {


    /*  메인 화면 이동용 = "/main"  */
    @GetMapping(value = "/main")
    public String main() throws Exception {

        log.info(this.getClass().getName() + ".controller 메인 화면으로 이동");

        return "/main";
    }

    /*  지도 화면 이동용 = "/map"  */
    @GetMapping(value = "/map")
    public String map() throws Exception {

        log.info(this.getClass().getName() + ".controller 지도 화면으로 이동");

        return "/map";
    }

    /*  로그인 전용 메인 화면 이동용 = "/watem"  */
    @GetMapping(value = "/watem")
    public String watem(HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".controller 로그인 전용 메인 화면으로 이동");

        String id = CmmUtil.nvl((String) session.getAttribute("SS_ID"));

        log.info("id : " + id);

        if (id == null) {
            return "/main";
        }

        return "/watem";
    }




}
