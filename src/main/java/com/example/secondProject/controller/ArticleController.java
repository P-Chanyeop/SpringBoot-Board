package com.example.secondProject.controller;

import com.example.secondProject.Dto.ArticleDto;
import com.example.secondProject.entity.Article;
import com.example.secondProject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

@Slf4j
@Controller
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/articles")
    public String index(ArticleDto dto, Model model){
        List<Article> articleDtoList = articleRepository.findAll();
        model.addAttribute("articles", articleDtoList);
        return "article/index";
    }
    @GetMapping("/articles/new")
    public String ArticleForm(){
        return "article/new";
    }


    // create new Article
    @PostMapping("/articles/create")
    public String createArticle(ArticleDto dto, Model model) {
        if (dto != null) {
            // 1. 브라우저에서 DTO 받아오기
            log.info("DTO : " + dto.toString());

            // 2. DTO -> Entity
            Article article = dto.toEntity();
            log.info("Entity : " + article.toString());

            // 3. DB 에 저장 및 index로 리다이렉트
            Article saved = articleRepository.save(article);
            log.info("saved : " + saved);
            return "redirect:/articles";
        }
        return null;
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable Long id, Model model){
        // 1. id를 통해 DB에 데이터를 가져오기
        Article article = articleRepository.findById(id).orElse(null);

        // 2. model에 등록
        model.addAttribute("article", article);

        // 3. 뷰 페이지 리턴
        return "article/showArticle";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        Article articleEntity = articleRepository.findById(id).orElse(null);
        model.addAttribute("article", articleEntity);
        return "article/edit";
    }

    @PostMapping("/articles/update")
    public String update(ArticleDto dto){
        log.info("update DTO : " + dto.toString());
        // DTO -> Entity 변환
        Article articleEntity = dto.toEntity();
        log.info("articleEntity : " + articleEntity);
        // Entity를 DB에 저장
        // DB에서 기존 데이터를 가져온다
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        // target이 있다면 데이터를 저장
        if(target != null){
          Article saved = articleRepository.save(articleEntity);  // entity가 DB로 갱신된다.
             log.info(saved.toString());
        }
        // 수정 결과페이지로 리다이렉트
        return "redirect:/articles/" + articleEntity.getId();
    }

    @GetMapping("/articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){    // RedirectAttributes 사용하여 1회성 메세지 출력
        // DB에서 id를 통해 검색
        Article target = articleRepository.findById(id).orElse(null);
        log.info("Delete target : " + target);
        // target 이 존재한다면 target을 삭제
        if(target != null){
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg", "Delete Article #" + id + " successful!");
        }
        return "redirect:/articles";
    }
}
