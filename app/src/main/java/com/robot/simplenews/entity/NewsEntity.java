package com.robot.simplenews.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 新闻实体类
 */
@Table(name = "news_table", id = "_id")
public class NewsEntity extends Model implements Parcelable {

    /**
     * docid
     */
    @Column
    private String docid;
    /**
     * 标题
     */
    @Column
    private String title;
    /**
     * 小内容
     */
    @Column
    private String digest;
    /**
     * 图片地址
     */
    @Column
    private String imgsrc;
    /**
     * 来源
     */
    @Column
    private String source;
    /**
     * 时间
     */
    @Column
    private String ptime;
    /**
     * TAG
     */
    @Column
    private String tag;

    @Column
    private int type;

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

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public NewsEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.docid);
        dest.writeString(this.title);
        dest.writeString(this.digest);
        dest.writeString(this.imgsrc);
        dest.writeString(this.source);
        dest.writeString(this.ptime);
        dest.writeString(this.tag);
        dest.writeInt(this.type);
    }

    protected NewsEntity(Parcel in) {
        this.docid = in.readString();
        this.title = in.readString();
        this.digest = in.readString();
        this.imgsrc = in.readString();
        this.source = in.readString();
        this.ptime = in.readString();
        this.tag = in.readString();
        this.type = in.readInt();
    }

    public static final Creator<NewsEntity> CREATOR = new Creator<NewsEntity>() {
        public NewsEntity createFromParcel(Parcel source) {
            return new NewsEntity(source);
        }

        public NewsEntity[] newArray(int size) {
            return new NewsEntity[size];
        }
    };
}
