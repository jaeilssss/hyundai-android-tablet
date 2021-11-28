package com.obigo.hkmotors.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * Class for custom message dialog
 */
public class MessageDialog extends Dialog {

    private static final String TAG = "MessageDialog";

    private String msg;
    private TextView txtMsg;

    public MessageDialog(Context context, String msg) {
        super(context);
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "== onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //  추후 개발_20181029
        /*setContentView(R.layout.custom_loading);

        txtMsg = (TextView)findViewById(R.id.msg_dialog);
        txtMsg.setText(msg);*/
    }
}
