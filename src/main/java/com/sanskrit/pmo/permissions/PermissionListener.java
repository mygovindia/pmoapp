package com.sanskrit.pmo.permissions;

public interface PermissionListener {
    void permissionsChanged(String str);

    void permissionsGranted(String str);

    void permissionsRemoved(String str);
}
