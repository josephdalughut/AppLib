package ng.joey.lib.android.gui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import ng.joey.lib.java.generic.Consumer;
import ng.joey.lib.java.generic.DoubleConsumer;
import ng.joey.lib.java.generic.QuatroReceiver;
import ng.joey.lib.java.util.Value;

public abstract class RecyclerViewItemAdapter<T, ViewHolderImpl extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<ViewHolderImpl> {

    public List<T> items;

    public RecyclerViewItemAdapter<T, ViewHolderImpl> setItems(List<T> items){this.items = items; refresh(); return this;}

    public List<T> getItems(){
        return items;
    }

    public T getItem(int position) {
        return Value.IS.emptyValue(getItems()) ? null : getItems().get(position);
    }

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
    public ViewHolderImpl onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolderImpl holder, int position) {
        setupViewHolder(holder, getItem(position), position, getItemViewType(position));
    }


    @Override
    public int getItemViewType(int position) {
        return (getViewType(getItem(position)));
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    public void release(){
        if(Value.IS.nullValue(items))
            return;
        items.clear();
        items = null;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public abstract long getItemId(T t, int position);
    public abstract ViewHolderImpl getViewHolder(ViewGroup viewGroup, int viewType);
    public abstract void setupViewHolder(ViewHolderImpl viewHolder, T item, int viewType, int position);
    public abstract int getViewType(T t);

}