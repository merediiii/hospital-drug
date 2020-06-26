package me.zbl.blog.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.python.antlr.ast.Str;

import java.io.Serializable;
import java.sql.Date;

public class  dDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "dDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", remaining=" + remaining +
                ", idOfDrug=" + idOfDrug +
                ", latestBefore=" + latestBefore +
                ", syt='" + syt + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRemaining() {
        return remaining;
    }

    public void setRemaining(Long remaining) {
        this.remaining = remaining;
    }

    public int getIdOfDrug() {
        return idOfDrug;
    }

    public void setIdOfDrug(int idOfDrug) {
        this.idOfDrug = idOfDrug;
    }

    public String getLatestBefore() {
        return latestBefore;
    }

    public void setLatestBefore(String latest_before) {
        this.latestBefore = latest_before;
    }

    public String getSyt() {
        return syt;
    }

    public void setSyt(String syt) {
        this.syt = syt;
    }

    private Long id;

    private  String name;

    private Long remaining;

    private int idOfDrug;

    private String latestBefore;

    private String syt;
}
