package com.dongdutec.ddmnc.citypicker;

import java.util.List;


public interface CheckPermissionsListener {
    void onGranted();
    void onDenied(List<String> permissions);
}
