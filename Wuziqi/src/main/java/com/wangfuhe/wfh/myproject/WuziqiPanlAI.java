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
 * Created by wfh on 2016/4/10.
 */
public class WuziqiPanlAI extends View {

    private int mPanelwidth;
    private float mLineHeight;
    private int Max_Line = 10;
    private Paint mpaint = new Paint();
    private Bitmap mMyPiece;
    private Bitmap mComputerPiece;
    private float ratioPieceOfLineHeight = 3 * 1.0f / 4;
    private boolean misWhite = true;
    private ArrayList<Point> mMyarray = new ArrayList<>();
    private ArrayList<Point> mComputerarray = new ArrayList<>();
    private boolean mIsGameover;
    private boolean mIsMyWinner;
    private int Max_count_Line = 5;
    private boolean isDeuce=false;

    private boolean[][][] isWin = new boolean[10][10][600];
    private  static int count = 0;
    private  int[] myWin;
    private  int[] computerWin;

    //构造view
    public WuziqiPanlAI(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setBackgroundColor(0x44ff0000);
        init();
        initarray();

    }
    //初始化画笔
    private void init() {
        mpaint.setColor(0x88000000);
        mpaint.setAntiAlias(true);
        mpaint.setDither(true);
        mpaint.setStyle(Paint.Style.STROKE);
        mMyPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mComputerPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);

    }
    //测量界面长宽，设置棋盘宽高
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

    //改变棋盘和棋子的大小
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelwidth = w;
        mLineHeight = mPanelwidth * 1.0f / Max_Line;

        int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);
        mMyPiece = Bitmap.createScaledBitmap(mMyPiece, pieceWidth, pieceWidth, false);
        mComputerPiece = Bitmap.createScaledBitmap(mComputerPiece, pieceWidth, pieceWidth, false);
    }

    //获取点击事件确定落子点
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameover) return false;
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getVaildpoint(x, y);
            if (mMyarray.contains(p) || mComputerarray.contains(p)) {
                return false;
            }
            if(mMyarray.size()+ mComputerarray.size()==100){
                isDeuce=true;
            }
            checkclear();
            mMyarray.add(p);
            checkGameover();
            //休眠
            mComputerarray.add(computeAI());
            checkclear();
            checkGameover();
            invalidate();
