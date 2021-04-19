package is.hi.hbv601g.hottopicsquiz.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.List;

import is.hi.hbv601g.hottopicsquiz.R;
import is.hi.hbv601g.hottopicsquiz.model.CompletedQuiz;

// Custom ListView adapter for an ArrayList of CompletedQuiz objects
// Lets us keep the array of quizzes (as opposed to using an array of strings)
// but correctly displays each quiz's name in the view

public class CompletedQuizAdapter extends ArrayAdapter<CompletedQuiz> {

    private final int layoutResourceId;

    public CompletedQuizAdapter(@NonNull Context context, int resource, @NonNull List<CompletedQuiz> objects) {
        super(context, resource, objects);
        layoutResourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CompletedQuiz quiz = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(layoutResourceId, parent, false);
        }

        AppCompatTextView title = convertView.findViewById(R.id.quizmenu_listview_itemtitle);
        title.setText(quiz.getQuiz().getName());

        return convertView;
    }
}
