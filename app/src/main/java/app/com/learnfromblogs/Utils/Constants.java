package app.com.learnfromblogs.Utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;


/**
 * Created by abc on 7/13/2016.
 */
public class Constants {





    public static final int PERMISSION_REQUEST_CODE = 1000;
    public static final int PERMISSION_GRANTED = 1001;
    public static final int PERMISSION_DENIED = 1002;

    public static final int REQUEST_CODE = 2000;

    public static final int FETCH_STARTED = 2001;
    public static final int FETCH_COMPLETED = 2002;
    public static final int ERROR = 2005;

    /**
     * Request code for permission has to be < (1 << 8)
     * Otherwise throws java.lang.IllegalArgumentException: Can only use lower 8 bits for requestCode
     */





    public static final String LoginPREFERENCES = "LoginPrefs";
    public static SharedPreferences loginSharedPreferences;
    public static String LoginStatus = "login_status";
    public static String logintoken = "logintoken";
    public static String uid = "uid";
    public static String firstname = "firstname";
    public static String lastname = "lastname";
    public static String fullname = "fullname";
    public static String email = "email";
    public static String deviceid = "deviceid";
    public static String profileurl = "profileurl";
    public static String cateactive = "cateactive";


    public static String place_id = "place_id";



    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




    public static String DateFormate(String gameDate) {
        String date1 = null;
        String day = null;
        String monthString = null;
        String yearstring = null;
        String hourtring = null;
        String minstring = null;
        String secondstring = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(gameDate);
            day = (String) DateFormat.format("dd", date);
            monthString = (String) DateFormat.format("MMM", date);
            yearstring = (String) DateFormat.format("yyyy", date);

            date1 = monthString + " " + day + " " + yearstring;

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("exception", e.getMessage());
        }
        return date1;
    }


    public static String TimeFormate1(String gameDate) {
        String date1 = null;
        String day = null;
        String monthString = null;
        String yearstring = null;
        String hourtring = null;
        String minstring = null;
        String secondstring = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = format.parse(gameDate);
            day = (String) DateFormat.format("dd", date);
            monthString = (String) DateFormat.format("MMM", date);
            yearstring = (String) DateFormat.format("yy", date);


            hourtring = (String) DateFormat.format("hh", date);
            minstring = (String) DateFormat.format("mm", date);


            date1 = hourtring + ":"+minstring;

//            StringTokenizer tk = new StringTokenizer(gameDate);
//            String date111 = tk.nextToken();
//            String time = tk.nextToken();
//            SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
//            SimpleDateFormat sdfs = new SimpleDateFormat("h:mm a");
//            Date dt = null;
//            try {
//                dt = sdf.parse(time);
//                date1 = day + " " + monthString + ", " + yearstring;
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                Log.e("exception", e.getMessage());
////            }
//
//            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("exception", e.getMessage());
        }
        return date1;
    }


    public static String DateFormate2(String gameDate) {
        String date1 = null;
        String day = null;
        String monthString = null;
        String yearstring = null;
        String hourtring = null;
        String minstring = null;
        String secondstring = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = format.parse(gameDate);
            day = (String) DateFormat.format("dd", date);
            monthString = (String) DateFormat.format("MMM", date);
            yearstring = (String) DateFormat.format("yy", date);

            StringTokenizer tk = new StringTokenizer(gameDate);
            String date111 = tk.nextToken();
            String time = tk.nextToken();
            SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
            SimpleDateFormat sdfs = new SimpleDateFormat("h:mm a");
            Date dt = null;
            try {
                dt = sdf.parse(time);
                date1 = monthString + " " + day + ", " + yearstring;
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("exception", e.getMessage());
//            }

            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("exception", e.getMessage());
        }
        return date1;
    }


    public static String TimeFormate(String gameDate) {

        String timein12Format = "";

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(gameDate);
            timein12Format = new SimpleDateFormat("K:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return timein12Format;
    }


    public static String DateFormate1(String gameDate) {
        String date1 = null;
        String day = null;
        String monthString = null;
        String yearstring = null;
        String hourtring = null;
        String minstring = null;
        String secondstring = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = format.parse(gameDate);
            day = (String) DateFormat.format("dd", date);
            monthString = (String) DateFormat.format("MM", date);
            yearstring = (String) DateFormat.format("yyyy", date);

            StringTokenizer tk = new StringTokenizer(gameDate);
            String date111 = tk.nextToken();
            String time = tk.nextToken();
            SimpleDateFormat sdf = new SimpleDateFormat("kk:mm:ss");
            SimpleDateFormat sdfs = new SimpleDateFormat("h:mm a");
            Date dt = null;
            try {
                dt = sdf.parse(time);
                date1 = "played on " + day + "/" + monthString + "/" + yearstring;
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e("exception", e.getMessage());
//            }

            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("exception", e.getMessage());
        }
        return date1;
    }



}
