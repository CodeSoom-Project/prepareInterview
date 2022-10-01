package com.example.interviewPrep.quiz.service;

import com.example.interviewPrep.quiz.domain.Question;
import com.example.interviewPrep.quiz.domain.QuestionRepository;
import com.example.interviewPrep.quiz.dto.QuestionDTO;
import com.example.interviewPrep.quiz.exception.QuestionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public List<Question> getQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestion(Long id) {
        return findQuestion(id);
    }

    public Question createQuestion(QuestionDTO questionDTO){
        Question question = Question.builder()
                        .id(questionDTO.getId())
                        .title(questionDTO.getTitle())
                        .type(questionDTO.getType())
                        .build();
        questionRepository.save(question);
        return question;
    }

    public Question updateQuestion(Long id, QuestionDTO questionDTO){

        Question question = findQuestion(id);

        question.change(questionDTO.getTitle(), questionDTO.getType());

        return question;
    }

    public Question deleteQuestion(Long id){
        Question question = findQuestion(id);
        questionRepository.delete(question);
        return question;
    }

    public List<Question> findQuestionsByType(String type){
        return questionRepository.findByType(type);
    }


    public Optional<Page<QuestionDTO>> findByType(String type, Pageable pageable){
        Page<Question> questions = questionRepository.findByType(type, pageable); //문제 타입과 페이지 조건 값을 보내어 question 조회, 반환값 page

        return Optional.of(questions.map(q -> QuestionDTO.builder()   //question list 값들을 dto로 변경
                                                .id(q.getId())
                                                .type(q.getType())
                                                .title(q.getTitle())
                                                .build()));
    }


    public Question findQuestion(Long id){
        return questionRepository.findById(id).orElseThrow(() -> new QuestionNotFoundException(id));
    }


    public QuestionDTO domainToDTO(Question question){
        QuestionDTO questionDTO = QuestionDTO.builder()
                                .title(question.getTitle())
                                .type(question.getType())
                                .build();

        return questionDTO;
    }
}
