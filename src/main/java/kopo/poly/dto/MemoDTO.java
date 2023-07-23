package kopo.poly.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoDTO {

    private String num;         // 게시번호
    private String nick;        // 작성자(사용자명)
    private String title;       // 글 제목
    private String date;        // 작성일
    private String mloc;        // 지도상 장소
    private String contents;    // 글 내용
    private String map;         // 이미지 지도
}
