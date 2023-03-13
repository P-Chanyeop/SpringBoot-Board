package com.example.secondProject;

import com.example.secondProject.entity.Answer;
import com.example.secondProject.entity.Question;
import com.example.secondProject.repository.AnswerRepository;
import com.example.secondProject.repository.QuestionRepository;
import com.example.secondProject.sercvice.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class SecondProjectApplicationTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    // question 객체 생성 테스트
    @Transactional
    @Test
    void testJpaQuestionSave(){
        Question q1 = new Question();
        q1.setSubject("what is SSB?");
        q1.setContent("teach me SSB");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);   // 첫번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("question about Spring Boot");
        q2.setContent("id will create auto?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);   // 두번째 질문 저장
    }

    // question 리스트 반환 테스트
    @Test
    void findAllTest(){
        List<Question> all = this.questionRepository.findAll();
        assertEquals(2, all.size());

        Question q = all.get(0);
        assertEquals("what is SSB?", q.getSubject());
    }

    // id를 통한 검색 테스트
    @Test
    void findByIdTest(){
        Optional<Question> q = this.questionRepository.findById(1);
        if(q.isPresent()){
            Question qq = q.get();
            assertEquals("what is SSB?", qq.getSubject());
        }
    }

    // 제목을 통한 검색 테스트
    @Test
    void findBySubject(){
        Question q = this.questionRepository.findBySubject("what is SSB").orElse(null);
        if(q != null){
            assertEquals(1, q.getId());
        }
    }

    // 제목, 내용을 통한 검색 테스트
    @Test
    void findBySubjectAndContent(){
        Question q = this.questionRepository.findBySubjectAndContent("what is SSB?", "teach me SSB");
        assertEquals(1, q.getId());
    }

    // 특정 문자열 포함 테스트
    @Test
    void findBySubjectLike(){
        List<Question> qList = this.questionRepository.findBySubjectLike("%SSB%");
        Question q = qList.get(0);
        assertEquals("what is SSB?", q.getSubject());
    }

    // 데이터 수정 테스트
    @Transactional
    @Test
    void updateTest(){
        Optional<Question> q = this.questionRepository.findById(1);
        assertTrue(q.isPresent());
        Question newQ = q.get();
        newQ.setSubject("updated subject");
        this.questionRepository.save(newQ);
    }

    // 데이터 삭제 테스트
    @Transactional
    @Test
    void deleteTest(){
        Optional<Question> q = this.questionRepository.findById(1);
        assertTrue(q.isPresent());
        Question newQ = q.get();
        this.questionRepository.delete(newQ);
        assertEquals(1,this.questionRepository.count());
    }

    // Answer 데이터 생성 및 저장 테스트
    @Test
    void newAnswer(){
        Optional<Question> q = questionRepository.findById(2);
        assertTrue(q.isPresent());
        Question newQ = q.get();

        Answer answer = new Answer();
        answer.setContent("yes");
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(newQ);
        this.answerRepository.save(answer);
    }

    // Answer 조회 테스트
    @Test
    void findAnswerById(){
        Optional<Answer> answer = this.answerRepository.findById(1);
        assertTrue(answer.isPresent());
        Answer newAnswer = answer.get();

        assertEquals(2, newAnswer.getQuestion().getId());
    }

    // 답변에 연결된 질문찾기, 질문에 달린 답변 찾기
    // 첫째줄 findById 메소드를 실행하면 DB 세션이 종료됨 => 후에 나오는 메소드들을 실행하지 못함.
    @Test
    @Transactional
    void findQuestionOrAnswer(){
        Optional<Question> q = questionRepository.findById(2);
        assertTrue(q.isPresent());
        Question newQ = q.get();

        List<Answer> answerList = newQ.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("yes", answerList.get(0).getContent());
    }

    // 대량 더미 데이터 생성
    @Test
    void testCreateData(){
        for(int i = 0; i < 300; i++){
            String subject = String.format("더미 데이터입니다 : [%03d]", i);
            String content = "내용무";
            this.questionService.create(subject, content, null);
        }
    }
}