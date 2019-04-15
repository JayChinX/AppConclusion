/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zzdc.workpolicy;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.UserHandle;
import android.os.UserManager;
import android.support.v4.os.BuildCompat;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.admin.DevicePolicyManager.EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE;

/**
 * Handles events related to the managed profile.
 */
public class DeviceAdminReceiver extends android.app.admin.DeviceAdminReceiver {
    private static final String TAG = "DeviceAdminReceiver";

    public static final String ACTION_PASSWORD_REQUIREMENTS_CHANGED =
            "com.afwsamples.testdpc.policy.PASSWORD_REQUIREMENTS_CHANGED";

    public static final String NETWORK_LOGS_FILE_PREFIX = "network_logs_";

    private static final String LOGS_DIR = "logs";

    private static final String FAILED_PASSWORD_LOG_FILE =
            "failed_pw_attempts_timestamps.log";

    private static final int CHANGE_PASSWORD_NOTIFICATION_ID = 101;
    private static final int PASSWORD_FAILED_NOTIFICATION_ID = 102;

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case ACTION_PASSWORD_REQUIREMENTS_CHANGED:
            case Intent.ACTION_BOOT_COMPLETED:
                updatePasswordConstraintNotification(context);
                break;
            case DevicePolicyManager.ACTION_PROFILE_OWNER_CHANGED:
                onProfileOwnerChanged(context);
                break;
            case DevicePolicyManager.ACTION_DEVICE_OWNER_CHANGED:
                onDeviceOwnerChanged(context);
                break;
            default:
               super.onReceive(context, intent);
               break;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onSecurityLogsAvailable(Context context, Intent intent) {
        Log.i(TAG, "onSecurityLogsAvailable() called");

    }


    /*
     * TODO: reconsider how to store and present the logs in the future, e.g. save the file into
     * internal memory and show the content in a ListView
     */
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public void onNetworkLogsAvailable(Context context, Intent intent, long batchToken,
            int networkLogsCount) {
        Log.i(TAG, "onNetworkLogsAvailable(), batchToken: " + batchToken
                + ", event count: " + networkLogsCount);

    }

    private static class EventSavingTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private long mBatchToken;
        private List<String> mLoggedEvents;

