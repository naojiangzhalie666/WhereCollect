package com.gongwu.wherecollect.base;


/**
 * MVP活动的基类，封装好了一些销毁presenter的解绑

 */

public abstract class BaseMvpActivity<V extends BaseView, P extends BasePresenter> extends BaseActivity {

    private P presenter;

    /**
     * 初始化presenter
     */
    @Override
    protected void initPresenter() {
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView((V) this);
        }
    }

    /**
     * 创建presenter
     *
     * @return Presenter
     */
    protected abstract P createPresenter();


    /**
     * 得到presenter
     *
     * @return presenter
     */
    protected P getPresenter() {
        return presenter;
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }


}
