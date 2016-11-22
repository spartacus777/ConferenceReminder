package apps.android.kizema.medconfreminder.main.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.model.ConferenceUserTable;
import apps.android.kizema.medconfreminder.model.User;
import apps.android.kizema.medconfreminder.util.ImageLoaderHelper;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.ConfViewHolder> {

    private List<User> doctors;
    private OnDoctorClickListener listener;
    private long confId;

    public DoctorsAdapter(List<User> conferences, long  confId, OnDoctorClickListener listener) {
        this.doctors = conferences;
        this.listener = listener;
        this.confId = confId;
    }

    public void update(List<User> conferences){
        this.doctors = conferences;
        notifyDataSetChanged();
    }

    public interface OnDoctorClickListener {
        void onDoctorClicked(User user);
    }

    public static class ConfViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public ImageView ivInvite, ivPerson;

        public ConfViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ivInvite = (ImageView) itemView.findViewById(R.id.ivInvite);
            ivPerson = (ImageView) itemView.findViewById(R.id.ivPerson);
        }
    }

    @Override
    public DoctorsAdapter.ConfViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);

        DoctorsAdapter.ConfViewHolder h = new DoctorsAdapter.ConfViewHolder(parentView);

        //TODO
        h.ivInvite.setImageResource(R.drawable.ic_add);
        h.ivInvite.setEnabled(true);

        return h;
    }

    @Override
    public void onBindViewHolder(final DoctorsAdapter.ConfViewHolder holder, final int position) {
        final User model = doctors.get(position);
        holder.tvName.setText(model.getName());

        List<ConferenceUserTable> list = model.getConferenceIds();
        for (ConferenceUserTable conferenceUserTable: list) {
            if (conferenceUserTable.getConferneceId() == confId){
                //he is invited
                holder.ivInvite.setImageResource(R.drawable.ic_ok);
                holder.ivInvite.setEnabled(false);
            }
        }

        holder.ivInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    if (holder.getAdapterPosition() >= 0) {
                        listener.onDoctorClicked(doctors.get(holder.getAdapterPosition()));
                    }
                }

                holder.ivInvite.setImageResource(R.drawable.ic_ok);
                holder.ivInvite.setEnabled(false);
            }
        });

        ImageLoader.getInstance().displayImage(ImageLoaderHelper.FILE_SYSTEM_PREF + model.getPhoto(), holder.ivPerson);
    }

    @Override
    public int getItemCount() {
        if (doctors == null){
            return 0;
        }

        return doctors.size();
    }

    public void clear(){
        doctors.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty(){
        if (doctors == null || doctors.size() == 0) {
            return true;
        }

        return false;
    }

}


