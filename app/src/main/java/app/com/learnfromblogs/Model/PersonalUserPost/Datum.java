
package app.com.learnfromblogs.Model.PersonalUserPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("link")
    @Expose
    private Object link;
    @SerializedName("tag_ids")
    @Expose
    private Object tagIds;
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
    private Object postLink;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("view_count")
    @Expose
    private String viewCount;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("profile_url")
    @Expose
    private String profileUrl;
    @SerializedName("sub_name")
    @Expose
    private String subName;
    @SerializedName("category_slug")
    @Expose
    private String categorySlug;
    @SerializedName("comments_count")
    @Expose
    private String commentsCount;
    @SerializedName("tagged")
    @Expose
    private List<Tagged> tagged = null;
    @SerializedName("is_favourite")
    @Expose
    private Integer isFavourite;
    @SerializedName("user_thumb_img")
    @Expose
    private String userThumbImg;
    @SerializedName("user_large_img")
    @Expose
    private String userLargeImg;
    @SerializedName("image_thumb")
    @Expose
    private String imageThumb;
    @SerializedName("image_large")
    @Expose
    private String imageLarge;

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

    public Object getTagIds() {
        return tagIds;
    }

    public void setTagIds(Object tagIds) {
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

    public Object getPostLink() {
        return postLink;
    }

    public void setPostLink(Object postLink) {
        this.postLink = postLink;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }

    public List<Tagged> getTagged() {
        return tagged;
    }

    public void setTagged(List<Tagged> tagged) {
        this.tagged = tagged;
    }

    public Integer getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Integer isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getUserThumbImg() {
        return userThumbImg;
    }

    public void setUserThumbImg(String userThumbImg) {
        this.userThumbImg = userThumbImg;
    }

    public String getUserLargeImg() {
        return userLargeImg;
    }

    public void setUserLargeImg(String userLargeImg) {
        this.userLargeImg = userLargeImg;
    }

    public String getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getImageLarge() {
        return imageLarge;
    }

    public void setImageLarge(String imageLarge) {
        this.imageLarge = imageLarge;
    }

}
