package com.reactlibrary;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

public class NotificationPermissionModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public NotificationPermissionModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "NotificationPermission";
    }

    @ReactMethod
    public void hasPermission(Promise promise) {
        String appOpsServiceId = "OP_POST_NOTIFICATION";
        Context context = getCurrentActivity().getApplicationContext();
        if (Build.VERSION.SDK_INT >= 24) {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(
                    Context.NOTIFICATION_SERVICE);
            promise.resolve(mNotificationManager.areNotificationsEnabled());
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();

            String pkg = context.getPackageName();
            int uid = appInfo.uid;
            Class appOpsClazz;

            try {
                appOpsClazz = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClazz.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE,
                        String.class);
                Field opValue = appOpsClazz.getDeclaredField(appOpsServiceId);
                int value = opValue.getInt(Integer.class);
                Object result = checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg);
                promise.resolve(Integer.parseInt(result.toString()) == AppOpsManager.MODE_ALLOWED);
            } catch (Exception e) {
                e.printStackTrace();
                promise.reject(e);
            }
        }
        promise.resolve(true);
    }

    @ReactMethod
    public void openSystemNoticeView(){
        try {
            // 跳转到通知设置界面
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);

            //适用于 API 26, 即8.0（含8.0）以上可以用
            intent.putExtra(EXTRA_APP_PACKAGE, this.reactContext.getPackageName());
            intent.putExtra(EXTRA_CHANNEL_ID, this.reactContext.getApplicationInfo().uid);

            //API21——25，即 5.0——7.1 之间的版本可以使用
            intent.putExtra("app_package", this.reactContext.getPackageName());
            intent.putExtra("app_uid", this.reactContext.getApplicationInfo().uid);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            this.reactContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常则跳转到应用设置界面
            Intent intent = new Intent();

            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", this.reactContext.getPackageName(), null);
            intent.setData(uri);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            this.reactContext.startActivity(intent);
        }
    }
}
