package tn.ridha.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import tn.ridha.other.Adapter;
import tn.ridha.other.ItemModel;
import tn.ridha.main.R;
import tn.ridha.other.Session;
import com.google.android.material.navigation.NavigationView;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import tn.ridha.other.loadingDialog;

public class DoctorsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ItemModel> doctorList;
    Adapter adapter;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);
        Realm.init(this);
        Toolbar toolbar;
        NavigationView navigationView;
        toolbar = findViewById(R.id.main_toolbar2);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout3);
        navigationView = findViewById(R.id.nav_view2);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        loadingDialog loadingDialog = new loadingDialog(this);
        loadingDialog.startLoadingDialog();
        User user = MainActivity.app.currentUser();
        MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("Doctor_appointments");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("doctors");
        mongoCollection.find().iterator().getAsync(result->{
            if (result.isSuccess()){
                loadingDialog.dismissDialog();
                MongoCursor<Document> mongoCursor = result.get();
                doctorList = new ArrayList<>();
                while(mongoCursor.hasNext()){
                    Document document = mongoCursor.next();
                    addData(document);
                    System.out.println(document.get("name"));
                }
                initRecyclerView();
            }else{
                System.out.println(result.getError());
            }
        });
    }

    private void addData(Document document) {
        String _id = document.get("_id").toString();
        String name =(String) document.get("name");
        String specialty =(String) document.get("speciality");
        String hospital =(String) document.get("hospital");
        String location = (String) document.get("location");
        doctorList.add(new ItemModel(_id, name,specialty,hospital, location));
    }


    private void initRecyclerView(){
        recyclerView=findViewById(R.id.recycleView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        if (adapter != null)
            System.out.println(adapter.getItemCount());
        System.out.println("doctor list : " + this.doctorList.size());
        adapter=new Adapter(doctorList);
        System.out.println("adapter : " +adapter.getItemCount());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void MenuItem_goProfile(MenuItem menuItem){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);

    }
    public void MenuItem_Logout(MenuItem menuItem){
        MainActivity.Connected = false;
        MainActivity.userData = null;
        Session session = new Session(this);
        session.Logout();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
        finish();
    }
    public void MenuItem_goHome(MenuItem menuItem){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);

    }
    public void MenuItem_goHospitals(MenuItem menuItem){
        Intent intent = new Intent(this, DoctorsActivity.class);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    public void theThing(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}