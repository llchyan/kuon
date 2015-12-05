package com.sxmbit.fire.activity;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.internal.widget.ListViewCompat;
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
import com.sxmbit.fire.widget.ShoppingCartView;
import com.sxmbit.library.loadview.CommonAdapter;
import com.sxmbit.library.loadview.CommonViewHolder;
import com.sxmbit.library.loadview.RecyclerGenericityAdapter;
import com.sxmbit.library.loadview.RecyclerHolder;
import com.sxmbit.library.stickyrecyclerview.StickyRecyclerHeadersAdapter;
import com.sxmbit.library.stickyrecyclerview.StickyRecyclerHeadersDecoration;
import com.sxmbit.library.stickyrecyclerview.StickyRecyclerHeadersTouchListener;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 *
 */
public class ShoppingActivity extends BaseActivity
{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.shopping_menu_recyclerview)
    RecyclerView mMenuRecyclerview;
    @Bind(R.id.shopping_content_recyclerview)
    RecyclerView mContentRecyclerview;
    @Bind(R.id.shopping_cart_recyclerview)
    ListViewCompat mCartRecyclerview;
    @Bind(R.id.shopping_content_shoppingcart)
    ShoppingCartView mShoppingCartView;

    @Bind(R.id.shopping_bottom_background)
    Button mBottomBackground;


    @Bind(R.id.shopping_cart_price)
    TextView mShoppingCartPrice;
    @Bind(R.id.shopping_cart_clearing)
    Button mShoppingCartClearing;


    private final AnimalsHeadersAdapter adapter = new AnimalsHeadersAdapter();


    private ArrayList<FoodModel> foodList = new ArrayList<>();
    private ArrayList<FoodModel> foodList2 = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();

    private LinearLayoutManager manager_content;

    private MenuAdapter menuAdapter = new MenuAdapter<String>(title, R.layout.view_item)
    {
        @Override
        public void convert(RecyclerHolder holder, String item, int position)
        {
            TextView textView = (TextView) holder.itemView;
            textView.setText(item);
            if (mSelect == position)
            {
                textView.setBackgroundResource(android.R.color.holo_green_light);  //选中项背景
                textView.setTextColor(Color.WHITE);
            } else
            {
                textView.setBackgroundResource(android.R.color.white);  //其他项背景
                textView.setTextColor(color_gray);
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
    };
    /**
     * 购物车点击事件，以及阴影背景点击事件
     */
    private View.OnClickListener cartClickListener = v -> calculationLayout(false);


    private ShoppingCartView.PlusMinusListener plusMinusListener = new ShoppingCartView.PlusMinusListener()
    {
        /**
         * 对mShoppingCartView的加一的监听变化
         * 计算总价，超过配送额，就可以结算了。
         *
         * @param afterCount 加完之后的数量
         */
        @Override
        public void plus(int afterCount)
        {
            if (afterCount > 0)
            {
                mShoppingCartPrice.setTextColor(color_red);

                int totlalmoney = 0;
                for (FoodModel model : foodList2)
                {
                    totlalmoney += model.unit_price * model.score;
                }

                if (totlalmoney >= 20)
                {
                    mShoppingCartClearing.setText("去结算");
                    mShoppingCartClearing.setTextColor(color_red);
                } else
                {
                    mShoppingCartClearing.setText(String.format("还差¥ %d", 20 - totlalmoney));
                    mShoppingCartClearing.setTextColor(color_gray);

                }
                mShoppingCartPrice.setText(String.format("¥ %d", totlalmoney));
            }
        }

        /**
         * 对mShoppingCartView的减一的监听变化
         * 计算总价，低于配送额，就无法结算了。
         *
         * @param afterCount 减完之后的数量
         */
        @Override
        public void minus(int afterCount)
        {
            if (afterCount == 0)
            {
                mShoppingCartClearing.setTextColor(color_gray);
                mShoppingCartPrice.setTextColor(color_gray);
                mShoppingCartClearing.setText(String.format("还差¥ %d", 20));
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
                    mShoppingCartClearing.setTextColor(color_red);
                } else
                {
                    mShoppingCartClearing.setText(String.format("还差¥ %d", 20 - totlalmoney));
                    mShoppingCartClearing.setTextColor(color_gray);

                }
                mShoppingCartPrice.setTextColor(color_red);
                mShoppingCartPrice.setText(String.format("¥ %d", totlalmoney));
            }
        }
    };

    /**
     * 购物车清单
     */
    private CommonAdapter<FoodModel> cartAdater = new CommonAdapter<FoodModel>(foodList2, R.layout.shopping_item)
    {

        @Override
        public void convert(CommonViewHolder holder, int position, FoodModel item)
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
                            if (ListViewCompat.NO_POSITION != indexOf)
                            {
                                foodList2.remove(indexOf);
                            }
                            notifyDataSetChanged();
                            mShoppingCartView.minus();
                            if (foodList2.isEmpty())
                            {
                                calculationLayout(true);
                            } else if (foodList2.size() < critical_value)
                            {
                                setCartVisibility(View.VISIBLE);
                            }
                            return;
                        }
                        notifyDataSetChanged();
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
                    item.score++;
                    notifyDataSetChanged();
                    mShoppingCartView.puls(false);
                }
            });
        }
    };

    /**
     * 购物车清单固定高度时要达到的临界值，vs →  footList2.size()
     */
    private static final int critical_value = 3;
    private int dp_16;
    private int dp_80;
    private static final int color_red = Color.parseColor("#FFFF0000");
    private static final int color_gray = Color.parseColor("#43000000");


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
            foodModel.unit_price = id + 1;
            foodList.add(foodModel);
        }
        adapter.addAll(foodList);
        mContentRecyclerview.setAdapter(adapter);

        cartAdater.registerDataSetObserver(new DataSetObserver()
        {
            @Override
            public void onChanged()
            {
                super.onChanged();
                adapter.notifyDataSetChanged();
            }
        });

        mCartRecyclerview.setAdapter(cartAdater);
        mCartRecyclerview.setVisibility(View.GONE);
        mContentRecyclerview.setLayoutManager(manager_content = new LinearLayoutManager(this));
        mMenuRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mMenuRecyclerview.setAdapter(menuAdapter);
        // Add the sticky headers decoration
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        mContentRecyclerview.addItemDecoration(headersDecor);
        // Add decoration for dividers between list items
        mContentRecyclerview.addItemDecoration(new DividerDecoration(this));
        // Add touch listeners
        StickyRecyclerHeadersTouchListener touchListener =new StickyRecyclerHeadersTouchListener(mContentRecyclerview, headersDecor);
        touchListener.setOnHeaderClickListener(
                (header, position, headerId) -> Toast.makeText(ShoppingActivity.this, "Header position: " + position + ", id: " + headerId,
                        Toast.LENGTH_SHORT).show());
        mContentRecyclerview.addOnItemTouchListener(touchListener);

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

        mBottomBackground.setOnClickListener(cartClickListener);
        mShoppingCartView.setOnCartClickListener(cartClickListener);
        mShoppingCartView.setPlusMinusListener(plusMinusListener);
    }

    private String[] getDummyDataSet()
    {
        return getResources().getStringArray(R.array.animals);
    }


    /**
     * 计算购物车动态改变的位置,及相应的一系列的变化
     *
     * @param isPop false → 当显示清单时，处理清单为空时调用的
     */
    private void calculationLayout(boolean isPop)
    {
        if (mShoppingCartView.getCount() == 0 && !isPop)
        {
            return;
        }

        RelativeLayout.LayoutParams cart_params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        LinearLayoutCompat.LayoutParams price_arams = (LinearLayoutCompat.LayoutParams) mShoppingCartPrice.getLayoutParams();
        if (mCartRecyclerview.getVisibility() == View.GONE)
        {
            setCartVisibility(View.VISIBLE);
            cart_params.addRule(RelativeLayout.ABOVE, R.id.shopping_bottom_parent);
            cart_params.leftMargin = dp_16;
            mShoppingCartView.setLayoutParams(cart_params);
            mBottomBackground.setVisibility(View.VISIBLE);
            price_arams.leftMargin = dp_16;
            mShoppingCartPrice.setLayoutParams(price_arams);
        } else
        {
            setCartVisibility(View.GONE);
            cart_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, R.id.shopping_parent);
            cart_params.leftMargin = dp_16;
            cart_params.bottomMargin = dp_16;
            mShoppingCartView.setLayoutParams(cart_params);
            mBottomBackground.setVisibility(View.GONE);
            price_arams.leftMargin = dp_80;
            mShoppingCartPrice.setLayoutParams(price_arams);
        }
    }


    /**
     * 设置mCartRecyclerview的隐藏显示.
     * 显示时判断foodList2.size() >= critical_value，为真代表清单已达到临界值，修改为最大高度;flase 修改为动态高度
     *
     * @param visibility mCartRecyclerview.setVisibility(visibility)
     */
    private void setCartVisibility(int visibility)
    {
        if (View.VISIBLE == visibility)
        {
            LinearLayoutCompat.LayoutParams layoutParams = (LinearLayoutCompat.LayoutParams) mCartRecyclerview.getLayoutParams();
            if (foodList2.size() >= critical_value)
            {
                layoutParams.height = (int) (mScreenHeight * 0.4);
                mCartRecyclerview.setLayoutParams(layoutParams);
            } else if (layoutParams.height != LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
            {
                layoutParams.height = LinearLayoutCompat.LayoutParams.WRAP_CONTENT;
                mCartRecyclerview.setLayoutParams(layoutParams);
            }
        }
        mCartRecyclerview.setVisibility(visibility);
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
            holder.setOnClickListener(R.id.shopping_item_food_minus, v -> {
                //减之前，先判断数量是否为零，不为零就减一
                if (item.score != 0)
                {
                    item.score--;
                    //减完之后，判断是否为零，为零就从foodList2移除该item
                    if (item.score == 0)
                    {
                        int indexOf = foodList2.indexOf(item);
                        if (ListViewCompat.NO_POSITION != indexOf)
                        {
                            foodList2.remove(indexOf);
                        }
                        holder.setVisibility(R.id.shopping_item_food_minus, View.GONE);
                        mShoppingCartView.minus();
                        return;
                    }
                    holder.setText(R.id.shopping_item_food_count, String.valueOf(item.score));
                    mShoppingCartView.minus();
                }
            });
            holder.setOnClickListener(R.id.shopping_item_food_plus, v -> {
                //加之前，先判断数量是否为零，为零就添加到foodList2
                if (item.score == 0)
                {
                    foodList2.add(item);
                    holder.setVisibility(R.id.shopping_item_food_minus, View.VISIBLE);
                }
                int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
                v.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
                ball = new ImageView(mContext);// buyImg是动画的图片，我的是一个小球（R.drawable.sign）
                ball.setImageResource(R.drawable.shoppingcart_anim_oval);// 设置buyImg的图片
                setAnim(ball, startLocation);// 开始执行动画
                item.score++;
                holder.setText(R.id.shopping_item_food_count, String.valueOf(item.score));
                mShoppingCartView.puls(true);
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

    private abstract class MenuAdapter<T> extends RecyclerGenericityAdapter<T>
    {

        int mSelect = 0;   //选中项

        public MenuAdapter(@NonNull List<T> mDatas, @LayoutRes int layoutId)
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



    /*------------------------------------------------我是萌萌的分割线----------------------------------------*/

    //    private static final int anim_mask_layout_id = Integer.MAX_VALUE / 100;
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
        //        animLayout.setId(anim_mask_layout_id);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }


    private View addViewToAnimLayout(final View view, int[] location)
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

    private void setAnim(final View v, int[] startLocation)
    {
        ViewGroup anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(v, startLocation);
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
            }
        });

    }
}
