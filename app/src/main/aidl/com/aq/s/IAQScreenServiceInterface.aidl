package com.aq.isxposedinstall;

import com.aq.isxposedinstall.IAQScreenCallbackInterface;

interface IAQScreenServiceInterface {

    void onSreenStatusChanged(int scrrenStatus);

    void registScreenCallback(IAQScreenCallbackInterface callback);
}
