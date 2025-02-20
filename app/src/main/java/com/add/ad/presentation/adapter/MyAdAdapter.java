package com.add.ad.presentation.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.add.ad.R;
import com.add.ad.databinding.ItemMyAdBinding;
import com.add.ad.entity.response.ResponseMyAdInfo;
import com.add.ad.presentation.viewModel.mypage.myad.MyAdViewModel;

import java.util.ArrayList;

public class MyAdAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<ResponseMyAdInfo> myAdItems;
    MyAdViewModel myAdViewModel;

    public MyAdAdapter(ArrayList<ResponseMyAdInfo> myAdItems, MyAdViewModel myAdViewModel) {
        this.myAdItems = myAdItems;
        this.myAdViewModel = myAdViewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyAdBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_my_ad, parent, false);
        return new MyAdAdapter.MyAdViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyAdAdapter.MyAdViewHolder){
            ((MyAdAdapter.MyAdViewHolder)holder).bind(myAdItems.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        myAdViewModel.myAdResult.setValue(myAdItems.size() != 0);
        return myAdItems.size();
    }

    public class MyAdViewHolder extends RecyclerView.ViewHolder {
        ItemMyAdBinding binding;
        ResponseMyAdInfo item;
        int position;

        public MyAdViewHolder(ItemMyAdBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ResponseMyAdInfo item, int position) {
            this.position = position;
            this.item = item;
            binding.setVh(this);
            binding.setMyAdItem(item);
        }

        public void clickAd(View v) {
            Log.d("userINFOFOFOF", String.valueOf(myAdViewModel.userInfo.getValue()));
//            feedViewModel.feedDetailEvent.call();
//            feedViewModel.getDetailFeed(position);
            if(myAdViewModel.userInfo.getValue()){
                //creator 일 때
            }else {
                myAdViewModel.clickSelectEvent.call();
                myAdViewModel.adPosition.setValue(myAdViewModel.advertiserAdList.getValue().get(position).getPostId());
            }
        }
    }
}