        public EventSavingTask(Context context, long batchToken, ArrayList<String> loggedEvents) {
            mContext = context;
            mBatchToken = batchToken;
            mLoggedEvents = loggedEvents;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Date timestamp = new Date();
            String filename = NETWORK_LOGS_FILE_PREFIX + mBatchToken + "_" + timestamp.getTime() + ".txt";
            File file = new File(mContext.getExternalFilesDir(null), filename);
            try (OutputStream os = new FileOutputStream(file)) {
                for (String event : mLoggedEvents) {
                    os.write((event + "\n").getBytes());
                }
                Log.d(TAG, "Saved network logs to file: " + filename);
            } catch (IOException e) {
                Log.e(TAG, "Failed saving network events to file" + filename, e);
            }
            return null;
        }
    }

    @Override
    public void onProfileProvisioningComplete(Context context, Intent intent) {
        Log.i(TAG, "onProfileProvisioningComplete() called");
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onBugreportSharingDeclined(Context context, Intent intent) {
        Log.i(TAG, "Bugreport sharing declined");

    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onBugreportShared(final Context context, Intent intent,
            final String bugreportFileHash) {
        Log.i(TAG, "Bugreport shared, hash: " + bugreportFileHash);

    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onBugreportFailed(Context context, Intent intent, int failureCode) {
        Log.i(TAG, "onBugreportFailed() called");
    }


    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public void onUserAdded(Context context, Intent intent, UserHandle newUser) {
        Log.i(TAG, "onUserAdded() called");
        handleUserAction(context, newUser, "on_user_added_title",
                "on_user_added_message", NotificationUtil.USER_ADDED_NOTIFICATION_ID);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public void onUserRemoved(Context context, Intent intent, UserHandle removedUser) {
        Log.i(TAG, "onUserRemoved() called");
    }

    @TargetApi(Build.VERSION_CODES.P)
    @Override
    public void onUserStarted(Context context, Intent intent, UserHandle startedUser) {
        Log.i(TAG, "onUserStarted() called");
    }

    @TargetApi(Build.VERSION_CODES.P)
    @Override
    public void onUserStopped(Context context, Intent intent, UserHandle stoppedUser) {
        Log.i(TAG, "onUserStopped() called");
    }

    @TargetApi(Build.VERSION_CODES.P)
    @Override
    public void onUserSwitched(Context context, Intent intent, UserHandle switchedUser) {
        Log.i(TAG, "onUserSwitched() called");
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onSystemUpdatePending(Context context, Intent intent, long receivedTime) {
        Log.i(TAG, "onSystemUpdatePending() called");
        if (receivedTime != -1) {
            DateFormat sdf = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
            String timeString = sdf.format(new Date(receivedTime));
            Toast.makeText(context, "System update received at: " + timeString,
                    Toast.LENGTH_LONG).show();
        } else {
            // No system update is currently available on this device.
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public String onChoosePrivateKeyAlias(Context context, Intent intent, int uid, Uri uri,
            String alias) {
        Log.i(TAG, "onChoosePrivateKeyAlias() called");
        return "";
    }

    /**
     * @param context The context of the application.
     * @return The component name of this component in the given context.
     */
    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext(), DeviceAdminReceiver.class);
    }

    @Deprecated
    @Override
    public void onPasswordExpiring(Context context, Intent intent) {
        Log.i(TAG, "onPasswordExpiring() called");
        onPasswordExpiring(context, intent, Process.myUserHandle());
    }

    @TargetApi(Build.VERSION_CODES.O)
    // @Override
    public void onPasswordExpiring(Context context, Intent intent, UserHandle user) {
        Log.i(TAG, "onPasswordExpiring() called");
    }

    @Deprecated
    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        Log.i(TAG, "onPasswordFailed() called");
        onPasswordFailed(context, intent, Process.myUserHandle());
    }

    @TargetApi(Build.VERSION_CODES.O)
    // @Override
    public void onPasswordFailed(Context context, Intent intent, UserHandle user) {
        Log.i(TAG, "onPasswordFailed() called");
    }

    @Deprecated
    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        Log.i(TAG, "onPasswordSucceeded() called");
        onPasswordSucceeded(context, intent, Process.myUserHandle());
    }

    @TargetApi(Build.VERSION_CODES.O)
    // @Override
    public void onPasswordSucceeded(Context context, Intent intent, UserHandle user) {
        Log.i(TAG, "onPasswordSucceeded() called");
        if (Process.myUserHandle().equals(user)) {
            logFile(context).delete();
        }
    }

    @Deprecated
    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        Log.i(TAG, "onPasswordChanged() called");
        onPasswordChanged(context, intent, Process.myUserHandle());
    }

    @TargetApi(Build.VERSION_CODES.O)
    // @Override
    public void onPasswordChanged(Context context, Intent intent, UserHandle user) {
        Log.i(TAG, "onPasswordChanged() called");
        if (Process.myUserHandle().equals(user)) {
            updatePasswordConstraintNotification(context);
        }
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        Log.i(TAG, "onEnabled() called");
        UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
        long serialNumber = userManager.getSerialNumberForUser(Binder.getCallingUserHandle());
        Log.i(TAG, "Device admin enabled in user with serial number: " + serialNumber);
    }

    private static File logFile(Context context) {
        File parent = context.getDir(LOGS_DIR, Context.MODE_PRIVATE);
        return new File(parent, FAILED_PASSWORD_LOG_FILE);
    }

    private static ArrayList<Date> getFailedPasswordAttempts(Context context) {
        File logFile = logFile(context);
        ArrayList<Date> result = new ArrayList<Date>();

        if(!logFile.exists()) {
            return result;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(logFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = null;
            while ((line = br.readLine()) != null && line.length() > 0) {
                result.add(new Date(Long.parseLong(line)));
            }

            br.close();
        } catch (IOException e) {
            Log.e(TAG, "Unable to read failed password attempts", e);
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e(TAG, "Unable to close failed password attempts log file", e);
                }
            }
        }

        return result;
    }

    private static void saveFailedPasswordAttempts(Context context, ArrayList<Date> attempts)
            throws IOException {
        File logFile = logFile(context);

        if(!logFile.exists()) {
            logFile.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(logFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for(Date date : attempts) {
            bw.write(Long.toString(date.getTime()));
            bw.newLine();
        }

        bw.close();
    }

    private static void updatePasswordConstraintNotification(Context context) {
        Log.i(TAG, "updatePasswordConstraintNotification() called");
    }

    @TargetApi(28)
    private static Boolean isUsingUnifiedPassword(Context context) {
        if (!BuildCompat.isAtLeastP()) {
            return false;
        }
        final DevicePolicyManager dpm = context.getSystemService(DevicePolicyManager.class);
        return dpm.isUsingUnifiedPassword(getComponentName(context));
    }

    /**
     * Notify the admin receiver that something about the password has changed, e.g. quality
     * constraints or separate challenge requirements.
     *
     * This has to be sent manually because the system server only sends broadcasts for changes to
     * the actual password, not any of the constraints related it it.
     *
     * <p>May trigger a show/hide of the notification warning to change the password through
     * Settings.
     */
    public static void sendPasswordRequirementsChanged(Context context) {
        final Intent changedIntent =
                new Intent(DeviceAdminReceiver.ACTION_PASSWORD_REQUIREMENTS_CHANGED);
        changedIntent.setComponent(getComponentName(context));
        context.sendBroadcast(changedIntent);
    }

    private void onProfileOwnerChanged(Context context) {
        Log.i(TAG, "onProfileOwnerChanged");

    }

    private void onDeviceOwnerChanged(Context context) {
        Log.i(TAG, "onDeviceOwnerChanged");

    }

    @TargetApi(Build.VERSION_CODES.P)
    public void onTransferOwnershipComplete(Context context, PersistableBundle bundle) {
        Log.i(TAG, "onTransferOwnershipComplete");

    }

    @TargetApi(Build.VERSION_CODES.P)
    public void onTransferAffiliatedProfileOwnershipComplete(Context context, UserHandle user) {
        Log.i(TAG, "onTransferAffiliatedProfileOwnershipComplete");

    }

    private void handleUserAction(Context context, UserHandle userHandle, String titleResId,
                                  String messageResId, int notificationId) {
        UserManager userManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
        String message = messageResId + userManager.getSerialNumberForUser(userHandle);
        Log.i(TAG, message);
        NotificationUtil.showNotification(context, titleResId, message, notificationId);
    }
}
