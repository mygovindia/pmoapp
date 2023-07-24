package com.sanskrit.pmo.permissions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION;


import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Nammu {
    private static final String KEY_IGNORED_PERMISSIONS = "ignored_permissions";
    private static final String KEY_PREV_PERMISSIONS = "previous_permissions";
    private static final String TAG = Nammu.class.getSimpleName();
    private static Context context;
    private static ArrayList<PermissionRequest> permissionRequests = new ArrayList();
    private static SharedPreferences sharedPreferences;

    public static void init(Context contexts) {
        context = contexts;
        sharedPreferences = context.getSharedPreferences("pl.tajchert.runtimepermissionhelper", 0);

    }

    public static boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != 0) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasPermission(Activity activity, String permission) {

        return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean hasPermission(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permissions) {
        return activity.shouldShowRequestPermissionRationale(permissions);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void askForPermission(Activity activity, String permission, PermissionCallback permissionCallback) {
        askForPermission(activity, new String[]{permission}, permissionCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void askForPermission(Activity activity, String[] permissions, PermissionCallback permissionCallback) {
        if (permissionCallback != null) {
            if (hasPermission(activity, permissions)) {
                permissionCallback.permissionGranted();
                return;
            }
            PermissionRequest permissionRequest = new PermissionRequest(new ArrayList(Arrays.asList(permissions)), permissionCallback);
            permissionRequests.add(permissionRequest);
            activity.requestPermissions(permissions, permissionRequest.getRequestCode());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionRequest requestResult = new PermissionRequest(requestCode);
        if (permissionRequests.contains(requestResult)) {
            PermissionRequest permissionRequest = (PermissionRequest) permissionRequests.get(permissionRequests.indexOf(requestResult));
            if (verifyPermissions(grantResults)) {
                permissionRequest.getPermissionCallback().permissionGranted();
            } else {
                permissionRequest.getPermissionCallback().permissionRefused();
            }
            permissionRequests.remove(requestResult);
        }
       // refreshMonitoredList();
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static ArrayList<String> getGrantedPermissions() {
//        if (context == null) {
//            throw new RuntimeException("Must call init() earlier");
//        }
//        ArrayList<String> permissions = new ArrayList();
//        ArrayList<String> permissionsGranted = new ArrayList();
//        //permissions.add("android.permission.ACCESS_FINE_LOCATION");
//        // permissions.add("android.permission.ACCESS_COARSE_LOCATION");
//        permissions.add("android.permission.WRITE_CALENDAR");
//        permissions.add("android.permission.READ_CALENDAR");
//        permissions.add("android.permission.CAMERA");
//        //permissions.add("android.permission.WRITE_CONTACTS");
//        // permissions.add("android.permission.READ_CONTACTS");
//        //permissions.add("android.permission.GET_ACCOUNTS");
//        //permissions.add("android.permission.RECORD_AUDIO");
//       // permissions.add("android.permission.CALL_PHONE");
//        //permissions.add("android.permission.READ_PHONE_STATE");
//       /* if (VERSION.SDK_INT >= 16) {
//            permissions.add("android.permission.READ_CALL_LOG");
//        }*/
//       /* if (VERSION.SDK_INT >= 16) {
//            permissions.add("android.permission.WRITE_CALL_LOG");
//        }*/
//        // permissions.add("com.android.voicemail.permission.ADD_VOICEMAIL");
//        // permissions.add("android.permission.USE_SIP");
//        // permissions.add("android.permission.PROCESS_OUTGOING_CALLS");
//        if (VERSION.SDK_INT >= 20) {
//            permissions.add("android.permission.BODY_SENSORS");
//        }
//
////        if (VERSION.SDK_INT >= 16) {
////            permissions.add("android.permission.READ_EXTERNAL_STORAGE");
////        }
////        permissions.add("android.permission.WRITE_EXTERNAL_STORAGE");
//        Iterator it = permissions.iterator();
//        while (it.hasNext()) {
//            String permission = (String) it.next();
//            if (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
//                permissionsGranted.add(permission);
//            }
//        }
//        return permissionsGranted;
//    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static void refreshMonitoredList() {
//        ArrayList<String> permissions = getGrantedPermissions();
//        Set<String> set = new HashSet();
//        Iterator it = permissions.iterator();
//        while (it.hasNext()) {
//            set.add((String) it.next());
//        }
//        sharedPreferences.edit().putStringSet(KEY_PREV_PERMISSIONS, set).apply();
//    }

    public static ArrayList<String> getPreviousPermissions() {
        ArrayList<String> prevPermissions = new ArrayList();
        prevPermissions.addAll(sharedPreferences.getStringSet(KEY_PREV_PERMISSIONS, new HashSet()));
        return prevPermissions;
    }

    public static ArrayList<String> getIgnoredPermissions() {
        ArrayList<String> ignoredPermissions = new ArrayList();
        ignoredPermissions.addAll(sharedPreferences.getStringSet(KEY_IGNORED_PERMISSIONS, new HashSet()));
        return ignoredPermissions;
    }

    public static boolean isIgnoredPermission(String permission) {
        if (permission != null && getIgnoredPermissions().contains(permission)) {
            return true;
        }
        return false;
    }

//    public static void ignorePermission(String permission) {
//        if (!isIgnoredPermission(permission)) {
//            ArrayList<String> ignoredPermissions = getIgnoredPermissions();
//            ignoredPermissions.add(permission);
//            Set<String> set = new HashSet();
//            set.addAll(ignoredPermissions);
//            sharedPreferences.edit().putStringSet(KEY_IGNORED_PERMISSIONS, set).apply();
//        }
//    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public static void permissionCompare(PermissionListener permissionListener) {
//        String permission;
//        if (context == null) {
//            throw new RuntimeException("Before comparing permissions you need to call Nammu.init(context)");
//        }
//        ArrayList<String> previouslyGranted = getPreviousPermissions();
//        ArrayList<String> currentPermissions = getGrantedPermissions();
//        Iterator it = getIgnoredPermissions().iterator();
//        while (it.hasNext()) {
//            permission = (String) it.next();
//            if (!(previouslyGranted == null || previouslyGranted.isEmpty() || !previouslyGranted.contains(permission))) {
//                previouslyGranted.remove(permission);
//            }
//            if (!(currentPermissions == null || currentPermissions.isEmpty() || !currentPermissions.contains(permission))) {
//                currentPermissions.remove(permission);
//            }
//        }
//        if (!(currentPermissions == null || currentPermissions.isEmpty())) {
//            it = currentPermissions.iterator();
//            while (it.hasNext()) {
//                permission = (String) it.next();
//                if (previouslyGranted != null && !previouslyGranted.isEmpty() && previouslyGranted.contains(permission)) {
//                    previouslyGranted.remove(permission);
//                } else if (permissionListener != null) {
//                    permissionListener.permissionsChanged(permission);
//                    permissionListener.permissionsGranted(permission);
//                }
//            }
//        }
//        if (!(previouslyGranted == null || previouslyGranted.isEmpty())) {
//            it = previouslyGranted.iterator();
//            while (it.hasNext()) {
//                permission = (String) it.next();
//                if (permissionListener != null) {
//                    permissionListener.permissionsChanged(permission);
//                    permissionListener.permissionsRemoved(permission);
//                }
//            }
//        }
//        refreshMonitoredList();
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkPermission(String permissionName) {
        if (context != null) {
            return context.checkSelfPermission(permissionName) == PackageManager.PERMISSION_GRANTED;
        } else {
            throw new RuntimeException("Before comparing permissions you need to call Nammu.init(context)");
        }
    }
}
