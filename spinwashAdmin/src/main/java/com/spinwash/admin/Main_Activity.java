package com.spinwash.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class Main_Activity extends Activity implements OnClickListener{
/******************************************************************************************************/
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
	private TextView mTitle;
	EditText editText;
	private RadioButton width_58mm;
	private Button sendButton = null;
	private Button btnScanButton = null;
	private Button btnClose = null;

/******************************************************************************************************/
	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the services
	private BluetoothService mService = null;
	
/***************************   指                 令****************************************************************/

/******************************************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DEBUG)
			Log.e(TAG, "+++ ON CREATE +++");

		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		// Set up the custom title
		mTitle = (TextView) findViewById(R.id.title_left_text);
		mTitle.setText(R.string.app_name);
		mTitle = (TextView) findViewById(R.id.title_right_text);

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
			finish();
		}
	}

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

/*****************************************************************************************************/	
	private void KeyListenerInit() {
		
		editText = (EditText) findViewById(R.id.edit_text_out);

		sendButton = (Button) findViewById(R.id.Send_Button);
		sendButton.setOnClickListener(this);
		

		btnScanButton = (Button)findViewById(R.id.button_scan);
		btnScanButton.setOnClickListener(this);


		width_58mm = (RadioButton)findViewById(R.id.width_58mm);
		width_58mm.setOnClickListener(this);
		

		btnClose = (Button)findViewById(R.id.btn_close);
		btnClose.setOnClickListener(this);
		sendButton.setEnabled(false);
		btnClose.setEnabled(false);
		mService = new BluetoothService(this, mHandler);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_scan:{
			Intent serverIntent = new Intent(Main_Activity.this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
			break;
		}
		case R.id.btn_close:{
			mService.stop();
			editText.setEnabled(false);
			width_58mm.setEnabled(false);
			sendButton.setEnabled(false);
			btnClose.setEnabled(false);
			btnScanButton.setEnabled(true);
			btnScanButton.setText(getText(R.string.connect));
			break;
		}


		case R.id.Send_Button:{
			String msg = editText.getText().toString();
			if(msg.length()>0){
				SendDataByte(PrinterCommand.POS_Print_Text(msg, CHINESE, 0, 0, 0, 0));
				SendDataByte(Command.LF);
			}else{
				Toast.makeText(Main_Activity.this, getText(R.string.empty), Toast.LENGTH_SHORT).show();
			}
			break;
		}

		}
	}
		
/*****************************************************************************************************/
	/*
	 * SendDataString
	 */
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
					mTitle.setText(R.string.title_connected_to);
					mTitle.append(mConnectedDeviceName);
					btnScanButton.setText(getText(R.string.Connecting));
 					btnScanButton.setEnabled(false);
					editText.setEnabled(true);
					width_58mm.setEnabled(true);
					sendButton.setEnabled(true);
					btnClose.setEnabled(true);
					break;
				case BluetoothService.STATE_CONNECTING:
					mTitle.setText(R.string.title_connecting);
					break;
				case BluetoothService.STATE_LISTEN:
				case BluetoothService.STATE_NONE:
					mTitle.setText(R.string.title_not_connected);
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
                editText.setEnabled(false);
				width_58mm.setEnabled(false);
				sendButton.setEnabled(false);
				btnClose.setEnabled(false);
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
		case REQUEST_CONNECT_DEVICE:{
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
			case REQUEST_ENABLE_BT:{
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

/****************************************************************************************************/
	/**
	 * 连接成功后打印测试页
	 */

	/**
	 * 打印测试页
	 * @param mPrinter
	 */

	
	/*
	 * 打印图片
	 */



	/**
	 * 打印自定义表格
	 */

}