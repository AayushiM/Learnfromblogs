package app.com.learnfromblogs.API;


import app.com.learnfromblogs.Model.AddFav.ModelAddFav;
import app.com.learnfromblogs.Model.AllFeed.ModelAllFeed;
import app.com.learnfromblogs.Model.Category.ModelCategory;
import app.com.learnfromblogs.Model.Country.ModelCountry;
import app.com.learnfromblogs.Model.CreateAd.ModelCreateAd;
import app.com.learnfromblogs.Model.CreatePost.ModelCreatePost;
import app.com.learnfromblogs.Model.DeleteMyCommnent.ModelDeleteComment;
import app.com.learnfromblogs.Model.Login.ModelLogin;
import app.com.learnfromblogs.Model.MyComment.ModelMyComment;
import app.com.learnfromblogs.Model.OtherProfile.ModelOtherProfile;
import app.com.learnfromblogs.Model.PersonalUserPost.ModelPersonalUserPost;
import app.com.learnfromblogs.Model.Point.ModelPoints;
import app.com.learnfromblogs.Model.PostCommentList.ModelPostComment;
import app.com.learnfromblogs.Model.PostDelete.ModelDelete;
import app.com.learnfromblogs.Model.Profile.ModelProfile;
import app.com.learnfromblogs.Model.SinglePost.ModelSinglePost;
import app.com.learnfromblogs.Model.Tag.ModelTag;
import app.com.learnfromblogs.Model.TopicCategory.ModelTopicCategory;
import app.com.learnfromblogs.Model.UpdateProfile.ModelUpdateProfile;
import app.com.learnfromblogs.Model.aboutus.AboutUs;
import app.com.learnfromblogs.Model.declaimerpolicy.DeclaimerPolicy;
import app.com.learnfromblogs.Model.privacypolicy.PrivacyPolicy;
import app.com.learnfromblogs.Model.repoer.ReportModel;
import app.com.learnfromblogs.Model.termofuse.TermOfUse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by softeaven on 6/8/2017.
 */

public interface API {


    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> api_signup(@Field("name") String name, @Field("email") String email, @Field("website_link") String website_link, @Field("password") String password);


    @FormUrlEncoded
    @POST("login")
    Call<ModelLogin> api_signIn(@Field("email") String email, @Field("password") String password, @Field("device_id") String device_id);


    @FormUrlEncoded
    @POST("verify-mail/otp")
    Call<ResponseBody> api_verify_otp(@Field("email") String email, @Field("verify_token") String verify_token, @Field("device_id") String device_id);


