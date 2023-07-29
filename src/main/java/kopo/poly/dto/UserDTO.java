package kopo.poly.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String nick;    // 사용자명
    private String id;      // 아이디
    private String pw;      // 비밀번호
    private String birth;   // 생년월일
    private String email;   // 이메일
    private String uloc;    // 거주지역
    private String pn;      // 전화번호
    private String gender;  // 성별

    /*
     * 회원가입시 중복가입을 방지하기 위해 사용할 변수
     * DB를 조회해서 회원이 존재하면 Y값 반환
     * DB 테이블에 존재하지 않는 가상의 컬럼(ALIAS)
     */

    private String exists_yn;
}