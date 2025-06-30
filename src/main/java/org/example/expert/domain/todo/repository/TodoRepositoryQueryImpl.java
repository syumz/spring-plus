package org.example.expert.domain.todo.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class TodoRepositoryQueryImpl implements TodoRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(todo)
                        .leftJoin(todo.user, user).fetchJoin()
                        .where(todo.id.eq(todoId))
                        .fetchOne()
        );
    }

    // 제목, 생성일 범위 최신순, 닉네임
    @Override
    public Page<TodoSearchResponse> findByTitleAndCreatedAtAndNickname(Pageable pageable, String title, LocalDateTime start, LocalDateTime end, String nickname) {
        BooleanBuilder builder = new BooleanBuilder();

        if (title != null && !title.isBlank()) {
            builder.and(todo.title.containsIgnoreCase(title));
        }
        if (nickname != null && !nickname.isBlank()) {
            builder.and(user.nickname.containsIgnoreCase(nickname));
        }
        if (start != null && end != null) {
            builder.and(todo.createdAt.between(start, end));
        }

        List<TodoSearchResponse> result = queryFactory
                .select(Projections.constructor(TodoSearchResponse.class, todo.id, todo.title, manager.count(), comment.count()))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .leftJoin(todo.comments, comment)
                .where(builder)
                .groupBy(todo.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(todo.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(todo.count())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .leftJoin(todo.comments, comment)
                .where(builder)
                .fetchOne();
        return new PageImpl<>(result, pageable, total);
    }
}
