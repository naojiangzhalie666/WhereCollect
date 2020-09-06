package com.gongwu.wherecollect.ImageSelect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.net.entity.ImageData;
import com.gongwu.wherecollect.object.SelectSortChildActivity;
import com.gongwu.wherecollect.util.PhotosDialog;
import com.gongwu.wherecollect.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ImageGridActivity extends BaseActivity implements PhotosDialog.OndismissListener {
    public static final String EXTRA_IMAGE_LIST = "imagelist";

    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.title_commit_tv_color_maincolor)
    TextView commitTv;

    public static final int RESULT = 834;
    public static int imgMax = 10;
    List<ImageData> dataList;
    GridView gridView;
    ImageGridAdapter adapter;// 列表适配器
    AlbumHelper helper;
    /**
     *
     */
    ArrayList<ImageData> temp = new ArrayList<ImageData>();
    private PhotosDialog photosDialog;

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent(context, ImageGridActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_grid;
    }

    @Override
    protected void initViews() {
        mTitleTv.setText("选择照片");
        imgMax = getIntent().getIntExtra("max", 10);
        if (imgMax > 1) {
            commitTv.setVisibility(View.VISIBLE);
        }
        helper = AlbumHelper.getHelper();
        helper.init(this);
        AlbumHelper helper = AlbumHelper.getHelper();
        helper.init(this);
        dataList = helper.getImages();
        initView();
    }

    @OnClick({R.id.back_btn, R.id.title_commit_tv_color_maincolor})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.title_commit_tv_color_maincolor:
                Intent intent = new Intent();
                intent.putExtra("list", (Serializable) adapter.chooseList);
                setResult(RESULT, intent);
                finish();
                break;
        }
    }

    @Override
    protected void initPresenter() {

    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new ImageGridAdapter(ImageGridActivity.this, dataList, imgMax) {
            @Override
            protected void change(List<ImageData> chooseList) {
                super.change(chooseList);
                if (StringUtils.isEmpty(chooseList)) {
                    mTitleTv.setText("选择照片");
                } else {
                    mTitleTv.setText(String.format("已选择(%s)", chooseList.size()));
                    //最大勾选图片数量为1的时候自动跳转到切割图片界面
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (imgMax == 1) {
                                Intent intent = new Intent();
                                intent.putExtra("list", (Serializable) adapter.chooseList);
                                setResult(RESULT, intent);
                                finish();
                            }
                        }
                    }, 100);
                }
            }
        };
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                temp.clear();
                temp.add(dataList.get(position));
                photosDialog = new PhotosDialog(ImageGridActivity.this, false, true, temp);
                photosDialog.setOndismissListener(ImageGridActivity.this);
                photosDialog.showPhotos(0);
            }
        });
    }

    @Override
    public void onDismiss() {
        if (temp.isEmpty()) {
            return;
        } else {
            if (temp.get(0).isSelect()) {
                if (!adapter.chooseList.contains(temp.get(0))) {
                    adapter.chooseList.add(temp.get(0));
                }
            } else {
                if (adapter.chooseList.contains(temp.get(0))) {
                    adapter.chooseList.remove(temp.get(0));
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
