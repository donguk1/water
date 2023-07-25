package kopo.poly.service.impl;

import kopo.poly.dto.UserDTO;
import kopo.poly.persistance.mapper.IUserMapper;
import kopo.poly.service.IUserService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements IUserService {


    // User 관련 SQL 사용하기 위한 Mapper 가져오기
    private final IUserMapper userMapper;

//    // 메일 서비스 사용시 사용할 메일관련 자바 객체 가져오기
//    현재 없음 메일 확인 안할에정
//    private final IMailService mailService;

    /*  회원가입(회원정보 등록)  */
    @Override
    public int insertUser(UserDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".회원가입 시작");

        // 성공 : 1 / 기타 에러 : 0
        int res = 0;

        // 회원가입 mapper 호출
        res = userMapper.insertUser(pDTO);

        log.info(this.getClass().getName() + ".회원가입 종료");

        return res;
    }


    /*  로그인 위한 id, pw 일치 확인  */
    @Override
    public UserDTO getLogin(UserDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".로그인 시작");

        // 로그인 위해 id, pw 일치 여부 확인위한 mapper 호출
        /* rDTO 에 null 값이 들어오면 orElseGet 실행
           -> null 대신  UserDTO 객체를 강제로 메모리에 값 올리기         */
        UserDTO rDTO = Optional.ofNullable(userMapper.getLogin(pDTO)).orElseGet(UserDTO::new);

        /* DTO 의 변수에 값이 있는지 확인하기 처리속도 측면에서 가장 좋은 방법 = 변수의 길이 가져오기
           따라서 .length() 함수를 통해 회원아이디의 글자수를 가져와 0보다 크면
           글자가 존재하는 것이기 때문에 값이 존재한다는걸 알 수 있음.*/
        if (CmmUtil.nvl(rDTO.getId()).length() > 0 ) {
            log.info("로그인 성공");
        }

        log.info(this.getClass().getName() + ".로그인 종료");

        return rDTO;
    }


    /*  회원가입 전 id 중복확인  */
    @Override
    public UserDTO getUserIdExists(UserDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".id 중복검사 시작");

        // getUserIdExists = id 중복 검사
        // 쿼리문에서 count() 사용하기에 반드시 조회결과 존재
        UserDTO rDTO = userMapper.getUserIdExists(pDTO);

        log.info(this.getClass().getName() + ".id 중복검사 종료");

        return rDTO;
    }


    /*  회원가입 전 email 중복확인  */
    @Override
    public UserDTO getEmailExists(UserDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".email 중복검사 시작");

        // getEmailExists email 중복 검사
        // 쿼리문에서 count() 사용하기에 반드시 조회결과 존재
        UserDTO rDTO = userMapper.getEmailExists(pDTO);

        log.info(this.getClass().getName() + ".email 중복검사 종료");

        return rDTO;
    }


    /*  여기서부턴 확인 필요  */
    /*  회원정보 표시(마이페이지)  */
    @Override
    public UserDTO selectUser(UserDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".회원정보 표시 시작");

        UserDTO rDTO = userMapper.selectUser(pDTO);

        log.info(this.getClass().getName() + ".회원정보 표시 종료");

        return rDTO;
    }


    /*  회원정보 수정  */
    @Override
    public UserDTO updateUser(UserDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".회원정보 수정 시작");

        UserDTO rDTO = userMapper.updateUser(pDTO);

        log.info(this.getClass().getName() + ".회원정보 수정 종료");

        return rDTO;
    }


    /*  pw 재설정  */
    @Override
    public UserDTO updatePw(UserDTO pDTO) throws Exception {
        return null;
    }
}
