package com.luyang.myapplication.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.midi.MidiDeviceInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.luyang.myapplication.R;
import com.luyang.myapplication.util.WinnerCheck;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义VIEW,自己自定义棋盘的样式
 * Created by luyang on 2017/12/17.
 */

public class WuziqiPanel extends View {

    //棋盘属性设置
    //棋盘的边长
    private int panelWidth;
    //棋盘上小格子的边长
    private float panelLineHeight;
    //小个子的个数
    private static final int MAX_LINE = 15;

    Paint mpaint = new Paint();

    //黑白棋子通过bitmapFactory引入资源
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    //设置棋子的比例，不能占满整个小格子
    private float ratioPeice = 3 * 1.0f / 4;

    //下棋先手以及坐标存储
    private boolean isWhiteFirst = true;
    private List<Point> whiteArray = new ArrayList<>();
    private List<Point> blackArray = new ArrayList<>();

    //输赢逻辑判断
    private boolean isGameOver;
    private boolean isWhiteWinner;


    public WuziqiPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44ff0000);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //棋盘正方形边长
        int width = Math.min(widthSize, heightSize);

        //做判断是否为0
        if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }

        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        panelWidth = w;
        panelLineHeight = panelWidth * 1.0f / MAX_LINE;

        //将棋子的图片设置成新的比例
        int peiceSize = (int) (ratioPeice * panelLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, peiceSize, peiceSize, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, peiceSize, peiceSize, false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画棋盘表格
        drawBoard(canvas);
        //点击屏幕画棋子
        drawPecies(canvas);
        //检查游戏是否结束
        checkGameOver();
    }

    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(whiteArray);
        boolean blackWin = checkFiveInLine(blackArray);
        if (whiteWin || blackWin) {
            isGameOver = true;
            isWhiteWinner = whiteWin;
            String text = isWhiteWinner ? "白棋获胜" : "黑棋获胜";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * 检查是否五子连珠
     *
     * @param peiceArray
     * @return
     */
    private boolean checkFiveInLine(List<Point> peiceArray) {
        return WinnerCheck.check(peiceArray);

    }

    private void drawPecies(final Canvas canvas) {
        blackArray.forEach((Point point) -> {
            canvas.drawBitmap(mBlackPiece, (point.x + (1 - ratioPeice) / 2) * panelLineHeight, (point.y + (1 - ratioPeice) / 2) * panelLineHeight, null);
        });

        whiteArray.forEach((Point point) -> {
            canvas.drawBitmap(mWhitePiece, (point.x + (1 - ratioPeice) / 2) * panelLineHeight, (point.y + (1 - ratioPeice) / 2) * panelLineHeight, null);
        });
    }


    //棋盘整体初始化
    protected void init() {
        mpaint.setColor(0x88000000);
        mpaint.setAntiAlias(true);
        mpaint.setDither(true);
        mpaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
    }

    //绘制棋盘函数
    protected void drawBoard(Canvas canvas) {
        int bigCubeSize = panelWidth;
        float smallCubeSize = panelLineHeight;

        for (int i = 0; i < MAX_LINE; i++) {
            int sx = (int) smallCubeSize / 2;
            int ex = (int) (bigCubeSize - smallCubeSize / 2);

            int y = (int) ((0.5 + i) * smallCubeSize);
            //画横线
            canvas.drawLine(sx, y, ex, y, mpaint);
            //画纵线
            canvas.drawLine(y, sx, y, ex, mpaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Point p = getValidatePoint(x, y);
            if (whiteArray.contains(p) || blackArray.contains(p)) {
                return false;
            }

            if (isWhiteFirst) {
                whiteArray.add(p);
            } else {
                blackArray.add(p);
            }
            isWhiteFirst = !isWhiteFirst;
            //请求重绘，会调用onDraw（）函数
            invalidate();

        }
        return true;
    }

    //判断子坐标的方法
    public Point getValidatePoint(int x, int y) {
        int newx = (int) (x / panelLineHeight);
        int newy = (int) (y / panelLineHeight);
        Point p = new Point(newx, newy);
        return p;
    }


    //VIEW的存储与回复
    private final static String INSTANCE = "instance";
    private final static String INSTANCE_GAME_OVER = "instance_gameover";
    private final static String INSTANCE_BLACKARRAY = "black";
    private final static String INSTANCE_WHITEARRAY = "white";

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, isGameOver);
        bundle.putParcelableArrayList(INSTANCE_BLACKARRAY, (ArrayList<? extends Parcelable>) blackArray);
        bundle.putParcelableArrayList(INSTANCE_WHITEARRAY, (ArrayList<? extends Parcelable>) whiteArray);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            isGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            blackArray = bundle.getParcelableArrayList(INSTANCE_BLACKARRAY);
            whiteArray = bundle.getParcelableArrayList(INSTANCE_WHITEARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

}
