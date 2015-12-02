package com.sxmbit.fire.widget;

import android.content.Context;
import android.content.Intent;
import android.view.SubMenu;
import android.view.View;

import com.sxmbit.fire.R;
import com.sxmbit.fire.activity.ChatActivity;
import com.sxmbit.fire.activity.NotifyActivity;
import com.sxmbit.fire.activity.RichScanActivity;
import com.sxmbit.fire.activity.ShoppingActivity;
import com.sxmbit.fire.activity.SrollActivity;
import com.sxmbit.fire.utils.KLog;

/**
 * Created by LinLin on 2015/11/18,仿微信的plus下拉菜单
 */
public class PlusActionProvider extends android.support.v7.widget.ShareActionProvider
{
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public PlusActionProvider(Context context)
    {
        super(context);
    }

    @Override
    public View onCreateActionView()
    {

        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu)
    {
        //super.onPrepareSubMenu(subMenu);
        //如果不用与父activity交流，那就用这个，否则在父activity创建
        subMenu.clear();
        subMenu.add("发起群聊").setIcon(R.drawable.ic_chat_white_24dp).setOnMenuItemClickListener(item -> {
//            KLog.i("发起群聊");
            Intent intent = new Intent(getContext(), ChatActivity.class);
            getContext().startActivity(intent);
            return true;
        });
        subMenu.add("添加朋友").setIcon(R.drawable.ic_group_add_white_24dp).setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(getContext(), SrollActivity.class);
            getContext().startActivity(intent);
            return true;
        });
        subMenu.add("扫一扫").setIcon(R.drawable.ic_filter_vintage_white_24dp).setOnMenuItemClickListener(item -> {
            KLog.i("扫一扫");
            Intent intent = new Intent(getContext(), RichScanActivity.class);
            getContext().startActivity(intent);
            return true;
        });
        subMenu.add("收钱").setIcon(R.drawable.ic_account_balance_wallet_white_24dp).setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(getContext(), NotifyActivity.class);
            getContext().startActivity(intent);
            return true;
        });
        subMenu.add("帮助与反馈").setIcon(R.drawable.ic_mail_white_24dp).setOnMenuItemClickListener(item -> {
            KLog.i("帮助与反馈");
            Intent intent = new Intent(getContext(), ShoppingActivity.class);
            getContext().startActivity(intent);
            return true;
        });
    }


    @Override
    public boolean hasSubMenu()
    {
        //return super.hasSubMenu();因为有子菜单，所有返回true
        return true;
    }


}
