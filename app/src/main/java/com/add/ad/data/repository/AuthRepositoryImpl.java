package com.add.ad.data.repository;

import com.add.ad.data.api.Api;
import com.add.ad.data.local.SharedPref;
import com.add.ad.entity.Auth;
import com.add.ad.entity.Token;

import javax.inject.Inject;

import io.reactivex.Single;
import retrofit2.Response;

public class AuthRepositoryImpl implements AuthRepository {
    Api api;
    SharedPref sharedPref;

    @Inject
    public AuthRepositoryImpl(Api api, SharedPref sharedPref) {
        this.api = api;
        this.sharedPref = sharedPref;
    }

    @Override
    public Single<Response<Token>> signIn(Auth auth) {
        return api.signIn(auth);
    }
}
