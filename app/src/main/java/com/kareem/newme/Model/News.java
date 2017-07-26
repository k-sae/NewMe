
package com.kareem.newme.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {

    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;
    @SerializedName("contain_image")
    @Expose
    private Boolean containImage;
    @SerializedName("contain_video")
    @Expose
    private Boolean containVideo;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("cover_image_url")
    @Expose
    private String coverImageUrl;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("default_image")
    @Expose
    private String defaultImage;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("likes")
    @Expose
    private List<Like> likes = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("video_url")
    @Expose
    private String videoUrl;
    @SerializedName("writer")
    @Expose
    private String writer;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Boolean getContainImage() {
        return containImage;
    }

    public void setContainImage(Boolean containImage) {
        this.containImage = containImage;
    }

    public Boolean getContainVideo() {
        return containVideo;
    }

    public void setContainVideo(Boolean containVideo) {
        this.containVideo = containVideo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDefaultImage() {
        return defaultImage;
    }

    public void setDefaultImage(String defaultImage) {
        this.defaultImage = defaultImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

}
