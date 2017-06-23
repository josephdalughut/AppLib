package ng.joey.lib.android.gui.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import ng.joey.lib.java.generic.Consumer;
import ng.joey.lib.java.generic.DoubleConsumer;
import ng.joey.lib.java.generic.QuatroReceiver;
import ng.joey.lib.java.util.Value;

/**
 * Created by Joey Dalughut on 8/14/16 at 12:19 PM,
 * Project: Litigy Libraries.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
@Deprecated
public class GenericRecyclerViewCursorAdapter<ViewHolderImpl extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolderImpl> {

    private Cursor cursor;
    /**
     * An instance of {@link DoubleConsumer} which would return the id for a specific item
     */
    DoubleConsumer<Long, Cursor, Integer> idConsumer;
    Consumer<Integer, Cursor> viewTypeConsumer;

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
    public GenericRecyclerViewCursorAdapter<ViewHolderImpl> setIdConsumer(DoubleConsumer<Long, Cursor, Integer> idConsumer){
        this.idConsumer = idConsumer; return this;
    }

    public GenericRecyclerViewCursorAdapter<ViewHolderImpl> setViewTypeConsumer(Consumer<Integer, Cursor> viewTypeConsumer){
        this.viewTypeConsumer = viewTypeConsumer; return this;
    }

    /**
     * Set An instance of {@link QuatroReceiver} which would collect view items from this adapter for further operations
     * @param viewReceiver the instance of {@link QuatroReceiver} which would collect view items from this adapter for further operations
     * @return this adapter instance
     */
    public GenericRecyclerViewCursorAdapter<ViewHolderImpl> setViewReceiver(QuatroReceiver<ViewHolderImpl, Cursor, Integer, Boolean> viewReceiver){
        this.viewReceiver = viewReceiver; return this;
    }

    /**
     * Set an instance of {@link DoubleConsumer} which would consume an item and return the view for it
     * @param viewConsumer the instance of {@link DoubleConsumer} which would consume an item and return the view for it
     * @return this adapter instance
     */
    public GenericRecyclerViewCursorAdapter<ViewHolderImpl> setViewConsumer(DoubleConsumer<ViewHolderImpl, ViewGroup, Integer> viewConsumer){
        this.viewConsumer = viewConsumer; return this;
    }

    /**
     * Returns a new instance of {@link GenericRecyclerViewCursorAdapter}
     * @param <ViewHolderImpl> the viewHolder type. This must extend {@link RecyclerView.ViewHolder}
     * @return
     */
    public static <ViewHolderImpl extends RecyclerView.ViewHolder> GenericRecyclerViewCursorAdapter<ViewHolderImpl> getInstance(){
        return new GenericRecyclerViewCursorAdapter<>();
    }

    public int getCount() {
        return Value.IS.nullValue(cursor) ? 0 : cursor.getCount();
    }


    @Override
    public ViewHolderImpl onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewConsumer.onConsume(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if(Value.IS.nullValue(viewTypeConsumer) || Value.IS.nullValue(getCursor()) || !getCursor().moveToPosition(position))
            return super.getItemViewType(position);
        return viewTypeConsumer.onConsume(cursor);
    }

    @Override
    public void onBindViewHolder(ViewHolderImpl holder, int position) {
        if(Value.IS.nullValue(getCursor())||!getCursor().moveToPosition(position))
            return;
        viewReceiver.onReceive(holder, getCursor(), position, Value.IS.SAME.integerValue(position, getCount() - 1));
    }

    @Override
    public long getItemId(int position) {
        if(Value.IS.nullValue(getCursor()))
            return Value.TO.longValue(position);
        return idConsumer.onConsume(getCursor(), position);
    }

    public Cursor getCursor(){
        return cursor;
    }

    /**
     * Refresh this adapter by telling it that items have changed
     */
    public void refresh(){
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    public void setCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            //old.close();
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
        if(Value.IS.nullValue(getCursor()))
            return;
        getCursor().close();
    }

    public boolean isEmpty(){
        return Value.IS.nullValue(cursor) || cursor.getCount() == 0;
    }

}