//            misWhite = !misWhite;
            return true;
        }
        return true;
    }

    //获取相对位置
    private Point getVaildpoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    //画棋盘
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画棋盘
        drawBoard(canvas);
        //画棋子
        drawPieces(canvas);

    }
     //检查游戏是否结束
    private void checkGameover() {
        boolean whiteWin = checkMy(mMyarray);
        boolean blackWin = checkComputer(mComputerarray);
        if (whiteWin || blackWin) {
            mIsGameover = true;
            mIsMyWinner = whiteWin;
            String text = mIsMyWinner ? "白棋胜利" : "黑棋胜利";
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

    //检查我是否赢了
    private boolean checkMy(ArrayList<Point> points) {

        for (Point p : points) {
            boolean win = checkmyWin(p);
            if(win)return true;
        }
        return false;
    }
//    检查电脑是否赢了
    private boolean checkComputer(ArrayList<Point> points) {
        for (Point p : points) {
            boolean win = checkcomputeWin(p);
            if(win)return true;
        }
        return false;
    }
    private void drawPieces(Canvas canvas) {
        for (int i = 0, n = mMyarray.size(); i < n; i++) {
            Point whittePoint = mMyarray.get(i);
            canvas.drawBitmap(mMyPiece,
                    (whittePoint.x + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight
                    , (whittePoint.y + (1 - ratioPieceOfLineHeight) / 2) * mLineHeight, null);
        }
        for (int i = 0, n = mComputerarray.size(); i < n; i++) {
            Point blackPoint = mComputerarray.get(i);
            canvas.drawBitmap(mComputerPiece,
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

    //保存界面信息
    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle=new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameover);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mMyarray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mComputerarray);
        return bundle;
    }

    //再来一局
    public void restart(){
        mMyarray.clear();
        mComputerarray.clear();
        mIsGameover=false;
        mIsMyWinner =false;
        invalidate();
    }
    //悔棋
    public void regraet(){
            if ((mComputerarray.size()-1)>-1)
            mComputerarray.remove((mComputerarray.size()-1));
            if((mMyarray.size()-1)>-1)
            mMyarray.remove((mMyarray.size()-1));
        invalidate();
    }
    //获取界面信息
    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if(state instanceof Bundle){
            Bundle bundle= (Bundle) state;
            mIsGameover=bundle.getBoolean(INSTANCE_GAME_OVER);
            mMyarray =bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mComputerarray =bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
    //赢法书法的初始化
    private void initarray() {
        //横向算法的初始化
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 6; j++) {
                for (int k = 0; k < 5; k++)
                    isWin[i][j + k][count] = true;
                count++;
            }
        }
        //纵向算法的初始化
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 6; j++) {
                    for (int k = 0; k < 5; k++)
                        isWin[j + k][i][count] = true;
                    count++;
                }
            }
        //正斜线方向
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    for (int k = 0; k < 5; k++)
                        isWin[i + k][j + k][count] = true;
                    count++;
                }
            }
        //凡斜线方向
            for (int i = 0; i < 6; i++) {
                for (int j = 9; j > 3; j--){
                    for (int k = 0; k < 5; k++)
                        isWin[i + k][j - k][count] = true;
                count++;
            }
        }
        myWin = new int[count];
        computerWin = new int[count];

    }


    //检测我是否赢了
    private boolean checkmyWin(Point p) {
        for (int k = 0; k < count; k++) {
            if (isWin[p.x][p.y][k]) {
                myWin[k]++;
                computerWin[k] = 6;
                if (myWin[k] == 5) {
                    return true;
                }
            }
        }
        return false;
    }
    //清理数据
    private void checkclear(){
        for(int k=0;k<count;k++){
            myWin[k]=0;
            computerWin[k]=0;
        }
    }
    //检查电脑是否赢了
    private boolean checkcomputeWin(Point p) {
        for (int k = 0; k < count; k++) {
            if (isWin[p.x][p.y][k]) {
                computerWin[k]++;
                myWin[k] = 6;
                if (computerWin[k] == 5) {
                    return true;
                }
            }
        }
        return false;
    }
    //通过赢法数组计算电脑的下子位置。
    private Point computeAI() {
        int max = 0;
        Point p=new Point();
        int u=0,v=0;
        int[][] myscore = new int[10][10];
        int[][] computerscore = new int[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
                if((mMyarray.contains(new Point(i,j))|| (mComputerarray.contains(new Point(i,j)))))
                    continue;
                for (int k = 0; k < count; k++) {
                    if (isWin[i][j][k]) {
                        if (myWin[k] == 1) {
                            myscore[i][j] += 200;
                        } else if (myWin[k] == 2) {
                            myscore[i][j] += 400;
                        } else if (myWin[k] == 3) {
                            myscore[i][j] += 2000;
                        } else if (myWin[k] == 4) {
                            myscore[i][j] += 10000;
                        }
                        if (computerWin[k] == 1) {
                            computerscore[i][j] += 220;
                        } else if (myWin[k] == 2) {
                            computerscore[i][j] += 420;
                        } else if (myWin[k] == 3) {
                            computerscore[i][j] += 2100;
                        } else if (myWin[k] == 4) {
                            computerscore[i][j] += 30000;
                        }
                    }
                }
                if (myscore[i][j] > max) {
                    max = myscore[i][j];
                    u=i;
                    v=j;
                } else if (myscore[i][j] == max) {
                    if (computerscore[i][j] > computerscore[u][v]) {
                        u=i;
                        v=j;
                    }
                }
                if (computerscore[i][j] > max) {
                    max = computerscore[i][j];
                    u=i;
                    v=j;

                } else if (computerscore[i][j] == max) {
                    if (myscore[i][j] > myscore[u][v]) {
                        u=i;
                        v=j;
                    }
                }
            }
        p.set(u, v);
        return p;
    }
}
