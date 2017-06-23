package ng.joey.lib.android.gui.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.util.List;

import ng.joey.lib.android.R;
import ng.joey.lib.android.gui.adapter.GenericRecyclerViewItemAdapter;
import ng.joey.lib.android.gui.view.textView.TextView;
import ng.joey.lib.java.generic.Consumer;
import ng.joey.lib.java.generic.DoubleConsumer;
import ng.joey.lib.java.generic.DoubleReceiver;
import ng.joey.lib.java.generic.QuatroReceiver;
import ng.joey.lib.java.util.Value;

/**
 * Created by Joey Dalughut on 8/13/16 at 1:11 PM,
 * Project: Skout.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class ListDialog<T> extends DialogFragment {

    public static <T> ListDialog<T> getInstance(String title, List<T> items,
                                         @NonNull OnItemClickListener<T> onItemClickListener,
                                                @NonNull Consumer<String, T> textItemRetriever,
                                         boolean cancelOnBack){
        return new ListDialog<T>().setTitle(title).setItems(items).setTextItemRetreiver(textItemRetriever).setCancelOnBack(cancelOnBack)
                .setOnItemClickListener(onItemClickListener);
    }

    private String title;
    TextView titleTextView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ImageButton cancelButton;
    List<T> items = null;
    boolean cancelOnBack = true;
    OnItemClickListener<T> onItemClickListener;
    Consumer<String, T> textItemRetreiver;

    public OnItemClickListener<T> getOnItemClickListener() {
        return onItemClickListener;
    }

    public ListDialog setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public Consumer<String, T> getTextItemRetreiver() {
        return textItemRetreiver;
    }

    public ListDialog setTextItemRetreiver(Consumer<String, T> textItemRetreiver) {
        this.textItemRetreiver = textItemRetreiver;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ListDialog<T> setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<T> getItems() {
        return items;
    }

    public ListDialog<T> setItems(List<T> items) {
        this.items = items;
        showProgress();
        if(!Value.IS.nullValue(recyclerView))
            loadItems();
        return this;
    }

    public boolean isCancelOnBack() {
        return cancelOnBack;
    }

    public ListDialog<T> setCancelOnBack(boolean cancelOnBack) {
        this.cancelOnBack = cancelOnBack;
        return this;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void showProgress(){
        if(!Value.IS.nullValue(progressBar))
           progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        if(!Value.IS.nullValue(progressBar))
            progressBar.setVisibility(View.GONE);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public int layoutId() {
        return R.layout.dialog_simple_list;
    }

    @Override
    public void bundle(Bundle bundle) {

    }

    @Override
    public void setupViews() {
        cancelButton = (ImageButton) findViewById(R.id.cancelButton);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.barProgress);
        showProgress();
        titleTextView.setText(title);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getDialog().setCancelable(isCancelOnBack());
        setCancelable(isCancelOnBack());
        cancelButton.setAlpha(isCancelOnBack() ? 1f : 0f);
        cancelButton.setEnabled(isCancelOnBack());
        if(isCancelOnBack()){
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismissAllowingStateLoss();
                }
            });
        }
        if(!Value.IS.emptyValue(items)){
            loadItems();
        }
    }

    public void loadItems(){
        GenericRecyclerViewItemAdapter<T, ItemViewHolder> adapter =
                GenericRecyclerViewItemAdapter.<T, ItemViewHolder>getInstance()
                .setIdConsumer(new DoubleConsumer<Long, T, Integer>() {
                    @Override
                    public Long onConsume(T t, Integer integer) {
                        return integer.longValue();
                    }

                    @Override
                    public Long onConsume1(T t) {
                        return null;
                    }

                    @Override
                    public Long onConsume2(Integer integer) {
                        return integer.longValue();
                    }
                })
                        .setTypeConsumer(new Consumer<Integer, T>() {
                            @Override
                            public Integer onConsume(T t) {
                                return 0;
                            }
                        })
                .setViewConsumer(new DoubleConsumer<ItemViewHolder, ViewGroup, Integer>() {

                    @Override
                    public ItemViewHolder onConsume(ViewGroup viewGroup, Integer integer) {
                        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.item_simple_list, viewGroup, false));
                    }

                    @Override
                    public ItemViewHolder onConsume1(ViewGroup viewGroup) {
                        return null;
                    }

                    @Override
                    public ItemViewHolder onConsume2(Integer integer) {
                        return null;
                    }
                }).setViewReceiver(new QuatroReceiver<ItemViewHolder, T, Integer, Boolean>() {
                    @Override
                    public void onReceive(ItemViewHolder itemViewHolder, T t, final Integer integer, Boolean aBoolean) {
                        T item = getItems().get(integer);
                        itemViewHolder.textView.setText(getTextItemRetreiver().onConsume(item));
                        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dismissAllowingStateLoss();
                                if(!Value.IS.nullValue(onItemClickListener)){
                                    onItemClickListener.onItemClick(getItems().get(integer), integer);
                                }
                            }
                        });
                    }

                    @Override
                    public void onReceive1(ItemViewHolder itemViewHolder) {

                    }

                    @Override
                    public void onReceive2(T t) {

                    }

                    @Override
                    public void onReceive3(Integer integer) {

                    }

                    @Override
                    public void onReceive4(Boolean aBoolean) {

                    }
                })
                .setItems(getItems());
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    @Override
    public int layoutWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int layoutHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public boolean cancelable() {
        return false;
    }

    public interface OnItemClickListener<T>{
        public void onItemClick(T t, int position);
    }

}
