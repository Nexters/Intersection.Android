package com.nexters.intersection.intersectionapp.model;

import java.io.Serializable;
import java.sql.Timestamp;
import com.google.gson.annotations.SerializedName;

public class Translation  implements Serializable {
	@SerializedName("trans_no")
	int transNo;
	String name;
	String type;
	@SerializedName("like_count")
	int likeCount;
	int seq;
	Timestamp created;
	Timestamp modified;

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
                '}';
    }
}
