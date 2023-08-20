package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiverCCDTO {
    private int Num;            // 순서
    private String riverName;   // 강 이름
    private String address;     // 주소
    private String facility;    // 시설
    private String ccName;      // 문화관 이름
    private Double lat;         // 위도
    private Double lng;         // 경도
}
