package com.moripro.morina.mylawdialog.main;

import android.app.Activity;
import android.content.IntentSender;
import android.util.Log;
import android.view.View;
import com.moripro.morina.mylawdialog.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

public class AppUpdate {

    private Activity activity;

    public AppUpdate(Activity activity){
        this.activity = activity;
    }


    public void checkForAppUpdate(final int MY_REQUEST_CODE){
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(activity);

        Log.i("AppUpdate", "Checking for updates");
        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    // Request the update.
                    Log.i("AppUpdate", "Update is available");
                    InstallStateUpdatedListener listener = monitorUpdateState(appUpdateManager);
                    appUpdateManager.registerListener(listener);

                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,    // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                AppUpdateType.FLEXIBLE,
                                activity, MY_REQUEST_CODE);
                    }catch (IntentSender.SendIntentException e){
                        Log.i("AppUpdate", "error");
                    }
                    appUpdateManager.unregisterListener(listener);
                }else{
                    Log.i("AppUpdate", "No update is available");
                }
            }
        });
    }


    private  InstallStateUpdatedListener monitorUpdateState(final AppUpdateManager appUpdateManager){
        // Create a listener to track request state updates.
        InstallStateUpdatedListener listener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState state) {
                if (state.installStatus() == InstallStatus.DOWNLOADING) {
                    Log.i("AppUpdate", "Downloading");
                    //long bytesDownloaded = state.bytesDownloaded();
                    //long totalBytesToDownload = state.totalBytesToDownload();
                    // Implement progress bar.

                } else if (state.installStatus() == InstallStatus.DOWNLOADED) {
                    Log.i("AppUpdate", "Downloaded");
                    popupSnackbarForCompleteUpdate(appUpdateManager);
                }
            }
        };
        return listener;
    }


    private void popupSnackbarForCompleteUpdate(final AppUpdateManager appUpdateManager) {
        Snackbar snackbar =
                Snackbar.make(
                        this.activity.findViewById(R.id.content),
                        "アプリアップデートが完了しました。",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("アプリの再起動", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }
}
