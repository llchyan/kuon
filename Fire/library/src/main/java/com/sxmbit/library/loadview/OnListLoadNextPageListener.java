package com.sxmbit.library.loadview;

import android.support.v7.widget.RecyclerView;

/**
 * Created by LinLin on 2015/11/23.
 * RecyclerView/ListView/GridView 滑动加载下一页时的回调接口
 */
public interface OnListLoadNextPageListener {

    /**
     * 开始加载下一页
     *
     * @param recyclerView 当前RecyclerView/ListView/GridView
     */
     void onLoadNextPage(RecyclerView recyclerView);
}
