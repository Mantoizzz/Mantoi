package com.forum.mantoi.sys.repository;

import com.forum.mantoi.sys.entity.Word;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author DELL
 */
@Repository
public interface WordRepository extends CrudRepository<Word, Long> {

    Optional<Word> findByContent(String content);
}
