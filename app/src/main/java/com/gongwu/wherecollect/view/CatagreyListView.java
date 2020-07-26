package com.gongwu.wherecollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gongwu.wherecollect.adapter.GuishuListAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.base.RequestBase;
import com.gongwu.wherecollect.net.entity.request.AddGoodsPropertyReq;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;
import com.gongwu.wherecollect.object.SelectSortActivity;
import com.gongwu.wherecollect.util.ApiUtils;
import com.gongwu.wherecollect.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 * Date: 2017/11/11
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class CatagreyListView extends ListView implements AdapterView.OnItemClickListener {
    public ChannelBean selectGuishu;
    List<ChannelBean> guiShuList = new ArrayList<>();
    List<ChannelBean> currentList = new ArrayList<>();//当前显示的归属list
    GuishuListAdapter guishuAdapter;
    Context context;
    TextView guishuTxt;

    public CatagreyListView(Context context) {
        this(context, null);
    }

    public CatagreyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(TextView textView) {
        guishuTxt = textView;
        guishuAdapter = new GuishuListAdapter(context, currentList);
        setAdapter(guishuAdapter);
        setOnItemClickListener(this);
        initChanalData(null);
    }

    /**
     * 获取归属列表
     */
    private void initChanalData(final ChannelBean channelBean) {
        AddGoodsPropertyReq base = new AddGoodsPropertyReq();
        base.setUid(App.getUser(context).getId());
        if (channelBean != null) {
            if (context instanceof SelectSortActivity) {
                base.setCategory_code(channelBean.getCode());
            } else {
                base.setCode(channelBean.getCode());
            }
        }
        ApiUtils.getChannelList(base, new ApiCallBack<List<ChannelBean>>() {
            @Override
            public void onSuccess(List<ChannelBean> data) {
                ChannelBean bean = new ChannelBean();
                bean.setName("自定义");
                data.add(0, bean);
                if (channelBean == null) {
                    guiShuList.clear();
                    guiShuList.addAll(data);
                    currentList = guiShuList;
                } else {
                    channelBean.setChildBeans(data);
                    for (ChannelBean cb : data) {
                        cb.setParentBeans(currentList);
                        cb.setParentsBean(channelBean);
                    }
                    currentList = data;
                }
                guishuAdapter.notifyDate(currentList);
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        guishuTxt.setText(currentList.get(position).getString());
        selectGuishu = currentList.get(position);
        if (currentList.get(position).getTag_count() != 0) {
            if (StringUtils.isEmpty(currentList.get(position).getChildBeans())) {
                initChanalData(currentList.get(position));
            } else {
                currentList = currentList.get(position).getChildBeans();
                guishuAdapter.notifyDate(currentList);
            }
        }
    }

    /**
     * 返回到上一个列表
     */
    public void lastList() {
        if (selectGuishu != null && (!StringUtils.isEmpty(selectGuishu.getParentBeans()))) {
            selectGuishu = selectGuishu.getParentsBean();
            currentList = selectGuishu.getChildBeans();
            guishuAdapter.notifyDate(currentList);
            guishuTxt.setText(selectGuishu.getString());
        } else {
            currentList = guiShuList;
            guishuAdapter.notifyDate(currentList);
            guishuTxt.setText("");
        }
    }

    /**
     * 还原到原始列表
     */
    public void resetData() {
        currentList = guiShuList;
        guishuAdapter.notifyDate(currentList);
        selectGuishu = null;
    }
}
