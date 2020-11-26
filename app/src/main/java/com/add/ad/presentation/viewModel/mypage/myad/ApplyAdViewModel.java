package com.add.ad.presentation.viewModel.mypage.myad;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;

import com.add.ad.data.repository.mypage.MyPageRepository;
import com.add.ad.entity.response.ResponseApplyInfo;
import com.add.ad.presentation.adapter.AccessAdAdapter;
import com.add.ad.presentation.base.BaseViewModel;
import com.add.ad.presentation.base.SingleLiveEvent;

import java.util.ArrayList;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ApplyAdViewModel extends BaseViewModel {
    CompositeDisposable compositeDisposable;
    MyPageRepository myPageRepository;

    public MutableLiveData<Boolean> accessResult = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> appliedResult = new MutableLiveData<>(true);
    public MutableLiveData<String> position = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ResponseApplyInfo>> appliedAdList = new MutableLiveData<>();

    public SingleLiveEvent<Void> appliedListEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> accessListEvent = new SingleLiveEvent<>();

    @ViewModelInject
    public ApplyAdViewModel(CompositeDisposable compositeDisposable, MyPageRepository myPageRepository) {
        this.compositeDisposable = compositeDisposable;
        this.myPageRepository = myPageRepository;
    }

    public void getAppliedList() {
        int listPosition = Integer.parseInt(Objects.requireNonNull(position.getValue()));
        compositeDisposable.add(myPageRepository.getAppliedList(listPosition)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(it -> {
                    appliedAdList.setValue(it.body());
                    appliedListEvent.call();
                }, it -> {
                    Log.e("errorGetAppliedList", it.getMessage());
                }));
    }

    public void clickBack() {
        backEvent.call();
    }

    public void clickComplete(){
        accessListEvent.call();
    }
}
