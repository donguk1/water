package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverDTO {

    private ResponseDTO response;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @Data
    public static class ResponseDTO {
        private String id;
        private String nickname;
        private String gender;
        private String email;
        private String mobile;
        private String mobile_e164;;
        private String birthday;
        private String birthyear;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}