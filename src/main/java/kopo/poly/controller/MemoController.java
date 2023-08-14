package kopo.poly.controller;


import kopo.poly.dto.MapDTO;
import kopo.poly.dto.MemoDTO;
import kopo.poly.service.IMapService;
import kopo.poly.service.IMemoService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MemoController {

    /**
     * info 상세보기
     * list 목록
     * new  새로쓰기
     * up   수정하기
     */

    // 값 전달은 반드시 DTO 객체를 이용하여 처리


    // 서비스를 안에서 사용할 수 있게 하는 선언문
    private final IMemoService memoService;
    private final IMapService mapService;


    /*  메모 리스트 = "/memo/list"  */
    @GetMapping(value = "/memo/list")
    public String memoList(ModelMap modelMap, @RequestParam(defaultValue = "1") int page) throws Exception {
        log.info(this.getClass().getName() + ".controller 메모 목록 실행");

        // 페이지당 보여줄 아이템 개수 정의
        int itemsPerPage = 10;

        // 메모 리스트 전체 조회
        List<MemoDTO> rList = memoService.getMemoList();

        // 페이지네이션을 위해 전체 아이템 개수 구하기
        int totalItems = rList.size();

        // 전체 페이지 개수 계산
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        // 현재 페이지에 해당하는 아이템들만 선택하여 rList에 할당
        int fromIndex = (page - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);
        rList = rList.subList(fromIndex, toIndex);

        // 조회된 리스트 결과값 넣어주기
        modelMap.addAttribute("rList", rList);

        // 현재 페이지 정보를 넣어주기
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("totalPages", totalPages);

        log.info(this.getClass().getName() + ".페이지 번호 : " + page);
        log.info(this.getClass().getName() + ".controller 메모 목록 종료");

        return "/memo/list";
    }

    /*  메모 검색 = "/memo/search" 추후 수정 필요  */
    @GetMapping(value = "/memo/search")
    public String search(HttpServletRequest request , ModelMap modelMap,
                         @RequestParam(defaultValue = "1") int page,
                         @RequestParam(value = "type") String type,
                         @RequestParam(value = "keyword") String keyword) throws Exception {

        log.info(this.getClass().getName() + ".controller 메모 검색 실행");

        /*  데이터 확인  */
        log.info("search(type) : " + type);
        log.info("search(keyword) : " + keyword);

        /*  데이터 저장  */
        MemoDTO pDTO = new MemoDTO();
        pDTO.setType(type);
        pDTO.setKeyword(keyword);

        // 페이지당 보여줄 아이템 개수 정의
        int itemsPerPage = 10;

        // 메모 리스트  검색 조회
        List<MemoDTO> rList = memoService.searchMemoList(pDTO);

        // 페이지네이션을 위해 전체 아이템 개수 구하기
        int totalItems = rList.size();

        // 전체 페이지 개수 계산
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        // 현재 페이지에 해당하는 아이템들만 선택하여 rList에 할당
        int fromIndex = (page - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, totalItems);
        rList = rList.subList(fromIndex, toIndex);


//        // 메모 리스트가 없을시 실행
//        if (rList == null) {
//            rList = new ArrayList<>();
//        }

        // 객체 바인딩
        modelMap.addAttribute("rList", rList);
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("totalPages", totalPages);

        log.info(this.getClass().getName() + ".페이지 번호 : " + page);
        log.info(this.getClass().getName() + ".controller 메모 검색 종료");

        return "/memo/list";

    }

    /*  메모 작성 페이지 = "/memo/new"  */
    @GetMapping(value = "/memo/new")
    public String MemoInsert() {

        log.info(this.getClass().getName() + ".controller 메모 작성 실행");

        return "/memo/new";
    }

    /*  메모 등록  */
    @PostMapping(value = "/memo/memoInsert")
    public String memoInsert(HttpServletRequest request, ModelMap modelMap, HttpSession session) {

        log.info(this.getClass().getName() + ".controller 메모 등록 실행");

        String msg = "";            // 메시지(알림용)
        String url = "/memo/new";   // 이동할 경로

        try {
            /*  선언 및 입력  */
            String id = CmmUtil.nvl((String) session.getAttribute("SS_ID")); // 로그인된 ID 가져오기
            String nick = CmmUtil.nvl((String) session.getAttribute("SS_NICK")); // 로그인된 NICK 가져오기
            String title = CmmUtil.nvl(request.getParameter("title"));           // 제목
            String contents = CmmUtil.nvl(request.getParameter("contents"));     // 글 내용
            String mloc = CmmUtil.nvl(request.getParameter("mloc"));


            /*  데이터 확인  */
            log.info("id : " + id);
            log.info("nick : " + nick);
            log.info("title : " + title);
            log.info("contents : " + contents);
            log.info("mloc : " + mloc);

            /*  데이터 저장  */
            MemoDTO pDTO = new MemoDTO();
            pDTO.setId(id);
            pDTO.setNick(nick);
            pDTO.setTitle(title);
            pDTO.setContents(contents);
            pDTO.setMloc(mloc);

            /*  메모 등록용 비즈니스 로직 호출(쿼리문)  */
            memoService.insertMemoInfo(pDTO);

            // 작성 완료시 유저에게 보여줄 메시지 및 이동할 url
            msg = "등록되었습니다.";
            url = "/memo/list";

        } catch (Exception e) {

            msg = "등록 실패 하였습니다.";

            /*  실패 사유 확인용 로그  */
            log.info(e.toString());
            e.printStackTrace();    // Exception 발생 이유와 위치는 어디에서 발생했는지 전체적인 단계 출력

        } finally {

            // 객체 바인딩
            modelMap.addAttribute("msg", msg);
            modelMap.addAttribute("url", url);

            log.info(this.getClass().getName() + ".controller 메모 등록 종료");

        }

        return "/redirect";
    }

    /*  메모 상세보기 = "/memo/info"  */
    @GetMapping(value = "/memo/info")
    public String info(HttpServletRequest request, ModelMap modelMap) throws Exception {

        log.info(this.getClass().getName() + ".controller 메모 상세보기 실행");

        String num = CmmUtil.nvl(request.getParameter("num"));  // 글 번호

        log.info("메모 번호 " + num + "번 입니다.");

        /*  데이터 저장  */
        MemoDTO pDTO = new MemoDTO();
        pDTO.setNum(num);

        MapDTO mapDTO = new MapDTO();
        mapDTO.setNum(num);

        /*  상세정보 가져오기  */
        MemoDTO rDTO = Optional.ofNullable(memoService.getMemoInfo(pDTO)).orElseGet(MemoDTO::new);
        MapDTO aDTO = Optional.ofNullable(memoService.getLatLng(mapDTO)).orElseGet(MapDTO::new);


        /*  객체 바인딩  */
        modelMap.addAttribute("rDTO", rDTO);
        modelMap.addAttribute("aDTO", aDTO);

        log.info("rDTO : " + rDTO.getNum());
        log.info("rDTO : " + rDTO.getNick());
        log.info("rDTO : " + rDTO.getTitle());
        log.info("rDTO : " + rDTO.getDt());
        log.info("rDTO : " + rDTO.getContents());
        log.info("rDTO : " + rDTO.getId());

        log.info("aDTO : " + aDTO.getNum());
        log.info("aDTO : " + aDTO.getLat());
        log.info("aDTO : " + aDTO.getLng());
        log.info("aDTO : " + aDTO.getLevel());
        log.info("aDTO : " + aDTO.getMloc());


        log.info(this.getClass().getName() + ".controller 메모 상세보기 종료");

        return "/memo/info";
    }

    /*  메모 수정 페이지 = "memo/up"  */
    @GetMapping(value = "/memo/up")
    public String memoEditInfo(HttpServletRequest request, ModelMap modelMap) throws Exception {

        log.info(this.getClass().getName() + ".controller 메모 수정페이지 접근 실행");

        String num = CmmUtil.nvl(request.getParameter("num"));

        log.info("메모 번호 " + num + "번 입니다.");

        /*  데이터 저장  */
        MemoDTO pDTO = new MemoDTO();
        pDTO.setNum(num);

        /*  상세정보 가져오기  */
        MemoDTO rDTO = memoService.getMemoInfo(pDTO);

        if (rDTO == null) {
            rDTO = new MemoDTO();
        }

        /*  조회된 리스트 결과값 넣어주기(확인 필요)  */
        modelMap.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".controller 메모 수정페이지 접근 종료");

        return "/memo/up";
    }

    /*  메모 글 수정 실행 로직  */
    @PostMapping(value = "/memo/memoUpdate")
    public String up(HttpSession session, ModelMap modelMap, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".controller 메모 수정 실행");

        String msg = "";
        String url = "";

        try {

            /*  선언 및 입력  */
            String id = CmmUtil.nvl((String) session.getAttribute("SS_ID")); // 로그인된 ID 가져오기
            String nick = CmmUtil.nvl((String) session.getAttribute("SS_NICK")); // 로그인된 NICK 가져오기
            String title = CmmUtil.nvl(request.getParameter("title"));           // 제목
            String map = CmmUtil.nvl(request.getParameter("map"));               // 이미지 지도
            String contents = CmmUtil.nvl(request.getParameter("contents"));     // 글 내용
            String num = CmmUtil.nvl(request.getParameter("num"));               // 글 번호

            /*  데이터 확인  */
            log.info("id : " + id);
            log.info("nick : " + nick);
            log.info("title : " + title);
            log.info("map : " + map);
            log.info("contents : " + contents);
            log.info("num : " + num);

            /*  데이터 저장  */
            MemoDTO pDTO = new MemoDTO();
            pDTO.setId(id);
            pDTO.setNick(nick);
            pDTO.setTitle(title);
            pDTO.setMap(map);
            pDTO.setContents(contents);
            pDTO.setNum(num);

            /*  메모 등록용 비즈니스 로직 호출(쿼리문)  */
            memoService.updateMemoInfo(pDTO);

            /* 수정 완료시 사용될 메시지와 이동 경로  */
            msg = "수정 되었습니다.";
            url = "/memo/info?num="+num;

        } catch (Exception e) {

            msg = "수정 실패하였습니다.";


            /*  실패 사유 확인용 로그  */
            log.info(e.toString());
            e.printStackTrace();    // Exception 발생 이유와 위치는 어디에서 발생했는지 전체적인 단계 출력

        } finally {

            // 객체 바인딩
            modelMap.addAttribute("msg", msg);
            modelMap.addAttribute("url", url);

            log.info(this.getClass().getName() + ".controller 메모 수정 종료");
        }

        return "/redirect";
    }

    /*  메모 글 삭제 실행 로직  */
    @GetMapping(value = "/memo/memoDelete")
    public String memoDelete(ModelMap modelMap, HttpServletRequest request) {

        log.info(this.getClass().getName() + ".controller 메모 삭제 실행");

        String num = CmmUtil.nvl(request.getParameter("num"));

        log.info("메모 번호 " + num + "번 입니다.");

        String msg = "";
        String url = "/memo/list";

        try {

            /*  데이터 삭제용 글 번호 데이터 저장  */
            MemoDTO pDTO = new MemoDTO();
            pDTO.setNum(num);

            /*  메모 삭제용 비즈니스 로직 호출(쿼리문)  */
            memoService.deleteMemoInfo(pDTO);

            msg = "메모가 삭제되었습니다.";

        } catch (Exception e) {

            msg = "메모 삭제에 실패하였습니다.";

            /*  실패 사유 확인용 로그  */
            log.info(e.toString());
            e.printStackTrace();    // Exception 발생 이유와 위치는 어디에서 발생했는지 전체적인 단계 출력

        } finally {

            modelMap.addAttribute("msg", msg);
            modelMap.addAttribute("url", url);

            log.info(this.getClass().getName() + ".controller 메모 삭제 종료");

        }

        return "/redirect";
    }

    /**  map.html 페이지에서 작성 버튼 눌렀을시
     *   이미지 지도 가져오기
     *   지도 정보 가져오기(contents로 합쳐서?)
     *   필요 정보(시/도)
     *   데이터 융합(백분위(수질오염정보 + 호우 예상 => 물놀이 가기 좋은 등급)
     */
//    public String mapMemo



}
