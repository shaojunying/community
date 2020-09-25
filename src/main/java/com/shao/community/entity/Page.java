package com.shao.community.entity;

/**
 * @author shao
 * @date 2020-09-02 22:12
 */
public class Page {

    /**
     * 上限的最大值
     */
    private final int MAX_LIMIT = 100;
    /**
     * 上限的最小值
     */
    private final int MIN_LIMIT = 1;
    /**
     * 当前页码
     */
    private int current = 1;
    /**
     * 显示上限
     */
    private int limit = 10;
    /**
     * 数据总数(用于计算总页数)
     */
    private int rows;
    /**
     * 查询路径
     */
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
        if (limit >= MIN_LIMIT && limit <= MAX_LIMIT) {
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

    /**
     * 获取当前页面起始行
     *
     * @return
     */
    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal() {
        int ans = 0;
        if (rows % limit != 0) {
            ans = 1;
        }
        return rows / limit + ans;
    }

    /**
     * 获取起始页
     * @return
     */
    public int getFrom() {
        return Math.max(1, current - 2);
    }

    /**
     * 获取结束页
     * @return
     */
    public int getTo() {
        return Math.min(getTotal(), current + 2);
    }


}
