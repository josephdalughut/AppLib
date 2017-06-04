package ng.joey.lib.android.gui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import ng.joey.lib.java.generic.DoubleConsumer;
import ng.joey.lib.java.generic.QuatroReceiver;
import ng.joey.lib.java.util.Value;

import java.util.List;

/**
 * Created by Joey Dalughut on 8/14/16 at 12:18 PM,
 * Project: Litigy Libraries.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class GenericItemAdapter<T, ViewHolderImpl extends RecyclerView.ViewHolder> extends BaseAdapter {

    public List<T> items; //list of items

    /**
     * An instance of {@link DoubleConsumer} which would return the id for a specific item
     */
    DoubleConsumer<Long, T, Integer> idConsumer;

    /**
     * An instance of {@link QuatroReceiver} which would collect view items from this adapter for further operations
     */
    QuatroReceiver<ViewHolderImpl, T, Integer, Boolean> viewReceiver;

    /**
     * An instance of {@link DoubleConsumer} which would consume an item and return the view for it
     */
    DoubleConsumer<ViewHolderImpl, ViewGroup, Integer> viewConsumer;

    /**
     * Set an instance of {@link DoubleConsumer} which would return the id for a specific item
     * @param idConsumer the instance of {@link DoubleConsumer} which would return the id for a specific item
     * @return this adapter instance
     */
    public GenericItemAdapter<T, ViewHolderImpl> setIdConsumer(DoubleConsumer<Long, T, Integer> idConsumer){
        this.idConsumer = idConsumer; return this;
    }

    /**
     * Set An instance of {@link QuatroReceiver} which would collect view items from this adapter for further operations
     * @param viewReceiver the instance of {@link QuatroReceiver} which would collect view items from this adapter for further operations
     * @return this adapter instance
     */
    public GenericItemAdapter<T, ViewHolderImpl> setViewReceiver(QuatroReceiver<ViewHolderImpl, T, Integer, Boolean> viewReceiver){
        this.viewReceiver = viewReceiver; return this;
    }

    /**
     * Set an instance of {@link DoubleConsumer} which would consume an item and return the view for it
     * @param viewConsumer the instance of {@link DoubleConsumer} which would consume an item and return the view for it
     * @return this adapter instance
     */
    public GenericItemAdapter<T, ViewHolderImpl> setViewConsumer(DoubleConsumer<ViewHolderImpl, ViewGroup, Integer> viewConsumer){
        this.viewConsumer = viewConsumer; return this;
    }

    /**
     * Returns a new instance of {@link GenericItemAdapter}
     * @param <T> the item data type
     * @param <ViewHolderImpl> the viewHolder type. This must extend {@link android.support.v7.widget.RecyclerView.ViewHolder}
     * @return
     */
    public static <T, ViewHolderImpl extends RecyclerView.ViewHolder> GenericItemAdapter<T, ViewHolderImpl> getInstance(){
        return new GenericItemAdapter<>();
    }

    /**
     * Set the items for ths adapter
     * @param items the items to querySet
     * @return this adapter instance
     */
    public GenericItemAdapter<T, ViewHolderImpl> setItems(List<T> items){this.items = items; refresh(); return this;}

    @Override
    public int getCount() {
        return Value.IS.emptyValue(items) ? 0 : items.size();
    }

    @Override
    public T getItem(int position) {
        return Value.IS.emptyValue(getItems()) ? null : getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return idConsumer.onConsume(getItem(position), position);
    }

    /**
     * Refresh this adapter by telling it that items have changed
     */
    public void refresh(){
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderImpl impl;
        if(Value.IS.nullValue(convertView)){
            impl = viewConsumer.onConsume(parent, getItemViewType(position));
            convertView = impl.itemView;
            convertView.setTag(impl);
        }else{
            impl = (ViewHolderImpl) convertView.getTag();
        }
        viewReceiver.onReceive(impl, getItem(position), position, Value.IS.SAME.integerValue(position, getCount() - 1));
        return convertView;
    }

    /**
     * @return the items associated with this adapter
     */
    public List<T> getItems(){
        return items;
    }

}