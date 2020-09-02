package com.shao.community.entity;

/**
 * Author: shao
 * Date: 2020-09-02
 * Time: 22:12
 */
public class Page {

    // 当前页码
    private int current = 1;
    // 显示上限
    private int limit = 10;
    // 数据总数(用于计算总页数)
    private int rows;
    // 查询路径
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /*
     * 获取当前页面起始行
     * */
    public int getOffset() {
        return (current - 1) * limit;
    }

    /*
     * 获取总页数
     * */
    public int getTotal() {
        int ans = 0;
        if (rows % limit != 0) {
            ans = 1;
        }
        return rows / limit + ans;
    }

    /*
     * 获取起始页
     * */
    public int getFrom() {
        return Math.max(1, current - 2);
    }

    /*
     * 获取结束页
     * */
    public int getTo() {
        return Math.min(getTotal(), current + 2);
    }


}
