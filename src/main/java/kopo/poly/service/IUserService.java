package kopo.poly.service;

import kopo.poly.dto.UserDTO;

public interface IUserService {

    // 회원가입(회원정보 등록)
    int insertUser(UserDTO pDTO) throws Exception;

    // 로그인 위한 id, pw 일치 확인
    UserDTO getLogin(UserDTO pDTO) throws Exception;

    // 회원가입 전 id 중복확인
    UserDTO getUserIdExists(UserDTO pDTO) throws Exception;

    // 회원가입 전 nick 중복확인
    UserDTO getUserNickExists(UserDTO pDTO) throws Exception;



    /* 여기서부턴 확인 필요 */

    // 회원정보 표시(마이페이지)
    UserDTO selectUser(UserDTO pDTO) throws Exception;

    // 회원정보 수정
    void updateUser(UserDTO pDTO) throws Exception;

    // pw 재설정
    UserDTO updatePw(UserDTO pDTO) throws Exception;


}