    @FormUrlEncoded
    @POST("get/profile")
    Call<ModelProfile> api_profile(@Field("token") String token, @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("get/profile/other")
    Call<ModelOtherProfile> api_other_profile(@Field("user_id") String user_id);


    @GET("get/post/tag")
    Call<ModelTag> api_tag();


    @GET("category/list")
    Call<ModelCategory> api_categories(@Query("token") String token, @Query("user_id") String user_id);


    @FormUrlEncoded
    @POST("post/add")
    Call<ModelCreatePost> api_create_post(@Field("token") String token, @Field("user_id") String user_id,
                                          @Field("title") String title, @Field("subject_id") int subject_id,
                                          @Field("other_category") int other_category, @Field("tag_input") String tag_input,
                                          @Field("post_link") String post_link, @Field("content") String content,
                                          @Field("img1") String img1, @Field("img2") String img2, @Field("img3") String img3,
                                          @Field("img4") String img4, @Field("img5") String img5,
                                          @Field("type") int type);


    @Multipart
    @POST("post/add")
    Call<ModelCreatePost> api_create_post_multipart(@Part MultipartBody.Part file, @Part MultipartBody.Part thumb, @Part("token") RequestBody token,
                                                 @Part("user_id") RequestBody userid, @Part("title") RequestBody title,
                                                 @Part("subject_id") RequestBody subject_id, @Part("other_category") RequestBody other_category,
                                                 @Part("tag_input") RequestBody tag_input, @Part("post_link") RequestBody post_link, @Part("content") RequestBody content,
                                                 @Part("img1") RequestBody img1, @Part("img2") RequestBody img2, @Part("img3") RequestBody img3,
                                                 @Part("img4") RequestBody img4, @Part("img5") RequestBody img5, @Part("type") RequestBody type);


    @FormUrlEncoded
    @POST("post/update")
    Call<ModelCreatePost> api_update_post(@Field("token") String token, @Field("user_id") String user_id, @Field("id") String id,
                                          @Field("title") String title, @Field("subject_id") int subject_id,
                                          @Field("other_category") int other_category, @Field("tag_input") String tag_input,
                                          @Field("post_link") String post_link, @Field("content") String content,
                                          @Field("img1") String img1, @Field("img2") String img2, @Field("img3") String img3,
                                          @Field("img4") String img4, @Field("img5") String img5,
                                          @Field("type") int type);


    @FormUrlEncoded
    @POST("my-post/all")
    Call<ModelPersonalUserPost> api_user_feed(@Field("token") String token, @Field("user_id") String user_id, @Field("page") String page);

    @FormUrlEncoded
    @POST("other-post/all")
    Call<ModelPersonalUserPost> api_other_user_feed(@Field("user_id") String user_id, @Field("page") String page);


    @FormUrlEncoded
    @POST("my-post/delete")
    Call<ModelDelete> api_post_delete(@Field("token") String token, @Field("user_id") String user_id, @Field("id") String id);


    @GET("post/list")
    Call<ModelAllFeed> api_feed(@Query("type") String type, @Query("slug") String slug, @Query("user_id") String user_id,
                                @Query("keyword") String keyword, @Query("subject_id") String subject_id, @Query("view_type") String view_type, @Query("page") String page);

    @FormUrlEncoded
    @POST("category/list")
    Call<ModelTopicCategory> api_category(@Field("token") String token, @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("category/selected")
    Call<ModelLogin> api_selected_cat(@Field("token") String token, @Field("user_id") String user_id, @Field("category_id") String category_id);


    @FormUrlEncoded
    @POST("my-post/single")
    Call<ModelSinglePost> api_my_post_single(@Field("token") String token, @Field("user_id") String user_id, @Field("id") String id);


    @FormUrlEncoded
    @POST("post-boost/submit")
    Call<ResponseBody> api_post_boost(@Field("token") String token, @Field("user_id") String user_id, @Field("post_id") String post_id, @Field("days") String days);


    @FormUrlEncoded
    @POST("favourite/post")
    Call<ModelAddFav> api_add_fav_post(@Field("token") String token, @Field("user_id") String user_id, @Field("id") String id);

    @FormUrlEncoded
    @POST("post/report")
    Call<ReportModel> api_flag_post(@Field("token") String token, @Field("user_id") String user_id, @Field("to_id") String id, @Field("report_id") String reportId, @Field("message") String message, @Field("category") String category);

    @FormUrlEncoded
    @POST("notifications/delete")
    Call<ReportModel> api_delete_notification(@Field("token") String token, @Field("user_id") String user_id, @Field("id") String id);


    @GET("get-favourite/post")
    Call<ModelAllFeed> api_get_fav_post(@Query("token") String token, @Query("user_id") String user_id, @Query("page") String page);

    @FormUrlEncoded
    @POST("post/comment-submit")
    Call<ModelDeleteComment> api_comment_submit(@Field("token") String token, @Field("user_id") String user_id, @Field("post_id") String post_id, @Field("message") String message);



    @GET("post/comments")
    Call<ModelPostComment> api_post_comment(@Query("id") String id);

    @FormUrlEncoded
    @POST("my/comments")
    Call<ModelMyComment> api_my_comments(@Field("token") String token, @Field("user_id") String user_id, @Field("page") String page);

    @FormUrlEncoded
    @POST("my-comment/delete")
    Call<ModelDeleteComment> api_delete_my_comment(@Field("token") String token, @Field("user_id") String user_id, @Field("id") String id);


    @FormUrlEncoded
    @POST("get/notifications")
    Call<ResponseBody> api_notification(@Field("token") String token, @Field("user_id") String user_id, @Field("page") String page);

    @GET("countrylist")
    Call<ModelCountry> api_country_list();


    @FormUrlEncoded
    @POST("users/point/list")
    Call<ModelPoints> api_point_list(@Field("user_id") String user_id, @Field("page") String page);


    @FormUrlEncoded
    @POST("update/profile")
    Call<ModelUpdateProfile> api_update_profile(@Field("token") String token, @Field("user_id") String user_id,
                                                @Field("first_name") String first_name, @Field("last_name") String last_name,
                                                @Field("website_link") String website_link, @Field("country") String country,
                                                @Field("about_me") String about_me);


    @Multipart
    @POST("ads-create/submit")
    Call<ModelCreateAd> api_create_ad(@Part MultipartBody.Part file, @Part MultipartBody.Part thumb, @Part("user_id") RequestBody requestBody, @Part("token") RequestBody userid,
                                      @Part("days") RequestBody days, @Part("subject_id") RequestBody subject_id, @Part("link") RequestBody link,
                                      @Part("show_type") RequestBody show_type);

    @FormUrlEncoded
    @POST("update/notification")
    Call<ModelTopicCategory> UpdateNotification(@Field("token") String strTokenId, @Field("user_id") String strUid, @Field("you_have_favourite_email") int i, @Field("you_have_favourite_notify") int i1, @Field("you_have_comment_email") int i2, @Field("you_have_comment_notify") int i3, @Field("you_have_selected_categories_notify") int i4, @Field("you_have_selected_categories_email") int i5);

    @FormUrlEncoded
    @POST("change/password")
    Call<ModelTopicCategory> UpdatePassword(@Field("token") String strTokenId, @Field("user_id") String strUid, @Field("old_password") String oldPass, @Field("password") String passord, @Field("password_confirmation") String confirmPass);

    @FormUrlEncoded
    @POST("update/sociallink")
    Call<ModelTopicCategory> UpdateSocial(@Field("token") String strTokenId, @Field("user_id") String strUid, @Field("facebook") String trim, @Field("instagram") String trim1, @Field("twitter") String trim2, @Field("pinterest") String trim3, @Field("linkedin") String trim4, @Field("mygroup") String trim5);

    @FormUrlEncoded
    @POST("deactivate/account")
    Call<ModelTopicCategory> DeactiveUser(@Field("token") String strTokenId, @Field("user_id") String strUid, @Field("reason") String reason, @Field("reason_other") String trim);

    @GET("static-pages/about-us")
    Call<AboutUs> api_aboutUs();

    @GET("static-pages/terms")
    Call<TermOfUse> api_termOfUse();

    @GET("static-pages/privacy-policy")
    Call<PrivacyPolicy> api_PrivacyPolicy();

    @GET("static-pages/disclaimer-policy")
    Call<DeclaimerPolicy> api_declaimerPolicy();
}
