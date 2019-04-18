package com.washnow.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.washnow.admin.adapter.ProductListAdapter;
import com.washnow.admin.async.GetProductsTask;
import com.washnow.admin.async.ProductsUpdateTask;
import com.washnow.admin.parser.Parser;
import com.washnow.admin.utils.DateUtil;
import com.washnow.admin.utils.Utils;
import com.washnow.admin.vo.OrderVo;
import com.washnow.admin.vo.ProductVo;
import com.washnow.admin.vo.TResponse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aswin on 23/02/17.
 */

public class InvoiceActivity extends BaseActivity implements View.OnClickListener {
    private ArrayList<ProductVo> pList;
    private ProductListAdapter pAdapter;
    private int total, totalItems;
    private OrderVo order;
    ArrayList<ProductVo> invoiceList;
    private String products;
    boolean listUpdate =false;
    boolean deliveryChargesAdded=false;


    // Debugging
    private static final String TAG = "Main_Activity";
    private static final boolean DEBUG = true;
    /******************************************************************************************************/
    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_CONNECTION_LOST = 6;
    public static final int MESSAGE_UNABLE_CONNECT = 7;
    /*******************************************************************************************************/
    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    /*******************************************************************************************************/
    private static final String CHINESE = "GBK";

    /*********************************************************************************/
    private Button btnScanButton = null;

