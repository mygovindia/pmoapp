package com.sanskrit.pmo.permissions;

import java.security.SecureRandom;
import java.util.ArrayList;

public class PermissionRequest {
    private static SecureRandom random;
    private PermissionCallback permissionCallback;
    private ArrayList<String> permissions;
    private int requestCode;

    public PermissionRequest(int requestCode) {
        this.requestCode = requestCode;
    }

    public PermissionRequest(ArrayList<String> permissions, PermissionCallback permissionCallback) {
        this.permissions = permissions;
        this.permissionCallback = permissionCallback;
        if (random == null) {
            random = new SecureRandom();
        }
        this.requestCode = random.nextInt(32768);
    }

    public ArrayList<String> getPermissions() {
        return this.permissions;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public PermissionCallback getPermissionCallback() {
        return this.permissionCallback;
    }

    public boolean equals(Object object) {
        if (object != null && (object instanceof PermissionRequest) && ((PermissionRequest) object).requestCode == this.requestCode) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.requestCode;
    }
}
