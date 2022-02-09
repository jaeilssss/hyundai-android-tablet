package com.obigo.hkmotors.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.View;
import android.view.WindowManager;

import com.obigo.hkmotors.R;

public class LoadingDialog extends Dialog {

    View view;

    public LoadingDialog(@NonNull Context context,int check) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(check==0){
            setContentView(R.layout.dialog_progress);

        }else if(check==1){
            setContentView(R.layout.dialog_car_data);
        }else{
            setContentView(R.layout.dialog_car_send);
        }
    }

    public void setNotTouch(){
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
    public void setTouch(){
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onBackPressed() {

    }
}
