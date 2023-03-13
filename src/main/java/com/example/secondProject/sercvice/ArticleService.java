package com.example.secondProject.sercvice;

import com.example.secondProject.Dto.ArticleDto;
import com.example.secondProject.entity.Article;
import com.example.secondProject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    public List<Article> index() {
        return articleRepository.findAll();

    }

    public Article show(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article create(ArticleDto dto) {
        Article article = dto.toEntity();
        if(article.getId() != null){
            return null;
        }
        return articleRepository.save(article);
    }

    public Article updated(Long id, ArticleDto dto) {
        // 수정용 엔티티 생성
        Article article = dto.toEntity();
        log.info(article.toString());

        // 대상 엔티티 조회
        Article target = articleRepository.findById(id).orElse(null);

        // 잘못된 요청 처리
        if(target == null || id != article.getId()){
            log.info("Bad Request! articleID = {}, requestID = {}", article.getId(), id);
            return null;
        }
        // 4 업데이트
        target.patch(article);
        return articleRepository.save(target);
    }

    public Article delete(Long id) {
        // 대상 엔티티 조회
        Article target = articleRepository.findById(id).orElse(null);

        // 대상 삭제
        if(target == null){
            log.info("BAD REQUEST!! NO EXIST TARGET ID #{} .", id);
            return null;
        }

        articleRepository.delete(target);
        return target;
    }
}
