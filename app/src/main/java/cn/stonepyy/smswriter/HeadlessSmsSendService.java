package cn.stonepyy.smswriter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by pyystone on 2017/1/24.
 * email: pyystone@163.com
 * QQ: 862429936
 * github: https://github.com/pyystone
 */
public class HeadlessSmsSendService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
