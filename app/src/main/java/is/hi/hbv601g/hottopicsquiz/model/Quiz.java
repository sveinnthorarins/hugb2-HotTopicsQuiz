package is.hi.hbv601g.hottopicsquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class
Quiz implements Parcelable {

    private Long id;
    private List<Question> questions;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Quiz(Long id, List<Question> questions, String name, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.questions = questions;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    protected Quiz(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        questions = in.createTypedArrayList(Question.CREATOR);
        name = in.readString();
        startDate = LocalDateTime.parse(in.readString());
        endDate = LocalDateTime.parse(in.readString());
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
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
        dest.writeTypedList(questions);
        dest.writeString(name);
        dest.writeString(startDate.toString());
        dest.writeString(endDate.toString());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

}
