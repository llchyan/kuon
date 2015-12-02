package com.sxmbit.fire.activity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.opengl.Visibility;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sxmbit.fire.R;
import com.sxmbit.fire.model.FoodModel;
import com.sxmbit.fire.utils.KLog;
import com.sxmbit.fire.widget.ShoppingCartView;
import com.sxmbit.fire.widget.SyLinearLayoutManager;
import com.sxmbit.library.loadview.RecyclerGenericityAdapter;
import com.sxmbit.library.loadview.RecyclerHolder;
import com.sxmbit.library.stickyrecyclerview.StickyRecyclerHeadersAdapter;
import com.sxmbit.library.stickyrecyclerview.StickyRecyclerHeadersDecoration;
import com.sxmbit.library.stickyrecyclerview.StickyRecyclerHeadersTouchListener;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ShoppingActivity extends BaseActivity implements ShoppingCartView.PlusMinusListener, View.OnClickListener
{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.shopping_menu_recyclerview)
    RecyclerView mMenuRecyclerview;
    @Bind(R.id.shopping_content_recyclerview)
    RecyclerView mContentRecyclerview;
    @Bind(R.id.shopping_cart_recyclerview)
    RecyclerView mCartRecyclerview;
    @Bind(R.id.shopping_content_shoppingcart)
    ShoppingCartView mShoppingCartView;

    @Bind(R.id.shopping_bottom_background)
    Button mBottomBackground;

    ArrayList<String> title = new ArrayList<>();
    MenuAdapter menuAdapter;

    @Bind(R.id.shopping_cart_price)
    TextView mShoppingCartPrice;
    @Bind(R.id.shopping_cart_clearing)
    Button mShoppingCartClearing;
    @Bind(R.id.shopping_bottom_parent)
    LinearLayoutCompat mShoppingBottomParent;
    SyLinearLayoutManager syLinearLayoutManager;

    int dp_16;
    int dp_80;
    final AnimalsHeadersAdapter adapter = new AnimalsHeadersAdapter();
    RecyclerGenericityAdapter<FoodModel> cartAdater;
    ArrayList<FoodModel> foodList = new ArrayList<>();
    ArrayList<FoodModel> foodList2 = new ArrayList<>();


    @Override
    protected int getContentViewLayoutID()
    {
        return R.layout.activity_shopping;
    }

    @Override
    protected void initView()
    {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null == actionBar)
            return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("购物车demo");
        dp_16 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, getResources().getDisplayMetrics());
        dp_80 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80f, getResources().getDisplayMetrics());

        // Set adapter populated with example dummy data
        //        adapter.add("Animals below!");
        String[] dummyDataSet = getDummyDataSet();

        int id = -1;
        String group_name = "";
        for (String dummy : dummyDataSet)
        {
            FoodModel foodModel = new FoodModel();
            if (!group_name.equals(String.valueOf(dummy.charAt(0))))
            {
                group_name = String.valueOf(dummy.charAt(0));
                title.add("呵呵：" + group_name);
                id++;
            }
            foodModel.group_id = id;
            foodModel.content = "米宝：" + dummy;
            foodModel.food_name = dummy;
            foodModel.group_name = "呵呵：" + group_name;
            foodModel.point_praise = 10;
            foodModel.sales_volume = 5;
            foodModel.score = 0;
            foodModel.unit_price = id;
            foodList.add(foodModel);
        }
        adapter.addAll(foodList);
        mContentRecyclerview.setAdapter(adapter);
        syLinearLayoutManager = new SyLinearLayoutManager(mContext);
        mCartRecyclerview.setLayoutManager(syLinearLayoutManager);

        cartAdater = new RecyclerGenericityAdapter<FoodModel>(foodList2, R.layout.shopping_item)
        {
            @Override
            public void convert(RecyclerHolder holder, FoodModel item, int position)
            {
                holder.setVisibility(R.id.shopping_item_food_picture, View.GONE);
                holder.setVisibility(R.id.shopping_item_food_price, View.GONE);
                holder.setText(R.id.shopping_item_food_name, item.food_name);
                if (item.score == 0)
                {
                    holder.setVisibility(R.id.shopping_item_food_minus, View.GONE);
                } else
                {
                    holder.setVisibility(R.id.shopping_item_food_minus, View.VISIBLE);
                    holder.setText(R.id.shopping_item_food_count, String.valueOf(item.score));
                }
                holder.setOnClickListener(R.id.shopping_item_food_minus, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (item.score != 0)
                        {
                            item.score--;
                            if (item.score == 0)
                            {
                                int indexOf = foodList2.indexOf(item);
                                if (-1 != indexOf)
                                {
                                    foodList2.remove(indexOf);
                                    notifyItemRemoved(indexOf);
                                } else
                                {
                                    notifyItemChanged(position);
                                }
                                mShoppingCartView.minus();
                                if (foodList2.isEmpty())
                                {
                                    calculationLayout(false);
                                }
                                return;
                            }
                            notifyItemChanged(position);
                        }
                        mShoppingCartView.minus();
                    }
                });
                holder.setOnClickListener(R.id.shopping_item_food_plus, new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (item.score == 0)
                        {
                            foodList2.add(item);
                        }
                        item.score++;
                        notifyItemChanged(position);
                        mShoppingCartView.puls(false);
                    }
                });
            }
        };
        cartAdater.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
        {
            @Override
            public void onChanged()
            {
                super.onChanged();
                KLog.i("onChanged");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount)
            {
                super.onItemRangeChanged(positionStart, itemCount);
                KLog.i("onItemRangeChanged 1");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload)
            {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                KLog.i("onItemRangeChanged 2");
                //                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount)
            {
                super.onItemRangeInserted(positionStart, itemCount);
                KLog.i("onItemRangeInserted");
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount)
            {
                super.onItemRangeRemoved(positionStart, itemCount);
                KLog.i("onItemRangeRemoved");
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount)
            {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                KLog.i("onItemRangeMoved");
                adapter.notifyDataSetChanged();
            }
        });

        mCartRecyclerview.setAdapter(cartAdater);
        mCartRecyclerview.setVisibility(View.GONE);
        final LinearLayoutManager manager_content = new LinearLayoutManager(this);
        mContentRecyclerview.setLayoutManager(manager_content);
        mMenuRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mMenuRecyclerview.setAdapter(menuAdapter = new MenuAdapter<String>(title, R.layout.view_item)
        {
            @Override
            public void convert(RecyclerHolder holder, String item, int position)
            {
                TextView textView = (TextView) holder.itemView;
                textView.setText(item);
                if (mSelect == position)
                {
                    textView.setBackgroundResource(android.R.color.holo_green_light);  //选中项背景
                    textView.setTextColor(getResources().getColor(android.R.color.white));
                } else
                {
                    textView.setBackgroundResource(android.R.color.white);  //其他项背景
                    textView.setTextColor(getResources().getColor(R.color.translate_30));
                }

                textView.setOnClickListener(v -> {
                    for (int i = 0, len = adapter.getItemCount(); i < len; i++)
                    {
                        if (item.equals(adapter.getItem(i).group_name))
                        {
                            manager_content.scrollToPositionWithOffset(i, 0);
                            changeSelected(position);
                            break;
                        }
                    }
                });

            }
        });
        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        mContentRecyclerview.addItemDecoration(headersDecor);
        // Add decoration for dividers between list items
        mContentRecyclerview.addItemDecoration(new DividerDecoration(this));
        // Add touch listeners
        StickyRecyclerHeadersTouchListener touchListener =
                new StickyRecyclerHeadersTouchListener(mContentRecyclerview, headersDecor);
        touchListener.setOnHeaderClickListener(
                new StickyRecyclerHeadersTouchListener.OnHeaderClickListener()
                {
                    @Override
                    public void onHeaderClick(View header, int position, long headerId)
                    {
                        Toast.makeText(ShoppingActivity.this, "Header position: " + position + ", id: " + headerId,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        mContentRecyclerview.addOnItemTouchListener(touchListener);
        //        mMenuRecyclerview.setCh

        mContentRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            String headItem = "";
            String firstString = "";

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0)
                {
                    firstString = adapter.getItem(manager_content.findFirstVisibleItemPosition()).group_name;
                } else if (dy < 0)
                {
                    firstString = adapter.getItem(manager_content.findFirstCompletelyVisibleItemPosition()).group_name;
                }
                synchronized (this)
                {
                    if (!headItem.equals(firstString))
                    {
                        headItem = firstString;
                        int indexOf = title.indexOf(firstString);
                        if (-1 == indexOf)
                            return;
                        mMenuRecyclerview.scrollToPosition(indexOf);
                        menuAdapter.changeSelected(indexOf);
                    }
                }
            }
        });

        mBottomBackground.setOnClickListener(this);
        mShoppingCartView.setOnCartClickListener(this);
        mShoppingCartView.setPlusMinusListener(this);
    }

    private String[] getDummyDataSet()
    {
        return getResources().getStringArray(R.array.animals);
    }

    private int getLayoutManagerOrientation(int activityOrientation)
    {
        if (activityOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        {
            return LinearLayoutManager.VERTICAL;
        } else
        {
            return LinearLayoutManager.HORIZONTAL;
        }
    }


    private void calculationMoney()
    {

    }


    private ViewGroup anim_mask_layout;//动画层
    private int anim_mask_layout_id = Integer.MAX_VALUE / 100;
    private ImageView ball;// 小圆点

    /**
     * 创建动画层
     */
    private ViewGroup createAnimLayout()
    {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(anim_mask_layout_id);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }


    private View addViewToAnimLayout(final ViewGroup parent, final View view,
                                     int[] location)
    {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    private void setAnim(final FoodModel foodModel, final int position, final View v, int[] startLocation)
    {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v, startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        mShoppingCartView.getFood_count().getLocationInWindow(endLocation);// shopCart是那个购物车

        // 计算位移
        int endX = 0 - startLocation[0] + 40 + (dp_80 / 2);// 动画位移的X坐标,这里我添加了leftMargin,不然对应的位置不对
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标
        TranslateAnimation translateAnimationX = new TranslateAnimation(0, endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(200);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener()
        {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation)
            {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
                // TODO Auto-generated method stub
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation)
            {
                v.setVisibility(View.GONE);
                foodModel.score++;
                adapter.notifyItemChanged(position);
                mShoppingCartView.puls(true);
            }
        });

    }

    @Override
    public void plus(int afterCount)
    {
        if (afterCount > 0)
        {
            mShoppingCartPrice.setTextColor(getResources().getColor(R.color.red));

            int totlalmoney = 0;
            for (FoodModel model : foodList2)
            {
                totlalmoney += model.unit_price * model.score;
            }

            if (totlalmoney >= 20)
            {
                mShoppingCartClearing.setText("去结算");
                mShoppingCartClearing.setTextColor(getResources().getColor(R.color.red));
            } else
            {
                mShoppingCartClearing.setText("还差¥ " + (20 - totlalmoney));
                mShoppingCartClearing.setTextColor(getResources().getColor(R.color.translate_30));

            }
            mShoppingCartPrice.setText("¥ " + totlalmoney);
        }
    }

    @Override
    public void minus(int afterCount)
    {
        if (afterCount == 0)
        {
            mShoppingCartClearing.setTextColor(getResources().getColor(R.color.translate_30));
            mShoppingCartPrice.setTextColor(getResources().getColor(R.color.translate_30));
            mShoppingCartClearing.setText("还差¥ 20");
            mShoppingCartPrice.setText("购物车空空如也~");
        } else
        {
            int totlalmoney = 0;
            for (FoodModel model : foodList2)
            {
                totlalmoney += model.unit_price * model.score;
            }

            if (totlalmoney >= 20)
            {
                mShoppingCartClearing.setText("去结算");
                mShoppingCartClearing.setTextColor(getResources().getColor(R.color.red));
            } else
            {
                mShoppingCartClearing.setText("还差¥ " + (20 - totlalmoney));
                mShoppingCartClearing.setTextColor(getResources().getColor(R.color.translate_30));

            }
            mShoppingCartPrice.setTextColor(getResources().getColor(R.color.red));
            mShoppingCartPrice.setText("¥ " + totlalmoney);
        }
    }


    /**
     * 购物车点击事件，以及阴影背景点击事件
     */
    @Override
    public void onClick(View v)
    {
        calculationLayout(true);
    }


    private void calculationLayout(boolean isNotPop)
    {
        if (mShoppingCartView.getCount() == 0 && isNotPop)
        {
            return;
        }

        RelativeLayout.LayoutParams cart_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutCompat.LayoutParams price_arams = (LinearLayoutCompat.LayoutParams) mShoppingCartPrice.getLayoutParams();
        if (mCartRecyclerview.getVisibility() == View.GONE)
        {
            cartAdater.notifyDataSetChanged();
            LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams) mCartRecyclerview.getLayoutParams();
            if (cartAdater.getItemCount() > 2)
            {
                layoutParams.height = (int) (mScreenHeight * 0.4);
            }
            mCartRecyclerview.setLayoutParams(layoutParams);
            mCartRecyclerview.setVisibility(View.VISIBLE);
            cart_params.addRule(RelativeLayout.ABOVE, R.id.shopping_bottom_parent);
            cart_params.leftMargin = dp_16;
            mShoppingCartView.setLayoutParams(cart_params);
            mBottomBackground.setVisibility(View.VISIBLE);
            price_arams.leftMargin = dp_16;
            mShoppingCartPrice.setLayoutParams(price_arams);
        } else
        {
            mCartRecyclerview.setVisibility(View.GONE);
            cart_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.id.shopping_parent);
            cart_params.leftMargin = dp_16;
            cart_params.bottomMargin = dp_16;
            mShoppingCartView.setLayoutParams(cart_params);
            mBottomBackground.setVisibility(View.GONE);
            price_arams.leftMargin = dp_80;
            mShoppingCartPrice.setLayoutParams(price_arams);
        }
    }


    private class AnimalsHeadersAdapter extends ShoppingAdapter<RecyclerHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>
    {
        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shopping_item, parent, false);
            return RecyclerHolder.get(view);
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position)
        {
            final FoodModel item = getItem(position);
            holder.setText(R.id.shopping_item_food_name, item.food_name);
            holder.setText(R.id.shopping_item_food_price, "¥ " + item.unit_price + "/份");
            if (item.score == 0)
            {
                holder.setVisibility(R.id.shopping_item_food_minus, View.GONE);
            } else
            {
                holder.setVisibility(R.id.shopping_item_food_minus, View.VISIBLE);
                holder.setText(R.id.shopping_item_food_count, String.valueOf(item.score));
            }
            holder.setOnClickListener(R.id.shopping_item_food_minus, new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (item.score != 0)
                    {
                        item.score--;
                        if (item.score == 0)
                        {
                            int indexOf = foodList2.indexOf(item);
                            if (-1 != indexOf)
                            {
                                foodList2.remove(indexOf);
                            }
                            notifyItemChanged(position);
                            mShoppingCartView.minus();
                            return;
                        }
                        notifyItemChanged(position);
                        mShoppingCartView.minus();
                    }
                }
            });
            holder.setOnClickListener(R.id.shopping_item_food_plus, new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (item.score == 0)
                    {
                        foodList2.add(item);
                    }
                    int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                    v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                    ball = new ImageView(mContext);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
                    ball.setImageResource(R.drawable.shoppingcart_anim_oval);// 设置buyImg的图片
                    setAnim(item, position, ball, startLocation);// 开始执行动画
                }
            });
        }

        @Override
        public long getHeaderId(int position)
        {
            return getItem(position).group_id;
        }


        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_header, parent, false);
            return new RecyclerView.ViewHolder(view)
            {
            };
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            TextView textView = (TextView) holder.itemView;
            textView.setText(getItem(position).group_name);
            holder.itemView.setBackgroundColor(getRandomColor());
        }

        private int getRandomColor()
        {
            SecureRandom rgen = new SecureRandom();
            return Color.HSVToColor(0xFF, new float[]{
                    rgen.nextInt(359), 1, 1
            });
        }

    }

    private abstract class MenuAdapter<String> extends RecyclerGenericityAdapter<String>
    {

        int mSelect = 0;   //选中项

        public MenuAdapter(@NonNull List<String> mDatas, @LayoutRes int layoutId)
        {
            super(mDatas, layoutId);
        }


        public void changeSelected(int positon)
        {
            final int pre = mSelect;
            if (positon != mSelect)
            {
                mSelect = positon;
                notifyItemChanged(pre);
                notifyItemChanged(positon);
            }
        }
    }

}
