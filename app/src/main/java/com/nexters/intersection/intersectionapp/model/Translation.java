package com.nexters.intersection.intersectionapp.model;

import java.io.Serializable;
import java.sql.Timestamp;
import com.google.gson.annotations.SerializedName;

public class Translation implements Serializable {
	@SerializedName("trans_no")
	private int transNo;
    private String name;
    private String type;
	@SerializedName("like_count")
    private int likeCount;
    @SerializedName("cur_like_status")
    private boolean likeStatus;
    private int seq;
    private Timestamp created;
    private Timestamp modified;

    private String address;

    public void setLikeStatus(boolean likeStatus){
        this.likeStatus = likeStatus;
    }

    public boolean getLikeStatus(){
        return likeStatus;
    }

    public int getTransNo() {
        return transNo;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getSeq() {
        return seq;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setTransNo(int transNo) {
        this.transNo = transNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Translation{" +
                "transNo=" + transNo +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", likeCount=" + likeCount +
                ", seq=" + seq +
                ", created=" + created +
                ", modified=" + modified +
                ", likeStatus=" + likeStatus +
                '}';
    }
}
