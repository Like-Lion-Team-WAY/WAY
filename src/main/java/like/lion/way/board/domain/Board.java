package like.lion.way.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

//    @OneToOne
//    private User user;

    @Column(name = "board_name", unique = true, nullable = false)
    private String name;

    @Column(name = "board_introduction")
    private String introduction;

    @Builder
    public Board(String name, String introduction) {

        this.name = name;
        this.introduction = introduction;

    }

}
