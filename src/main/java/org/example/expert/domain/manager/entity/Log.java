package org.example.expert.domain.manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "log_table")
public class Log extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 매니저 등록 요청을 한 사용자의 아이디
    private Long userId;

    private Long todoId;

    public Log(Long userId, Long todoId) {
        this.userId = userId;
        this.todoId = todoId;
    }
}
