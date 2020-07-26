package com.gongwu.wherecollect.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.util.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class PopupEditFurnitureName extends BasePopupWindow {

    @BindView(R.id.clear)
    ImageView clear;
    @BindView(R.id.pop_furniture_iv)
    ImageView imageView;
    @BindView(R.id.edit_name_tv)
    EditText mEditText;
    @BindView(R.id.iv_layout)
    View ivLayout;
    @BindView(R.id.pop_title_tv)
    TextView titleTv;

    private FurnitureBean bean;
    private boolean isEditImgAndName = true;

    public PopupEditFurnitureName(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_edit_furniture_name);
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

    @OnClick({R.id.cancel_iv, R.id.pop_furniture_iv, R.id.clear, R.id.pop_commit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_iv:
                dismiss();
                break;
            case R.id.pop_furniture_iv:
                if (listener != null) {
                    listener.onImgClick();
                }
                break;
            case R.id.clear:
                mEditText.setText("");
                break;
            case R.id.pop_commit_tv:
                if (TextUtils.isEmpty(mEditText.getText().toString().trim())) {
                    Toast.makeText(getContext(), "请输入名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEditImgAndName && imageView.isEnabled()) {
                    if (TextUtils.isEmpty(bean.getBackground_url())) {
                        Toast.makeText(getContext(), "请添加展示图", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (listener != null) {
                    if (isEditImgAndName) {
                        bean.setName(mEditText.getText().toString().trim());
                        listener.onCommitClick(bean);
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

        void onImgClick();

        void onCommitClick(FurnitureBean bean);

        void onEditNameCommitClick(String name);
    }

    public void setPopupClickListener(PopupClickListener listener) {
        this.listener = listener;
    }

    public void setImg(String path) {
        ImageLoader.load(getContext(), imageView, path);
        if (bean != null) {
            bean.setBackground_url(path);
            bean.setImage_url(path);
        }
    }

    public void initData(int titleId, String editText, FurnitureBean bean, boolean isEditImgAndName) {
        titleTv.setText(titleId);
        if (!TextUtils.isEmpty(editText)) {
            mEditText.setText(editText);
            mEditText.setSelection(mEditText.getText().toString().length());
        }
        if (bean != null) {
            this.bean = bean;
            if (!TextUtils.isEmpty(bean.getBackground_url())) {
                ImageLoader.load(getContext(), imageView, bean.getBackground_url());
            }
            if (!TextUtils.isEmpty(bean.getName())) {
                mEditText.setText(bean.getName());
                mEditText.setSelection(mEditText.getText().toString().length());
                clear.setVisibility(View.VISIBLE);
            }
        } else {
            this.bean = new FurnitureBean();
        }
        this.isEditImgAndName = isEditImgAndName;
        ivLayout.setVisibility(isEditImgAndName ? View.VISIBLE : View.GONE);
    }

    public void setImgPathAndEnabled(int resId, boolean enabled) {
        imageView.setImageDrawable(getContext().getDrawable(resId));
        imageView.setEnabled(enabled);
    }
}
