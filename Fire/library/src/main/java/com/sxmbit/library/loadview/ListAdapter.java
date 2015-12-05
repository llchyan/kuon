package com.sxmbit.library.loadview;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by LinLin on 2015/7/18.ListView通用的Adapter
 */
@SuppressWarnings("unused")
public abstract class ListAdapter extends BaseAdapter
{
    /**
     * 数据源
     */
    protected List<ViewItem> mDatas;
    /**
     * 布局集合
     */
    private SparseIntArray ids;
    /**
     * 默认item type
     */
    protected static final int DEFAULT_TYPE = 0;

    private ListAdapter()
    {
        throw new IllegalStateException("不允许建立对象");
    }

    public ListAdapter(@NonNull List<ViewItem> mDatas,@LayoutRes int layoutId)
    {
        this.mDatas = mDatas;
        this.ids = new SparseIntArray(1);
        this.ids.append(0, layoutId);
    }

    public ListAdapter(@NonNull List<ViewItem> mDatas,@NonNull  SparseIntArray ids)
    {
        this.mDatas = mDatas;
        this.ids = ids;
    }

    @Override
    public int getViewTypeCount()
    {
        return ids.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return mDatas.isEmpty() ? DEFAULT_TYPE : mDatas.get(position).viewType;
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public ViewItem getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final CommonViewHolder viewHolder = getViewHolder(position, convertView, parent);
        ViewItem item = getItem(position);
        if (null != item)
            convert(viewHolder, item, item.viewType, position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(CommonViewHolder helper, ViewItem item, int type, int position);

    private CommonViewHolder getViewHolder(int position,@NonNull  View convertView,@NonNull  ViewGroup parent)
    {
        return CommonViewHolder.get(convertView, parent, ids.get(getItemViewType(position)), position);
    }


    public void addItem(int position,@NonNull  ViewItem item)
    {
        if (position < 0)
            mDatas.add(0, item);
        else if (position > mDatas.size())
            mDatas.add(item);
        else
            mDatas.add(position, item);
        notifyDataSetChanged();
    }

    public void addData(@NonNull ViewItem item)
    {
        mDatas.add(item);
        notifyDataSetChanged();
    }

    public void changeItem(int position,@NonNull  ViewItem item)
    {
        if (position < 0)
            mDatas.set(0, item);
        else if (position > mDatas.size())
            mDatas.add(item);
        else
            mDatas.set(position, item);
        notifyDataSetChanged();
    }

    public void addAll(int position,@NonNull  List<ViewItem> list)
    {
        if (position < 0)
            mDatas.addAll(0, list);
        else if (position > mDatas.size())
            mDatas.addAll(list);
        else
            mDatas.addAll(position, list);
        notifyDataSetChanged();
    }

    public void addAll(@NonNull List<ViewItem> list)
    {
        mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public void removeItem(int position)
    {
        if (mDatas.isEmpty())
            return;
        if (position >= 0 && position < mDatas.size())
            mDatas.remove(position);
        notifyDataSetChanged();
    }


    public void removeItem(@NonNull ViewItem item)
    {
        if (mDatas.isEmpty())
            return;
        int i = mDatas.indexOf(item);
        if (i < 0)
        {
            notifyDataSetChanged();
            return;
        }
        mDatas.remove(item);
        notifyDataSetChanged();
    }



    public void clearAll()
    {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void replaceAll(List<ViewItem> list)
    {
        clearAll();
        addAll(list);
    }

}