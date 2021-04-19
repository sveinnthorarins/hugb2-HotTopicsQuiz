package is.hi.hbv601g.hottopicsquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CompletedQuiz implements Parcelable {

    private Long id;
    private Quiz quiz;
    private int score;
    private boolean[] correctAnswers;

    public CompletedQuiz(Long id, Quiz quiz, int score, boolean[] correctAnswers) {
        this.id = id;
        this.quiz = quiz;
        this.score = score;
        this.correctAnswers = correctAnswers;
    }

    protected CompletedQuiz(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        quiz = in.readParcelable(Quiz.class.getClassLoader());
        score = in.readInt();
        correctAnswers = in.createBooleanArray();
    }

    public static final Creator<CompletedQuiz> CREATOR = new Creator<CompletedQuiz>() {
        @Override
        public CompletedQuiz createFromParcel(Parcel in) {
            return new CompletedQuiz(in);
        }

        @Override
        public CompletedQuiz[] newArray(int size) {
            return new CompletedQuiz[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeParcelable(quiz, flags);
        dest.writeInt(score);
        dest.writeBooleanArray(correctAnswers);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean[] getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(boolean[] correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

}
