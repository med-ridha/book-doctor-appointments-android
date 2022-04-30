package tn.ridha.other;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.bson.Document;

import tn.ridha.Dao.AppointmentsDao;
import tn.ridha.main.MainActivity;
import tn.ridha.main.R;

import java.sql.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<ItemModel> doctorList;
    public Adapter( List<ItemModel> doctorList){
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String _id = doctorList.get(position).get_id();
        String name=doctorList.get(position).getItemName();
        String speciality=doctorList.get(position).getSpeciality();
        String hospital=doctorList.get(position).getHospital();
        String location = doctorList.get(position).getLocation();
        holder.setData(name, speciality, hospital, location);
        holder.set_id(_id);
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView speciality;
        private TextView hospital;
        private String _id;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name);
            speciality = itemView.findViewById(R.id.speciality);
            hospital = itemView.findViewById(R.id.hospital);
            Button book = itemView.findViewById(R.id.book);
            book.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v){
                   Document document = new Document();
                   document.append("doctorName", name.getText().toString());
                   document.append("doctor_id", _id);
                   document.append("user_id", MainActivity.userData.get_id());
                   long min = 999999999L;
                   long max = 9999999999L;
                   long rand = (long) Math.floor(Math.random()*(max-min+1)+min);
                   System.out.println(rand);
                   document.append("date", new Date(System.currentTimeMillis()+rand).toString());
                   document.append("createdDate", new Date(System.currentTimeMillis()).toString());
                   String location = hospital.getText().toString() +", "+ speciality.getText().toString();
                   document.append("location", location);
                   System.out.println(document);
                   AppointmentsDao appDao = new AppointmentsDao();
                   book.setEnabled(false);
                   book.setBackgroundColor(Color.GREEN);
                   Thread thread = new Thread(new Runnable() {
                       @Override
                       public void run() {
                           appDao.insert(document);
                       }
                   });
                   thread.start();
                   System.out.println(_id);
               }
            });
        }
        public void set_id(String _id){
            this._id = _id;
        }
        public String get_id(){
            return this._id;
        }
        public void setData(String name, String speciality, String hospital, String location) {
            String nameSpecialty = name+", "+speciality;
            this.name.setText(nameSpecialty);
            this.speciality.setText(hospital);
            this.hospital.setText(location);
        }
    }
}
