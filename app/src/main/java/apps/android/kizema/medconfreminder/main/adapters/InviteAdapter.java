package apps.android.kizema.medconfreminder.main.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import apps.android.kizema.medconfreminder.App;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.model.Conference;
import apps.android.kizema.medconfreminder.model.ConferenceUserTable;
import apps.android.kizema.medconfreminder.model.ConferenceUserTableDao;
import apps.android.kizema.medconfreminder.model.User;
import apps.android.kizema.medconfreminder.util.UserHelper;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.ConferenceViewHolder> {

    private List<Conference> conferences;
    private OnInviteCLickListener listener;

    public interface OnInviteCLickListener {
        void onConferenceCLicked(Conference conference);
        void onRefresh();
    }

    public InviteAdapter(List<Conference> conferences, OnInviteCLickListener listener) {
        this.conferences = conferences;
        this.listener = listener;
    }

    public void update(List<Conference> conferences) {
        this.conferences = conferences;
        notifyDataSetChanged();
    }

    public static class ConferenceViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName, tvLocation, tvReject;
        public ImageView ivNext;

        public ConferenceViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvLocation = (TextView) itemView.findViewById(R.id.tvLocation);
            tvReject = (TextView) itemView.findViewById(R.id.tvReject);
            ivNext = (ImageView) itemView.findViewById(R.id.ivNext);
        }
    }

    @Override
    public ConferenceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_item, parent, false);

        return new ConferenceViewHolder(parentView);
    }

    @Override
    public void onBindViewHolder(final ConferenceViewHolder holder, final int position) {
        final Conference model = conferences.get(position);
        holder.tvName.setText(model.getConferenceName());
        holder.tvLocation.setText(model.getLocation());

        holder.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.getAdapterPosition() >= 0) {
                    remove(holder, conferences.get(holder.getAdapterPosition()));
                    listener.onRefresh();
                }
            }
        });

        holder.ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                if (pos >= 0) {
                    listener.onConferenceCLicked(conferences.get(pos));
                    remove(holder, conferences.get(pos));
                    listener.onRefresh();
                }
            }
        });
    }

    private void remove(ConferenceViewHolder holder, Conference model) {
        User user = UserHelper.getMyUser();
        for (ConferenceUserTable conferenceUserTable : user.getConferenceIds()) {
            if (conferenceUserTable.getConferneceId() == model.getId()) {
                ConferenceUserTableDao dao = App.getDaoSession().getConferenceUserTableDao();
                dao.delete(conferenceUserTable);

                user.getConferenceIds().remove(conferenceUserTable);

                Conference conference = Conference.findById(conferenceUserTable.getConferneceId());
                if (conference != null) {
                    conferences.remove(conference);
                }
                if (holder.getAdapterPosition() >= 0) {
                    notifyItemRemoved(holder.getAdapterPosition());
                } else {
                    notifyDataSetChanged();
                }
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (conferences == null) {
            return 0;
        }

        return conferences.size();
    }

    public void clear() {
        conferences.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        if (conferences == null || conferences.size() == 0) {
            return true;
        }

        return false;
    }

}

