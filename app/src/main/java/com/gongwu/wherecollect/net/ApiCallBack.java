package com.gongwu.wherecollect.net;


import com.gongwu.wherecollect.net.entity.base.ResponseBase;
import com.gongwu.wherecollect.util.Lg;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author pengqm
 * @name FilmApplication
 * @class name：com.baidu.iov.dueros.film.net
 * @time 2018/10/10 13:51
 * @change
 * @class describe
 */

public abstract class ApiCallBack<T> implements Callback<ResponseBase<T>> {
    private static final String TAG = ApiCallBack.class.getSimpleName();
    private static final int SUCCESS_CODE = 200;

    @Override
    public void onResponse(Call<ResponseBase<T>> call, Response<ResponseBase<T>> response) {

        try {
            ResponseBase<T> responseBase = response.body();
            if ("ERROR".equals(responseBase.getCode())) {
                Lg.getInstance().e(TAG, responseBase.getMsg());
                onFailed(responseBase.getMsg());
                return;
            }
            if (Integer.valueOf(responseBase.getCode()) == SUCCESS_CODE) {
                onSuccess(responseBase.getData());
            } else {
                Lg.getInstance().e(TAG, responseBase.getMsg());
                onFailed(responseBase.getMsg());
            }
        } catch (Exception e) {
            Lg.getInstance().e(TAG, e.getMessage());
            onFailed(e.getMessage());
        }
    }


    @Override
    public void onFailure(Call<ResponseBase<T>> call, Throwable throwable) {
        Lg.getInstance().e(TAG, throwable.getMessage());
        onFailed(throwable.getMessage());
    }

    public abstract void onSuccess(T data);

    public abstract void onFailed(String msg);
}
