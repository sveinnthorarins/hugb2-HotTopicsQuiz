package is.hi.hbv601g.hottopicsquiz.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class User implements Parcelable {
    private Long id;
    private String username;
    private String name;
    private List<CompletedQuiz> completed;
    private boolean admin;

    public User(Long id, String username, String name, List<CompletedQuiz> completed, boolean admin) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.completed = completed;
        this.admin = admin;
    }

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        username = in.readString();
        name = in.readString();
        completed = in.createTypedArrayList(CompletedQuiz.CREATOR);
        admin = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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
        dest.writeString(username);
        dest.writeString(name);
        dest.writeTypedList(completed);
        dest.writeByte((byte) (admin ? 1 : 0));
    }

    public void addCompletedQuiz(CompletedQuiz completedQuiz) {
        completed.add(completedQuiz);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CompletedQuiz> getCompleted() {
        return completed;
    }

    public void setCompleted(List<CompletedQuiz> completed) {
        this.completed = completed;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
