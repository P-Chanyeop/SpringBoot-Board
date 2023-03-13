package com.example.secondProject.Dto;

import com.example.secondProject.entity.Answer;
import com.example.secondProject.entity.Question;
import com.example.secondProject.entity.SiteUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@ToString
public class QuestionDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 200)
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String content;;
    private LocalDateTime createDate;
    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
    @ManyToOne
    private SiteUser author;
    private LocalDateTime modifyDate;
    @ManyToMany
    Set<SiteUser> voter;    // set 자료형은 중복이 허용되지 않음.

    public Question ToEntity(){
        return new Question(id, subject, content, createDate, answerList, author, modifyDate, voter);
    }
}
