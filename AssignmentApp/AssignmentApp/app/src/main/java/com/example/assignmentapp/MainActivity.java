package com.example.assignmentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    int totalTrue;
    int totalQuestions;
    int presentQuestion;
    boolean completedStatus;
    QuestionGetter questionGetter;
    Button tButton;
    Button fButton;
    TextView questionTextView;
    ProgressBar progbar;
    AlertDialog.Builder builderResult;
    AlertDialog.Builder builderReset;
    Button changeLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));
        totalTrue = 0;
        completedStatus = false;
        tButton = findViewById(R.id.btnt);
        fButton = findViewById(R.id.btnf);
        questionTextView = findViewById(R.id.textViewF);
        progbar = findViewById(R.id.progressBar);
        questionGetter = new QuestionGetter();
        totalQuestions = questionGetter.getDatabaseSize();
        progbar.setMax(questionGetter.getDatabaseSize());
        builderResult = new AlertDialog.Builder(this);
        builderReset = new AlertDialog.Builder(this);
        changeLanguage= findViewById(R.id.language);
        changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageChangeDialog();
            }

            private void showLanguageChangeDialog() {
                final String [] languageItems={"Arabic","German","Sindhi","Urdu","English"};
                AlertDialog.Builder mBuilder=new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("Choose Language");
                mBuilder.setSingleChoiceItems(languageItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0)
                        {
                         setLocale("ar");
                         recreate();
                        }
                        if(which==1)
                        {
                            setLocale("de");
                            recreate();
                        }
                        if(which==2)
                        {
                            setLocale("sd");
                            recreate();
                        }
                        if(which==3)
                        {
                            setLocale("ur");
                            recreate();
                        }
                        if(which==4)
                        {
                            setLocale("en");
                            recreate();
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog mDialog=mBuilder.create();
                mDialog.show();
            }
        });
        updateQuestion();
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (completedStatus == true) {
                    Fragment fragment=new Fragment();
                   FragmentManager fragmentManager=getSupportFragmentManager();
                   fragmentManager.beginTransaction().add(R.id.questionFragment, fragment).commit();
                    builderReset.setMessage("\nReset quiz(Yes/No)")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    totalTrue = 0;
                                    presentQuestion = 0;
                                    completedStatus = false;
                                    progbar.setProgress(0);
                                    questionGetter.shuffle();
                                    updateQuestion();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            completedStatus = true;
                        }
                    });
                    AlertDialog alert = builderReset.create();
                    alert.setTitle("Quiz has Finished");
                    alert.show();
                }
                if (completedStatus == false) {
                    Fragment fragment=new Fragment();
                    FragmentManager fragmentManager=getSupportFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.questionFragment, fragment).commit();
                    if (questionGetter.checkAnswer(presentQuestion)){
                        Toast.makeText(getApplicationContext(),R.string.correctAnswer,Toast.LENGTH_SHORT).show();
                        totalTrue++;
                    }else {
                        Toast.makeText(getApplicationContext(),R.string.wrongAnswer,Toast.LENGTH_SHORT).show();
                    }
                    if (presentQuestion <= totalQuestions){
                        presentQuestion++;
                        if(totalQuestions == presentQuestion) {
                            completedStatus = true;
                            progbar.setProgress(presentQuestion);
                            result();
                        } else {
                            progbar.setProgress(presentQuestion);
                            updateQuestion();
                        }
                    }
                }
            }
        });
        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (completedStatus == true) {
                    builderReset.setMessage("\nReset Quiz(Yes/No)")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    totalTrue = 0;
                                    presentQuestion = 0;
                                    completedStatus = false;
                                    progbar.setProgress(0);
                                    questionGetter.shuffle();
                                    updateQuestion();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            completedStatus = true;
                        }
                    });
                    AlertDialog alert = builderReset.create();
                    alert.setTitle("Quiz has Completed!");
                    alert.show();
                }
                if (completedStatus == false) {
                    Fragment fragment=new Fragment();

                    if (!questionGetter.checkAnswer(presentQuestion)){
                        Toast.makeText(getApplicationContext(),R.string.correctAnswer,Toast.LENGTH_SHORT).show();
                        totalTrue++;
                    }else {
                        Toast.makeText(getApplicationContext(),R.string.wrongAnswer,Toast.LENGTH_SHORT).show();
                    }
                    if (presentQuestion <= totalQuestions){
                        presentQuestion++;
                        if(totalQuestions == presentQuestion) {
                            completedStatus = true;
                            progbar.setProgress(presentQuestion);
                            result();

                        } else {
                            progbar.setProgress(presentQuestion);
                            updateQuestion();
                        }
                    }
                }
            }

        });
    }
    private void setLocale(String lang) {
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration config =new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("Language",lang);
        editor.apply();
    }
    public void loadLocale()
    {
        SharedPreferences sp=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=sp.getString("Language","");
        setLocale(language);
    }
    public void updateQuestion() {
        questionTextView.setText(questionGetter.getNextQuestion(presentQuestion));
    }

    public void result (){
        builderResult.setMessage("Your Score is: " + totalTrue + " out of "+ questionGetter.getDatabaseSize())
                .setPositiveButton("Repeat", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        totalTrue = 0;
                        presentQuestion = 0;
                        completedStatus = false;
                        progbar.setProgress(0);
                        questionGetter.shuffle();
                        updateQuestion();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                completedStatus = true;
            }
        });
        AlertDialog alert = builderResult.create();
        alert.setTitle("Result");
        alert.show();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
        {
            Toast.makeText(this, "Portrait", Toast.LENGTH_SHORT).show();
        }
        else if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
        {
            Toast.makeText(this, "Landscape", Toast.LENGTH_SHORT).show();
        }
    }
}