    /******************************************************************************************************/
    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    private BluetoothService mService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_layout);
        actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_backnav);
        Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
        parent.setContentInsetsAbsolute(0, 0);
        setTitle("WASH NOW");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        order = (OrderVo) getIntent().getSerializableExtra("vo");
        new GetProductsTask(context).execute();


    }

    public void initLayout() {
        if (pList != null && !pList.isEmpty()) {

            ListView listView = (ListView) findViewById(R.id.listView);
            pAdapter = new ProductListAdapter(context, pList);
            listView.setAdapter(pAdapter);

        }

        findViewById(R.id.btConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pAdapter != null && pAdapter.getList() != null) {
                    pList = pAdapter.getList();
                    total = 0;
                    totalItems=0;
                    invoiceList = new ArrayList<ProductVo>();
                    for (ProductVo vo : pList) {
                        if (vo.getDryCount() > 0) {
                            ProductVo product = new ProductVo();
                            product.setId(vo.getId());
                            product.setName(vo.getName());
                            product.setType("dryclean");
                            product.setQuantity(vo.getDryCount());
                            invoiceList.add(product);
                            total = total + (int) (vo.getDryCount() * vo.getDryclean_sale_price());
                            totalItems = totalItems + vo.getDryCount();
                        }
                        if (vo.getWashCount() > 0) {
                            ProductVo product = new ProductVo();
                            product.setName(vo.getName());
                            product.setId(vo.getId());
                            product.setType("wash");
                            product.setQuantity(vo.getWashCount());
                            invoiceList.add(product);
                            total = total + (int) (vo.getWashCount() * vo.getWash_sale_price());
                            totalItems = totalItems + vo.getWashCount();
                        }
                        if (vo.getIronCount() > 0) {
                            ProductVo product = new ProductVo();
                            product.setName(vo.getName());
                            product.setId(vo.getId());
                            product.setType("ironing");
                            product.setQuantity(vo.getIronCount());
                            invoiceList.add(product);
                            total = total + (int) (vo.getIronCount() * vo.getIroning_sale_price());
                            totalItems = totalItems + vo.getIronCount();
                        }

                    }
                    try {
                         products = Utils.writeListToJsonArray(invoiceList);

                        Log.e("products " + total, products);
                        new ProductsUpdateTask(context).execute(new String[]{order.getId(), products});
                    } catch (Exception e) {
                        Log.e("wash now", e.toString());
                    }

                }
            }
        });
    }

    public void fetchProducts(TResponse<String> result) {


        if (result == null) {
            Toast.makeText(context, "unable to fetch products \n please check network connection", Toast.LENGTH_SHORT).show();
        } else if (result.isHasError()) {
            Toast.makeText(context, "unable to fetch products \n please try later", Toast.LENGTH_SHORT).show();

        } else if (result.getResponseContent() != null) {

            try {
                pList = Parser.getProducts(result.getResponseContent());
                initLayout();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public void updateOrder(TResponse<String> result) {


        if (result == null) {
            Toast.makeText(context, "unable to update order status \n please check network connection", Toast.LENGTH_SHORT).show();
        } else if (result.isHasError()) {
            Toast.makeText(context, "unable to update order status \n please try later", Toast.LENGTH_SHORT).show();

        } else if (result.getResponseContent() != null) {

            try {
                if (result.getResponseContent().toLowerCase().contains("success")) {
                    listUpdate=true;
                    showPrintOption();
                }else {
                    Toast.makeText(context,result.getResponseContent(),Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    public void showPrintOption() {
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            finish();
        } else {
            findViewById(R.id.llPrintOption).setVisibility(View.VISIBLE);
            findViewById(R.id.btConfirm).setVisibility(View.INVISIBLE);
            findViewById(R.id.button_scan).setVisibility(View.VISIBLE);
        }


    }


    /********
     * Bluetooth Printer
     ******/

    @Override
    public void onStart() {
        super.onStart();

        // If Bluetooth is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        } else {
            if (mService == null)
                KeyListenerInit();//监听
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();

        if (mService != null) {

            if (mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                mService.start();
            }
        }
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (DEBUG)
            Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (DEBUG)
            Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth services
        if (mService != null)
            mService.stop();
        if (DEBUG)
            Log.e(TAG, "--- ON DESTROY ---");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.button_scan: {
                Intent serverIntent = new Intent(InvoiceActivity.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                break;
            }
            case R.id.btn_close: {
                mService.stop();
                //  sendButton.setEnabled(false);
                findViewById(R.id.btCustomer).setEnabled(false);
                findViewById(R.id.btLaundry).setEnabled(false);
                //  btnClose.setEnabled(false);
                btnScanButton.setEnabled(true);
                btnScanButton.setText(getText(R.string.connect));
                break;
            }

            case R.id.btLaundry: {

                printStaffInvoice();
                break;
            }
            case R.id.btCustomer: {
              /*  String msg = "customer print";
                if (msg.length() > 0) {
                    SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 0, 0, 0));
                    SendDataByte(Command.LF);
                } else {
                    Toast.makeText(InvoiceActivity.this, getText(R.string.empty), Toast.LENGTH_SHORT).show();
                }*/
                printCustomerInvoice();
                break;
            }

        }
    }

    private void SendDataString(String data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        if (data.length() > 0) {
            try {
                mService.write(data.getBytes("GBK"));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
     *SendDataByte
     */
    private void SendDataByte(byte[] data) {

        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mService.write(data);
    }

    /****************************************************************************************************/
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (DEBUG)
                        Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:

                            btnScanButton.setText(getText(R.string.Connecting));
                            btnScanButton.setEnabled(false);
                            //  sendButton.setEnabled(true);
                            findViewById(R.id.btCustomer).setEnabled(true);
                            findViewById(R.id.btLaundry).setEnabled(true);
                            //  btnClose.setEnabled(true);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            //   mTitle.setText(R.string.title_connecting);
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            //mTitle.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:

                    break;
                case MESSAGE_READ:

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(),
                            "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(),
                            msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case MESSAGE_CONNECTION_LOST:    //蓝牙已断开连接
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    //   editText.setEnabled(false);
                    // sendButton.setEnabled(false);
                    findViewById(R.id.btCustomer).setEnabled(false);
                    findViewById(R.id.btLaundry).setEnabled(false);
                    //     btnClose.setEnabled(false);
                    break;
                case MESSAGE_UNABLE_CONNECT:     //无法连接设备
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (DEBUG)
            Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE: {
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    if (BluetoothAdapter.checkBluetoothAddress(address)) {
                        BluetoothDevice device = mBluetoothAdapter
                                .getRemoteDevice(address);
                        // Attempt to connect to the device
                        mService.connect(device);
                    }
                }
                break;
            }
            case REQUEST_ENABLE_BT: {
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a session
                    KeyListenerInit();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }

        }
    }

    private void KeyListenerInit() {

        findViewById(R.id.btLaundry).setOnClickListener(this);
        findViewById(R.id.btCustomer).setOnClickListener(this);


        btnScanButton = (Button) findViewById(R.id.button_scan);
        btnScanButton.setOnClickListener(this);


        //btnClose = (Button)findViewById(R.id.btn_close);
        //  btnClose.setOnClickListener(this);
        findViewById(R.id.btLaundry).setEnabled(false);
        findViewById(R.id.btCustomer).setEnabled(false);
        //  btnClose.setEnabled(false);
        mService = new BluetoothService(this, mHandler);
    }

    public void printCustomerInvoice() {
        String title = addspaceleft(11,"WASHNOW")+"\n";
        String website = addspaceleft(21,"washnow.me");
        String disclaimer_1 ="Kindly make sure your clothes are received in a good condition upon delivery. ";
        String disclaimer_2 =         "Washnow does not accept any claims after two days of delivery.";
        String disclaimer_3 = "The maximum compensation for any genuine complains or lost items will be up to 10 times of the laundry charges. ";

        SendDataByte(PrinterCommand.POS_Print_Text(" ", CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(title, CHINESE, 0, 1, 1, 0));
        String orderString = "Invoice No: " + addspaceleft(18,  order.getId());
        String date = DateUtil.changeDateFormat(order.getPick_up_date(),DateUtil.DATETIME_SQL,DateUtil.MONTH_PATTERN);
        date = "Date: " + addspaceleft(24, date);
        String time = DateUtil.dateToString(new Date(),DateUtil.TIME_12HRS_SHORT);
        time = "Collection Time: " + addspaceleft(13, time);
        String customer ="Customer: " +addspaceleft(20,order.getName());
        SendDataByte(PrinterCommand.POS_Print_Text(website, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(customer, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(date, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(time, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(orderString, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.star, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        String table = addspaceRight(20,"ITEM")+addspaceleft(5,"TYPE")+addspaceleft(5,"QTY");
        SendDataByte(PrinterCommand.POS_Print_Text(table, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.star, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        for(ProductVo vo : invoiceList) {
            if(vo.getName().length()>20){
                SendDataByte(PrinterCommand.POS_Print_Text(vo.getName().substring(0,19)+"-", CHINESE, 0, 0, 0, 0));
                SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
                String content = addspaceRight(20,vo.getName().substring(19,vo.getName().length()));
                content = content + Utils.getType(vo) +addspaceleft(5,String.valueOf(vo.getQuantity()));
                SendDataByte(PrinterCommand.POS_Print_Text(content, CHINESE, 0, 0, 0, 0));
                SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));

            }else {
                String content = addspaceRight(20,vo.getName());
                content = content + Utils.getType(vo) + addspaceleft(5,String.valueOf(vo.getQuantity()));
                SendDataByte(PrinterCommand.POS_Print_Text(content, CHINESE, 0, 0, 0, 0));
                SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
            }
        }
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.star, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));

        String totalCount = "Total Items: " + addspaceleft(17,String.valueOf(totalItems));
        String deliveryCharge;
        if(total>99) {
            deliveryCharge = "Delivery Charge: " + addspaceleft(13,"QAR"+String.valueOf(0));

        }else {
            deliveryCharge = "Delivery Charge: " + addspaceleft(13,"QAR"+String.valueOf(5));
            if(!deliveryChargesAdded)
             total =total+5;

            deliveryChargesAdded=true;

        }
        String laudryCharge;
        if(deliveryChargesAdded) {
             laudryCharge = "laundry Charge: " + addspaceleft(14,"QAR"+String.valueOf((total-5)));
        }else {
            laudryCharge = "laundry Charge: " + addspaceleft(14
                    ,"QAR"+String.valueOf(total));


        }
        String totalCost = "Total Charge: " + addspaceleft(16,"QAR"+String.valueOf(total));
        String contact = "Email: " + addspaceleft(23,"info@washnow.me");

        SendDataByte(PrinterCommand.POS_Print_Text(totalCount, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(laudryCharge, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(deliveryCharge, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(totalCost, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.star, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(contact, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text("Disclaimer", CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(disclaimer_1, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(disclaimer_2, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(disclaimer_3, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));

        SendDataByte(PrinterCommand.POS_Set_UnderLine(1));


        SendDataByte(PrinterCommand.POS_Set_Cut(1));
        SendDataByte(PrinterCommand.POS_Set_PrtInit());

    }

    public void printStaffInvoice() {
        String title = addspaceleft(11,"WASHNOW")+"\n\n";
        SendDataByte(PrinterCommand.POS_Print_Text(title, CHINESE, 0, 1, 1, 0));
        String key= Utils.getRandomLetters()+Utils.getRandomLetters();
        key=key+ DateUtil.changeDateFormat(order.getPick_up_date(),DateUtil.DATETIME_SQL,DateUtil.MONTH_PATTERN_SHORT_NO)+ Utils.getRandomLetters()+String.valueOf(total)+Utils.getRandomLetters()+totalItems;
        String orderString = "Invoice No: " + addspaceleft(18, order.getId());
        String date = DateUtil.changeDateFormat(order.getPick_up_date(),DateUtil.DATETIME_SQL,DateUtil.MONTH_PATTERN);
        date = "Date: " + addspaceleft(24, date);
        String time = DateUtil.dateToString(new Date(),DateUtil.DATE_MESSAGE);
        time = "Collection Time: " + addspaceleft(13, time);
        String customer ="Customer: " +addspaceleft(20,order.getName());
        SendDataByte(PrinterCommand.POS_Print_Text(customer, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(date, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(time, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(key, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(orderString, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.star, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        String table = addspaceRight(20,"ITEM")+addspaceleft(5,"TYPE")+addspaceleft(5,"QTY");
        SendDataByte(PrinterCommand.POS_Print_Text(table, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.star, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        for(ProductVo vo : invoiceList) {
            if(vo.getName().length()>20){
                SendDataByte(PrinterCommand.POS_Print_Text(vo.getName().substring(0,19)+"-", CHINESE, 0, 0, 0, 0));
                SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
                String content = addspaceRight(20,vo.getName().substring(19,vo.getName().length()));
                content = content + Utils.getType(vo) +addspaceleft(5,String.valueOf(vo.getQuantity()));
                SendDataByte(PrinterCommand.POS_Print_Text(content, CHINESE, 0, 0, 0, 0));
                SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));

            }else {
                String content = addspaceRight(20,vo.getName());
                content = content + Utils.getType(vo) + addspaceleft(5,String.valueOf(vo.getQuantity()));
                SendDataByte(PrinterCommand.POS_Print_Text(content, CHINESE, 0, 0, 0, 0));
                SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
            }
        }
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.star, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));

        String totalCount = "Total Items: " + addspaceleft(17,String.valueOf(totalItems));
      //  String totalCost = "Total Cost: " + addspaceleft(18,"QAR"+String.valueOf(total));

        SendDataByte(PrinterCommand.POS_Print_Text(totalCount, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
      //  SendDataByte(PrinterCommand.POS_Print_Text(totalCost, CHINESE, 0, 0, 0, 0));
        //SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.star, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));
        SendDataByte(PrinterCommand.POS_Print_Text(PrinterCommand.linefeed, CHINESE, 0, 0, 0, 0));

        SendDataByte(PrinterCommand.POS_Set_UnderLine(1));


        SendDataByte(PrinterCommand.POS_Set_Cut(1));
        SendDataByte(PrinterCommand.POS_Set_PrtInit());

    }



    String addspaceleft(int pos, String value) {

        int len = value.length();
        len = pos - len;
        if (len > 0)
            for (int i = 0; i < len; i++) {
                value = " " + value;
            }
        return value;
    }

    ;

    String addspaceRight(int pos, String value) {
        int len = value.length();
        len = pos - len;
        if (len > 0)
            for (int i = 0; i < len; i++) {
                value = value + " ";
                // Log.i("temp 0", ""+temp[0]);

            }
        return value;
    }

    @Override
    public void finish() {
        if(listUpdate){

            Intent intent=new Intent();
            intent.putExtra("cost",total);
            intent.putExtra("products" , products);
            setResult(2,intent);
        }

        super.finish();
    }
}
