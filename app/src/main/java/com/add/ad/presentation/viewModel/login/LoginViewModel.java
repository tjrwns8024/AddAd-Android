package com.add.ad.presentation.viewModel.login;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.add.ad.data.local.SharedPref;
import com.add.ad.data.repository.auth.AuthRepository;
import com.add.ad.entity.Auth;
import com.add.ad.entity.Token;
import com.add.ad.presentation.base.BaseViewModel;
import com.add.ad.presentation.base.SingleLiveEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class LoginViewModel extends BaseViewModel {
    CompositeDisposable compositeDisposable;
    AuthRepository authRepository;
    SharedPref sharedPref;

    public MutableLiveData<String> userId = new MutableLiveData<>("");
    public MutableLiveData<String> userPassword = new MutableLiveData<>("");

    public MediatorLiveData<Boolean> loginMediator = new MediatorLiveData<>();

    public SingleLiveEvent<Void> startMain = new SingleLiveEvent<>();
    public SingleLiveEvent<String> pwErrorEvent = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> startRegister = new SingleLiveEvent<>();

    @ViewModelInject
    public LoginViewModel(AuthRepository authRepository, CompositeDisposable compositeDisposable, SharedPref sharedPref, @Assisted SavedStateHandle savedStateHandle) {
        this.authRepository = authRepository;
        this.compositeDisposable = compositeDisposable;
        this.sharedPref = sharedPref;
    }

    public void autoLogin() {
        if (!sharedPref.getInfo().isEmpty()) {
            startMain.call();
        }
    }

    public void login() {
        createProgressEvent.call();
        String deviceToken = sharedPref.getDeviceToken();
        Auth auth = new Auth(userId.getValue(), userPassword.getValue(), deviceToken);

        compositeDisposable.add(
                authRepository.signIn(auth)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::loginSuccess
                                , t -> loginFail(t)));

    }


    private void loginSuccess(Response<Token> data) {
        if (data.code() / 2 == 100) {
            sharedPref.saveToken(data.body() != null ? data.body().getAccessToken() : null, true);
            sharedPref.saveToken(data.body() != null ? data.body().getRefreshToken() : null, false);
            sharedPref.saveInfo(data.body() != null ? data.body().getUserInfo() : null);
            startMain.call();
            dismissProgressEvent.call();
            createSnackEvent.setValue("로그인 성공");
        } else {
            dismissProgressEvent.call();
            pwErrorEvent.setValue("비밀번호가 일치하지 않습니다.");
        }
    }

    public void goRegister() {
        startRegister.call();
    }

    private void loginFail(Throwable t) {
        dismissProgressEvent.call();
        createToastEvent.setValue(t.getLocalizedMessage());
    }
}
