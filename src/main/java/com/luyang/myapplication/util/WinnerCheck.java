package com.luyang.myapplication.util;

import android.graphics.Point;

import java.util.List;

/**
 * Created by luyang on 2017/12/18.
 */

public class WinnerCheck {

    private final static int MAX_STEP = 5;


    public static boolean check(List<Point> points) {
        boolean res = false;
        for (int i = 0; i < points.size(); i++) {
            int x = points.get(i).x;
            int y = points.get(i).y;
            res = checkInLine(x, y, points);
            if (res) {
                break;
            }
        }
        return res;

    }

    /**
     * 判断上下左右斜着是否有连五子
     *
     * @param x
     * @param y
     * @param points
     * @return
     */
    public static boolean checkInLine(int x, int y, List<Point> points) {
        if (checkVertical(x, y, points)) {
            return true;
        }
        if (checkHorizontal(x, y, points)) {
            return true;
        }
        if (checkDiagonalPositisive(x, y, points)) {
            return true;
        }
        if (checkDiagonalNegative(x, y, points)) {
            return true;
        }
        return false;
    }

    public static boolean checkHorizontal(int x, int y, List<Point> points) {
        int counter =1;
        //左
        for (int i = 1; i < MAX_STEP; i++) {
            if (!points.contains(new Point(x - i, y))) {
                break;
            }
            counter++;
        }
        if (counter == 5) {
            return true;
        }
        //右
        for (int i = 1; i < MAX_STEP; i++) {
            if (!points.contains(new Point(x + i, y))) {
                break;
            }
            counter++;
        }
        if (counter == 5) {
            return true;
        }
        return false;
    }

    public static boolean checkVertical(int x, int y, List<Point> points) {
        int counter =1;
        //下
        for (int i = 1; i < MAX_STEP; i++) {
            if (!points.contains(new Point(x , y-i))) {
                break;
            }
            counter++;
        }
        if (counter == 5) {
            return true;
        }
        //上
        for (int i = 1; i < MAX_STEP; i++) {
            if (!points.contains(new Point(x , y+i))) {
                break;
            }
            counter++;
        }
        if (counter == 5) {
            return true;
        }
        return false;
    }

    public static boolean checkDiagonalPositisive(int x, int y, List<Point> points) {
        int counter =1 ;
        //45度上
        for (int i = 1; i < MAX_STEP; i++) {
            if (!points.contains(new Point(x + i, y + i))) {
                break;
            }
            counter++;
        }
        if (counter == 5) {
            return true;
        }

        //45度下
        for (int i = 1; i < MAX_STEP; i++) {
            if (!points.contains(new Point(x - i, y - i))) {
                break;
            }
            counter++;
        }
        if (counter == 5) {
            return true;
        }
        return false;
    }


    public static boolean checkDiagonalNegative(int x, int y, List<Point> points) {
        int counter =1;
        //135度上
        for (int i = 1; i < MAX_STEP; i++) {
            if (!points.contains(new Point(x - i, y + i))) {
                break;
            }
            counter++;
        }

        if (counter == 5) {
            return true;
        }
        //135度下
        for (int i = 1; i < MAX_STEP; i++) {
            if (!points.contains(new Point(x + i, y - i))) {
                break;
            }
            counter++;
        }
        if (counter == 5) {
            return true;
        }
        return false;
    }


}
