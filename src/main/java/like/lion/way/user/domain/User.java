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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import like.lion.way.alarm.domain.AlarmSetting;
import like.lion.way.alarm.domain.ChatAlarm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column( name = "username")
    private String username;

    @Column( name = "nickname")
    private String nickname;

    @Column(nullable = false,name = "provider")
    private String provider;

    @Column(nullable = false, name = "email" , unique = true)
    private String email;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "user_image")
    private String userImage;

    @ManyToMany
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private AlarmSetting alarmSetting;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatAlarm chatAlarm;

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

    public void initializeAlarmSetting() {
        if (this.alarmSetting == null) {
            this.alarmSetting = new AlarmSetting(this);
        }
    }

    public void initializeChattingAlarm() {
        if (this.chatAlarm == null) {
            this.chatAlarm = new ChatAlarm(this);
        }
    }
}
