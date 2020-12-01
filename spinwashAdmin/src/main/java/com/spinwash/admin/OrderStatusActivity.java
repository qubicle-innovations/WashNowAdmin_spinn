package com.spinwash.admin;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spinwash.admin.async.OrderUpdateTask;
import com.spinwash.admin.parser.Parser;
import com.spinwash.admin.utils.DateUtil;
import com.spinwash.admin.utils.Utils;
import com.spinwash.admin.utils.ZTypeface;
import com.spinwash.admin.vo.OrderVo;
import com.spinwash.admin.vo.TResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OrderStatusActivity extends BaseActivity implements OnClickListener, OnMapReadyCallback {
    private int activeIM, activeETDate, activeETTime;
    private Calendar startDate, endDate;
    private DatePickerDialog dialog = null;
    private OrderVo order;
    private boolean dataUpdated = false;
    public String currency;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderstatus_layout);
        actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_backnav);
        context = this;
        String country =  Utils.getSharedPreference(context,"country");
        if( country!=null&&country.equalsIgnoreCase(Utils.COUNTRY_OMAN)){
            currency = "OMR";
        }else {
            currency = "QAR";

        }
        Intent intent = getIntent();
        if (intent.hasExtra("order")) {
            order = (OrderVo) intent.getSerializableExtra("order");
            initLayout();
        } else {
        //    new GetOrderTask(context).execute("19");
        }
