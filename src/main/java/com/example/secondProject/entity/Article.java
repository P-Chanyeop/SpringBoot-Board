package com.example.secondProject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@AllArgsConstructor
@Getter
@Entity
@ToString
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title",length = 50)
    private String title;
    @Column(name = "content", length = 2048)
    private String content;

    public void patch(Article article) {
        if(article.getTitle() != null){
            this.title = article.title;
        }
        if(article.getContent() != null){
            this.content = article.content;
        }
    }
}
