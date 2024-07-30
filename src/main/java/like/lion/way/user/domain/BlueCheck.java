package like.lion.way.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bluecheck_application")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlueCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bluecheck_application_id")
    private Long blueCheckId;

    @Column(name = "bluecheck_application_date")
    private LocalDateTime blueCheckDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
