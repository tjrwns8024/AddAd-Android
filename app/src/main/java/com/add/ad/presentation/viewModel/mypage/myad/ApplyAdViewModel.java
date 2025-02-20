package com.add.ad.presentation.viewModel.mypage.myad;

import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;

import com.add.ad.data.repository.mypage.MyPageRepository;
import com.add.ad.entity.AccessData;
import com.add.ad.entity.UserListData;
import com.add.ad.entity.response.ResponseApplyInfo;
import com.add.ad.presentation.adapter.AccessAdAdapter;
import com.add.ad.presentation.base.BaseViewModel;
import com.add.ad.presentation.base.SingleLiveEvent;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ApplyAdViewModel extends BaseViewModel {
    CompositeDisposable compositeDisposable;
    MyPageRepository myPageRepository;

    public MutableLiveData<Integer> applyCount = new MutableLiveData<>(0);
    public MutableLiveData<Integer> accessCount = new MutableLiveData<>(0);
    public MutableLiveData<Boolean> accessResult = new MutableLiveData<>(true);
    public MutableLiveData<Boolean> appliedResult = new MutableLiveData<>(true);
    public MutableLiveData<String> position = new MutableLiveData<>();
    public MutableLiveData<ArrayList<ResponseApplyInfo>> appliedAdList = new MutableLiveData<>();
    public ArrayList<UserListData> accessList = new ArrayList<>();

    public SingleLiveEvent<Void> appliedListEvent = new SingleLiveEvent<>();

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

    public void postUserId() {
        createProgressEvent.call();

        int listPosition = Integer.parseInt(Objects.requireNonNull(position.getValue()));
        AccessData accessData = new AccessData(listPosition, accessList);

        compositeDisposable.add(myPageRepository.postAppliedList(accessData)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(it -> {
                    if (it.code() == 200) {
                        dismissProgressEvent.call();
                        createSnackEvent.setValue("선택 성공");
                        backEvent.call();
                    } else {
                        dismissProgressEvent.call();
                        createToastEvent.setValue(String.valueOf(it.code()));
                    }
                }, it -> {
                    dismissProgressEvent.call();
                    Log.e("select Error", it.getMessage());
                }));
    }

    public void clickBack() {
        backEvent.call();
    }
}
