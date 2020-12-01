package com.spinwash.admin.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.LatLng;
import com.spinwash.admin.vo.OrderVo;
import com.spinwash.admin.vo.PickupSlot;
import com.spinwash.admin.vo.ProductVo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Utils {


    public static String STATICMAP_1 = "https://maps.googleapis.com/maps/api/staticmap?center=";
    public static String STATICMAP_2 = "&zoom=15&size=200x200&markers=color:red%7Clabel:W%7C";

    public static final int CONNECTION_TIME_OUT_STATUS_CODE = 1000;

    public static final int FETCH_TWITTER_DETAILS_ERROR = 2001;

    public static final int CLIENT_PROTOCOL_EXCEPTION_STATUS_CODE = 1001;

    public static final int API_RESULT_OK = 0;

    public static final int API_RESULT_ERROR = 1;

    public static final int PLAN_COUNT = 3;

    public static final int Max_Image_Limit = 10;

    public static final int MESSAGE_SEND_ALL = -100;
    public static final int MESSAGE_SEND_UNDER_CAT = -101;

    public static boolean refreshData = false;
    public static String randomLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static int msgNotifyID = 1001;

    public static String COUNTRY_QATAR = "Qatar";
    public static String COUNTRY_OMAN = "Oman";
    public static String URL_SERVER = "http://qubicle.me/spinwash/orderapi.php";
    public static String USERNAME_0 = "washnowAdmin";
    public static String PASSWORD_0 = "admin!$wA";
    public static String PASSWORD_0B = "adm1n!$wA";
    public static String USERNAME_1 = "employee1";
    public static String NAME_1 = "Employee 1";
    public static String PASSWORD_1 = "emp#4231&";
    public static String PASSWORD_1B = "emp#2631&";
    public static String USERNAME_2 = "employee2";
    public static String NAME_2 = "Employee 2";
    public static String PASSWORD_2 = "emp675!$";
    public static String PASSWORD_2B = "emp231!$";
    public static String USERNAME_3 = "employee3";
    public static String NAME_3 = "Employee 3";
    public static String PASSWORD_3 = "emp3*409";
    public static String PASSWORD_3B = "emp54#09";
    public static String USERNAME_4 = "employee4";
    public static String PASSWORD_4 = "emp4@5%$";
    public static String PASSWORD_4B = "emp231$";
    public static String USERNAME_5 = "employee5";
    public static String PASSWORD_5 = "emp5*634";
    public static String PASSWORD_5B = "emp5*124";
    public static String USERNAME_6 = "employee6";
    public static String PASSWORD_6 = "emp*6$#6";
    public static String PASSWORD_6B = "emp*6236";


    public static String URL_USER_SIGNUP = URL_SERVER + "/api/v2/user/signup";
    public static int USER = 0; //0 for all

    public static String[] MENU = new String[]{"HUB", "ORGANIZATION", "EVENTS", "FAVORITES", "NOTIFICATIONS"};

    public static Bitmap cropCircleBitmap(Bitmap bitmapimg) {
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 2,
                bitmapimg.getHeight() / 2, bitmapimg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

    public static File bitmaptoFile(Bitmap bitmap) {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.Washnow";
        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "Washnow" + showRandomInteger(0, 10000, new Random()) + ".png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 95, fOut);
            fOut.flush();
            fOut.close();
            return file;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public static String getText(EditText et) {
        try {
            if (et.getText().length() > 0)
                return et.getText().toString();

        } catch (Exception e) {

        }
        return null;
    }

    public static void setSharedPreference(Context ctx, String key, String value) {
        SharedPreferences pref = ctx.getSharedPreferences("Washnow", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharedPreference(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences("Washnow", Context.MODE_PRIVATE);
        if (pref.contains(key)) {
            return pref.getString(key, "0");
        } else {
            return null;
        }
    }

    public static void deleteSharedPreference(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences("Washnow", Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        if (pref.contains(key)) {
            editor.remove(key);
            editor.commit();
        }
    }

    public static int showRandomInteger(int aStart, int aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        //   int randomNumber =  (int)(fraction + aStart);
        return (int) (fraction + aStart);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URLConnection conn = new URL(src).openConnection();
            conn.connect();
            return BitmapFactory.decodeStream(conn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Intent getShareIntent(String type, String subject, String text, Context context) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(share, 0);
        System.out.println("resinfo: " + resInfo);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type)) {
                    share.putExtra(Intent.EXTRA_SUBJECT, subject);
                    share.putExtra(Intent.EXTRA_TEXT, text);
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return null;

            return share;
        }
        return null;
    }

    public static String getPath(Uri uri, Activity ctx) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ctx.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

    public static String getExtension(String fileName) {
        final String emptyExtension = null;
        if (fileName == null) {
            return emptyExtension;
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return emptyExtension;
        }
        return fileName.substring(index + 1);
    }

    public static void hideSoftKeyBoard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        //  if(imm.isAcceptingText()) { // verify if the soft keyboard is open
        imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
    /*}else {
    	Log.v("no keyboard", "No keyboard");
    }*/
    }

    public static OrderVo getOrder(Context context) {
        try {
            String user = getSharedPreference(context, "orderObject");
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jp = jsonFactory.createJsonParser(user);
            OrderVo bean = mapper.readValue(jp, OrderVo.class);
            return bean;
        } catch (Exception e) {

        }
        return null;
    }

    public static void setOrder(Context context, OrderVo user) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            String result = mapper.writeValueAsString(user);
            setSharedPreference(context, "orderObject", result);
            Log.v("user", result);
        } catch (Exception e) {

        }

    }

    public static String getAddress(LatLng point, Context context) {
        String filterAddress = "";
        Geocoder geoCoder = new Geocoder(
                context, Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(
                    point.latitude,
                    point.longitude, 1);

            if (addresses.size() > 0) {
                for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++)
                    filterAddress += addresses.get(0).getAddressLine(index) + " ";
                return filterAddress;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (Exception e2) {
            // TODO: handle exception

            e2.printStackTrace();
        }
        return "Venue";
    }

    public static void launchGoogleMaps(Context context, String latitude, String longitude, String label) {
        String format = "geo:0,0?q=" + latitude + "," + longitude;
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }


    public static float calculateDpToPixel(float dp, Context context) {

        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;

    }

    public static void clearNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Utils.msgNotifyID);

    }


    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (round(dist, 2));
    }

    public static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    public static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void addAsContactConfirmed(final Context context, String name, String number) {

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, number);

        context.startActivity(intent);

    }

    public static ArrayList<PickupSlot> getPickupSlot() {
        ArrayList<PickupSlot> pickupSlot = new ArrayList<PickupSlot>();
        ;

        if (pickupSlot == null || pickupSlot.isEmpty()) {
            PickupSlot pSlot = new PickupSlot();
            pSlot.id = 7;
            pSlot.time = "7:00AM - 8:00AM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 8;
            pSlot.time = "8:00AM - 9:00AM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 9;
            pSlot.time = "9:00AM - 10:00AM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 10;
            pSlot.time = "10:00AM - 11:00AM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 11;
            pSlot.time = "11:00AM - 12:00AM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 12;
            pSlot.time = "12:00PM - 1:00PM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 13;
            pSlot.time = "1:00PM - 2:00PM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 14;
            pSlot.time = "2:00PM - 3:00PM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 15;
            pSlot.time = "3:00PM - 4:00PM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 16;
            pSlot.time = "4:00PM - 5:00PM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 17;
            pSlot.time = "5:00PM - 6:00PM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 18;
            pSlot.time = "6:00PM - 7:00PM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 19;
            pSlot.time = "7:00PM - 8:00PM";
            pickupSlot.add(pSlot);
            pSlot = new PickupSlot();
            pSlot.id = 20;
            pSlot.time = "8:00PM - 9:00PM";
            pickupSlot.add(pSlot);


        }

        return pickupSlot;

    }

    public static String writeListToJsonArray(ArrayList<ProductVo> eList) throws IOException {


        final OutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out, eList);

        final byte[] data = ((ByteArrayOutputStream) out).toByteArray();
        return new String(data);
    }

    public static String getType(ProductVo vo) {

        if (vo.getType().equalsIgnoreCase("ironing"))
            return " iron";
        if (vo.getType().equalsIgnoreCase("wash"))
            return " wash";
        if (vo.getType().equalsIgnoreCase("dryclean"))
            return " dryc";
        else
            return "";
    }

    public static String getCount(ProductVo vo) {

        if (vo.getType().equalsIgnoreCase("ironing"))
            return addspaceleft(5, String.valueOf(vo.getIronCount()));
        if (vo.getType().equalsIgnoreCase("wash"))
            return addspaceleft(5, String.valueOf(vo.getWashCount()));
        ;
        if (vo.getType().equalsIgnoreCase("dryclean"))
            return addspaceleft(5, String.valueOf(vo.getDryCount()));
        ;

        return "";
    }

    public static String addspaceleft(int pos, String value) {

        int len = value.length();
        len = pos - len;
        if (len > 0)
            for (int i = 0; i < len; i++) {
                value = " " + value;
            }
        return value;
    }

    ;

    public static String addspaceRight(int pos, String value) {
        int len = value.length();
        len = pos - len;
        if (len > 0)
            for (int i = 0; i < len; i++) {
                value = value + " ";
                // Log.i("temp 0", ""+temp[0]);

            }
        return value;
    }

    public static String roundPrice(String value, String currency) {
        if (currency.equalsIgnoreCase("OMR")) {
            double amount = Double.parseDouble(value);
            NumberFormat formatter = new DecimalFormat("#0.000");
            return formatter.format(amount);
        } else {
            double amount = Double.parseDouble(value);
            NumberFormat formatter = new DecimalFormat("#0.00");
            return formatter.format(amount);
        }

    }

    public static String getRandomLetters() {
        Random rnd = new Random();

        return (randomLetters.charAt(rnd.nextInt(randomLetters.length()))) + "";
    }
}