//		Log.v("time", order)

        Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
        parent.setContentInsetsAbsolute(0, 0);
        //setMenu();
    }

    public void initLayout() {
        setTitle("ORDER STATUS");
        TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
        TextView tvPickUp = (TextView) findViewById(R.id.tvPickUp);
        TextView tvPickUpDate = (TextView) findViewById(R.id.tvPickUpDate);
        TextView tvPickUpTime = (TextView) findViewById(R.id.tvPickUpTime);
        TextView tvDelivery = (TextView) findViewById(R.id.tvDelivery);
        TextView tvDeliveryDate = (TextView) findViewById(R.id.tvDeliveryDate);
        TextView tvDeliveryTime = (TextView) findViewById(R.id.tvDeliveryTime);
        TextView tvTitle = (TextView) findViewById(R.id.tv_actionbar_title);
        TextView tvStatusTitle = (TextView) findViewById(R.id.tvStatusTitle);
        TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
        TextView tvNameTitle = (TextView) findViewById(R.id.tvNameTitle);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvNumberTitle = (TextView) findViewById(R.id.tvNumberTitle);
        TextView tvNumber = (TextView) findViewById(R.id.tvNumber);
        TextView tvNotesTitle = (TextView) findViewById(R.id.tvNotesTitle);
        TextView tvNotes = (TextView) findViewById(R.id.tvNotes);
        TextView tvAmountTitle = (TextView) findViewById(R.id.tvAmountTitle);
        TextView tvPrice = (TextView) findViewById(R.id.tvPrice);
        TextView tvTagTitle = (TextView) findViewById(R.id.tvTagTitle);
        TextView tvTag = (TextView) findViewById(R.id.tvTag);
        TextView tvCoupon = (TextView) findViewById(R.id.tvCoupon);

        tvStatusTitle.setTypeface(ZTypeface.UbuntuR(context));
        tvStatus.setTypeface(ZTypeface.UbuntuR(context));
        tvNameTitle.setTypeface(ZTypeface.UbuntuR(context));
        tvName.setTypeface(ZTypeface.UbuntuR(context));
        tvNumberTitle.setTypeface(ZTypeface.UbuntuR(context));
        tvNumber.setTypeface(ZTypeface.UbuntuR(context));
        tvNotesTitle.setTypeface(ZTypeface.UbuntuR(context));
        tvNotes.setTypeface(ZTypeface.UbuntuR(context));
        tvAmountTitle.setTypeface(ZTypeface.UbuntuR(context));
        tvPrice.setTypeface(ZTypeface.UbuntuR(context));
        tvTagTitle.setTypeface(ZTypeface.UbuntuR(context));
        tvTag.setTypeface(ZTypeface.UbuntuR(context));
        tvPickUp.setTypeface(ZTypeface.UbuntuR(context));
        tvPickUpDate.setTypeface(ZTypeface.UbuntuR(context));
        tvPickUpTime.setTypeface(ZTypeface.UbuntuR(context));
        tvDelivery.setTypeface(ZTypeface.UbuntuR(context));
        tvDeliveryDate.setTypeface(ZTypeface.UbuntuR(context));
        tvDeliveryTime.setTypeface(ZTypeface.UbuntuR(context));
        tvAddress.setTypeface(ZTypeface.UbuntuR(context));
        tvCoupon.setTypeface(ZTypeface.UbuntuR(context));

        if (order.getStatus().equalsIgnoreCase("pending")) {

            setTitle("UPDATE ORDER");
            tvStatus.setText("Pending");
        } else if (order.getStatus().equalsIgnoreCase("accepeted")) {
            setTitle("UPDATE ORDER");
            tvStatus.setText("Pick Up requested");
        } else if (order.getStatus().equalsIgnoreCase("washing")) {
            setTitle("UPDATE ORDER");
            tvStatus.setText("Washing");
        } else if (order.getStatus().equalsIgnoreCase("completed")) {
            setTitle("UPDATE ORDER");


            tvStatus.setText("Delivery required");
        } else if (order.getStatus().equalsIgnoreCase("paid")) {
            setTitle("WASH NOW");
            tvStatus.setText("Payment Collected");

        } else if (order.getStatus().equalsIgnoreCase("cancelled")) {
            setTitle("WASH NOW");
            tvStatus.setText("Cancelled");

        }
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        startDate.setTime(DateUtil.stringToDate(order.getPick_up_date(), DateUtil.DATETIME_SQL));
        endDate.setTime(DateUtil.stringToDate(order.getDelivery_date(), DateUtil.DATETIME_SQL));
        tvStatusTitle.setText("#"+order.getId());
        Date date = startDate.getTime();
        int diff = DateUtil.daysDiff(new Date(), date);
        if (diff == 0) {
            tvPickUpDate.setText("Today");
        } else if (diff == 1) {
            tvPickUpDate.setText("Tomorrow");
        } else if (diff == -1) {
            tvPickUpDate.setText("Yesterday");
        } else if (diff < -1) {
            tvPickUpDate.setText(Math.abs(diff) + " days ago");
        } else {
            tvPickUpDate.setText(DateUtil.dateToString(date, DateUtil.MONTH_DATE_PATTERN));
        }
        date = endDate.getTime();
        diff = DateUtil.daysDiff(new Date(), date);
        if (diff == 0) {
            tvDeliveryDate.setText("Today");
        } else if (diff == 1) {
            tvDeliveryDate.setText("Tomorrow");
        } else if (diff == -1) {
            tvDeliveryDate.setText("Yesterday");
        } else if (diff < -1) {
            tvDeliveryDate.setText(Math.abs(diff) + " days ago");
        } else {
            tvDeliveryDate.setText(DateUtil.dateToString(date, DateUtil.MONTH_DATE_PATTERN));

        }
        tvPickUpTime.setText(DateUtil.dateToString(startDate.getTime(), DateUtil.TIME_12HRS_SHORT));
        tvDeliveryTime.setText(DateUtil.dateToString(endDate.getTime(), DateUtil.TIME_12HRS_SHORT));
        tvPickUp.setText("PICKUP TIME");
        tvDelivery.setText("RETURN TIME");


        tvName.setText(order.getName());
        tvNumber.setText(order.getPhone_number());
        tvAddress.setText(order.getAddress());
        if (order.getNotes() != null)
            tvNotes.setText(order.getNotes());
        if (order.getExtras() != null) {
            if (order.getExtras().equalsIgnoreCase("Employee 1")) {
                tvTag.setText(order.getExtras());
                tvTag.setBackgroundColor(getResources().getColor(R.color.home_item_pink));
                tvTagTitle.setBackgroundColor(getResources().getColor(R.color.home_item_pink));

            } else if (order.getExtras().equalsIgnoreCase("Employee 2")) {
                tvTag.setText(order.getExtras());
                tvTag.setBackgroundColor(getResources().getColor(R.color.home_item_blue));
                tvTagTitle.setBackgroundColor(getResources().getColor(R.color.home_item_blue));

            } else if (order.getExtras().equalsIgnoreCase("Employee 3")) {
                tvTag.setText(order.getExtras());
                tvTag.setBackgroundColor(getResources().getColor(R.color.home_item_yellow));
                tvTagTitle.setBackgroundColor(getResources().getColor(R.color.home_item_yellow));

            } else if (order.getExtras().equalsIgnoreCase("Employee 4")) {
                tvTag.setText(order.getExtras());
                tvTag.setBackgroundColor(getResources().getColor(R.color.home_item_green));
                tvTagTitle.setBackgroundColor(getResources().getColor(R.color.home_item_green));

            } else if (order.getExtras().equalsIgnoreCase("Employee 5")) {
                tvTag.setText(order.getExtras());
                tvTag.setBackgroundColor(getResources().getColor(R.color.orange));
                tvTagTitle.setBackgroundColor(getResources().getColor(R.color.orange));

            } else if (order.getExtras().equalsIgnoreCase("Employee 6")) {
                tvTag.setText(order.getExtras());
                tvTag.setBackgroundColor(getResources().getColor(R.color.iconpressed_red));
                tvTagTitle.setBackgroundColor(getResources().getColor(R.color.iconpressed_red));

            }
        }

        if (order.getTotal() != null)

            tvPrice.setText(  currency+" "+ Utils.roundPrice(order.getTotal(),currency));
        else {
            tvPrice.setText("0 "+currency);
            order.setTotal("0");
        }


        if (order.getCoupon_code() != null &&!order.getCoupon_code().equalsIgnoreCase("null"))
            tvCoupon.setText(order.getCoupon_code());


        if (order.getLatitude() != null && order.getLatitude().length() > 1) {
            setMapLocation();
        }

        if (!order.getStatus().equalsIgnoreCase("paid")) {
            tvTitle.setOnClickListener(this);
            tvTag.setOnClickListener(this);
            tvTagTitle.setOnClickListener(this);
            tvStatus.setOnClickListener(this);
            tvStatusTitle.setOnClickListener(this);

        }
        if (order.getStatus().equalsIgnoreCase("pending")) {

            tvPickUpDate.setOnClickListener(this);
            tvDeliveryDate.setOnClickListener(this);
            tvPickUpTime.setOnClickListener(this);
            tvDeliveryTime.setOnClickListener(this);

        } else if (order.getStatus().equalsIgnoreCase("accepeted")) {
            tvPickUpDate.setOnClickListener(this);
            tvDeliveryDate.setOnClickListener(this);
            tvPickUpTime.setOnClickListener(this);
            tvDeliveryTime.setOnClickListener(this);

        } else if (!order.getStatus().equalsIgnoreCase("paid")) {
            tvDeliveryDate.setOnClickListener(this);
            tvDeliveryTime.setOnClickListener(this);
        }
        tvName.setOnClickListener(this);
        if (!order.getStatus().equalsIgnoreCase("cancelled")) {
            tvAmountTitle.setOnClickListener(this);
            tvPrice.setOnClickListener(this);
        }

    }


    public void updateOrder(TResponse<String> result) {


        if (result == null) {
            Toast.makeText(context, "unable to update order status \n please check network connection", Toast.LENGTH_SHORT).show();
        } else if (result.isHasError()) {
            Toast.makeText(context, "unable to update order status \n please try later", Toast.LENGTH_SHORT).show();

        } else if (result.getResponseContent() != null) {

            try {
                order = Parser.getOrders(result.getResponseContent()).get(0);
                dataUpdated = false;
                Toast.makeText(context, "Order Updated", Toast.LENGTH_SHORT).show();
                initLayout();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, InvoiceActivity.class);
        intent.putExtra("vo", order);
        switch (v.getId()) {
            case R.id.tv_actionbar_title:
                createOrder();
                break;
            case R.id.tvTag:
                showTagsDialog();
                break;
            case R.id.tvTagTitle:
                showTagsDialog();
                break;
            case R.id.tvStatus:
                showStatusDialog();
                break;
            case R.id.tvStatusTitle:
                showStatusDialog();
                break;
            case R.id.tvAmountTitle:
                startActivityForResult(intent, 2);
                //showAmountDialog();
                break;
            case R.id.tvPrice:
                startActivityForResult(intent, 2);
                break;
            case R.id.tvName:
                saveContactDialog();
                break;
            case R.id.tvPickUpDate:
                activeETDate = R.id.tvPickUpDate;
                showDateDailog("Select Pickup Date");
                break;
            case R.id.tvDeliveryDate:
                activeETDate = R.id.tvDeliveryDate;
                showDateDailog("Select Delivery Date");
                break;

            case R.id.tvPickUpTime:
                activeETTime = R.id.tvPickUpTime;
                showTimeDialog("Select Pickup Time");
                break;
            case R.id.tvDeliveryTime:
                activeETTime = R.id.tvDeliveryTime;
                showTimeDialog("Select Delivery Time");
                break;

            default:
                break;
        }
    }

    public void setMapLocation() {

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);


    }

    public void showStatusDialog() {
        final String[] items = new String[]{"Ready to pickup", "Ready to wash", "Ready to deliver", "Payment collected", "Cancel"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Order Status");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { //pick from camera
                if (item == 0) {
                    order.setStatus("accepeted");
                    dataUpdated = true;
                    initLayout();

                } else if (item == 1) {
                    order.setStatus("washing");
                    dataUpdated = true;
                    initLayout();

                } else if (item == 2) {
                    order.setStatus("completed");
                    dataUpdated = true;
                    //if(order.getBill_no()==null||order.getBill_no().length()<2)
                    //showAmountDialog(0);

                } else if (item == 3) {
                    //if(order.getBill_no()==null||order.getBill_no().length()<2)
                    //showAmountDialog(0);
                    order.setStatus("paid");
                    dataUpdated = true;
                    initLayout();

                } else if (item == 4) {
                    order.setStatus("cancelled");
                    dataUpdated = true;
                    showNoteDialog();

                }
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showTagsDialog() {
        final String[] items = new String[]{"Employee 1", "Employee 2", "Employee 3", "Employee 4", "Employee 5", "Employee 6"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Employee");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { //pick from camera
                if (item == 0) {
                    order.setExtras("Employee 1");
                    dataUpdated = true;
                    initLayout();

                } else if (item == 1) {
                    order.setExtras("Employee 2");
                    dataUpdated = true;
                    initLayout();

                } else if (item == 2) {
                    order.setExtras("Employee 3");
                    dataUpdated = true;
                    initLayout();

                } else if (item == 3) {
                    order.setExtras("Employee 4");
                    dataUpdated = true;
                    initLayout();

                } else if (item == 4) {
                    order.setExtras("Employee 5");
                    dataUpdated = true;
                    initLayout();

                } else {
                    order.setExtras("Employee 6");
                    dataUpdated = true;
                    initLayout();

                }
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showAmountDialog(final int cost) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Total Amount");


        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflator.inflate(R.layout.amount_dialog, null);
        final EditText etAmount = (EditText) convertView.findViewById(R.id.etAmount);
        final TextView etBill = (TextView) convertView.findViewById(R.id.etBillNo);

        if (order.getStatus().equalsIgnoreCase("cancelled")) {
            etAmount.setFocusable(false);
            etAmount.setClickable(false);
            etBill.setFocusable(false);
            etBill.setClickable(false);
        }
        if (order.getBill_no() != null && order.getBill_no().length() > 0)
            etBill.setText(order.getBill_no());
        if (cost > 0) {
            etAmount.setText(String.valueOf(cost));
        } else if (order.getTotal() != null && order.getTotal().length() > 0)
            etAmount.setText(order.getTotal());
        // Set an EditText view to get user input
        //final EditText input = new EditText(this);
        etAmount.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
        alert.setView(convertView);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = etAmount.getText().toString();
                // String billNo = etBill.getText().toString();
                order.setTotal(value);
                // order.setBill_no(billNo);
                initLayout();

                dataUpdated = true;
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                initLayout();
                dataUpdated = false;
            }
        });

        alert.show();
    }

    public void showNoteDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Wash Now");
        alert.setMessage("Please share the reason for cancellation");
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        //input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                order.setNotes(value);
                initLayout();
                dataUpdated = true;
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                initLayout();
                dataUpdated = true;
            }
        });

        alert.show();
    }

    public void createOrder() {


        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("name", order.getName()));
        params.add(new BasicNameValuePair("email", order.getEmail()));
        params.add(new BasicNameValuePair("address", order.getAddress()));
        params.add(new BasicNameValuePair("phone_number", order.getPhone_number()));
        params.add(new BasicNameValuePair("longitude", order.getLongitude()));
        params.add(new BasicNameValuePair("latitude", order.getLatitude()));
        params.add(new BasicNameValuePair("notes", order.getNotes()));

        params.add(new BasicNameValuePair("pick_up_date", DateUtil.dateToString(startDate.getTime(), DateUtil.DATETIME_SQL)));
        params.add(new BasicNameValuePair("delivery_date", DateUtil.dateToString(endDate.getTime(), DateUtil.DATETIME_SQL)));
        params.add(new BasicNameValuePair("status", order.getStatus()));
        params.add(new BasicNameValuePair("extras", order.getExtras()));
        params.add(new BasicNameValuePair("id", order.getId()));
        params.add(new BasicNameValuePair("total", order.getTotal()));
        params.add(new BasicNameValuePair("bill_no", order.getBill_no()));
        params.add(new BasicNameValuePair("country", order.getCountry()));
        params.add(new BasicNameValuePair("process", "updateorder"));
        new OrderUpdateTask(context, params).execute("");


    }

    public boolean exitDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Wash Now");
        builder.setMessage("Would you like to save the changes made");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

                createOrder();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                //dialog.dismiss();
                dataUpdated = false;
                finish();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        return false;


    }

    public void showDateDailog(String title) {
        Calendar dtTxt = null;
        int year, month, day;
        String preExistingDate;
        if (activeETDate == R.id.tvPickUpDate) {
            TextView etDOB = (TextView) findViewById(R.id.tvPickUpDate);
            preExistingDate = (String) etDOB.getText().toString();
            year = startDate.get(Calendar.YEAR);
            month = startDate.get(Calendar.MONTH); // Note: zero based!
            day = startDate.get(Calendar.DAY_OF_MONTH);

        } else {
            TextView etDOB = (TextView) findViewById(R.id.tvDeliveryDate);
            preExistingDate = (String) etDOB.getText().toString();
            year = endDate.get(Calendar.YEAR);
            month = endDate.get(Calendar.MONTH); // Note: zero based!
            day = endDate.get(Calendar.DAY_OF_MONTH);

        }


        if (preExistingDate != null && !preExistingDate.equals("")) {

            dialog = new DatePickerDialog(context,
                    new PickDate(), year,
                    month,
                    day);


        } else {
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);


            dialog = new DatePickerDialog(context,
                    new PickDate(), year,
                    month,
                    day);

        }

        dialog.setTitle(title);

        dialog.show();
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        // 	dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap map;
        Marker eventMarker = null;
        map = googleMap;
        double lat = Double.parseDouble(order.getLatitude());
        double lng = Double.parseDouble(order.getLongitude());
        LatLng point = new LatLng(lat, lng);
        Log.e("location" ,"lat " + order.getLatitude() +" long "+ order.getLongitude() );
        eventMarker = map.addMarker(new MarkerOptions()
                .position(point)
                .title(order.getAddress())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));

    }

    private class PickDate implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            TextView tvPickUpDate = (TextView) findViewById(R.id.tvPickUpDate);
            TextView tvDeliveryDate = (TextView) findViewById(R.id.tvDeliveryDate);
            view.updateDate(year, monthOfYear, dayOfMonth);
            Calendar mcurrentDate = Calendar.getInstance();
            ;
            mcurrentDate.set(year, monthOfYear, dayOfMonth);

            dataUpdated = true;
            if (activeETDate == R.id.tvPickUpDate) {
                mcurrentDate.set(Calendar.HOUR_OF_DAY, startDate.get(Calendar.HOUR_OF_DAY));
                mcurrentDate.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE));
                mcurrentDate.set(Calendar.SECOND, 0);

                Date date = mcurrentDate.getTime();
                int diff = DateUtil.daysDiff(new Date(), date);
                if (diff < 0) {
                    Toast.makeText(context, "Please choose a day within next 7 days", Toast.LENGTH_SHORT).show();

                } else if (diff == 0) {
                    startDate = mcurrentDate;
                    tvPickUpDate.setText("Today");
                } else if (diff == 1) {
                    startDate = mcurrentDate;
                    tvPickUpDate.setText("Tomorrow");
                } else if (diff < 7) {
                    startDate = mcurrentDate;
                    tvPickUpDate.setText(DateUtil.dateToString(date, DateUtil.DATE_NAME_PATTERN));
                } else {
                    Toast.makeText(context, "Please choose a day within next 7 days", Toast.LENGTH_SHORT).show();
                }


            } else {

                mcurrentDate.set(Calendar.HOUR_OF_DAY, endDate.get(Calendar.HOUR_OF_DAY));
                mcurrentDate.set(Calendar.MINUTE, endDate.get(Calendar.MINUTE));
                mcurrentDate.set(Calendar.SECOND, 0);
                Date date = mcurrentDate.getTime();
                Date pDate = startDate.getTime();
                int diff = DateUtil.daysDiff(pDate, date);
                if (diff < 0) {
                    Toast.makeText(context, "Please choose a day within next 7 days of pickup", Toast.LENGTH_SHORT).show();

                } else if (diff == 0) {
                    Toast.makeText(context, "Please choose a day within next 7 days of pickup", Toast.LENGTH_SHORT).show();

                } else if (diff == 1) {
                    if (DateUtil.daysDiff(new Date(), date) == 1) {
                        endDate = mcurrentDate;
                        tvDeliveryDate.setText("Tomorrow");
                    } else {
                        endDate = mcurrentDate;
                        tvDeliveryDate.setText(DateUtil.dateToString(date, DateUtil.DATE_NAME_PATTERN));
                    }
                } else if (diff < 8) {
                    endDate = mcurrentDate;
                    tvDeliveryDate.setText(DateUtil.dateToString(date, DateUtil.DATE_NAME_PATTERN));
                } else {
                    Toast.makeText(context, "Please choose a day within next 7 days of pickup", Toast.LENGTH_SHORT).show();
                }


            }
            dialog.hide();
        }

    }

    public void showTimeDialog(String title) {
        final TextView tvPickUpTime = (TextView) findViewById(R.id.tvPickUpTime);
        final TextView tvDeliveryTime = (TextView) findViewById(R.id.tvDeliveryTime);
        Calendar mcurrentTime = null;
        ;
        dataUpdated = true;
        if (activeETTime == R.id.tvPickUpTime) {
            mcurrentTime = startDate;
        } else if (activeETTime == R.id.tvDeliveryTime) {
            mcurrentTime = endDate;

        }
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time = selectedHour + ":" + selectedMinute;
                if (activeETTime == R.id.tvPickUpTime) {
                    startDate.set(Calendar.HOUR_OF_DAY, selectedHour);
                    startDate.set(Calendar.MINUTE, selectedMinute);
                    startDate.set(Calendar.SECOND, 0);

                    tvPickUpTime.setText(DateUtil.changeDateFormat(time, DateUtil.TIME_24HRS_SHORT, DateUtil.TIME_12HRS_SHORT));
                } else if (activeETTime == R.id.tvDeliveryTime) {
                    tvDeliveryTime.setText(DateUtil.changeDateFormat(time, DateUtil.TIME_24HRS_SHORT, DateUtil.TIME_12HRS_SHORT));
                    endDate.set(Calendar.HOUR_OF_DAY, selectedHour);
                    endDate.set(Calendar.MINUTE, selectedMinute);
                    endDate.set(Calendar.SECOND, 0);
                }

            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle(title);

        mTimePicker.show();
        mTimePicker.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        mTimePicker.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

    }


    public void updateOrderStatus(TResponse<String> result) {

        if (result != null && !result.isHasError()) {
            if (result.getResponseContent() != null) {

                try {
                    order = Parser.getOrders(result.getResponseContent()).get(0);
                    initLayout();
                    dataUpdated = false;

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(context, "Network Error, Try Again ", Toast.LENGTH_SHORT).show();
                }

            }
        } else if (result != null && result.isHasError()) {
            Toast.makeText(context, "Network Error, Try Again ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Network Error, Try Again ", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void finish() {
        // TODO Auto-generated method stub
        if (dataUpdated)
            exitDialog();
        else
            super.finish();
    }


    public boolean saveContactDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Wash Now");
        builder.setMessage("Would you like to save the contact");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                Utils.addAsContactConfirmed(context, order.getName(), order.getPhone_number());
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                //dialog.dismiss();
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        return false;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2 && data != null) {

            double cost = data.getDoubleExtra("cost", 0);
            String products = data.getStringExtra("products");
//			Log.e("products " +cost,products);

            //		new ProductsUpdateTask(context).execute(new String[]{order.getId(),products});
            order.setTotal(Utils.roundPrice(String.valueOf(cost),currency));
            dataUpdated = true;


            // order.setBill_no(billNo);


            initLayout();
            //showAmountDialog(cost);
        } else {
        }
        //showAmountDialog(0);
    }

}
