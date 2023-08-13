package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MapDTO {

    private Double lat;
    private Double lng;
    private Integer level;


}
