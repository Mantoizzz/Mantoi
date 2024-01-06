package com.forum.mantoi.sys.services;


import com.forum.mantoi.sys.entity.Comment;
import com.forum.mantoi.sys.entity.User;
import com.forum.mantoi.sys.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService implements PublishService<Comment> {

    private final CommentRepository commentRepository;

    @Override
    public Comment publish(Comment object) {
        return null;
    }

    @Override
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        commentRepository.delete(comment);
    }

    @Override
    public Comment modify(User author, Comment object, Object request) {
        return null;
    }
}
