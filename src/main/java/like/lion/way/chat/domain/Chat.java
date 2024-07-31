package like.lion.way.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import like.lion.way.user.domain.User;

@Entity
@Table(name="chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name="user_id2")
    private User user2;

//    @OneToOne
//    @JoinColumn(name="question_id")
//    private Question question;

    @Column(name="chat_nickname_open")
    private boolean nicknameOpen1;

    @Column(name="chat_nickname_open2")
    private boolean nicknameOpen2;

    @Column(name="chat_user_active")
    private boolean userActive1;

    @Column(name="chat_user_active2")
    private boolean userActive2;

    @Column(name="chat_name")
    private String name;

    @Column(name="chat_created_at")
    private LocalDateTime createdAt;

}
