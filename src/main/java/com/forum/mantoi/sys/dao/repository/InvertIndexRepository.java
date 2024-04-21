package com.forum.mantoi.sys.dao.repository;

import com.forum.mantoi.sys.dao.entity.InvertIndex;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvertIndexRepository extends MongoRepository<InvertIndex, String> {

    InvertIndex findInvertIndexByKeyword(String keyWord);

}
