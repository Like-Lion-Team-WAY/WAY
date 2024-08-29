package like.lion.way.user.service;

import like.lion.way.user.domain.Role;

public interface RoleService {
    Role findByRoleName(String RoleName);
}
