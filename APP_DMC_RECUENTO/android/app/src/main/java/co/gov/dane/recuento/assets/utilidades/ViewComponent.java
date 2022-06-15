package co.gov.dane.recuento.assets.utilidades;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import co.gov.dane.recuento.model.UserTokenViewModel;

public class ViewComponent  extends AppCompatActivity  {
    public Activity activity;
    public String activityString = "";
    public UserTokenViewModel userToken;
    public Bundle userBuble;


    public Bundle getUserBuble() {
        return userBuble;
    }

    public UserTokenViewModel getUserToken() {
        return userToken;
    }

    public void setUserToken(UserTokenViewModel userToken) {
        this.userToken = userToken;
    }


    public ViewComponent(Activity _activity){
        this.activity = _activity;
    }

    public ViewComponent(Activity _activity, String _activityString, UserTokenViewModel _userToken) {
        this.activity = _activity;
        this.activityString = _activityString;
    }

    public void progressBarProcess(int id, boolean loading) {
        ProgressBar bar = this.activity.findViewById(id);
        if(bar!= null){
            if (loading) {
                bar.setVisibility(View.VISIBLE);
            } else {
                bar.setVisibility(View.GONE);
            }
        }
    }

    public void setDatosLogin(){
        Bundle requestUser = activity.getIntent().getExtras();
        if(requestUser != null){
            userToken = (UserTokenViewModel) requestUser.getSerializable("USER");
            userBuble = new Bundle();
            userBuble.putSerializable("USER",userToken);
        }
    }


}
