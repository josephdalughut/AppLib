package ng.joey.lib.android.gui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import ng.joey.lib.java.util.Value;

/**
 *
 */
public abstract class ListViewItemAdapter<T, ViewHolderImpl extends RecyclerView.ViewHolder> extends BaseAdapter {

    public List<T> items;

    public ListViewItemAdapter<T, ViewHolderImpl> setItems(List<T> items){this.items = items; refresh(); return this;}

    public List<T> getItems(){
        return items;
    }

    @Override
    public T getItem(int position) {
        return Value.IS.emptyValue(getItems()) ? null : getItems().get(position);
    }

    @Override
    public int getCount() {
        return Value.IS.emptyValue(items) ? 0 : items.size();
    }

    @Override
    public long getItemId(int position) {
        return getItemId(getItem(position), position);
    }

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
        setupViewHolder(impl, getItem(position), position, getViewType(getItem(position)));
        return convertView;
    }

    public void release(){
        if(Value.IS.nullValue(items))
            return;
        items.clear();
        items = null;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    public abstract long getItemId(T t, int position);
    public abstract ViewHolderImpl getViewHolder(ViewGroup viewGroup, int viewType);
    public abstract void setupViewHolder(ViewHolderImpl viewHolder, T item, int position, int viewType);
    public abstract int getViewType(T t);


}