package org.vazteixeira.rui.lockscreen;

import android.annotation.TargetApi;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 23;

    private Button enableOrDisableButton;
    private Button lockButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableOrDisableButton = (Button) findViewById(R.id.enable_disable_admin);
        lockButton = (Button) findViewById(R.id.lock);

        setEnableOrDisableAdminClickListener();
        setLockClickListener();
    }

    private void setEnableOrDisableAdminClickListener() {
        enableOrDisableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdminActive()) {
                    disableAdmin();
                } else {
                    requestEnableAdmin();
                }
            }
        });
    }

    private void setLockClickListener() {
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lock();
            }
        });
    }

    private void updateEnableOrDisableAdminButton() {
        if (isAdminActive()) {
            enableOrDisableButton.setText("Disable Admin");
        } else {
            enableOrDisableButton.setText("Enable Admin");
        }
    }

    private void updateLockButton() {
        lockButton.setEnabled(isAdminActive());
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateEnableOrDisableAdminButton();
        updateLockButton();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean isLockPermitted(String pckg) {
        DevicePolicyManager devicePolicyManager = getDevicePolicyManager();
        return devicePolicyManager.isLockTaskPermitted(pckg);
    }

    private boolean isAdminActive() {
        DevicePolicyManager devicePolicyManager = getDevicePolicyManager();
        return devicePolicyManager.isAdminActive(getAdminComponentName());
    }

    private DevicePolicyManager getDevicePolicyManager() {
        return (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DPM","onActivityResult: reqCode="+requestCode+" resCode="+resultCode);
    }

    private void requestEnableAdmin() {
        // Launch the activity to have the user enable our admin.
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, getAdminComponentName());
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.app_name));
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void disableAdmin() {
        getDevicePolicyManager().removeActiveAdmin(getAdminComponentName());
        updateEnableOrDisableAdminButton(); // // FIXME: 03/04/2016 the call to remove is async
    }

    private ComponentName getAdminComponentName() {
        return new ComponentName(this, AdminReceiver.class);
    }

    private void lock() {
        getDevicePolicyManager().lockNow();
    }
}
