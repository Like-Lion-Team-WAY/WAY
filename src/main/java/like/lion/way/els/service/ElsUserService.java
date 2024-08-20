package like.lion.way.els.service;


import java.util.List;
import like.lion.way.els.domain.ElsUser;

public interface ElsUserService {

    ElsUser saveOrUpdate(ElsUser elsUser);

    ElsUser findByUserId(Long userId);

    List<ElsUser> getAllUsers();

    List<ElsUser> searchUsersByUsername(String username);

    boolean deleteByUserId(String userId);
}
