package is.hi.hbv601g.hottopicsquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Question implements Parcelable {

    private Long id;
    private String text;
    private String infoUrl;
    private List<String> answers;
    private boolean[] correctAnswers;

    public Question(Long id, String text, List<String> answers, boolean[] correctAnswers, String infoUrl) {
        this.id = id;
        this.text = text;
        this.answers = answers;
        this.correctAnswers = correctAnswers;
        this.infoUrl = infoUrl;
    }

    protected Question(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        text = in.readString();
        infoUrl = in.readString();
        answers = in.createStringArrayList();
        correctAnswers = in.createBooleanArray();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
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
        dest.writeString(text);
        dest.writeString(infoUrl);
        dest.writeStringList(answers);
        dest.writeBooleanArray(correctAnswers);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public boolean[] getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(boolean[] correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

}
