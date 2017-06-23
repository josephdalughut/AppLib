package ng.joey.lib.android.gui.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import ng.joey.lib.java.util.Value;

public abstract class ListViewCursorAdapter<ViewHolderImpl extends RecyclerView.ViewHolder> extends BaseAdapter {

    Cursor cursor;

    public void setItems(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor getItems(){
        return cursor;
    }

    @Override
    public Cursor getItem(int position) {
        cursor.moveToPosition(position);
        return cursor;
    }

    @Override
    public int getCount() {
        return Value.IS.nullValue(cursor) ? 0 : cursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        cursor.moveToPosition(position);
        return getItemId(cursor, position);
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
            impl = getViewHolder(parent, getViewType(getItem(position)));
            convertView = impl.itemView;
            convertView.setTag(impl);
        }else{
            impl = (ViewHolderImpl) convertView.getTag();
        }
        int viewType = getViewType(getItem(position));
        setupViewHolder(impl, getItem(position), position, viewType);
        return convertView;
    }

    private Cursor swapCursor(Cursor newCursor) {
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
        cursor = null;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    public abstract long getItemId(Cursor cursor, int position);
    public abstract ViewHolderImpl getViewHolder(ViewGroup viewGroup, int viewType);
    public abstract void setupViewHolder(ViewHolderImpl viewHolder, Cursor cursor, int position, int viewType);
    public abstract int getViewType(Cursor cursor);



}