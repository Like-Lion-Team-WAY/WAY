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

    /**
     * ROLENAME으로 ROLE 찾기
     * @param roleName 권한이름
     */
    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
