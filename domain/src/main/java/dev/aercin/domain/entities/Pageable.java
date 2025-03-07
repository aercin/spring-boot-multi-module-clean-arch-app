package dev.aercin.domain.entities;

public class Pageable {
    private Integer pageNo;
    private Integer pageSize;

    private Pageable(Integer pageNo, Integer pageSize){
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public static Pageable init(Integer pageNo, Integer pageSize) {
       return new Pageable(pageNo,pageSize);
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}
