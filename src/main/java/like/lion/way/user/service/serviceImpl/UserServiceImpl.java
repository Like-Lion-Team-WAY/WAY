package like.lion.way.user.service.serviceImpl;

import java.time.LocalDate;
import like.lion.way.user.domain.User;
import like.lion.way.user.oauth2.dto.OAuthAttributes;
import like.lion.way.user.repository.UserRepository;
import like.lion.way.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findByUserId(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail()).orElse(new User());

        user.setUsername(attributes.getName());
        user.setProvider(attributes.getProvider());
        user.setProviderId(attributes.getProviderId());
        user.setCreatedAt(LocalDate.now());
        user.setEmail(attributes.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User findByProviderId(String providerId) {
        return userRepository.findByProviderId(providerId);
    }
}
