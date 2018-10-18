package com.eormega.pojo;

public class Tagmap {
    private Integer id;

    private Integer tId;

    private Integer aId;

    public Tagmap(Integer id, Integer tId, Integer aId) {
        this.id = id;
        this.tId = tId;
        this.aId = aId;
    }

    public Tagmap() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer gettId() {
        return tId;
    }

    public void settId(Integer tId) {
        this.tId = tId;
    }

    public Integer getaId() {
        return aId;
    }

    public void setaId(Integer aId) {
        this.aId = aId;
    }
}