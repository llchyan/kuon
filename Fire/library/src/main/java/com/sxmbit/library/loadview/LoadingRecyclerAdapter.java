package com.sxmbit.library.loadview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sxmbit.library.R;

import java.util.List;

@SuppressWarnings("unused")
public abstract class LoadingRecyclerAdapter extends RecyclerView.Adapter<RecyclerHolder>
{
    /**
     * 数据源
     */
    private List<ViewItem> mDatas;
    /**
     * 布局集合
     */
    private SparseIntArray layoutIds;
    /**
     * 是否正在加载
     */
    private boolean isLoading;
    /**
     * 是否加载完所有数据
     */
    private boolean isCompleted;

    /**
     * 加载更多回调接口
     */
    private OnLoadMore callback;
    private LayoutInflater inflater;

    /**
     * 当前页码
     */
    public int curpage = 0;
    /**
     * 当前页数据量
     */
    public int pagecount = 0;
    /**
     * 每页数据长度
     */
    public int everyCount = 0;
    /**
     * 默认的每页数据长度,10个
     */
    public static final int defaultCount = 10;


    //    public static final String ZERO = "0";
    //    public static final String CURPAGE = "curpage";
    //    public static final String PAGESIZE = "pagesize";

    private LoadingRecyclerAdapter()
    {
        throw new IllegalStateException("不允许调用默认构造器");
    }


    public LoadingRecyclerAdapter(int pageCount, @NonNull List<ViewItem> mDatas, @LayoutRes int layoutId)
    {
        this.mDatas = mDatas;
        if (layoutIds == null)
        {
            layoutIds = new SparseIntArray(2);
        }
        layoutIds.put(ViewItem.FOOT_TYPE, R.layout.footer_view);
        layoutIds.put(ViewItem.DEFAULT_TYPE, layoutId);
        this.everyCount = pageCount;
        if (this.mDatas.size() >= this.everyCount)
        {
            this.mDatas.add(getLoadMoreItem());
        }
    }

    public LoadingRecyclerAdapter(int pageCount, @NonNull List<ViewItem> mDatas, @NonNull SparseIntArray layoutIds)
    {
        this.mDatas = mDatas;
        layoutIds.put(ViewItem.FOOT_TYPE, R.layout.footer_view);
        this.layoutIds = layoutIds;
        this.everyCount = pageCount;
        if (this.mDatas.size() >= this.everyCount)
        {
            this.mDatas.add(getLoadMoreItem());
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        return mDatas.isEmpty() ? ViewItem.DEFAULT_TYPE : mDatas.get(position).viewType;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (null == inflater)
        {
            synchronized (this)
            {
                if (null == inflater)
                {
                    inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                }
            }
        }
        return RecyclerHolder.get(inflater.inflate(layoutIds.get(viewType), parent, false));
    }


    /**
     * 当GridLayoutManager时，设置头部type或者尾部type占一行
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager)
        {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
            {
                @Override
                public int getSpanSize(int position)
                {
                    int type = getItemViewType(position);
                    if (type == ViewItem.HEAD_TYPE || type == ViewItem.FOOT_TYPE)
                    {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

    /**
     * StaggeredGridLayoutManager时，设置头部type或者尾部type占一行
     */
    @Override
    public void onViewAttachedToWindow(RecyclerHolder holder)
    {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams)
        {
            int type = getItemViewType(holder.getLayoutPosition());
            if (type == ViewItem.HEAD_TYPE || type == ViewItem.FOOT_TYPE)
            {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position)
    {
        int type = getItemViewType(position);
        if (type == ViewItem.FOOT_TYPE)
        {
            if (curpage >= pagecount)//数量满了，就返回
            {
                isLoading = false;
                return;
            }
            if (isLoading)//正在加载，就返回
            {
                return;
            }
            if (callback != null)
            {
                isLoading = true;
                callback.loadMore(holder.getConvertView());
                return;
            }
            return;
        }
        convert(holder, mDatas.get(position), type, position);

    }

    public abstract void convert(RecyclerHolder holder, ViewItem viewItem, int type, int position);


    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    public List<ViewItem> getDataList()
    {
        return mDatas;
    }

    public synchronized void addItem(@NonNull ViewItem viewItem)
    {
        mDatas.add(viewItem);
        notifyItemInserted(mDatas.size() - 1);
    }

    public synchronized void addItem(int position, @NonNull ViewItem item)
    {
        if (position < 0)
        {
            mDatas.add(0, item);
            notifyItemInserted(0);
            return;
        }
        if (position > mDatas.size())
        {
            mDatas.add(item);
            notifyItemInserted(mDatas.size() - 1);
            return;
        }
        mDatas.add(position, item);
        notifyItemInserted(position);
    }

    public synchronized void remove(int position)
    {
        if (mDatas.isEmpty())
            return;
        if (position < 0 || position >= mDatas.size())
        {
            notifyDataSetChanged();
            return;
        }
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public synchronized void remove(@NonNull ViewItem item)
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
        notifyItemRemoved(i);
    }

    public synchronized void set(int position, @NonNull ViewItem item)
    {
        if (mDatas.isEmpty())
            return;
        if (position < 0 || position >= mDatas.size())
        {
            notifyDataSetChanged();
            return;
        }
        mDatas.set(position, item);
        notifyItemChanged(position);
    }

    public synchronized void addAll(@NonNull List<ViewItem> newData)
    {
        hideLoadMore();
        if (newData.isEmpty())//当没有新数据的时候
        {
            notifyDataSetChanged();
            return;
        }

        if (newData.size() == everyCount && curpage < pagecount)//当前页小于总页数(页数从1开始的说...)
        {
            newData.add(getLoadMoreItem());
        }
        int start = mDatas.size();
        mDatas.addAll(newData);
        notifyItemRangeInserted(start, newData.size());
    }


    public synchronized void replaceAll(@NonNull List<ViewItem> newData)
    {
        mDatas.clear();
        if (newData.isEmpty())//当没有新数据的时候
        {
            notifyDataSetChanged();
            return;
        }
        notifyDataSetChanged();
        if (newData.size() == everyCount && curpage < pagecount)//当前页小于总页数(页数从1开始的说...)
        {
            newData.add(getLoadMoreItem());
        }
        mDatas.addAll(newData);
        notifyItemRangeInserted(0, newData.size());
    }


    public synchronized void clearAll()
    {
        if (mDatas.isEmpty())
            return;
        int size = mDatas.size();
        mDatas.clear();
        notifyItemMoved(0, size);
    }

    public void setLoadMoreCallback(@NonNull OnLoadMore callback)
    {
        this.callback = callback;
    }


    private ViewItem getLoadMoreItem()
    {
        return new ViewItem(ViewItem.FOOT_TYPE, null);
    }

    public synchronized void hideLoadMore()
    {
        if (mDatas.isEmpty())
            return;
        int lastPosition = mDatas.size() - 1;
        ViewItem viewItem = mDatas.get(lastPosition);
        if (viewItem.viewType == ViewItem.FOOT_TYPE)
        {
            mDatas.remove(lastPosition);
            notifyItemRemoved(lastPosition);
        }
    }

    public boolean isLoading()
    {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading)
    {
        this.isLoading = isLoading;
    }

    public interface OnLoadMore
    {
        void loadMore(View view);
    }


}
