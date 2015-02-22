package com.nexters.intersection.intersectionapp.model;

import com.google.gson.annotations.SerializedName;
import java.sql.Timestamp;

public class User {
    @SerializedName("user_no")
	public int userNo;
	@SerializedName("phone_id")
    public String phoneId;
	@SerializedName("token_id")
    public String tokenId;
	@SerializedName("push_yn")
    public String pushYn;
    public Timestamp created;
    public Timestamp modified;

    @Override
    public String toString() {
        return "User{" +
                "userNo=" + userNo +
                ", phoneId='" + phoneId + '\'' +
                ", tokenId='" + tokenId + '\'' +
                ", pushYn='" + pushYn + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                '}';
    }
}
