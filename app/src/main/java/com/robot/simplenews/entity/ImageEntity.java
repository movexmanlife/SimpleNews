package com.robot.simplenews.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图片实体类
 */
public class ImageEntity implements Parcelable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String thumburl;
    private String sourceurl;
    private int height;
    private int width;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumburl() {
        return thumburl;
    }

    public void setThumburl(String thumburl) {
        this.thumburl = thumburl;
    }

    public String getSourceurl() {
        return sourceurl;
    }

    public void setSourceurl(String sourceurl) {
        this.sourceurl = sourceurl;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.thumburl);
        dest.writeString(this.sourceurl);
        dest.writeInt(this.height);
        dest.writeInt(this.width);
    }

    public ImageEntity() {
    }

    protected ImageEntity(Parcel in) {
        this.title = in.readString();
        this.thumburl = in.readString();
        this.sourceurl = in.readString();
        this.height = in.readInt();
        this.width = in.readInt();
    }

    public static final Creator<ImageEntity> CREATOR = new Creator<ImageEntity>() {
        public ImageEntity createFromParcel(Parcel source) {
            return new ImageEntity(source);
        }

        public ImageEntity[] newArray(int size) {
            return new ImageEntity[size];
        }
    };
}
