package com.sxmbit.library.loadview;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by LinLin on 2015/7/18.ListView通用的Adapter
 */
@SuppressWarnings("unused")
public abstract class ListGenericityAdapter<T> extends BaseAdapter
{
    /**
     * 数据源
     */
    protected List<T> mDatas;
    /**
     * 布局集合
     */
    private int layoutId;
    /**
     * 默认item type
     */
    protected static final int DEFAULT_TYPE = 0;

    private ListGenericityAdapter()
    {
        throw new IllegalStateException("不允许建立对象");
    }

    public ListGenericityAdapter(@NonNull List<T> mDatas, @LayoutRes int layoutId)
    {
        this.mDatas = mDatas;
        this.layoutId = layoutId;
    }


    @Override
    public int getViewTypeCount()
    {
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position)
    {
        return DEFAULT_TYPE;
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public T getItem(int position)
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
        T item = getItem(position);
        if (null != item)
            convert(viewHolder, item, position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(CommonViewHolder helper, T item, int position);

    private CommonViewHolder getViewHolder(int position, @NonNull View convertView, @NonNull ViewGroup parent)
    {
        return CommonViewHolder.get(convertView, parent, layoutId, position);
    }


    public void addItem(int position, @NonNull T item)
    {
        if (position < 0)
            mDatas.add(0, item);
        else if (position > mDatas.size())
            mDatas.add(item);
        else
            mDatas.add(position, item);
        notifyDataSetChanged();
    }

    public void addData(@NonNull T item)
    {
        mDatas.add(item);
        notifyDataSetChanged();
    }

    public void changeItem(int position, @NonNull T item)
    {
        if (position < 0)
            mDatas.set(0, item);
        else if (position > mDatas.size())
            mDatas.add(item);
        else
            mDatas.set(position, item);
        notifyDataSetChanged();
    }

    public void addAll(int position, @NonNull List<T> list)
    {
        if (position < 0)
            mDatas.addAll(0, list);
        else if (position > mDatas.size())
            mDatas.addAll(list);
        else
            mDatas.addAll(position, list);
        notifyDataSetChanged();
    }

    public void addAll(@NonNull List<T> list)
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


    public void removeItem(@NonNull T item)
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

    public void replaceAll(List<T> list)
    {
        clearAll();
        addAll(list);
    }

}