package like.lion.way.user.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import like.lion.way.feed.domain.Like;
import like.lion.way.feed.domain.Post;
import like.lion.way.feed.domain.PostComment;
import like.lion.way.feed.domain.QuestionBox;
import like.lion.way.feed.domain.Question;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column( name = "username" , nullable = false)
    private String username;

    @Column( name = "nickname")
    private String nickname;

    @Column(nullable = false,name = "provider")
    private String provider;


    @Column(nullable = false, name = "email" , unique = true)
    private String email;

    @Column(name = "created_at")
    private LocalDate createdAt;


    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
            name="user_interest",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private Set<Interest> interests;

    public String getNickname(boolean check) {
        if(check){
            return "익명";
        }else{
            return nickname;
        }
    }
    public User update(String name , String provider){
        this.username = name;
        this.provider = provider;
        return this;
    }
}
