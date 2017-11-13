package com.bignerdranch.android.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;
    private int mTotalQuestions = mQuestionBank.length;
    private int mAnswerCount = 0;
    private int mUserScore = 0;
    private int mFinalScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnswerCount = savedInstanceState.getInt("AnswerCount", 0);
            mUserScore = savedInstanceState.getInt("UserScore", 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswerCount++;
                checkAnswer(true);
                mQuestionBank[mCurrentIndex].setIsAnswered(true);
                incrementQuestion();
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnswerCount++;
                checkAnswer(false);
                mQuestionBank[mCurrentIndex].setIsAnswered(true);
                incrementQuestion();
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentIndex > 0) {
                    mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                    updateQuestion();
                }
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuestion();
            }
        });

        updateQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt("AnswerCount", mAnswerCount);
        savedInstanceState.putInt("UserScore", mUserScore);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void incrementQuestion() {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestion();
    }

    private void updateQuestion() {
        if(mQuestionBank[mCurrentIndex].getIsAnswered()) {
            disableAnswerButtons();
        } else {
            enableAnswerButtons();
        }
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        if (mTotalQuestions == mAnswerCount) {
            calculateScore();
            resetQuiz();
        }
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].getAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mUserScore++;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this,messageResId, Toast.LENGTH_SHORT).show();
    }

    private void calculateScore() {
        mFinalScore = Math.round(100 - ((float) (mTotalQuestions - mUserScore) / mTotalQuestions) * 100);
        Log.d("TAG", "Total Questions:" + mTotalQuestions);
        Log.d("TAG", "mUserScore: " + mUserScore);
        Log.d("TAG", "AnswerCount " + mAnswerCount);
        String scoreToastText = getString(R.string.score_toast);
        Toast.makeText(this, scoreToastText + " " + mFinalScore + "%", Toast.LENGTH_LONG).show();

        resetQuiz();
    }

    private void disableAnswerButtons() {
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
    }

    private void enableAnswerButtons() {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    //reset quiz values
    private void resetQuiz() {
        mCurrentIndex = 0;
        mAnswerCount = 0;
        mUserScore = 0;
        mFinalScore = 0;
        updateQuestion();

        //reset true false buttons
        for(int i = 0; i < mQuestionBank.length; i++) {
            mQuestionBank[i].setIsAnswered(false);
        }
    }


}
