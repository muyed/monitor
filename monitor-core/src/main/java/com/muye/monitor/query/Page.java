package com.muye.monitor.query;

/**
 * 分页属性
 * @author qinshuangping
 */
public class Page {

    private static Integer MAX_PAGE_SIZE = 2000; //一页显示的最大数量

    private static Integer DEFAULT_PAGE_SIZE = 20; // 默认的一页显示的数量

    /**
     * 每页的记录数量
     */
    private Integer pageSize ;


    /**
     * 当前页
     */
    private Integer currentPage;

    /**
     * 总记录条数
     */
    private Integer totalRecored;


    public Integer getPageSize() {
        return (pageSize == null || pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if(pageSize == null){
            this.pageSize = DEFAULT_PAGE_SIZE;
            return;
        }
        if(pageSize > MAX_PAGE_SIZE){
            this.pageSize = MAX_PAGE_SIZE;
            return;
        }
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return (currentPage == null || currentPage < 1) ? 1 : currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageTotal() {
        if(totalRecored == null ) return 0;
        int tp = totalRecored/getPageSize();
        if(totalRecored % getPageSize() > 0){
            tp = tp + 1;
        }
        return tp;
    }


    public Integer getTotalRecored() {
        return totalRecored;
    }

    public void setTotalRecored(Integer totalRecored) {
        this.totalRecored = totalRecored;
    }

    public Integer getOffset() {
        return (getCurrentPage() - 1) * getPageSize();
    }
}
