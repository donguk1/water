package kopo.poly.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@Data
public class KakaoDTO {
    private String id;
    private String connected_at;
    private Properties properties;
    private KakaoAccount kakao_account;
    private Map<String, Object> additional_properties = new LinkedHashMap<>();

    @Data
    public static class Properties {
        private String nickname;
        private Map<String, Object> additional_properties = new LinkedHashMap<>();
    }

    @Data
    public static class KakaoAccount {
        private Boolean profile_nickname_needs_agreement;
        private Profile profile;
        private Boolean has_email;
        private Boolean email_needs_agreement;
        private Boolean is_email_valid;
        private Boolean is_email_verified;
        private String email;
        private Boolean has_birthday;
        private Boolean birthday_needs_agreement;
        private String birthday;
        private String birthday_type;
        private Boolean has_gender;
        private Boolean gender_needs_agreement;
        private String gender;
        private Map<String, Object> additional_properties = new LinkedHashMap<>();

        @Data
        public static class Profile {
            private String nickname;
            private Map<String, Object> additional_properties = new LinkedHashMap<>();
        }
    }
}