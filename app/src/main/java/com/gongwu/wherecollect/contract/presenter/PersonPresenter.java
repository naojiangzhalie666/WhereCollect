package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IPersonContract;
import com.gongwu.wherecollect.contract.model.PersonModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditPersonReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

public class PersonPresenter extends BasePresenter<IPersonContract.IPersonView> implements IPersonContract.IPersonPresenter {
    private static final String TAG = "PersonPresenter";

    public static final int AVATAR = 0;
    public static final int NICKNAME = 1;
    public static final int GENDER = 2;
    public static final int BIRTHDAY = 3;


    private IPersonContract.IPersonModel mModel;

    private PersonPresenter() {
        mModel = new PersonModel();
    }

    public static PersonPresenter getInstance() {
        return PersonPresenter.Inner.instance;
    }

    private static class Inner {
        private static final PersonPresenter instance = new PersonPresenter();
    }

    @Override
    public void editInfo(String uid, int keyword, String value) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditPersonReq req = new EditPersonReq();
        req.setUid(uid);
        switch (keyword) {
            case AVATAR:
                req.setAvatar(value);
                break;
            case NICKNAME:
                req.setNickname(value);
                break;
            case GENDER:
                req.setGender(value);
                break;
            case BIRTHDAY:
                req.setBirthday(value);
                break;
        }
        mModel.editInfo(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().editInfoSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }
}
