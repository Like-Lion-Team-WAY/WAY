package like.lion.way.user.oauth2.service;

import com.amazonaws.services.kms.model.DisabledException;
import like.lion.way.user.domain.Role;
import like.lion.way.user.domain.User;
import like.lion.way.user.oauth2.dto.OAuthAttributes;
import like.lion.way.user.service.RoleService;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserService userService;
    private final RoleService roleService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId , userNameAttributeName , oAuth2User.getAttributes());

        System.out.println(attributes.getEmail());
        User user = userService.saveOrUpdate(attributes);
        Role limitRole = roleService.findByRoleName("ROLE_LIMITED");
        for (Role role : user.getRoles()){
            if(role.getRoleName().equals(limitRole.getRoleName())){
                OAuth2Error error = new OAuth2Error("user_blocked", "User is blocked", null);
                throw new OAuth2AuthenticationException(error);
            }
        }
        return new DefaultOAuth2User(
                null,
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }


}
