package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
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
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
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
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAnswerCount = savedInstanceState.getInt("AnswerCount", 0);
            mUserScore = savedInstanceState.getInt("UserScore", 0);
            mIsCheater = savedInstanceState.getBoolean("IsCheater", false);
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

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].getAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
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
                mIsCheater = false;
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            if(mIsCheater == true) {
                mQuestionBank[mCurrentIndex].setIsCheated(true);
            }
        }
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
        savedInstanceState.putBoolean("IsCheater", mIsCheater);
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
        boolean answerCheated = mQuestionBank[mCurrentIndex].getIsCheated();

        int messageResId = 0;

        //chech if user has cheated before allowing answer
        if (mIsCheater  || answerCheated ) {
            messageResId = R.string.judgement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mUserScore++;
            } else {
                messageResId = R.string.incorrect_toast;
            }
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
        mCheatButton.setEnabled(false);
    }

    private void enableAnswerButtons() {
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
        mCheatButton.setEnabled(true);
    }

    //reset quiz values
    private void resetQuiz() {
        mCurrentIndex = 0;
        mAnswerCount = 0;
        mUserScore = 0;
        mFinalScore = 0;
        mIsCheater = false;
        updateQuestion();

        //reset true false buttons and cheated status
        for(Question question:mQuestionBank) {
            question.setIsAnswered(false);
            question.setIsCheated(false);
        }
    }


}
