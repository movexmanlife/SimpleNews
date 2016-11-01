package com.robot.simplenews.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 新闻详情实体类
 */
public class NewsDetailEntity implements Parcelable {
    /**
     * docid
     */
    private String docid;
    /**
     * title
     */
    private String title;
    /**
     * source
     */
    private String source;
    /**
     * body
     */
    private String body;
    /**
     * ptime
     */
    private String ptime;
    /**
     * cover
     */
    private String cover;
    /**
     * 图片列表
     */
    private List<String> imgList;


    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.docid);
        dest.writeString(this.title);
        dest.writeString(this.source);
        dest.writeString(this.body);
        dest.writeString(this.ptime);
        dest.writeString(this.cover);
        dest.writeStringList(this.imgList);
    }

    public NewsDetailEntity() {
    }

    protected NewsDetailEntity(Parcel in) {
        this.docid = in.readString();
        this.title = in.readString();
        this.source = in.readString();
        this.body = in.readString();
        this.ptime = in.readString();
        this.cover = in.readString();
        this.imgList = in.createStringArrayList();
    }

    public static final Creator<NewsDetailEntity> CREATOR = new Creator<NewsDetailEntity>() {
        public NewsDetailEntity createFromParcel(Parcel source) {
            return new NewsDetailEntity(source);
        }

        public NewsDetailEntity[] newArray(int size) {
            return new NewsDetailEntity[size];
        }
    };
}
