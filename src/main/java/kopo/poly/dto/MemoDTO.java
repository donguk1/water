package kopo.poly.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoDTO {


    private String num;         // 게시번호
    private String id;          // 작성자 (아이디)
    private String nick;        // 작성자 (닉네임)
    private String title;       // 글 제목
    private String dt;        // 작성일
    private String mloc;        // 지도상 장소
    private String contents;    // 글 내용
    private String map;         // 이미지 지도
    private String type;        // 검색 타입(title, mloc, nick)
    private String keyword;     // 검색 내용
//    private String[] typeArr;   //
    private Double lat;     // 위도
    private Double lng;     // 경도
    private Integer level;
}
