package com.sxmbit.library.loadview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sxmbit.library.R;


/**
 * Created by LinLin on 2015/10/29.LoadingRecyclerView
 */
@SuppressWarnings("unused")
public class LoadingRecyclerView extends FrameLayout
{
    /**
     * 空白页，错误页，加载页，内容页
     */
    private int id_loadingView, id_emptyView, id_errorView, id_contentView;
    private LayoutInflater inflater;
    private OnClickListener onRetryClickListener;
    private RecyclerView mRecycler;
    private SwipeRefreshLayout mPtrLayout;
    private boolean hasFixedSize;
    private Button btn_error_retry;
    private TextView text_empty_retry, text_error_retry;
    public static final int NODRAWABLE = -1;
    //    private ArrayList<View> views = new ArrayList<>(4);

    public LoadingRecyclerView(@NonNull Context context)
    {
        super(context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        id_emptyView = R.layout.loadinglayout_empty_view;
        id_errorView = R.layout.loadinglayout_error_view;
        id_loadingView = R.layout.loadinglayout_loading_view;
        id_contentView = R.layout.loadinglayout_content_recyclerview;
        initView();
    }

    public LoadingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
        initView();
    }

    public LoadingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs);
        initView();
    }

    private void init(AttributeSet attrs)
    {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LoadingRecyclerview);
        id_emptyView = a.getResourceId(R.styleable.LoadingRecyclerview_loadingRcv_emptyView, R.layout.loadinglayout_empty_view);
        id_errorView = a.getResourceId(R.styleable.LoadingRecyclerview_loadingRcv_errorView, R.layout.loadinglayout_error_view);
        id_loadingView = a.getResourceId(R.styleable.LoadingRecyclerview_loadingRcv_loadingView, R.layout.loadinglayout_loading_view);
        id_contentView = a.getResourceId(R.styleable.LoadingRecyclerview_loadingRcv_contentLayoutId, R.layout.loadinglayout_content_recyclerview);
        hasFixedSize = a.getBoolean(R.styleable.LoadingRecyclerview_loadingRcv_hasFixedSize, true);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        a.recycle();
    }

    private void initView()
    {
        if (isInEditMode())
        {
            return;
        }
        inflater.inflate(id_loadingView, this, true);
        inflater.inflate(id_emptyView, this, true);
        inflater.inflate(id_errorView, this, true);
        inflater.inflate(id_contentView, this, true);
        mPtrLayout = (SwipeRefreshLayout) findViewById(R.id.ptr_layout);
        mRecycler = (RecyclerView) findViewById(android.R.id.list);
        mPtrLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
        mRecycler.setHasFixedSize(hasFixedSize);
        mRecycler.setAdapter(new RecyclerView.Adapter<RecyclerHolder>()
        {
            @Override
            public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType)
            {
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerHolder holder, int position)
            {

            }

            @Override
            public int getItemCount()
            {
                return 0;
            }
        });


        //        btn_empty_retry = (Button) findViewById(R.id.btn_empty_retry);
        btn_error_retry = (Button) findViewById(R.id.btn_error_retry);
        text_empty_retry = (TextView) findViewById(R.id.text_empty_retry);
        text_error_retry = (TextView) findViewById(R.id.text_error_retry);
        //        btn_empty_retry.setOnClickListener(listener);
        btn_error_retry.setOnClickListener(listener);
    }

    /**
     * Set the layout manager to the recycler
     */
    public void setLayoutManager(@NonNull RecyclerView.LayoutManager manager)
    {
        mRecycler.setLayoutManager(manager);
    }

    public void setVerticalScrollBarEnabled(boolean isVer)
    {
        mRecycler.setVerticalScrollBarEnabled(isVer);
    }

    public void setHorizontalScrollBarEnabled(boolean isHor)
    {
        mRecycler.setHorizontalScrollBarEnabled(isHor);
    }

    public RecyclerView.LayoutManager getLayoutManager()
    {
        return mRecycler.getLayoutManager();
    }

    public void setAdapter(@NonNull RecyclerView.Adapter adapter)
    {
        mRecycler.setAdapter(adapter);
        mPtrLayout.setRefreshing(false);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
        {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount)
            {
                super.onItemRangeChanged(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount)
            {
                super.onItemRangeInserted(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount)
            {
                super.onItemRangeRemoved(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount)
            {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                update();
            }

            @Override
            public void onChanged()
            {
                super.onChanged();
                update();
            }
        });
    }

    /**
     * Remove the adapter from the recycler
     */
    public void clear()
    {
        mRecycler.setAdapter(null);
    }

    public void update()
    {
        mPtrLayout.setRefreshing(false);
        RecyclerView.Adapter adapter = mRecycler.getAdapter();
        if (null == adapter)
        {
            showError();
            return;
        }
        if (adapter.getItemCount() == 0)
        {
            showEmpty();
            return;
        }
        if (adapter instanceof LoadingRecyclerAdapter)
        {
            ((LoadingRecyclerAdapter) adapter).setIsLoading(false);
        }
        showContent();
    }

    public void onError()
    {
        mPtrLayout.setRefreshing(false);
        RecyclerView.Adapter adapter = mRecycler.getAdapter();
        if (null == adapter || adapter.getItemCount() == 0)
        {
            showError();
            return;
        }
        showContent();
        if (adapter instanceof LoadingRecyclerAdapter)
        {
            ((LoadingRecyclerAdapter) adapter).setIsLoading(false);
        }
        Snackbar.make(mRecycler, "加载失败", Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 有可能是用户未登录
     */
    public void onError2()
    {
        mPtrLayout.setRefreshing(false);
        RecyclerView.Adapter adapter = mRecycler.getAdapter();
        if (null == adapter || adapter.getItemCount() == 0)
        {
            showError();
            return;
        }
        if (adapter instanceof LoadingRecyclerAdapter)
        {
            ((LoadingRecyclerAdapter) adapter).setIsLoading(false);
        }
        showContent();
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        for (int i = 0, count = getChildCount(); i < count; i++)
        {
            getChildAt(i).setVisibility(i == 0 ? VISIBLE : GONE);//只显示加载页
        }
    }

    public void setOnRetryClickListener(OnClickListener onRetryClickListener)
    {
        this.onRetryClickListener = onRetryClickListener;
    }

    public void setEmptyTextDrawable(String text)
    {
        if (!TextUtils.isEmpty(text))
        {
            text_empty_retry.setText(text);
        }
    }

    public void setErrorTextDrawable(String text, @DrawableRes int resId)
    {
        if (!TextUtils.isEmpty(text))
        {
            text_error_retry.setText(text);
        }
        if (resId != NODRAWABLE)
        {
            btn_error_retry.setBackgroundResource(resId);
        }
    }

    public void setRefreshing(boolean refreshing)
    {
        mPtrLayout.setRefreshing(refreshing);
    }

    public void setRefreshEnabled(boolean enabled)
    {
        mPtrLayout.setEnabled(enabled);
    }

    /**
     * Set the listener when refresh is triggered and enable the SwipeRefreshLayout
     */
    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener)
    {
        mPtrLayout.setEnabled(true);
        mPtrLayout.setOnRefreshListener(listener);
    }

    /**
     * Set the colors for the SwipeRefreshLayout states
     */

    public void setRefreshingColorResources(@ColorRes int colRes1, @ColorRes int colRes2, @ColorRes int colRes3, @ColorRes int colRes4)
    {
        mPtrLayout.setColorSchemeResources(colRes1, colRes2, colRes3, colRes4);
    }

    /**
     * Set the colors for the SwipeRefreshLayout states
     */
    public void setRefreshingColor(@ColorRes int col1, @ColorRes int col2, @ColorRes int col3, @ColorRes int col4)
    {
        mPtrLayout.setColorSchemeColors(col1, col2, col3, col4);
    }

    public void showLoading()
    {
        for (int i = 0, count = getChildCount(); i < count; i++)
        {
            getChildAt(i).setVisibility(i == 0 ? VISIBLE : GONE);//只显示加载页
        }
    }

    public void showEmpty()
    {
        for (int i = 0, count = getChildCount(); i < count; i++)
        {
            getChildAt(i).setVisibility(i == 1 ? VISIBLE : GONE);//只显示空白页
        }
    }

    public void showError()
    {
        for (int i = 0, count = getChildCount(); i < count; i++)
        {
            getChildAt(i).setVisibility(i == 2 ? VISIBLE : GONE);//只显示错误页
        }
    }


    public void showContent()
    {
        for (int i = 0, count = getChildCount(); i < count; i++)
        {
            getChildAt(i).setVisibility(i == 3 ? VISIBLE : GONE);//只显示内容页
        }
    }

    /**
     * Add the onItemTouchListener for the recycler
     */
    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener)
    {
        mRecycler.addOnItemTouchListener(listener);
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener)
    {
        mRecycler.addOnScrollListener(listener);
    }

    /**
     * Remove the onItemTouchListener for the recycler
     */
    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener)
    {
        mRecycler.removeOnItemTouchListener(listener);
    }

    public void setOnTouchListener(OnTouchListener listener)
    {
        mRecycler.setOnTouchListener(listener);
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator)
    {
        mRecycler.setItemAnimator(animator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration)
    {
        mRecycler.addItemDecoration(itemDecoration);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index)
    {
        mRecycler.addItemDecoration(itemDecoration, index);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration)
    {
        mRecycler.removeItemDecoration(itemDecoration);
    }

    public RecyclerView getRecycler()
    {
        return mRecycler;
    }

    private OnClickListener listener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == R.id.btn_error_retry && null != onRetryClickListener)
            {
                onRetryClickListener.onClick(v);
            }
        }
    };

}
