package com.bignerdranch.android.geoquiz;

/**
 * Created by adam on 10/28/17.
 */

public class Question {

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mIsAnswered;
    private boolean mIsCheated;

    public Question(int textResId, boolean answerTrue) {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mIsAnswered = false;
        mIsCheated = false;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean getAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public boolean getIsAnswered() { return mIsAnswered; }

    public void setIsAnswered(boolean isAnswered) { mIsAnswered = isAnswered; }

    public boolean getIsCheated() { return mIsCheated; }

    public void setIsCheated(boolean isCheated) { mIsCheated = isCheated; }
}
