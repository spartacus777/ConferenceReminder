package apps.android.kizema.medconfreminder.secondary.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.model.Topic;
import apps.android.kizema.medconfreminder.model.User;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<Topic> topics;

    public TopicAdapter(List<Topic> conferences) {
        this.topics = conferences;
    }

    public void update(List<Topic> conferences){
        this.topics = conferences;
        notifyDataSetChanged();
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvAuthor;

        public TopicViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
        }
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_item, parent, false);

        return new TopicViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(final TopicViewHolder holder, final int position) {
        Topic model = topics.get(position);
        holder.tvName.setText(model.getName());

        User author = User.findById(model.getUserId());
        if (author != null){
            holder.tvAuthor.setText(" Author: " + author.getName());
        }

    }

    @Override
    public int getItemCount() {
        if (topics == null){
            return 0;
        }

        return topics.size();
    }

    public void clear(){
        topics.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty(){
        if (topics == null || topics.size() == 0) {
            return true;
        }

        return false;
    }

}

