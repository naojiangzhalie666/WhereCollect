package com.gongwu.wherecollect.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.gongwu.wherecollect.R;


/**
 * @author KaraShokZ
 * DESCRIPTION 解决中英文混排，自动折行问题
 * @name AutoWrapedTextView
 **/
public class AutoSplitTextView extends AppCompatTextView {

    private int mLineY = 0;//总行高
    private int mViewWidth;//TextView的总宽度
    private TextPaint paint;
    private Context mContext;

    public AutoSplitTextView(Context context) {
        this(context, null);
    }

    public AutoSplitTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoSplitTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mLineY = 0;
        mViewWidth = getMeasuredWidth();//获取textview的实际宽度
        mLineY += getTextSize();

        String text = getText().toString();

        Layout layout = getLayout();
        int lineCount = layout.getLineCount();
        for (int i = 0; i < lineCount; i++) {//每行循环
            int lineStart = layout.getLineStart(i);
            int lineEnd = layout.getLineEnd(i);
            String lineText = text.substring(lineStart, lineEnd);//获取TextView每行中的内容
            if (needScale(lineText)) {
                for (int j = 0; j < lineText.length(); j++) {

                }
                if (i == lineCount - 1) {//最后一行不需要重绘
                    canvas.drawText(lineText, 0, mLineY, paint);
                } else {
                    float width = StaticLayout.getDesiredWidth(text, lineStart, lineEnd, paint);
                    //i就是行数
                    drawScaleText(canvas, lineText, width, i);
                }
            } else {
                canvas.drawText(lineText, 0, mLineY, paint);
            }
            mLineY += getLineHeight();//写完一行以后，高度增加一行的高度
        }
    }

    /**
     * 重绘此行.
     *
     * @param canvas    画布
     * @param lineText  该行所有的文字
     * @param lineWidth 该行每个文字的宽度的总和
     */
    private void drawScaleText(Canvas canvas, String lineText, float lineWidth, int firstline) {
        float x = 0;
        if (isFirstLineOfParagraph(lineText)) {
            String blanks = "  ";
            canvas.drawText(blanks, x, mLineY, paint);
            float width = StaticLayout.getDesiredWidth(blanks, paint);
            x += width;
            lineText = lineText.substring(3);
        }

        //比如说一共有5个字，中间有4个间隔，
        //那就用整个TextView的宽度 - 5个字的宽度，
        //然后除以4，填补到这4个空隙中

        float interval = (mViewWidth - lineWidth) / (lineText.length() - 1);
        for (int i = 0; i < lineText.length(); i++) {
            //等于第一行的且前六个字
            if (i > 4 && i < 11 && firstline == 2) {
                paint.setColor(ContextCompat.getColor(mContext, R.color.maincolor));
            } else if (i > 11 && i <= 15 && firstline == 2) {
                paint.setColor(ContextCompat.getColor(mContext, R.color.maincolor));
            } else if (i <= 1 && firstline == 3) {
                paint.setColor(ContextCompat.getColor(mContext, R.color.maincolor));
            } else {
                paint.setColor(getCurrentTextColor());
            }
            String character = String.valueOf(lineText.charAt(i));
            float cw = StaticLayout.getDesiredWidth(character, paint);
            canvas.drawText(character, x, mLineY, paint);
            x += (cw + interval);
        }
    }


    /**
     * 判断是不是段落的第一行.
     * 一个汉字相当于一个字符，此处判断是否为第一行的依据是：
     * 字符长度大于3且前两个字符为空格
     *
     * @param lineText 该行所有的文字
     */
    private boolean isFirstLineOfParagraph(String lineText) {
        return lineText.length() > 3 && lineText.charAt(0) == ' ' && lineText.charAt(1) == ' ';
    }

    /**
     * 判断需不需要缩放.
     *
     * @param lineText 该行所有的文字
     * @return true 该行最后一个字符不是换行符  false 该行最后一个字符是换行符
     */
    private boolean needScale(String lineText) {
        if (lineText.length() == 0) {
            return false;
        } else {
            return lineText.charAt(lineText.length() - 1) != '\n';
        }
    }

}

