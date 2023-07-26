package kopo.poly.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

@Getter
@Setter
@Data
public class KakaoUserInfoDTO {

    private Long id;

    private String connected_at;

    private Properties properties;

    private KakaoAccount kakao_account;


    @Data
    public class properties {

        private String nickname;

        private String profile_image;

        private String thumbnail_image;

    }

    @Data
    public class KakaoAccount {


    }


}
