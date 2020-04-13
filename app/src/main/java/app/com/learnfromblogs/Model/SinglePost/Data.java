
package app.com.learnfromblogs.Model.SinglePost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("link")
    @Expose
    private Object link;
    @SerializedName("position")
    @Expose
    private Object position;
    @SerializedName("tag_ids")
    @Expose
    private String tagIds;
    @SerializedName("subject_id")
    @Expose
    private String subjectId;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("post_link")
    @Expose
    private String postLink;
    @SerializedName("image")
    @Expose
    private Object image;
    @SerializedName("old_file_name")
    @Expose
    private Object oldFileName;
    @SerializedName("privacy_status")
    @Expose
    private String privacyStatus;
    @SerializedName("is_youtube")
    @Expose
    private String isYoutube;
    @SerializedName("view_count")
    @Expose
    private String viewCount;
    @SerializedName("total_words_val")
    @Expose
    private String totalWordsVal;
    @SerializedName("quote_total_words_val")
    @Expose
    private String quoteTotalWordsVal;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("is_approved")
    @Expose
    private String isApproved;
    @SerializedName("is_point")
    @Expose
    private String isPoint;
    @SerializedName("is_compress")
    @Expose
    private String isCompress;
    @SerializedName("is_publish")
    @Expose
    private String isPublish;
    @SerializedName("is_boosted")
    @Expose
    private String isBoosted;
    @SerializedName("boost_date")
    @Expose
    private Object boostDate;
    @SerializedName("is_notification")
    @Expose
    private String isNotification;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Object getLink() {
        return link;
    }

    public void setLink(Object link) {
        this.link = link;
    }

    public Object getPosition() {
        return position;
    }

    public void setPosition(Object position) {
        this.position = position;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }

    public Object getImage() {
        return image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public Object getOldFileName() {
        return oldFileName;
    }

    public void setOldFileName(Object oldFileName) {
        this.oldFileName = oldFileName;
    }

    public String getPrivacyStatus() {
        return privacyStatus;
    }

    public void setPrivacyStatus(String privacyStatus) {
        this.privacyStatus = privacyStatus;
    }

    public String getIsYoutube() {
        return isYoutube;
    }

    public void setIsYoutube(String isYoutube) {
        this.isYoutube = isYoutube;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getTotalWordsVal() {
        return totalWordsVal;
    }

    public void setTotalWordsVal(String totalWordsVal) {
        this.totalWordsVal = totalWordsVal;
    }

    public String getQuoteTotalWordsVal() {
        return quoteTotalWordsVal;
    }

    public void setQuoteTotalWordsVal(String quoteTotalWordsVal) {
        this.quoteTotalWordsVal = quoteTotalWordsVal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsPoint() {
        return isPoint;
    }

    public void setIsPoint(String isPoint) {
        this.isPoint = isPoint;
    }

    public String getIsCompress() {
        return isCompress;
    }

    public void setIsCompress(String isCompress) {
        this.isCompress = isCompress;
    }

    public String getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(String isPublish) {
        this.isPublish = isPublish;
    }

    public String getIsBoosted() {
        return isBoosted;
    }

    public void setIsBoosted(String isBoosted) {
        this.isBoosted = isBoosted;
    }

    public Object getBoostDate() {
        return boostDate;
    }

    public void setBoostDate(Object boostDate) {
        this.boostDate = boostDate;
    }

    public String getIsNotification() {
        return isNotification;
    }

    public void setIsNotification(String isNotification) {
        this.isNotification = isNotification;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
