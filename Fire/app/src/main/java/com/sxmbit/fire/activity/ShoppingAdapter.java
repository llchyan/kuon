package com.sxmbit.fire.activity;

/**
 * Created by LinLin on 2015/11/25.
 */

import android.support.v7.widget.RecyclerView;

import com.sxmbit.fire.model.FoodModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Adapter holding a list of animal names of type String. Note that each item must be unique.
 */
public abstract class ShoppingAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
{
    private ArrayList<FoodModel> items = new ArrayList<FoodModel>();

    public ShoppingAdapter()
    {
        setHasStableIds(true);
    }

    public void add(FoodModel object)
    {
        items.add(object);
        notifyDataSetChanged();
    }

    public void add(int index, FoodModel object)
    {
        items.add(index, object);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends FoodModel> collection)
    {
        if (collection != null)
        {
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(FoodModel... items)
    {
        addAll(Arrays.asList(items));
    }

    public void clear()
    {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(String object)
    {
        items.remove(object);
        notifyDataSetChanged();
    }

    public FoodModel getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }
}