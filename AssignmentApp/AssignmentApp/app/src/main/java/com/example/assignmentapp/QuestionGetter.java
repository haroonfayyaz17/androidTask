package com.example.assignmentapp;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionGetter {
    public ArrayList<QuestionClass> questions = new ArrayList<QuestionClass>();

    public QuestionGetter() {
        QuestionClass q1 = new QuestionClass(R.string.q1,true);
        QuestionClass q2 = new QuestionClass(R.string.q2,true);
        QuestionClass q3 = new QuestionClass(R.string.q3,false);
        QuestionClass q4 = new QuestionClass(R.string.q4,true);
        QuestionClass q5 = new QuestionClass(R.string.q5,false);
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        questions.add(q4);
        questions.add(q5);
    }
    public boolean checkAnswer(int questionIndex){
        return questions.get(questionIndex).answer;
    }
    public int getDatabaseSize(){
        return questions.size();
    }
    public int getNextQuestion(int index){
        return questions.get(index).question;
    }
    public void shuffle(){
        Collections.shuffle(questions);
    }
}
