package like.lion.way.user.service.serviceImpl;

import like.lion.way.user.domain.Role;
import like.lion.way.user.repository.RoleRepository;
import like.lion.way.user.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
