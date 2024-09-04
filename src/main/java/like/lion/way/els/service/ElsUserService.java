package like.lion.way.els.service;


import java.util.List;
import like.lion.way.els.domain.ElsUser;

public interface ElsUserService {

    ElsUser saveOrUpdate(ElsUser elsUser);

    ElsUser findByUserId(Long userId);

    List<ElsUser> getAllUsers();

    boolean deleteByUserId(String userId);

    List<ElsUser> searchUsersByInterest(String interest);
}
