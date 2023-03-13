package com.example.secondProject.repository;

import com.example.secondProject.entity.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long>{   // CrudRepository -> 엔티티, id타입

    @Override
    List<Article> findAll();
}
