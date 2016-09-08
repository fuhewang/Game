package com.wangfuhe.wfh.myproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 人人对战
 * Created by wfh on 2016/4/10.
 */
public class WuziqiPanl extends View {

    private int mPanelwidth;
    private float mLineHeight;
    private int Max_Line = 10;
    private Paint mpaint = new Paint();
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;

    private boolean misWhite = true;

    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();


    private boolean mIsGameover;
    private boolean mIsWhitewinner;
    private int Max_count_Line = 5;
    private boolean isDeuce=false;
    //构造
    public WuziqiPanl(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setBackgroundColor(0x44ff0000);
        init();

    }

    //初始化画笔信息
    private void init() {
        mpaint.setColor(0x88000000);
        mpaint.setAntiAlias(true);
        mpaint.setDither(true);
        mpaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelwidth = w;
        mLineHeight = mPanelwidth * 1.0f / Max_Line;

        int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameover) return false;
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getVaildpoint(x, y);
            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                return false;
            }
            if(mWhiteArray.size()+mBlackArray.size()==100){
                isDeuce=true;
            }
            if (misWhite) {
                mWhiteArray.add(p);
            } else {
                mBlackArray.add(p);
            }
            invalidate();
            misWhite = !misWhite;
            return true;
        }
        return true;
    }

    private Point getVaildpoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);
        drawPieces(canvas);
        checkGameover();
    }

    private void checkGameover() {
        boolean whiteWin = checkFiveLine(mWhiteArray);
        boolean blackWin = checkFiveLine(mBlackArray);

        if (whiteWin || blackWin) {
            mIsGameover = true;
            mIsWhitewinner = whiteWin;
            String text = mIsWhitewinner ? "白棋胜利" : "黑棋胜利";
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext()).setTitle(text)
                    .setMessage("再来一局吧！少年！").setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("再来", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restart();
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.create().show();
            Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
        }
        if(isDeuce){
            AlertDialog.Builder builder=new AlertDialog.Builder(getContext()).setTitle("你俩平局了")
                    .setMessage("再来一局吧！少年！").setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("再来", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restart();
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.create().show();
        }

    }

    private boolean checkFiveLine(ArrayList<Point> points) {

        for (Point p : points) {
            int x = p.x;
            int y = p.y;
            boolean win = checkHorizontal(x, y, points);
            if (win) return true;
            win = checkVertical(x, y, points);
            if (win) return true;
            win = checkLeftDiagonal(x, y, points);
            if (win) return true;
            win = checkRightDiagonal(x, y, points);
            if (win) return true;
        }

        return false;
    }

    private boolean checkHorizontal(int x, int y, ArrayList<Point> points) {

        int count = 1;
        for (int i = 1; i < Max_count_Line; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == Max_count_Line) return true;
        for (int i = 1; i < Max_count_Line; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == Max_count_Line) return true;
        return false;
    }

    private boolean checkVertical(int x, int y, ArrayList<Point> points) {

        int count = 1;
        for (int i = 1; i < Max_count_Line; i++) {
            if (points.contains(new Point(x, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == Max_count_Line) return true;
        for (int i = 1; i < Max_count_Line; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == Max_count_Line) return true;
        return false;
    }

    private boolean checkLeftDiagonal(int x, int y, ArrayList<Point> points) {

        int count = 1;
        for (int i = 1; i < Max_count_Line; i++) {
            if (points.contains(new Point(x + i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == Max_count_Line) return true;
        for (int i = 1; i < Max_count_Line; i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == Max_count_Line) return true;
        return false;
    }

    private boolean checkRightDiagonal(int x, int y, ArrayList<Point> points) {

        int count = 1;
        for (int i = 1; i < Max_count_Line; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == Max_count_Line) return true;
        for (int i = 1; i < Max_count_Line; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == Max_count_Line) return true;
        return false;
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
            Point whittePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whittePoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight
                    , (whittePoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }
        for (int i = 0, n = mBlackArray.size(); i < n; i++) {
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blackPoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight
                    , (blackPoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }

    }

    private void drawBoard(Canvas canvas) {

        int w = mPanelwidth;
        float lineheight = mLineHeight;
        for (int i = 0; i < Max_Line; i++) {
            int startx = (int) (lineheight / 2);
            int endx = (int) (w - lineheight / 2);

            int starty = (int) ((0.5 + i) * lineheight);
            canvas.drawLine(startx, starty, endx, starty, mpaint);
            canvas.drawLine(starty, startx, starty, endx, mpaint);
        }
    }

    private static final String INSTANCE="instance";
    private static final String INSTANCE_GAME_OVER="instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY="instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY="instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameover);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        return bundle;
    }

    public void restart(){
        mWhiteArray.clear();
        mBlackArray.clear();
        mIsGameover=false;
        mIsWhitewinner=false;
        invalidate();
    }
    public void regraet(){
        if(misWhite){
            if ((mBlackArray.size()-1)>-1)
            mBlackArray.remove((mBlackArray.size()-1));
        }else {
            if((mWhiteArray.size()-1)>-1)
            mWhiteArray.remove((mWhiteArray.size()-1));
        }
        misWhite=!misWhite;
        invalidate();
    }
    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if(state instanceof Bundle){
            Bundle bundle= (Bundle) state;
            mIsGameover=bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray=bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray=bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
