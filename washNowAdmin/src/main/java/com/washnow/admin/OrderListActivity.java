package com.washnow.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.washnow.admin.adapter.OrderListAdapter;
import com.washnow.admin.async.GetOrderListTask;
import com.washnow.admin.parser.Parser;
import com.washnow.admin.utils.Utils;
import com.washnow.admin.utils.ZTypeface;
import com.washnow.admin.vo.OrderVo;
import com.washnow.admin.vo.TResponse;

import java.util.ArrayList;
import java.util.Collections;

public class OrderListActivity extends BaseActivity implements OnClickListener {

    int type = 0;
    ArrayList<OrderVo> oList = new ArrayList<OrderVo>();
    String user;
    boolean allOrders = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderlist_layout);
        actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_backnav);
        context = this;
        Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
        parent.setContentInsetsAbsolute(0, 0);
        initLayout();
        user = Utils.getSharedPreference(context, "user");
        TextView tvStatusTitle = (TextView) findViewById(R.id.tvStatusTitle);
        if (user == null || user.equalsIgnoreCase("0") | user.equalsIgnoreCase(Utils.NAME_1) || user.equalsIgnoreCase(Utils.NAME_2) || user.equalsIgnoreCase(Utils.NAME_3)) {
            allOrders = true;
            tvStatusTitle.setText("All Orders");
        } else {

        }
        setTitle("WASH NOW");
        setMenu();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        new GetOrderListTask(context).execute(new String());

    }

    public void initLayout() {
        TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
        TextView tvStatusTitle = (TextView) findViewById(R.id.tvStatusTitle);
        TextView tvWeek0 = (TextView) findViewById(R.id.tvWeek0);
        TextView tvWeek1 = (TextView) findViewById(R.id.tvWeek1);
        TextView tvWeek2 = (TextView) findViewById(R.id.tvWeek2);
        EditText etSearch = (EditText) findViewById(R.id.etSearch);


        tvStatus.setTypeface(ZTypeface.UbuntuR(context));
        tvStatusTitle.setTypeface(ZTypeface.UbuntuR(context));
        tvWeek0.setTypeface(ZTypeface.UbuntuR(context));
        tvWeek1.setTypeface(ZTypeface.UbuntuR(context));
        tvWeek2.setTypeface(ZTypeface.UbuntuR(context));
        etSearch.setTypeface(ZTypeface.UbuntuR(context));
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ListView list = (ListView) findViewById(R.id.list);

                if (oList != null) {
                    if (s == null || s.length() < 3) {
                        OrderListAdapter oAdaper = new OrderListAdapter(context, oList, type);

                        list.setAdapter(oAdaper);
                    } else {
                        ArrayList<OrderVo> temp = new ArrayList<>();
                        for (OrderVo vo : oList) {

//							Log.e("search", vo)
                            if (vo.getName().toLowerCase().contains(s.toString().toLowerCase()) || vo.getId().contains(s.toString()) || vo.getAddress().contains(s.toString()) || vo.getPhone_number().contains(s.toString())) {
                                temp.add(vo);
                            }
                        }

                        OrderListAdapter oAdaper = new OrderListAdapter(context, temp, type);
                        list.setAdapter(oAdaper);

                    }
                }
            }
        });
        tvStatus.setOnClickListener(this);
        tvStatusTitle.setOnClickListener(this);
        tvWeek0.setOnClickListener(this);
        tvWeek1.setOnClickListener(this);
        tvWeek2.setOnClickListener(this);

    }

    public void setOrderList(TResponse<String> result) {
        TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
        ListView list = (ListView) findViewById(R.id.list);
        TextView tvWeek0 = (TextView) findViewById(R.id.tvWeek0);
        TextView tvWeek1 = (TextView) findViewById(R.id.tvWeek1);
        TextView tvWeek2 = (TextView) findViewById(R.id.tvWeek2);


        if (result == null) {
            Toast.makeText(context, " please check network connection", Toast.LENGTH_SHORT).show();
        } else if (result.isHasError()) {
            Toast.makeText(context, " please try later", Toast.LENGTH_SHORT).show();

        } else if (result.getResponseContent() != null) {

            try {
                //  oList = Parser.getOrders(result.getResponseContent());

                String user = Utils.getSharedPreference(context, "user");

                ArrayList<OrderVo> temp = Parser.getOrders(result.getResponseContent());
                if (user == null || user.equalsIgnoreCase("0") || allOrders) {
                    oList = temp;
                } else {
                    oList = new ArrayList<>();
                    for (OrderVo vo : temp) {
                        if (vo.getExtras() != null && vo.getExtras().equalsIgnoreCase(user)) {
                            oList.add(vo);
                        }
                    }
                }

                Collections.reverse(oList);
                if (type == 0) {
                    tvStatus.setText("All");
                    tvWeek0.setSelected(false);
                    tvWeek1.setSelected(false);
                    tvWeek2.setSelected(false);

                }
                if (oList != null) {
                    OrderListAdapter oAdaper = new OrderListAdapter(context, oList, type);
                    list.setAdapter(oAdaper);
                    list.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> gView, View arg1,
                                                int postion, long arg3) {
                            // TODO Auto-generated method stub

                            OrderVo order = ((OrderListAdapter) gView.getAdapter()).getItembyPos(postion);
                            Intent intent = new Intent(context, OrderStatusActivity.class);
                            intent.putExtra("order", order);
                            startActivity(intent);

                        }
                    });
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public void showStatusDialog() {
        final String[] items = new String[]{"All", "Pending", "Ready to pickup", "Ready to wash", "Ready to deliver", "Payment collected", "Cancelled"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Order Status");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { //pick from camera
                TextView tvStatus = (TextView) findViewById(R.id.tvStatus);
                ListView list = (ListView) findViewById(R.id.list);

                type = item;
                if (item == 0) {
                    type = 0;
                    tvStatus.setText("All");
                } else if (item == 0) {

                } else if (item == 1) {
                    tvStatus.setText("pending");

                } else if (item == 2) {
                    tvStatus.setText("Ready to pickup");


                } else if (item == 3) {
                    tvStatus.setText("Ready to wash");

                } else if (item == 4) {
                    tvStatus.setText("Ready to deliver");

                } else if (item == 5) {
                    tvStatus.setText("Payment collected");

                } else {
                    tvStatus.setText("Cancelled");

                }

                showOrderList();

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showOrderList() {

        TextView tvWeek0 = (TextView) findViewById(R.id.tvWeek0);
        TextView tvWeek1 = (TextView) findViewById(R.id.tvWeek1);
        TextView tvWeek2 = (TextView) findViewById(R.id.tvWeek2);
        ListView list = (ListView) findViewById(R.id.list);

        if (type == 0) {
            tvWeek0.setSelected(false);
            tvWeek1.setSelected(false);
            tvWeek2.setSelected(false);

        }

        if (oList == null || oList.isEmpty()) {
            new GetOrderListTask(context).execute(new String());
        } else {
            OrderListAdapter oAdaper = new OrderListAdapter(context, oList, type);
            list.setAdapter(oAdaper);
            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> gView, View arg1,
                                        int postion, long arg3) {
                    // TODO Auto-generated method stub

                    OrderVo order = ((OrderListAdapter) gView.getAdapter()).getItembyPos(postion);
                    Intent intent = new Intent(context, OrderStatusActivity.class);
                    intent.putExtra("order", order);
                    startActivity(intent);

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        EditText etSearch = (EditText) findViewById(R.id.etSearch);
        etSearch.setText(null);
        super.onClick(v);

        switch (v.getId()) {
            case R.id.tvStatus:
                showStatusDialog();
                break;
            case R.id.tvStatusTitle:
                if (user == null || user.equalsIgnoreCase("0") | user.equalsIgnoreCase(Utils.NAME_1) || user.equalsIgnoreCase(Utils.NAME_2) || user.equalsIgnoreCase(Utils.NAME_3)) {
                    showOrderDialog();
                } else {
                    showStatusDialog();
                }

                break;
            case R.id.tvWeek0:
                break;
            case R.id.tvWeek1:
                break;
            case R.id.tvWeek2:
                break;

            default:
                break;
        }
    }


    public void showOrderDialog() {
        final String[] items = new String[]{"All Orders", "My Orders"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Order Status");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { //pick from camera
                TextView tvStatus = (TextView) findViewById(R.id.tvStatusTitle);

                if (item == 0) {
                    tvStatus.setText("All Orders");
                    allOrders = true;
                } else if (item == 1) {
                    tvStatus.setText("My Orders");
                    allOrders = false;

                }
                new GetOrderListTask(context).execute(new String());


            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}
