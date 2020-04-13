
package app.com.learnfromblogs.Model.OtherProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("profile_url")
    @Expose
    private String profileUrl;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("website_link")
    @Expose
    private String websiteLink;
    @SerializedName("view_count")
    @Expose
    private String viewCount;
    @SerializedName("website_verified")
    @Expose
    private String websiteVerified;
    @SerializedName("device_id")
    @Expose
    private String deviceId;
    @SerializedName("country_name")
    @Expose
    private String countryName;
    @SerializedName("about_me")
    @Expose
    private String aboutMe;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("twitter")
    @Expose
    private String twitter;
    @SerializedName("linkedin")
    @Expose
    private String linkedin;
    @SerializedName("instagram")
    @Expose
    private String instagram;
    @SerializedName("pinterest")
    @Expose
    private Object pinterest;
    @SerializedName("mygroup")
    @Expose
    private Object mygroup;
    @SerializedName("display_post")
    @Expose
    private String displayPost;
    @SerializedName("you_have_favourite_notify")
    @Expose
    private String youHaveFavouriteNotify;
    @SerializedName("you_have_favourite_email")
    @Expose
    private String youHaveFavouriteEmail;
    @SerializedName("you_have_comment_notify")
    @Expose
    private String youHaveCommentNotify;
    @SerializedName("you_have_comment_email")
    @Expose
    private String youHaveCommentEmail;
    @SerializedName("you_have_selected_categories_email")
    @Expose
    private String youHaveSelectedCategoriesEmail;
    @SerializedName("you_have_selected_categories_notify")
    @Expose
    private String youHaveSelectedCategoriesNotify;
    @SerializedName("image_thumb")
    @Expose
    private String imageThumb;
    @SerializedName("image_large")
    @Expose
    private String imageLarge;
    @SerializedName("points")
    @Expose
    private String points;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getWebsiteVerified() {
        return websiteVerified;
    }

    public void setWebsiteVerified(String websiteVerified) {
        this.websiteVerified = websiteVerified;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public Object getPinterest() {
        return pinterest;
    }

    public void setPinterest(Object pinterest) {
        this.pinterest = pinterest;
    }

    public Object getMygroup() {
        return mygroup;
    }

    public void setMygroup(Object mygroup) {
        this.mygroup = mygroup;
    }

    public String getDisplayPost() {
        return displayPost;
    }

    public void setDisplayPost(String displayPost) {
        this.displayPost = displayPost;
    }

    public String getYouHaveFavouriteNotify() {
        return youHaveFavouriteNotify;
    }

    public void setYouHaveFavouriteNotify(String youHaveFavouriteNotify) {
        this.youHaveFavouriteNotify = youHaveFavouriteNotify;
    }

    public String getYouHaveFavouriteEmail() {
        return youHaveFavouriteEmail;
    }

    public void setYouHaveFavouriteEmail(String youHaveFavouriteEmail) {
        this.youHaveFavouriteEmail = youHaveFavouriteEmail;
    }

    public String getYouHaveCommentNotify() {
        return youHaveCommentNotify;
    }

    public void setYouHaveCommentNotify(String youHaveCommentNotify) {
        this.youHaveCommentNotify = youHaveCommentNotify;
    }

    public String getYouHaveCommentEmail() {
        return youHaveCommentEmail;
    }

    public void setYouHaveCommentEmail(String youHaveCommentEmail) {
        this.youHaveCommentEmail = youHaveCommentEmail;
    }

    public String getYouHaveSelectedCategoriesEmail() {
        return youHaveSelectedCategoriesEmail;
    }

    public void setYouHaveSelectedCategoriesEmail(String youHaveSelectedCategoriesEmail) {
        this.youHaveSelectedCategoriesEmail = youHaveSelectedCategoriesEmail;
    }

    public String getYouHaveSelectedCategoriesNotify() {
        return youHaveSelectedCategoriesNotify;
    }

    public void setYouHaveSelectedCategoriesNotify(String youHaveSelectedCategoriesNotify) {
        this.youHaveSelectedCategoriesNotify = youHaveSelectedCategoriesNotify;
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

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}
