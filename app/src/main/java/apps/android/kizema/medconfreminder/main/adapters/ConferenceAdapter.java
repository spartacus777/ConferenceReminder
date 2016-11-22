package apps.android.kizema.medconfreminder.main.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.model.Conference;


public class ConferenceAdapter extends RecyclerView.Adapter<ConferenceAdapter.ConfViewHolder> {

    private List<Conference> conferences;
    private OnConferenceClickListener listener;

    public ConferenceAdapter(List<Conference> conferences, OnConferenceClickListener listener) {
        this.conferences = conferences;
        this.listener = listener;
    }

    public void update(List<Conference> conferences){
        this.conferences = conferences;
        notifyDataSetChanged();
    }

    public interface OnConferenceClickListener{
        void onConferenceClicked(Conference c);
    }

    public static class ConfViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvAddress, tvDate;

        public ConfViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        }
    }

    @Override
    public ConferenceAdapter.ConfViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.conf_item, parent, false);

        return new ConferenceAdapter.ConfViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(final ConferenceAdapter.ConfViewHolder holder, final int position) {
        Conference model = conferences.get(position);
        holder.tvName.setText(model.getConferenceName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onConferenceClicked(conferences.get(holder.getAdapterPosition()));
                }
            }
        });
        holder.tvAddress.setText(model.getLocation());
        holder.tvDate.setText(model.getDate());
    }

    @Override
    public int getItemCount() {
        if (conferences == null){
            return 0;
        }

        return conferences.size();
    }

    public void clear(){
        conferences.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty(){
        if (conferences == null || conferences.size() == 0) {
            return true;
        }

        return false;
    }

}

