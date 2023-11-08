package com.forum.mantoi.sys.repository;

import com.forum.mantoi.sys.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingRepository extends JpaRepository<Posting, Long> {

}
