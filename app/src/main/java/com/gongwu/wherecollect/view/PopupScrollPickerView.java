package com.gongwu.wherecollect.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.BuyVIPActivity;
import com.gongwu.wherecollect.activity.ConfigChangePhoneActivity;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.ToastUtil;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.forward.androids.views.BitmapScrollPicker;
import cn.forward.androids.views.ScrollPickerView;
import razerdp.basepopup.BasePopupWindow;

public class PopupScrollPickerView extends BasePopupWindow {

    @BindView(R.id.clear)
    ImageView clear;
    @BindView(R.id.picker_horizontal)
    BitmapScrollPicker mPickerHorizontal;
    @BindView(R.id.edit_name_tv)
    EditText mEditText;
    @BindView(R.id.iv_layout)
    View ivLayout;
    @BindView(R.id.pop_title_tv)
    TextView titleTv;

    private Context mContext;
    private FurnitureBean bean;
    private ObjectBean boxBean;
    private boolean isEditImgAndName = true;
    private ChangeHeaderImgDialog changeHeaderdialog;
    private File imgFile;

    public PopupScrollPickerView(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_scroll_picker_layout);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        ButterKnife.bind(this, contentView);
        mEditText.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                clear.setVisibility(TextUtils.isEmpty(mEditText.getText().toString()) ? View.GONE : View.VISIBLE);
            }
        });
    }

    @OnClick({R.id.cancel_iv, R.id.clear, R.id.pop_commit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_iv:
                dismiss();
                break;
            case R.id.clear:
                mEditText.setText("");
                break;
            case R.id.pop_commit_tv:
                if (TextUtils.isEmpty(mEditText.getText().toString().trim())) {
                    Toast.makeText(getContext(), "请输入名称", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (isEditImgAndName && imageView.isEnabled()) {
//                    if (TextUtils.isEmpty(bean.getBackground_url())) {
//                        Toast.makeText(getContext(), "请添加展示图", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
                if (listener != null) {
                    if (isEditImgAndName) {
                        bean.setName(mEditText.getText().toString().trim());
                        boolean isEdit = false;
                        //imgFile有值说明重新选择了图片
                        if (imgFile != null) {
                            isEdit = true;
                            boxBean = null;
                        }
                        //下标在1的话,说明选择的是默认
                        if (mPickerHorizontal.getSelectedPosition() == 1) {
                            imgFile = null;
                            isEdit = true;
                        }
                        //默认图片
                        if (boxBean == null && imgFile == null && mPickerHorizontal.getSelectedPosition() == AppConstant.DEFAULT_INDEX_OF) {
                            isEdit = true;
                        }
                        //boxBean有值,说明收纳盒原先有设置图片,这次并没有修改
                        listener.onCommitClick(bean, imgFile, isEdit);
                    } else {
                        listener.onEditNameCommitClick(mEditText.getText().toString().trim());
                    }
                }
                dismiss();
                break;
        }
    }

    private PopupClickListener listener;

    public interface PopupClickListener {

        void onCommitClick(FurnitureBean bean, File file, boolean isEdit);

        void onEditNameCommitClick(String name);
    }

    public void setPopupClickListener(PopupClickListener listener) {
        this.listener = listener;
    }

    public void initData(Activity activity, int titleId, String editText, ObjectBean boxBean, boolean isEditImgAndName) {
        imgFile = null;
        titleTv.setText(titleId);
        if (!TextUtils.isEmpty(editText)) {
            mEditText.setText(editText);
            mEditText.setSelection(mEditText.getText().toString().length());
        }
        this.bean = new FurnitureBean();
        this.boxBean = boxBean;
        this.isEditImgAndName = isEditImgAndName;
        ivLayout.setVisibility(isEditImgAndName ? View.VISIBLE : View.GONE);
        if (isEditImgAndName) {
            mPickerHorizontal.setIsCirculation(false);
            mPickerHorizontal.setOnSelectedListener(new ScrollPickerView.OnSelectedListener() {
                @Override
                public void onSelected(ScrollPickerView scrollPickerView, int position) {
                    if (App.getUser(mContext).isIs_vip()) {
                        if (position == AppConstant.DEFAULT_INDEX_OF) {
                            showSelectImgDialog(activity, mPickerHorizontal, boxBean);
                        }
                    } else {
                        BuyVIPActivity.start(mContext);
                    }
                }
            });
            final CopyOnWriteArrayList<Bitmap> bitmaps = new CopyOnWriteArrayList<Bitmap>();
            //系统默认
            bitmaps.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.icon_template_box));
            if (boxBean != null && !TextUtils.isEmpty(boxBean.getImage_url()) && boxBean.getImage_url().contains("http")) {
                //默认两种
                ImageLoader.getBitmap(activity, boxBean.getImage_url(), new ImageLoader.GlideLoadBitmapCallback() {
                    @Override
                    public void getBitmapCallback(Bitmap bitmap) {
                        //拍照
                        bitmaps.add(0, bitmap);
                        mPickerHorizontal.setData(bitmaps);
                        mPickerHorizontal.setSelectedPosition(AppConstant.DEFAULT_INDEX_OF);
                    }
                });
            } else {
                //拍照
                bitmaps.add(0, BitmapFactory.decodeResource(activity.getResources(), R.drawable.icon_add_box_img));
                mPickerHorizontal.setData(bitmaps);
                mPickerHorizontal.setSelectedPosition(bitmaps.size());
            }
        }
    }

    //拍照dialog
    private void showSelectImgDialog(Activity activity, BitmapScrollPicker mPickerHorizontal, ObjectBean boxBean) {
        changeHeaderdialog = new ChangeHeaderImgDialog(activity, null, null) {
            @Override
            public void getResult(File file) {
                CopyOnWriteArrayList<Bitmap> bitmaps = new CopyOnWriteArrayList<Bitmap>();
                bitmaps.add(BitmapFactory.decodeFile(file.getPath()));
                bitmaps.add(BitmapFactory.decodeResource(activity.getResources(), R.drawable.icon_template_box));
                mPickerHorizontal.setData(bitmaps);
                mPickerHorizontal.setSelectedPosition(AppConstant.DEFAULT_INDEX_OF);
                imgFile = file;
            }
        };
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (changeHeaderdialog != null) {
            changeHeaderdialog.onActivityResult(requestCode, resultCode, data);
        }
    }
}
