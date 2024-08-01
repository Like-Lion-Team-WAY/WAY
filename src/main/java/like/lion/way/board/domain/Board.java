package like.lion.way.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import like.lion.way.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "board_name", unique = true, nullable = false)
    private String name;

    @Column(name = "board_introduction")
    private String introduction;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean anonymousPermission;


    @Builder
    public Board(
            User user,
            String name,
            String introduction,
            boolean anonymousPermission) {

        this.user = user;
        this.name = name;
        this.introduction = introduction;
        this.anonymousPermission = anonymousPermission;

    }

    public void updateBoard(String name, String introduction, boolean anonymousPermission) {

        this.name = name;
        this.introduction = introduction;
        this.anonymousPermission = anonymousPermission;

    }

}
