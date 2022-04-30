package tn.ridha.other;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;

import tn.ridha.main.R;

public class loadingDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    public loadingDialog(Activity myActivity)
    {
        activity= myActivity;
    }

    public void startLoadingDialog()
    {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_loading,null));
        builder.setCancelable(false);


        alertDialog = builder.create();
        alertDialog.show();
    }


    public void dismissDialog()
    {
        if (alertDialog != null)
            alertDialog.dismiss();
    }
}
