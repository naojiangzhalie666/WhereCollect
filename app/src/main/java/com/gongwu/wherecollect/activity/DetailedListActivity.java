package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IDetailedListContract;
import com.gongwu.wherecollect.contract.presenter.DetailedListPresenter;
import com.gongwu.wherecollect.net.entity.response.DetailedListBean;
import com.gongwu.wherecollect.net.entity.response.DetailedListGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureResponse;
import com.gongwu.wherecollect.util.FileShareUtils;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.DetailedListView;
import com.gongwu.wherecollect.view.Loading;
import com.gongwu.wherecollect.view.SplitView;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 清单
 */
public class DetailedListActivity extends BaseMvpActivity<DetailedListActivity, DetailedListPresenter> implements IDetailedListContract.IDetailedListView {

    private Loading loading;
    private RoomFurnitureResponse structure;

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;
    @BindView(R.id.image_btn)
    ImageButton image_btn;

    private String userId, family_code, room_id, room_code, furniture_code, qrcodeString;
    private List<DetailedListView> views = new ArrayList<>();
    private static final String PDFpath = App.CACHEPATH + "收哪儿_物品清单.pdf";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detailed_list;
    }

    @Override
    protected void initViews() {
        titleTv.setText("物品清单");
        image_btn.setVisibility(View.VISIBLE);
        structure = (RoomFurnitureResponse) getIntent().getSerializableExtra("structure");
        userId = App.getUser(mContext).getId();
        family_code = getIntent().getStringExtra("family_code");
        room_id = getIntent().getStringExtra("room_id");
        room_code = getIntent().getStringExtra("room_code");
        furniture_code = getIntent().getStringExtra("furniture_code");
        qrcodeString = new StringBuilder("goFurniture:fc=").append(furniture_code).
                append(",rd=").append(room_id)
                .append(",rc=").append(room_code)
                .append(",fmc=").append(family_code).toString();
        getPresenter().getDetailedList(userId, family_code, room_code, furniture_code);
    }

    private int indexof = 1;
    private int initCount = 1;
    private List<String> gcCodes = new ArrayList<>();
    private List<String> boxCodes = new ArrayList<>();

    private void initData(DetailedListBean data) {
        if (structure == null) return;
        DetailedListView detailedListView = new DetailedListView(mContext);
        DetailedListBean newBean = detailedListView.initData(data, qrcodeString, structure, indexof, initCount, gcCodes, boxCodes);
        contentLayout.addView(detailedListView);
        views.add(detailedListView);
        if (newBean != null && newBean.getObjects() != null && newBean.getObjects().size() > 0) {
            indexof++;
            contentLayout.addView(new SplitView(mContext));
            initData(newBean);
        }
    }

    private void getPDF() {
        showProgressDialog();
        Observable.create(new ObservableOnSubscribe<File>() {

            @Override
            public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                Document doc = new Document(PageSize.A4, 0, 0, 0, 0);
                File file = new File(PDFpath);
                try {
                    //获取PDF书写器
                    PdfWriter.getInstance(doc, new FileOutputStream(file));
                    //打开文档
                    doc.open();
                    //图片对象
                    Image img = null;
                    //遍历
                    for (int i = 0; i < views.size(); i++) {
                        DetailedListView view = views.get(i);
                        Bitmap bitmap = getBitmapFromView(view);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        //获取图片
                        img = Image.getInstance(byteArray);
                        //使图片与A4纸张大小自适应
                        img.scaleToFit(new Rectangle(PageSize.A4));
                        //添加到PDF文档
                        doc.add(img);
                        //下一页，每张图片一页
                        doc.newPage();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //关闭文档
                    doc.close();
                    emitter.onNext(file);
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
//                        ToastUtil.show(mContext, "生成成功", Toast.LENGTH_SHORT);
                        FileShareUtils.sharePdfFile(mContext, file);
                        hideProgressDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideProgressDialog();
                    }
                });
    }

    public Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    @Override
    protected DetailedListPresenter createPresenter() {
        return DetailedListPresenter.getInstance();
    }

    @OnClick({R.id.back_btn, R.id.image_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.image_btn:
                getPDF();
                break;
        }
    }

    @Override
    public void getDetailedListSuccess(DetailedListBean data) {
        if (data != null) {
            int initLine = 0;
            initCount = 1;
            indexof = 1;
            gcCodes.clear();
            boxCodes.clear();
            views.clear();
            for (int i = 0; i < data.getObjects().size(); i++) {
                DetailedListGoodsBean childBean = data.getObjects().get(i);
                int line = (int) Math.ceil((childBean.getObjs().size()) / 5.0f);
                if (initLine >= 5) {
                    initLine = 0;
                    initCount++;
                }
                initLine += line;
            }
            initData(data);
        }
    }

    @Override
    public void showProgressDialog() {
        loading = Loading.show(null, mContext, "");
    }

    @Override
    public void hideProgressDialog() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void onError(String result) {

    }

    public static void start(Context context, String family_code, String room_id, String room_code, String furniture_code, RoomFurnitureResponse mRoomFurniture) {
        Intent intent = new Intent(context, DetailedListActivity.class);
        intent.putExtra("family_code", family_code);
        intent.putExtra("room_id", room_id);
        intent.putExtra("room_code", room_code);
        intent.putExtra("furniture_code", furniture_code);
        intent.putExtra("structure", mRoomFurniture);
        context.startActivity(intent);
    }
}
