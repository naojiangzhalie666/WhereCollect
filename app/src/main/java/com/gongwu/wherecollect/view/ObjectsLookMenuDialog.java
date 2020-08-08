package com.gongwu.wherecollect.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.AddRemindActivity;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.object.AddGoodsActivity;
import com.gongwu.wherecollect.util.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Function://物品查看时右上角menu
 * Date: 2017/9/7
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class ObjectsLookMenuDialog {
    Context context;
    Dialog dialog;
    ObjectBean bean;


    public ObjectsLookMenuDialog(Activity context, ObjectBean bean) {
        this.bean = bean;
        this.context = context;
        dialog = new Dialog(context,
                R.style.Transparent2);
        dialog.setCanceledOnTouchOutside(true);
        View view = View.inflate(context,
                R.layout.layout_goods_look_menu, null);
        ButterKnife.bind(this, view);
        dialog.setContentView(view);
        Animation ani = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in);
        view.findViewById(R.id.linearLayout).startAnimation(ani);
        dialog.show();
    }

    @OnClick({R.id.edit_location, R.id.edit_goods, R.id.add_remind_tv, R.id.delete_tv, R.id.cancel, R.id.linearLayout, R
            .id.root})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_location:
                editLocation();
                dialog.dismiss();
                break;
            case R.id.edit_goods:
                editGoods();
                dialog.dismiss();
                break;
            case R.id.add_remind_tv:
                AddRemindActivity.start(context, bean);
                dialog.dismiss();
                break;
            case R.id.delete_tv:
                deleteObject();
                dialog.dismiss();
                break;
            case R.id.cancel:
            case R.id.linearLayout:
            case R.id.root:
                dialog.dismiss();
                break;
        }
    }

    public void editLocation() {

    }

    public void deleteGoods() {

    }

    /**
     * 删除物品
     */
    private void deleteObject() {
        DialogUtil.show("提醒", "确认删除此物品？", "删除", "取消", ((Activity) context), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteGoods();
            }
        }, null);
    }

    /**
     * 编辑物品
     */
    public void editGoods() {
//        Intent intent = new Intent(context, AddGoodsActivity.class);
//        intent.putExtra("bean", bean);
//        ((Activity) context).startActivityForResult(intent, 0);
//        MobclickAgent.onEvent(context, "050103");
    }
}
