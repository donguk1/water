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

    // 서버 아이디로 사용자 정보를 조회하는 메서드
    UserDTO getUserById(String id) throws Exception;

    // 아이디 찾기
    UserDTO findId(UserDTO pDTO) throws Exception;

    // 회원정보 표시(마이페이지)
    UserDTO selectUser(UserDTO pDTO) throws Exception;

    // 회원정보 수정
    void updateUser(UserDTO pDTO) throws Exception;

    // pw 재설정
    void updatePw(UserDTO pDTO) throws Exception;

    // 로그인 상태 pw 재설정
    void loginNewPw(UserDTO pDTO) throws Exception;

    // 임시 비번 메일로 보내기
    UserDTO sendEmailPwd(UserDTO pDTO) throws Exception;

    // 이메일 존재 여부 확인
    UserDTO getEmailExists(UserDTO pDTO) throws Exception;

    // 회원 탈퇴
    void deleteUser(UserDTO pDTO) throws Exception;

}
