package com.sxmbit.fire;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by LinLin on 2015/11/20.
 */
public class NetBroadCastReciver extends BroadcastReceiver
{

    /**
     * 只有当网络改变的时候才会 经过广播。
     */

    @Override
    public void onReceive(Context context, Intent intent)
    {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        AlertDialog.Builder ab = new AlertDialog.Builder(context);
//        if (!gprs.isConnected() && !wifi.isConnected())
//        {
//            ab.setMessage("网络连接断开，请检查网络");
//            ab.setPositiveButton("确定", (dialog, which) -> {
//                // TODO Auto-generated method stub
//                dialog.dismiss();
//            });
//        } else
//        {
//            ab.setMessage("网络连接成功");
//            ab.setPositiveButton("确定", (dialog, which) -> {
//                // TODO Auto-generated method stub
//                dialog.dismiss();
//            });
//        }
//        AlertDialog dialog = ab.create();
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.show();
    }

    //    if (NetworkInfo.State.CONNECTED == info.getState())
    //    {
    //        KLog.i("有网络连接");
    //    } else
    //    {
    //        KLog.e("无网络连接");
    //    }
}
