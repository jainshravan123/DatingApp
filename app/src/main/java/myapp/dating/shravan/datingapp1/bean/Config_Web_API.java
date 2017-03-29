package myapp.dating.shravan.datingapp1.bean;

/**
 * Created by admin on 12/07/16.
 */
public class Config_Web_API
{

    public static String root_domain_uri = "http://nodedating-datingnode.rhcloud.com/api/";
    public static String root_image_uri  = "http://nodedating-datingnode.rhcloud.com/";


    public static String sign_in_api              = root_domain_uri + "user/login_user";
    public static String sign_up_api              = root_domain_uri + "user/create_user";
    public static String profile_api              = root_domain_uri + "profile/userprofile/";
    public static String question_api             = root_domain_uri + "profile/question-match/1";
    public static String all_users_api            = root_domain_uri + "inside/all-user/1";
    public static String update_profile_image_api = root_domain_uri + "profile/cover_profileImage";
    public static String update_user_info         = root_domain_uri + "user/update-user";
    public static String all_gifts_api            = root_domain_uri + "gift/all-gifts";

}
