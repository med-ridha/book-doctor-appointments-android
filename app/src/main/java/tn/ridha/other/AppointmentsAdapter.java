package tn.ridha.other;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.bson.Document;
import org.bson.types.ObjectId;

import io.realm.mongodb.App;
import tn.ridha.Beans.Appointments;
import tn.ridha.Dao.AppointmentsDao;
import tn.ridha.main.MainActivity;
import tn.ridha.main.R;

import java.sql.Date;
import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder> {

    private List<Appointments> appointmentsList;
    public AppointmentsAdapter( List<Appointments> appointmentsList){
        this.appointmentsList = appointmentsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_design, parent, false);
        return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String _id = appointmentsList.get(position).get_id();
        String doctorName=appointmentsList.get(position).getDoctorName();
        String location=appointmentsList.get(position).getLocation();
        String date=appointmentsList.get(position).getDate();
        holder.setData(doctorName, location, date);
        holder.set_id(_id);
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView doctorName;
        private TextView location;
        private TextView date;
        private String _id;
        private AppointmentsAdapter adapter;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctorName);
            location = itemView.findViewById(R.id.location);
            date = itemView.findViewById(R.id.date);
            Button book = itemView.findViewById(R.id.cancel);
            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    System.out.println(_id);
                    AppointmentsDao appDao = new AppointmentsDao();
                    Document document = new Document();
                    document.append("_id", new ObjectId(_id));
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            appDao.delete(document);
                        }
                    });
                    thread.start();
                    adapter.appointmentsList.remove(getAdapterPosition());
                    adapter.notifyItemRemoved(getAdapterPosition());
                }
            });
        }
        public void set_id(String _id){
            this._id = _id;
        }
        public String get_id(){
            return this._id;
        }
        public void setData(String doctorName, String location, String date) {
            this.doctorName.setText(doctorName);
            this.location.setText(location);
            this.date.setText(date);
        }
        public ViewHolder linkAdapter (AppointmentsAdapter adapter){
            this.adapter = adapter;
            return this;
        }
    }
}
