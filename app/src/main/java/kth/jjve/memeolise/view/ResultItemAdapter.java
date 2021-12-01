package kth.jjve.memeolise.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import javax.xml.transform.Result;

import kth.jjve.memeolise.R;

/*
Class that adapts the made weather items to the UI.
 */
public class ResultItemAdapter extends RecyclerView.Adapter<ResultItemAdapter.ResultItemViewHolder> {
    private ArrayList<ResultItem> mResultItemsList;

    public static class ResultItemViewHolder extends RecyclerView.ViewHolder{
        public TextView mName;
        public TextView mScore;
        public TextView mMaxScore;

        public ResultItemViewHolder(@NonNull View itemView){
            super(itemView);
            // Get the references to the view-objects
            mName = itemView.findViewById(R.id.textView_rItem_nameVar);
            mScore = itemView.findViewById(R.id.textView_rItem_score);
            mMaxScore = itemView.findViewById(R.id.textView_rItem_maxScore);

        }
    }

    public ResultItemAdapter(ArrayList<ResultItem> resultItemsList){
        mResultItemsList = resultItemsList;
    }

    @NonNull
    @Override
    public ResultItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        // pass layout of weather item layout to adapter
        // returning view holder
        View layoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_item, parent, false);
        return new ResultItemViewHolder(layoutView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ResultItemViewHolder holder, int position){
        ResultItem currentItem = mResultItemsList.get(position); //Item on a specific position
        holder.mName.setText(currentItem.getRName());
        holder.mScore.setText(Integer.toString(currentItem.getRScore()));
        holder.mMaxScore.setText(Integer.toString(currentItem.getrMaxscore()));
    }

    @Override
    public int getItemCount() {
        return mResultItemsList.size();
    }
}
