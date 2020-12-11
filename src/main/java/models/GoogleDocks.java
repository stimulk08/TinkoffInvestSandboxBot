package models;

import models.quiz.Question;
import models.quiz.Quiz;

import java.util.ArrayList;
import java.util.List;

public class GoogleDocks {
    public static Quiz getQuizByNumber(Integer number){
        Question question1 = new Question("Some question1", List.of("1", "2","3","4"),"1");
        Question question2 = new Question("Some question2", List.of("1", "2","3","4"),"2");
        return new Quiz(number, List.of(question1, question2));
    }

    public static void uploadQuizResult(Long chatId, Integer testNumber, List<Boolean> userAnswers){

    }

    public static List<Boolean> getUserCompletedTests(Long chatId){
        ArrayList<Boolean> list = new ArrayList<>();
        list.add(false);
        return list;
    }
}
