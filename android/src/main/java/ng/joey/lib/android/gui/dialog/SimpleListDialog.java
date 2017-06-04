package ng.joey.lib.android.gui.dialog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import ng.joey.lib.android.R;
import ng.joey.lib.android.gui.adapter.GenericRecyclerViewItemAdapter;
import ng.joey.lib.android.gui.view.textView.TextView;
import ng.joey.lib.java.generic.Consumer;
import ng.joey.lib.java.generic.DoubleConsumer;
import ng.joey.lib.java.generic.DoubleReceiver;
import ng.joey.lib.java.generic.QuatroReceiver;
import ng.joey.lib.java.util.Value;

import java.util.List;

/**
 * Created by Joey Dalughut on 8/13/16 at 1:11 PM,
 * Project: Skout.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
@Deprecated
public class SimpleListDialog extends DialogFragtivity {

    public static SimpleListDialog getInstance(String title, List<String> items,
                                               DoubleReceiver<String, Integer> callbackReceiver,
                                               boolean cancelOnBack){
        return new SimpleListDialog().setTitle(title).setItems(items).setCancelOnBack(cancelOnBack)
                .setCallbackReceiver(callbackReceiver);
    }

    private String title;
    TextView titleTextView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ImageButton cancelButton;
    List<String> items = null;
    boolean cancelOnBack = true;
    DoubleReceiver<String, Integer> callbackReceiver;

    public DoubleReceiver<String, Integer> getCallbackReceiver() {
        return callbackReceiver;
    }

    public SimpleListDialog setCallbackReceiver(DoubleReceiver<String, Integer> callbackReceiver) {
        this.callbackReceiver = callbackReceiver;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SimpleListDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<String> getItems() {
        return items;
    }

    public SimpleListDialog setItems(List<String> items) {
        this.items = items;
        showProgress();
        if(!Value.IS.nullValue(recyclerView))
            loadItems();
        return this;
    }

    public boolean isCancelOnBack() {
        return cancelOnBack;
    }

    public SimpleListDialog setCancelOnBack(boolean cancelOnBack) {
        this.cancelOnBack = cancelOnBack;
        return this;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void showProgress(){
        if(!Value.IS.nullValue(getProgress()))
            progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        if(!Value.IS.nullValue(getProgress()))
            progressBar.setVisibility(View.GONE);
    }

    public ProgressBar getProgress(){
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
    public void findViews() {
        cancelButton = (ImageButton) findViewById(R.id.cancelButton);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.barProgress);
    }

    @Override
    public void setupViews() {
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
        GenericRecyclerViewItemAdapter<String, ItemViewHolder> adapter =
                GenericRecyclerViewItemAdapter.<String, ItemViewHolder>getInstance()
                .setIdConsumer(new DoubleConsumer<Long, String, Integer>() {
                    @Override
                    public Long onConsume(String s, Integer integer) {
                        return Value.TO.longValue(integer);
                    }

                    @Override
                    public Long onConsume1(String s) {
                        return null;
                    }

                    @Override
                    public Long onConsume2(Integer integer) {
                        return Value.TO.longValue(integer);
                    }
                })
                .setTypeConsumer(new Consumer<Integer, String>() {
                    @Override
                    public Integer onConsume(String s) {
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
                })
                .setViewReceiver(new QuatroReceiver<ItemViewHolder, String, Integer, Boolean>() {
                    @Override
                    public void onReceive(ItemViewHolder itemViewHolder, final String s, final Integer integer, Boolean aBoolean) {
                        itemViewHolder.textView.setText(s);
                        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dismissAllowingStateLoss();
                                if(!Value.IS.nullValue(callbackReceiver)){
                                    callbackReceiver.onReceive(s, integer);
                                }
                            }
                        });
                    }

                    @Override
                    public void onReceive1(ItemViewHolder itemViewHolder) {

                    }

                    @Override
                    public void onReceive2(String s) {

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
}
