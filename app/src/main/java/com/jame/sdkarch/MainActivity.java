package com.jame.sdkarch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jame.sdklib.IAPICallback;
import com.jame.sdklib.ISDKEventListener;
import com.jame.sdklib.SDKAPIManager;
import com.jame.sdklib.model.base.ProtocolBaseModel;
import com.jame.sdklib.model.base.ProtocolErrorModel;
import com.jame.sdklib.model.response.RspLocationModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ISDKEventListener {

    TextView mLog;
    SDKAPIManager mSDKAPIManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLog = findViewById(R.id.tv_log);
        findViewById(R.id.btn_getloc).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_getloc_async).setOnClickListener(this);
        mSDKAPIManager = new SDKAPIManager();
        mSDKAPIManager.setSDKEventListener(this);
    }

    @Override
    public void onClick(View view) {
        printLog("onClick");
        switch (view.getId()){
            case R.id.btn_register:
                mSDKAPIManager.init(getApplicationContext());
                break;
            case R.id.btn_getloc:
                ProtocolBaseModel result = mSDKAPIManager.getLocation();
                printLog("getLoc result:"+result);
                break;
            case R.id.btn_getloc_async:
                mSDKAPIManager.getLocationAsync(new IAPICallback<RspLocationModel>(){
                    @Override
                    public void onSuccess(RspLocationModel model) {
                        printLog("getLocAsync onsucc:"+model);
                    }

                    @Override
                    public void onError(ProtocolErrorModel errModel) {
                        printLog("getLocAsync onError:"+errModel);
                    }
                });
                break;
        }
    }

    @Override
    public void onSDKEvent(ProtocolBaseModel eventModel) {
        printLog("onSDKEvent:"+eventModel);
    }

    private void printLog(final String log){
        Log.d("jtag"," client: "+log);
        mLog.post(new Runnable() {
            @Override
            public void run() {
                mLog.setText(log + "\n" + mLog.getText());
            }
        });
    }
}
