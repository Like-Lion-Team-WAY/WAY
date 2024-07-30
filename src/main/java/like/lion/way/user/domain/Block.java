package like.lion.way.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "blocks",
        uniqueConstraints = @UniqueConstraint(columnNames={"blocker_user_id", "blocked_user_id"}, name="chk_block_unique_users"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long blockId;

    @ManyToOne
    @JoinColumn(name = "blocker_user_id")
    private User blockerUserId;

    @ManyToOne
    @JoinColumn(name = "blocked_user_id")
    private User blockedUserId;
}
