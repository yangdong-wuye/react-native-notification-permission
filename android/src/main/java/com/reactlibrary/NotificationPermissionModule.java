package com.reactlibrary;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
        promise.resolve(hasPermission("OP_POST_NOTIFICATION"));
    }

    private boolean hasPermission(String appOpsServiceId) {

        Context context = getCurrentActivity().getApplicationContext();
        if (Build.VERSION.SDK_INT >= 24) {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(
                    Context.NOTIFICATION_SERVICE);
            return mNotificationManager.areNotificationsEnabled();
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
                return Integer.parseInt(result.toString()) == AppOpsManager.MODE_ALLOWED;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
