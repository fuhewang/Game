package com.wangfuhe.wfh.myproject;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * 用于AI算法的实现
 * Created by wfh on 2016/4/11.
 */
public class WuziqiAi {
    private boolean[][][] isWin = new boolean[10][10][];
    private static int count = 0;
    private static int[] myWin;
    private static int[] computerWin;

    //赢法书法的初始化
    private void init() {
        //横向算法的初始化
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 6; j++)
                for (int k = 0; k < 5; k++)
                    isWin[i][j + k][count] = true;
            count++;
           //纵向算法的初始化
            for (i = 0; i < 10; i++) {
                for (int j = 0; j < 6; j++)
                    for (int k = 0; k < 5; k++)
                        isWin[j + k][i][count] = true;
                count++;
            }
            //正斜线方向
            for (i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++)
                    for (int k = 0; k < 5; k++)
                        isWin[i + k][j + k][count] = true;
                count++;
            }
            //凡斜线方向
            for (i = 0; i < 6; i++) {
                for (int j = 9; j > 3; j--)
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
    //检查电脑是否赢了
    private boolean checkcomputeWin(Point p) {
        for (int k = 0; k < count; k++) {
            if (isWin[p.x][p.y][k]) {
                computerWin[k]++;
                myWin[k] = 6;
                    if (myWin[k] == 5) {
                    return true;
                }
            }
        }
        return false;
    }

    //通过赢法数组计算电脑的下子位置。
    private Point computeAI() {
        int max = 0;
        Point p=null;
        int u=0,v=0;
        int[][] myscore = new int[10][10];
        int[][] computerscore = new int[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
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
                            computerscore[i][j] += 20000;
                        }
                    }
                }
                if (myscore[i][j] > max) {
                    max = myscore[i][j];
                     u=i;
                     v=j;
                } else if (myscore[i][j] == max) {
                    if (computerscore[i][j] > computerscore[p.x][p.y]) {
                        u=i;
                        v=j;
                    }
                }
                if (computerscore[i][j] > max) {
                    max = computerscore[i][j];
                    u=i;
                    v=j;

                } else if (computerscore[i][j] == max) {
                    if (myscore[i][j] > myscore[p.x][p.y]) {
                        u=i;
                        v=j;
                    }
                }
            }
        p.set(u,v);
        return p;
    }
}

