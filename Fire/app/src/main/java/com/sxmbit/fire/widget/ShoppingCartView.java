package com.sxmbit.fire.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sxmbit.fire.R;

/**
 * Created by LinLin on 2015/11/27
 */
public class ShoppingCartView extends FrameLayout
{
    ImageView shop_cart;
    CircleButton food_count;
    ImageView arrow_up;
    Animation animation;
    PlusMinusListener mPlusMinusListener;

    public ShoppingCartView(Context context)
    {
        super(context);
        init();
    }

    public ShoppingCartView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ShoppingCartView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.shopping_cart, this);
        shop_cart = (ImageView) findViewById(R.id.img_shop_cart);
        food_count = (CircleButton) findViewById(R.id.txt_food_count);
        arrow_up = (ImageView) findViewById(R.id.img_food_arrow_up);
        shop_cart.setImageResource(getCount() == 0 ? R.drawable.takeout_ic_shopping_cart_disable : R.drawable.takeout_ic_shopping_cart_normal);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.shop_cart_scale);
    }

    /**
     * 数量加1,加之前先判断数量是否为零，为零时设置不为零时图片，同时显示food_count并开启缩放动画
     */
    public void puls(boolean hasAnim)
    {
        int count = getCount();
        if (count == 0)
            shop_cart.setImageResource(R.drawable.takeout_ic_shopping_cart_normal);
        if (food_count.getVisibility() == GONE)
            food_count.setVisibility(VISIBLE);
        count++;
        if (hasAnim)
        startAnimation(animation);
        food_count.setText(String.valueOf(count));
        if (null != mPlusMinusListener)
        {
            mPlusMinusListener.plus(count);
        }
    }

    /**
     * 数量减1,减之前先判断数量是否为零，为零时直接返回；
     * 当减完之后刚好为零，设置为零时图片，同时隐藏food_count
     */
    public void minus()
    {
        int count = getCount();
        if (count == 0)
        {
            return;
        }
        count--;
        food_count.setText(String.valueOf(count));
        if (count == 0)
        {
            if (food_count.getVisibility() == VISIBLE)
                food_count.setVisibility(GONE);
            shop_cart.setImageResource(R.drawable.takeout_ic_shopping_cart_disable);
        }
        if (null != mPlusMinusListener)
        {
            mPlusMinusListener.minus(count);
        }
    }


    public int setCount(int count)
    {
        if (count <= 0)
        {
            food_count.setText("0");
        } else
        {
            food_count.setText(String.valueOf(count));
        }
        return getCount();
    }

    /**
     * 获取当前显示的数量，并当获取数量失败或者获取成功但数量为零时，隐藏food_count
     */
    public int getCount()
    {
        if (TextUtils.isEmpty(food_count.getText().toString()) || food_count.getText().toString().equals("0"))
        {
            if (food_count.getVisibility() == VISIBLE)
                food_count.setVisibility(GONE);
            return 0;
        } else
        {
            try
            {
                if (food_count.getVisibility() == GONE)
                    food_count.setVisibility(VISIBLE);
                return Integer.parseInt(food_count.getText().toString());
            } catch (NumberFormatException e)
            {
                e.printStackTrace();
                if (food_count.getVisibility() == VISIBLE)
                    food_count.setVisibility(GONE);
                return 0;
            }
        }

    }

    public ImageView getShop_cart()
    {
        return shop_cart;
    }

    public CircleButton getFood_count()
    {
        return food_count;
    }

    /**
     * 购物车点击事件
     */
    public void setOnCartClickListener(View.OnClickListener listener)
    {
        shop_cart.setOnClickListener(listener);
    }


    public void setPlusMinusListener(PlusMinusListener plusMinusListener)
    {
        mPlusMinusListener = plusMinusListener;
    }

    public interface PlusMinusListener
    {
        void plus(int afterCount);

        void minus(int afterCount);
    }
}
