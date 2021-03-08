package com.gongwu.wherecollect.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.SearchListAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.ISearchContract;
import com.gongwu.wherecollect.contract.presenter.SearchPresenter;
import com.gongwu.wherecollect.net.entity.SerchBean;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.SerchListBean;
import com.gongwu.wherecollect.net.entity.response.SerchListDetailsBean;
import com.gongwu.wherecollect.view.EditTextWatcher;
import com.gongwu.wherecollect.view.FlowViewGroup;
import com.gongwu.wherecollect.view.Loading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseMvpActivity<SearchActivity, SearchPresenter> implements ISearchContract.ISearchView {

    @BindView(R.id.flow_view_group)
    FlowViewGroup mFlowViewGroup;
    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.search_clear_btn)
    ImageButton clearBtn;
    @BindView(R.id.history_hint)
    TextView historyHint;
    @BindView(R.id.clear_search)
    TextView clearSearch;
    @BindView(R.id.search_list_view)
    RecyclerView mRecyclerView;

    private Loading loading;
    private List<SerchBean> mSearchlist = new ArrayList<>();
    private List<SerchListDetailsBean> mlist = new ArrayList<>();

    private SearchListAdapter mAdapter;
    private SerchListBean serchListBean;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViews() {
        initData();
        searchEdit.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(searchEdit.getText())) {
                    clearBtn.setVisibility(View.GONE);
                    initData();
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchKeyWord();
                }
                return false;
            }
        });
        mAdapter = new SearchListAdapter(mContext, mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new SearchListAdapter.OnItemChildClickListener() {
            @Override
            public void onItemClick(FamilyBean bean) {

            }

            @Override
            public void onOpenBtnClick(boolean isOpen) {
                showOpenBtn(serchListBean, isOpen);
            }

            @Override
            public void onSearchText(String keyword) {
                StringBuilder sb = new StringBuilder();
                sb.append("#").append(keyword).append("#");
                searchEdit.setText(sb.toString());
                searchEdit.setSelection(searchEdit.getText().toString().length());
                searchKeyWord();
            }
        });
    }

    @OnClick({R.id.back_btn, R.id.clear_search, R.id.serch, R.id.search_clear_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.serch:
                searchKeyWord();
                break;
            case R.id.clear_search:
                SerchBean.deleteAll(SerchBean.class);
                initData();
                break;
            case R.id.search_clear_btn:
                searchEdit.setText("");
                if (historyHint.getVisibility() != View.VISIBLE) {
                    initData();
                }
                break;
            default:
                break;
        }
    }

    private void searchKeyWord() {
        if (TextUtils.isEmpty(searchEdit.getText())) {
            Toast.makeText(mContext, "请输入文字", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put("upDateTime", System.currentTimeMillis());
        int i = SerchBean.updateAll(SerchBean.class, cv, "title=?", searchEdit.getText().toString());
        if (i == 0) {
            SerchBean bean = new SerchBean();
            bean.setTitle(searchEdit.getText().toString());
            bean.setUpDateTime(System.currentTimeMillis());
            bean.save();
        }
        getPresenter().getSearchList(App.getUser(mContext).getId(), searchEdit.getText().toString());
    }

    private void initData() {
        mSearchlist.clear();
        mSearchlist.addAll(SerchBean.limit(10).order("upDateTime desc").find(SerchBean.class));
        mFlowViewGroup.removeAllViews();
        for (SerchBean bean : mSearchlist) {
            TextView text = (TextView) View.inflate(mContext, R.layout.flow_textview_layout, null);
            mFlowViewGroup.addView(text);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) text.getLayoutParams();
            lp.bottomMargin = 5;
            lp.topMargin = 5;
            lp.rightMargin = 10;
            lp.leftMargin = 10;
            text.setLayoutParams(lp);
            text.setText(bean.getTitle());
            text.setBackgroundResource(R.drawable.shape_white_split_r4dp);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchEdit.setText(text.getText().toString());
                    searchEdit.setSelection(searchEdit.getText().toString().length());
                    searchKeyWord();
                }
            });
        }
        historyHint.setVisibility(mSearchlist.size() > 0 ? View.VISIBLE : View.GONE);
        clearSearch.setVisibility(mSearchlist.size() > 0 ? View.VISIBLE : View.GONE);
        mFlowViewGroup.setVisibility(mSearchlist.size() > 0 ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void hideHistory() {
        clearSearch.setVisibility(View.GONE);
        historyHint.setVisibility(View.GONE);
        mFlowViewGroup.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected SearchPresenter createPresenter() {
        return SearchPresenter.getInstance();
    }

    @Override
    public void getSearchListSuccess(SerchListBean data) {
        serchListBean = data;
        mAdapter.setOpenType(false);
        showOpenBtn(data, false);
    }

    private void showOpenBtn(SerchListBean data, boolean isOpen) {
        hideHistory();
        mlist.clear();
        mAdapter.setShowOpenBtnType(false);
        if (data.getObjects() != null && data.getObjects().getItems() != null && data.getObjects().getItems().size() > 0) {
            SerchListDetailsBean bean = new SerchListDetailsBean();
            bean.setType(0);
            if (data.getObjects().getItems().size() > 2 && !isOpen) {
                List<ObjectBean> lists = new ArrayList<>();
                lists.add(data.getObjects().getItems().get(0));
                lists.add(data.getObjects().getItems().get(1));
                bean.setItems(lists);
            } else {
                bean.setItems(data.getObjects().getItems());
            }
            mlist.add(bean);
            mAdapter.setShowOpenBtnType(data.getObjects().getItems().size() > 2);
        }
        if (data.getCategories() != null && data.getCategories().getItems() != null && data.getCategories().getItems().size() > 0) {
            data.getCategories().setType(1);
            mlist.add(data.getCategories());
        }
        if (data.getLocations() != null && data.getLocations().getItems() != null && data.getLocations().getItems().size() > 0) {
            data.getLocations().setType(2);
            mlist.add(data.getLocations());
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressDialog() {
        loading = Loading.show(loading, mContext, "");
    }

    @Override
    public void hideProgressDialog() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void onError(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }
}
