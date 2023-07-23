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
}
