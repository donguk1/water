package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapDTO {

    private Double lat;     // 위도
    private Double lng;     // 경도
    private Integer level;  // 스케일
    private String mloc;

    private String num;

//    private Double markerLat;
//    private Double markerLng;
}
