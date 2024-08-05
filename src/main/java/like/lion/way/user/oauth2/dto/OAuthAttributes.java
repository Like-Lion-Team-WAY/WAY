package like.lion.way.user.oauth2.dto;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import like.lion.way.user.domain.Role;
import like.lion.way.user.domain.RoleType;
import like.lion.way.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OAuthAttributes {
    private Map<String , Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String provider;


    public static OAuthAttributes of(String registrationId , String userNameAttributeName , Map<String , Object> attributes){
        if("naver".equals(registrationId)){
            return ofNaver("id",attributes);
        }else if("kakao".equals(registrationId)){
            return ofKakao("id",attributes);
        }
        return ofGoogle(userNameAttributeName,attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String , Object> attributes){
        return OAuthAttributes.builder()
                .name(UUID.randomUUID().toString())
                .email((String)attributes.get("email"))
                .provider("Google")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName , Map<String , Object> attributes){
        Map<String,Object> response = (Map<String, Object>) attributes.get("response");
        System.out.println(response);
        return OAuthAttributes.builder()
                .name(UUID.randomUUID().toString())
                .email((String)response.get("email"))
                .provider("Naver")
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName ,Map<String , Object> attributes ){

        Map<String,Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String,Object> account = (Map<String, Object>) response.get("profile");

        return OAuthAttributes.builder()
                .name(UUID.randomUUID().toString())
                .email((String) response.get("email"))
                .provider("Kakao")
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

}
