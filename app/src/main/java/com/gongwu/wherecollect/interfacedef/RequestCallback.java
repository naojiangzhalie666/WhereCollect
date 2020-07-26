package com.gongwu.wherecollect.interfacedef;

public interface RequestCallback<T> {
    void onSuccess(T data);
    void onFailure(String msg);
}
