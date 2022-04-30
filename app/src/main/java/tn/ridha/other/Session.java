package tn.ridha.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

public class Session extends AppCompatActivity {
    private SharedPreferences prefs;

    public Session(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setPassword(String password) {
        prefs.edit().putString("password", password).commit();
    }

    public String getPassword() {
        String username = prefs.getString("password","");
        return username;
    }
    public void setUsername(String username) {
        prefs.edit().putString("username", username).commit();
    }

    public String getUsername() {
        String username = prefs.getString("username","");
        return username;
    }
    public void setEmail(String email) {
        prefs.edit().putString("email", email).commit();
    }

    public String getEmail() {
        return prefs.getString("email","");
    }
    public void Logout(){
        prefs.edit().putString("password", null).commit();
        prefs.edit().putString("username", null).commit();
        prefs.edit().putString("email", null).commit();
    }
}
