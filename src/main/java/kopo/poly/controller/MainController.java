package kopo.poly.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
