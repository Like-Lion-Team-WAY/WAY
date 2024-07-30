package like.lion.way.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true, name = "username" , nullable = false)
    private String username;

    @Column(nullable = false , name = "nickname")
    private String nickname;

    @Column(nullable = false,name = "provider")
    private String provider;

    @Column(nullable = false,name = "provider_id")
    private String providerId;

    @Column(name = "created_at")
    private LocalDate createdAt;
}
