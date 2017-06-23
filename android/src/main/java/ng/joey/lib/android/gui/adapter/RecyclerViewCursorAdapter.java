package ng.joey.lib.android.gui.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import ng.joey.lib.java.util.Value;

public abstract class RecyclerViewCursorAdapter<T, ViewHolderImpl extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolderImpl> {

    public Cursor cursor;

    public RecyclerViewCursorAdapter<T, ViewHolderImpl> setItems(Cursor cursor){this.cursor = cursor; refresh(); return this;}

    public Cursor getItems(){
        return cursor;
    }

    public Cursor getItem(int position) {
        if(Value.IS.nullValue(getItems()))
            return null;
        getItems().moveToPosition(position);
        return getItems();
    }

    public int getCount() {
        return Value.IS.nullValue(getItems()) ? 0 : getItems().getCount();
    }

    @Override
    public long getItemId(int position) {
        getItems().moveToPosition(position);
        return getItemId(getItem(position), position);
    }


    public void refresh(){
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderImpl onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolderImpl holder, int position) {
        int viewType = getViewType(getItem(position));
        setupViewHolder(holder, getItem(position), position, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(getItem(position));
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    public void release(){
        if(Value.IS.nullValue(cursor))
            return;
        cursor.close();
        cursor = null;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public abstract long getItemId(Cursor cursor, int position);
    public abstract ViewHolderImpl getViewHolder(ViewGroup viewGroup, int viewType);
    public abstract void setupViewHolder(ViewHolderImpl viewHolder, Cursor cursor, int viewType, int position);
    public abstract int getViewType(Cursor cursor);

}