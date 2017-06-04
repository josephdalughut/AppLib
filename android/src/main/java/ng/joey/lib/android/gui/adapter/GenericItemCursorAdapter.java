package ng.joey.lib.android.gui.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import ng.joey.lib.java.generic.DoubleConsumer;
import ng.joey.lib.java.generic.QuatroReceiver;
import ng.joey.lib.java.util.Value;

/**
 * Created by Joey Dalughut on 8/14/16 at 12:18 PM,
 * Project: Litigy Libraries.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class GenericItemCursorAdapter<ViewHolderImpl extends RecyclerView.ViewHolder> extends BaseAdapter {

    Cursor cursor;

    /**
     * An instance of {@link DoubleConsumer} which would return the id for a specific item
     */
    DoubleConsumer<Long, Cursor, Integer> idConsumer;

    /**
     * An instance of {@link QuatroReceiver} which would collect view items from this adapter for further operations
     */
    QuatroReceiver<ViewHolderImpl, Cursor, Integer, Boolean> viewReceiver;

    /**
     * An instance of {@link DoubleConsumer} which would consume an item and return the view for it
     */
    DoubleConsumer<ViewHolderImpl, ViewGroup, Integer> viewConsumer;

    /**
     * Set an instance of {@link DoubleConsumer} which would return the id for a specific item
     * @param idConsumer the instance of {@link DoubleConsumer} which would return the id for a specific item
     * @return this adapter instance
     */
    public GenericItemCursorAdapter<ViewHolderImpl> setIdConsumer(DoubleConsumer<Long, Cursor, Integer> idConsumer){
        this.idConsumer = idConsumer; return this;
    }

    /**
     * Set An instance of {@link QuatroReceiver} which would collect view items from this adapter for further operations
     * @param viewReceiver the instance of {@link QuatroReceiver} which would collect view items from this adapter for further operations
     * @return this adapter instance
     */
    public GenericItemCursorAdapter<ViewHolderImpl> setViewReceiver(QuatroReceiver<ViewHolderImpl, Cursor, Integer, Boolean> viewReceiver){
        this.viewReceiver = viewReceiver; return this;
    }

    /**
     * Set an instance of {@link DoubleConsumer} which would consume an item and return the view for it
     * @param viewConsumer the instance of {@link DoubleConsumer} which would consume an item and return the view for it
     * @return this adapter instance
     */
    public GenericItemCursorAdapter<ViewHolderImpl> setViewConsumer(DoubleConsumer<ViewHolderImpl, ViewGroup, Integer> viewConsumer){
        this.viewConsumer = viewConsumer; return this;
    }

    /**
     * Returns a new instance of {@link GenericItemCursorAdapter}
     * @param <ViewHolderImpl> the viewHolder type. This must extend {@link RecyclerView.ViewHolder}
     * @return
     */
    public static <ViewHolderImpl extends RecyclerView.ViewHolder> GenericItemCursorAdapter<ViewHolderImpl> getInstance(){
        return new GenericItemCursorAdapter<>();
    }

    @Override
    public int getCount() {
        return Value.IS.nullValue(cursor) ? 0 : cursor.getCount();
    }

    @Override
    public Cursor getItem(int position) {
        cursor.moveToPosition(position);
        return cursor;
    }

    @Override
    public long getItemId(int position) {
        if(!Value.IS.nullValue(idConsumer) && cursor.moveToPosition(position))
            return idConsumer.onConsume(cursor, position);
        return position;
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
        if(cursor.moveToPosition(position)) {
            viewReceiver.onReceive(impl, cursor, position, Value.IS.SAME.integerValue(position, getCount() - 1));
            return convertView;
        }
        else{
            return null;
        }
    }

    public void setCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) {
            return null;
        }
        Cursor oldCursor = cursor;
        cursor = newCursor;
        notifyDataSetChanged();

        return oldCursor;
    }

    public void release(){
        if(Value.IS.nullValue(cursor))
            return;
        cursor.close();
    }

    public boolean isEmpty(){
        return Value.IS.nullValue(cursor) || cursor.getCount() == 0;
    }

}