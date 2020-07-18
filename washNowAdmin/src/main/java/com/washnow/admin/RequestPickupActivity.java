package com.washnow.admin;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.washnow.admin.async.OrderCreateTask;
import com.washnow.admin.loader.ImageLoader;
import com.washnow.admin.parser.Parser;
import com.washnow.admin.utils.DateUtil;
import com.washnow.admin.utils.Utils;
import com.washnow.admin.utils.ZTypeface;
import com.washnow.admin.vo.OrderVo;
import com.washnow.admin.vo.PickupSlot;
import com.washnow.admin.vo.TResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RequestPickupActivity extends BaseBackActivity implements OnClickListener{

	private int activeIM, activeETDate,activeETTime;
	private Calendar startDate, endDate;
	private DatePickerDialog dialog = null;
	private static final int PICK_FROM_LOCATION = 4;
	public   String latitude,longitude,address="";
	private ImageLoader mLoader;
	private   OrderVo order ;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.requestpu_layout);
		context=this;
		
		mLoader = new ImageLoader(context);
		setTitle("NEW ORDER");
		initLayout();
		
		findViewById(R.id.im_bck).setVisibility(View.VISIBLE);
		 findViewById(R.id.ll_logo).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                 onBackPressed();
	            }
	        });
 	}
	
	public void initLayout(){
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
		TextView tvPickUp = (TextView) findViewById(R.id.tvPickUp);
		TextView tvAddress = (TextView) findViewById(R.id.tvAddress);
		TextView tvDelivery = (TextView) findViewById(R.id.tvDelivery);
		TextView tvMap = (TextView) findViewById(R.id.tvMap);
		TextView tvNotes = (TextView) findViewById(R.id.tvNotes);
		
		EditText etName = (EditText) findViewById(R.id.etName);
		EditText etEmail = (EditText) findViewById(R.id.etEmail);
		EditText etStartDate = (EditText) findViewById(R.id.etStartDate);
		EditText etStartTime = (EditText) findViewById(R.id.etStartTime);
		EditText etEndDate = (EditText) findViewById(R.id.etEndDate);
		EditText etEndTime = (EditText) findViewById(R.id.etEndTime);
		EditText etPhone = (EditText) findViewById(R.id.etPhone);
		Button btSubmit = (Button) findViewById(R.id.btSubmit);
		RelativeLayout rlStartDate = (RelativeLayout) findViewById(R.id.rlStartDate);
		RelativeLayout rlStartTime = (RelativeLayout) findViewById(R.id.rlStartTime);
		RelativeLayout rlEndDate = (RelativeLayout) findViewById(R.id.rlEndDate);
		RelativeLayout rlEndTime = (RelativeLayout) findViewById(R.id.rlEndTime);
			
		tvName.setTypeface(ZTypeface.UbuntuR(this));
		tvEmail.setTypeface(ZTypeface.UbuntuR(this));
		tvAddress.setTypeface(ZTypeface.UbuntuR(this));
		tvPickUp.setTypeface(ZTypeface.UbuntuR(this));
		tvDelivery.setTypeface(ZTypeface.UbuntuR(this));
		btSubmit.setTypeface(ZTypeface.UbuntuR(this));
		tvMap.setTypeface(ZTypeface.UbuntuR(this));
		tvNotes.setTypeface(ZTypeface.UbuntuR(this));
		
		etName.setTypeface(ZTypeface.UbuntuR(this));
		etEmail.setTypeface(ZTypeface.UbuntuR(this));
		etStartDate.setTypeface(ZTypeface.UbuntuR(this));
		etStartTime.setTypeface(ZTypeface.UbuntuR(this));
		etEndDate.setTypeface(ZTypeface.UbuntuR(this));
		etEndTime.setTypeface(ZTypeface.UbuntuR(this));
		startDate = Calendar.getInstance();
		endDate = Calendar.getInstance();
		
		setDefaultDate();
		rlStartDate.setOnClickListener(this);
		rlStartTime.setOnClickListener(this);
		rlEndDate.setOnClickListener(this);
		rlEndTime.setOnClickListener(this);
		etStartDate.setOnClickListener(this);
		etStartTime.setOnClickListener(this);
		etEndDate.setOnClickListener(this);
		etEndTime.setOnClickListener(this);
		tvMap.setOnClickListener(this);
		btSubmit.setOnClickListener(this);
		latitude=null;
		longitude=null;
	 
		
		
	}

	@Override
	public void onClick(View v) {
		 Intent intent;
		switch (v.getId()) {
		case R.id.etStartDate:
			activeETDate = R.id.etStartDate;
			showDateDailog("Select Pickup Date");
			break;
		case R.id.etEndDate:
			activeETDate = R.id.etEndDate;
			showDateDailog("Select Delivery Date");
			break;
		case R.id.rlStartDate:
			activeETDate = R.id.etStartDate;
			showDateDailog("Select Pickup Date");
			break;
		case R.id.rlEndDate:
			activeETDate = R.id.etEndDate;
			showDateDailog("Select Delivery Date");
			break;
		case R.id.rlStartTime:
			activeETTime = R.id.etStartTime;
			//showTimeDialog("Select Pickup Time");
			showPickupMenu(findViewById(R.id.etEndTime));
			break;
		case R.id.rlEndTime:
			//activeETTime = R.id.etEndTime;
		//	showTimeDialog("Select Delivery Time");
			showDeliveryMenu(findViewById(R.id.etEndTime));
			break;
		case R.id.etStartTime:
			activeETTime = R.id.etStartTime;
			//showTimeDialog("Select Pickup Time");
			showPickupMenu(v);
			break;
		case R.id.etEndTime:
			//activeETTime = R.id.etEndTime;
			//showTimeDialog("Select Delivery Time");
			showDeliveryMenu(v);
			break;
			
		case R.id.tvMap:
			 intent = new Intent(this, MapActivity.class);
			 intent.putExtra("return-data", true);
			 startActivityForResult(intent, PICK_FROM_LOCATION);
			
			break;
		case R.id.imMap:
			 intent = new Intent(this, MapActivity.class);
			 intent.putExtra("return-data", true);
			 startActivityForResult(intent, PICK_FROM_LOCATION);
			break;
			
		case R.id.btSubmit:
			createOrder();
			break;
		default:
			break;
		}
	}

	
	
	public void showDateDailog(String title){
		Calendar dtTxt = null;
		int year, month,day;
		String preExistingDate;
		if(activeETDate==R.id.etStartDate) {
			EditText etDOB = (EditText) findViewById(R.id.etStartDate);
			preExistingDate  = (String) etDOB.getText().toString();
			  year = startDate.get(Calendar.YEAR);
			  month = startDate.get(Calendar.MONTH); // Note: zero based!
			  day = startDate.get(Calendar.DAY_OF_MONTH);

		}else {
			EditText etDOB = (EditText) findViewById(R.id.etEndDate);
			preExistingDate  = (String) etDOB.getText().toString();
			  year = endDate.get(Calendar.YEAR);
			  month = endDate.get(Calendar.MONTH); // Note: zero based!
			  day = endDate.get(Calendar.DAY_OF_MONTH);

		}
	
	    
	    if(preExistingDate != null && !preExistingDate.equals("")){
	            
	            dialog = new DatePickerDialog(context,
	                             new PickDate(),year,
	                            month,
	                             day);
	             
	            
	    } else {
	    	 final Calendar c = Calendar.getInstance();
	         year  = c.get(Calendar.YEAR);
	         month = c.get(Calendar.MONTH);
	         day   = c.get(Calendar.DAY_OF_MONTH);
	  
	        
	        	  dialog = new DatePickerDialog(context,
	                      new PickDate(),year,
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
	
	public void showTimeDialog(String title){
		final EditText etStartTime = (EditText) findViewById(R.id.etStartTime);
    	final EditText etEndTime = (EditText) findViewById(R.id.etEndTime);
    	 Calendar mcurrentTime = null; ;
    	if(activeETTime== R.id.etStartTime){
    		mcurrentTime=startDate;
    	}else if(activeETTime== R.id.etEndTime) {
    		mcurrentTime=endDate;
            
    	}  
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
             TimePickerDialog mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                	String time = selectedHour + ":" + selectedMinute;
                	if(activeETTime== R.id.etStartTime){
                		
                		if(selectedHour>7&&selectedHour<22){
                			 startDate.set(Calendar.HOUR_OF_DAY, selectedHour);
                   		  startDate.set(Calendar.MINUTE, selectedMinute);
                   		  startDate.set(Calendar.SECOND, 0);
                 		
                   		etStartTime.setText(DateUtil.changeDateFormat(time, DateUtil.TIME_24HRS_SHORT, DateUtil.TIME_12HRS_SHORT) );
                   		
                		}else {
                		  	Toast.makeText(context, "Please choose Pick up time between 8AM to 10PM", Toast.LENGTH_SHORT).show();
           			     
                		}
                	}else if(activeETTime== R.id.etEndTime) {
                		if(selectedHour>17&&selectedHour<22){
                			etEndTime.setText(DateUtil.changeDateFormat(time, DateUtil.TIME_24HRS_SHORT, DateUtil.TIME_12HRS_SHORT) );
        	                	
                		}else {
                			Toast.makeText(context, "Please choose Delivery time between 6PM to 10PM", Toast.LENGTH_SHORT).show();
              			     
                		}
                		
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
	
	private class PickDate implements DatePickerDialog.OnDateSetListener {

	    @Override
	    public void onDateSet(DatePicker view, int year, int monthOfYear,
	            int dayOfMonth) {
	    	EditText etStartDate = (EditText) findViewById(R.id.etStartDate);
	    	EditText etEndDate = (EditText) findViewById(R.id.etEndDate);
	    	 view.updateDate(year, monthOfYear, dayOfMonth);
	        if(activeETDate==R.id.etStartDate){
	        	
	        	 startDate.set(year, monthOfYear, dayOfMonth);
			        Date date = startDate.getTime();
			        int diff = DateUtil.daysDiff(new Date(), date);
			        if(diff<0){
			        	setDefaultDate();
			        	Toast.makeText(context, "Please choose a day within next 7 days", Toast.LENGTH_SHORT).show();
			
			        }else if(diff==0){
			        	  etStartDate.setText("Today");
			        }else if(diff==1){
			        	  etStartDate.setText("Tomorrow");
			        }else if(diff<7){
			        	  etStartDate.setText(DateUtil.dateToString(date, DateUtil.DATE_NAME_PATTERN));
			        }else {
			        	setDefaultDate();
			        	Toast.makeText(context, "Please choose a day within next 7 days", Toast.LENGTH_SHORT).show();
			        }
			        
			        
			      
				          
	        }else {
	        	 endDate.set(year, monthOfYear, dayOfMonth);
	        	 
			        Date date = endDate.getTime();
			        Date pDate = startDate.getTime();
			        int diff = DateUtil.daysDiff(pDate, date);
			        if(diff<2){
			        	setDefaultDate();
			        	Toast.makeText(context, "Please choose a day after 48 Hrs and next 9 days of pickup", Toast.LENGTH_SHORT).show();
			     
			        }else if(diff==0){
			        	setDefaultDate();
			        	Toast.makeText(context, "Please choose a day within next 7 days of pickup", Toast.LENGTH_SHORT).show();
			         
			        }else if(diff==1){
			        	if(DateUtil.daysDiff(new Date(), date)==1)
			        		etEndDate.setText("Tomorrow");
			        	else {
			        		etEndDate.setText(DateUtil.dateToString(date, DateUtil.DATE_NAME_PATTERN));
			        	}
			        }else if(diff<8){
			        	etEndDate.setText(DateUtil.dateToString(date, DateUtil.DATE_NAME_PATTERN));
			        }else {
			        	setDefaultDate();
			        	Toast.makeText(context, "Please choose a day within next 7 days of pickup", Toast.LENGTH_SHORT).show();
			        }
			        
				 
	        }
	        dialog.hide();
	    }
	    
	}
	
	  @Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
					
			    if (resultCode != RESULT_OK) return;
			   
			    switch (requestCode) {
			    case PICK_FROM_LOCATION:
			    	
			    	EditText etAddress = (EditText) findViewById(R.id.etAddress);
					EditText etApt = (EditText) findViewById(R.id.etApt);
					EditText etStreet = (EditText) findViewById(R.id.etStreet);
			    	if(data.hasExtra("address")){
			    		  address =data.getStringExtra("address");
			    		
			    	}
			    	ImageView imMap = (ImageView) findViewById(R.id.imMap);
					
			    	 latitude=data.getStringExtra("latitude");
		    		 longitude=data.getStringExtra("longitude");
			 		String url = Utils.STATICMAP_1+latitude+","+longitude+Utils.STATICMAP_2+latitude+","+longitude;
			 		imMap.setVisibility(View.VISIBLE);
			    	Log.v("map url", url);
			 		mLoader.DisplayImage(url, imMap, false);
			    	imMap.setOnClickListener(this); 
			    	etAddress.setVisibility(View.INVISIBLE);
			    	etApt.setVisibility(View.INVISIBLE);
			    	etStreet.setVisibility(View.INVISIBLE);
				 		break;

			    }
			    
	  }
	  
	  public void setDefaultDate(){
		  Date date  = new Date();
		  EditText etStartDate = (EditText) findViewById(R.id.etStartDate);
			EditText etStartTime = (EditText) findViewById(R.id.etStartTime);
			EditText etEndDate = (EditText) findViewById(R.id.etEndDate);
			EditText etEndTime = (EditText) findViewById(R.id.etEndTime);
		
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.DAY_OF_MONTH); 
			
			int month =  calendar.get(Calendar.MONTH)+1; 
			int year = calendar.get(Calendar.YEAR); 
			int hrs =  calendar.get(Calendar.HOUR_OF_DAY); 
			if(hrs>=15){
				calendar.add(Calendar.DATE, 1);

				  day = calendar.get(Calendar.DAY_OF_MONTH); 
				  month =  calendar.get(Calendar.MONTH)+1; 
				  year = calendar.get(Calendar.YEAR); 
				
			}
			
			//etStartDate.setText(day+"/"+month+"/"+year);
			etStartTime.setText("6 PM - 7 PM");
			
			 calendar.add(Calendar.DATE, 2);
			
			  day = calendar.get(Calendar.DAY_OF_MONTH); 
			  month =  calendar.get(Calendar.MONTH)+1; 
			  year = calendar.get(Calendar.YEAR); 
			 // etEndDate.setText(day+"/"+month+"/"+year);
			  etEndTime.setText("6 PM - 7PM");
			  calendar.set(Calendar.HOUR_OF_DAY, 18);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				
			  endDate.setTime(calendar.getTime());
			  
			   calendar.add(Calendar.DATE, -2);
			  startDate.setTime(calendar.getTime());
			
			  int diff = DateUtil.daysDiff(new Date(), startDate.getTime());
		        if(diff==0){
		        	etStartDate.setText("Today");
		         
		        }else if(diff==1){
		        	etStartDate.setText("Tomorrow");
		        	
		        } 
		        etEndDate.setText(DateUtil.dateToString(endDate.getTime(), DateUtil.DATE_NAME_PATTERN));
				   
			  
	  }
	  
	  public void createOrder(){
		    order = new OrderVo();
		    EditText etName = (EditText) findViewById(R.id.etName);
		    EditText etEmail = (EditText) findViewById(R.id.etEmail);
		    EditText etAddress = (EditText) findViewById(R.id.etAddress);
			EditText etApt = (EditText) findViewById(R.id.etApt);
			EditText etStreet = (EditText) findViewById(R.id.etStreet);
			EditText etPhone = (EditText) findViewById(R.id.etPhone);
			EditText etNote = (EditText) findViewById(R.id.etNote);
			String email = Utils.getText(etEmail);
			String address = Utils.getText(etAddress);
			String phone = Utils.getText(etPhone);
			String note = Utils.getText(etNote);
			if(Utils.getText(etApt)!=null)
				address = address+"\n"+ Utils.getText(etApt);
			if(Utils.getText(etStreet)!=null)
				address = address+"\n"+ Utils.getText(etStreet);
			
			
			if(latitude!=null){
				address=this.address;
				
			}else if(latitude==null&&address==null){
				Toast.makeText(context, "Please enter address or pick location", Toast.LENGTH_SHORT).show();
				return;
			}else if(address!=null&&address.length()<5){
				Toast.makeText(context, "Please enter address or pick location", Toast.LENGTH_SHORT).show();
				return;
			}else {
				/*if(Utils.getText(etApt)!=null)
					address = address+"\n"+ Utils.getText(etApt);
				if(Utils.getText(etStreet)!=null)
					address = address+"\n"+ Utils.getText(etStreet);
				*/
			}
			String name = Utils.getText(etName);
			
			if(name==null||name.length()<5){
				Toast.makeText(context, "Please enter valid name", Toast.LENGTH_SHORT).show();
				return;
			}
			if(email==null||email.length()<5){
				Toast.makeText(context, "Please enter valid email", Toast.LENGTH_SHORT).show();
				return;
			}
			if(phone==null||phone.length()<5){
				Toast.makeText(context, "Please enter valid contact number", Toast.LENGTH_SHORT).show();
				return;
			}
			Utils.setSharedPreference(context, "phone", phone);
				
				
				
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add( new BasicNameValuePair("name",name) ); 
				order.setName(name);
				params.add( new BasicNameValuePair("email",email) ); 
				params.add( new BasicNameValuePair("address",address) );
				order.setAddress(address);
				params.add( new BasicNameValuePair("phone_number",Utils.getText(etPhone)) ); 
				order.setPhone_number(Utils.getText(etPhone));
				params.add( new BasicNameValuePair("pick_up_date",DateUtil.dateToString(startDate.getTime(), DateUtil.DATETIME_SQL)) );
				order.setPick_up_date(DateUtil.dateToString(startDate.getTime(), DateUtil.DATETIME_SQL));
				params.add( new BasicNameValuePair("delivery_date",DateUtil.dateToString(endDate.getTime(), DateUtil.DATETIME_SQL)) ); 
				order.setDelivery_date(DateUtil.dateToString(endDate.getTime(), DateUtil.DATETIME_SQL));
				if(latitude!=null){
					params.add( new BasicNameValuePair("longitude",longitude) ); 
					params.add( new BasicNameValuePair("latitude",latitude) );
					order.setLongitude(longitude);
					order.setLatitude(latitude);
					
				}
				if(Utils.getText(etNote)!=null) {
					params.add( new BasicNameValuePair("notes",Utils.getText(etNote)) ); 
					order.setNotes(Utils.getText(etNote));
				}
				
				
				params.add( new BasicNameValuePair("status","pending") ); 
				order.setStatus("pending");
		  params.add( new BasicNameValuePair("country",Utils.getSharedPreference(context,"country")) );
		  params.add( new BasicNameValuePair("process","addorder") );
				new OrderCreateTask(context, params).execute("");
				
		  
	  }
	  
	  public void createOrderStatus(TResponse<String> result){
		  
		  if(result!=null&& !result.isHasError()){
			  if(result.getResponseContent()!=null){
				  
				  try {
					order = Parser.getOrders(result.getResponseContent()).get(0);
					  /*Utils.setOrder(context, order);
					  Intent intent = new Intent(context, OrderStatusActivity.class);
					  intent.putExtra("order", order);
					  startActivity(intent);*/
					  finish();
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					  Toast.makeText(context, "Network Error, Try Again ", Toast.LENGTH_SHORT).show();
				}
				 
			  }
		  }else if(result!=null&& result.isHasError()) {
			  Toast.makeText(context, "Network Error, Try Again ", Toast.LENGTH_SHORT).show();
		  }else {
			  Toast.makeText(context, "Network Error, Try Again ", Toast.LENGTH_SHORT).show();
		  }
		  
			
	  }
	  
	 public void showDeliveryMenu(View v){
		 
		 PopupMenu popup = new PopupMenu(context, v);
		 MenuInflater inflater = popup.getMenuInflater();
		 inflater.inflate(R.menu.menu_deliverytime, popup.getMenu());
		 popup.setOnMenuItemClickListener(new  OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				EditText etEndTime = (EditText) findViewById(R.id.etEndTime);
				
				switch (item.getItemId()) {
				case R.id.slot_1:
					 endDate.set(Calendar.HOUR_OF_DAY, 18);
              		  endDate.set(Calendar.MINUTE, 0);
              		  endDate.set(Calendar.SECOND, 0);
              		etEndTime.setText("6PM - 7PM");
					break;
				case R.id.slot_2:
					 endDate.set(Calendar.HOUR_OF_DAY, 19);
             		  endDate.set(Calendar.MINUTE, 0);
             		  endDate.set(Calendar.SECOND, 0);
             		 etEndTime.setText("7PM - 8PM");
					break;
				case R.id.slot_3:
					 endDate.set(Calendar.HOUR_OF_DAY, 20);
             		  endDate.set(Calendar.MINUTE, 0);
             		  endDate.set(Calendar.SECOND, 0);
             		 etEndTime.setText("8PM - 9PM");
           		
					break;
				case R.id.slot_4:
					 endDate.set(Calendar.HOUR_OF_DAY, 21);
             		  endDate.set(Calendar.MINUTE, 0);
             		  endDate.set(Calendar.SECOND, 0);
             		 etEndTime.setText("9PM - 10PM");
           		
					break;
			 
				default:
					break;
				}
				return false;
			}
		});
		 popup.show();
	 }
 public void showPickupMenu(View v){
		 
		 PopupMenu popup = new PopupMenu(context, v);
		 
		 final ArrayList<PickupSlot> sList = Utils.getPickupSlot();
		 int diff = DateUtil.daysDiff(new Date(), startDate.getTime());
	     if(diff>0){
	    	 for(PickupSlot vo : sList){
	    		 popup.getMenu().add(vo.time);
	    	 }
	     }else {
	    	 Date d = new Date();
	    	 int hour = d.getHours()+2;
	    	 if(hour < 20) {
	    		 for(PickupSlot vo : sList){
	    			 if(vo.id>hour)
    	    		 popup.getMenu().add(vo.time);
    	    	 }
	    		 
	    	 }else {
	    		 Toast.makeText(context, "please choose next day for pick up " , Toast.LENGTH_SHORT).show();
	    	 }
	    			 
	     }
		 
		  popup.setOnMenuItemClickListener(new  OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				EditText etStartTime = (EditText) findViewById(R.id.etStartTime);
				for(PickupSlot vo : sList){
					if(vo.time.equalsIgnoreCase(item.getTitle().toString())){
						 startDate.set(Calendar.HOUR_OF_DAY, vo.id);
						 startDate.set(Calendar.MINUTE, 0);
						 startDate.set(Calendar.SECOND, 0);
	             		 etStartTime.setText(vo.time.replace(":00", ""));
	             		 
	           		
					}
				}
				
			 
				return false;
			}
		});
		 popup.show();
	 }
 
  
}

