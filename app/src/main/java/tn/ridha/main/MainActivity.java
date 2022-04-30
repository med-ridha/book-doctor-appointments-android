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
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.mongodb.mongo.iterable.FindIterable;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import tn.ridha.Beans.Appointments;
import tn.ridha.Dao.AppointmentsDao;
import tn.ridha.main.R;
import tn.ridha.other.Adapter;
import tn.ridha.other.AppointmentsAdapter;
import tn.ridha.other.ItemModel;
import tn.ridha.other.Session;
import tn.ridha.Beans.UserData;
import tn.ridha.other.loadingDialog;
import com.google.android.material.navigation.NavigationView;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public final static String appId = "doctor_appointments-vhjzo";
    public static boolean connected = false;
    public static final String MyPREFERENCES = "MyPrefs";
    public static SharedPreferences sharedPreferences;
    public static String password;
    public static boolean Connected = false;
    public static UserData userData;
    public static User user;
    public static App app;
    public static MongoClient mongoClient;
    public static MongoDatabase mongoDatabase;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<Appointments> appList;
    AppointmentsAdapter adapter;
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(appId).build());
        user = MainActivity.app.currentUser();
        if (user != null){
            mongoClient = user.getMongoClient("mongodb-atlas");
            mongoDatabase = mongoClient.getDatabase("Doctor_appointments");
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar;
        NavigationView navigationView;
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
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
        loadingDialog loadingDialog = new loadingDialog(MainActivity.this);
        if (userData == null){
            loadingDialog.startLoadingDialog();
        }
        Session session = new Session(this);
        String email = session.getEmail();
        Handler handler = new Handler();
        if (!checkConnection()){
            costumeToast("Something went wrong! please check your internet connection", getApplicationContext());
            connected = false;
            loadingDialog.dismissDialog();
        }else{
            connected = true;
            if (email != null && email.length() > 0){
                String password = session.getPassword();
                if (password.length()> 0){
                    Credentials credentials = Credentials.emailPassword(email, password);
                    app.loginAsync(credentials, result->{
                        if(result.isSuccess()){
                                getUser(email, loadingDialog);
                        }else{
                            loadingDialog.dismissDialog();
                            costumeToast("something went wrong, try to login manually", getApplicationContext());
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else{
                    loadingDialog.dismissDialog();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }else{
                loadingDialog.dismissDialog();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public boolean checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network or not
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    public static void costumeToast(String value, Context context){
        Log.v("User", value);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, value, duration);
        toast.show();
    }
    public void getUser(String email, loadingDialog loadingDialog){
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("users");
        Document queryFilter = new Document().append("email", email);
        mongoCollection.findOne(queryFilter).getAsync((searchResult) -> {
            if (searchResult.isSuccess()) {
                Document resultData = searchResult.get();
                if (resultData != null) {
                    MainActivity.Connected = true;
                    String _id = resultData.getObjectId("_id").toString();
                    String resultName = resultData.getString("name");
                    String resultEmail = resultData.getString("email");
                    String resultCity = resultData.getString("city");
                    String resultPhone = resultData.getString("phone");
                    String resultBirthDate = resultData.getString("birthdate");
                    userData = new UserData(_id, resultName, resultEmail, resultCity, resultPhone, resultBirthDate);
                    TextView textView = findViewById(R.id.welcomeWelcome);
                    String t = textView.getText().toString();
                    String text = t+" "+userData.getName();
                    textView.setText(text);
                    AppointmentsDao appDao = new AppointmentsDao();
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FindIterable<Document> findIterable = appDao.getMany("user_id", userData.get_id());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    findIterable.iterator().getAsync(result->{
                                        if (result.isSuccess()){
                                            MongoCursor<Document> mongoCursor = result.get();
                                            appList = new ArrayList<>();
                                            while(mongoCursor.hasNext()){
                                                Document document = mongoCursor.next();
                                                addData(document);
                                                System.out.println(document.get("user_id"));
                                            }
                                            initRecyclerView();
                                            loadingDialog.dismissDialog();
                                        }else{
                                            System.out.println(result.getError());
                                        }
                                    });
                                }
                            });
                        }
                    });
                    thread.start();
                } else {
                    Intent intent = new Intent(this, SetUpProfile.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void addData(Document document) {
        String _id = document.get("_id").toString();
        String name =(String) document.get("doctorName");
        String location =(String) document.get("location");
        String date =(String) document.get("date");
        appList.add(new Appointments(_id, name,location,date));
    }


    private void initRecyclerView(){
        recyclerView=findViewById(R.id.recycleViewMain);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        if (adapter != null)
            System.out.println(adapter.getItemCount());
        System.out.println("doctor list : " + this.appList.size());
        adapter = new AppointmentsAdapter(appList);
        System.out.println("adapter : " +adapter.getItemCount());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void MenuItem_Logout(MenuItem menuItem){
        Connected = false;
        userData = null;
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
        finish();
    }

    public void MenuItem_goProfile(MenuItem menuItem){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    public void MenuItem_goHospitals(MenuItem menuItem){
        Intent intent = new Intent(this, DoctorsActivity.class);
        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}