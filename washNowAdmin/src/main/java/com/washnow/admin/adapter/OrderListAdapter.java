package com.washnow.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.washnow.admin.R;
import com.washnow.admin.utils.DateUtil;
import com.washnow.admin.utils.ZTypeface;
import com.washnow.admin.vo.OrderVo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class OrderListAdapter extends BaseAdapter{
	
	private Context context;
	private LayoutInflater inflator;
	private ArrayList<OrderVo> pList ;

	   
	public OrderListAdapter(Context context, ArrayList<OrderVo> pList ,int type) {
		super();
		this.context = context;
		this.inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
		this.pList = new ArrayList<OrderVo>();
		switch (type) {
		case 0:
			this.pList=pList;
			break;
		case 1:
			for(OrderVo order : pList){
				if(order.getStatus().equalsIgnoreCase("pending"))
					this.pList.add(order);
			}
		
			break;
		case 2:
			for(OrderVo order : pList){
				if(order.getStatus().equalsIgnoreCase("accepeted"))
					this.pList.add(order);
			}
			break;
		case 3:
			for(OrderVo order : pList){
				if(order.getStatus().equalsIgnoreCase("washing"))
					this.pList.add(order);
			}
			break;
		case 4:
			for(OrderVo order : pList){
				if(order.getStatus().equalsIgnoreCase("completed"))
					this.pList.add(order);
			}
			break;
		case 5:
			for(OrderVo order : pList){
				if(order.getStatus().equalsIgnoreCase("paid"))
					this.pList.add(order);
			}
			break;

		case 6:
			for(OrderVo order : pList){
				if(order.getStatus().equalsIgnoreCase("cancelled"))
					this.pList.add(order);
			}
			break;


		default:
			break;
		}
		
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public OrderVo getItembyPos(int pos) {
		// TODO Auto-generated method stub
		return pList.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int postion, View convertView, ViewGroup arg2) {

		ViewHolder holder;
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
	
	 	if(convertView!=null&&convertView.getTag()!= null) {
	 		holder = (ViewHolder) convertView.getTag();
		}else {
			
			holder = new ViewHolder();
			convertView = inflator.inflate(R.layout.order_item, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvContact = (TextView) convertView.findViewById(R.id.tvContact);
			holder.tvOrder = (TextView) convertView.findViewById(R.id.tvOrder);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
			holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
			
			holder. tvPickUp = (TextView) convertView.findViewById(R.id.tvPickUp);
			holder. tvPickUpDate = (TextView) convertView.findViewById(R.id.tvPickUpDate);
			holder. tvPickUpTime = (TextView) convertView.findViewById(R.id.tvPickUpTime);
			holder. tvDelivery = (TextView) convertView.findViewById(R.id.tvDelivery);
			holder. tvDeliveryDate = (TextView) convertView.findViewById(R.id.tvDeliveryDate);
			holder. tvDeliveryTime = (TextView) convertView.findViewById(R.id.tvDeliveryTime);
		
			
			holder.tvPickUp.setTypeface(ZTypeface.UbuntuR(context));
			holder.tvPickUpDate.setTypeface(ZTypeface.UbuntuR(context));
			holder.tvPickUpTime.setTypeface(ZTypeface.UbuntuR(context));
			holder.tvDelivery.setTypeface(ZTypeface.UbuntuR(context));
			holder.tvDeliveryDate.setTypeface(ZTypeface.UbuntuR(context));
			holder.tvDeliveryTime.setTypeface(ZTypeface.UbuntuR(context));
			
			 
			holder.tvName.setTypeface(ZTypeface.UbuntuR(context));
			holder.tvOrder.setTypeface(ZTypeface.UbuntuR(context));
			holder.tvContact.setTypeface(ZTypeface.UbuntuR(context));
			holder.tvStatus.setTypeface(ZTypeface.UbuntuR(context));
			holder.tvAddress.setTypeface(ZTypeface.UbuntuR(context));
			
			
			
		}
	 	OrderVo order = pList.get(postion);	
	 	startDate.setTime(DateUtil.stringToDate(order.getPick_up_date(), DateUtil.DATETIME_SQL));
	 	endDate.setTime(DateUtil.stringToDate(order.getDelivery_date(), DateUtil.DATETIME_SQL));
	 	holder.tvName.setText(order.getName());
		holder.tvContact.setText(order.getPhone_number());
		holder.tvOrder.setText("#"+order.getId());
		holder.tvPickUpTime.setText(DateUtil.dateToString(startDate.getTime(), DateUtil.TIME_12HRS_SHORT));
		holder.tvDeliveryTime.setText(DateUtil.dateToString(endDate.getTime(), DateUtil.TIME_12HRS_SHORT));
       
		if(order.getAddress()!=null){
			holder.tvAddress.setText(order.getAddress());
		}else {
			holder.tvAddress.setText("");
		}
		Date date = startDate.getTime();
        int diff = DateUtil.daysDiff(new Date(), date);
        if(diff==0){
        	holder.tvPickUpDate.setText("Today");
        }else if(diff==1){
			holder.tvPickUpDate.setText("Tomorrow");
		} else if(diff==-1){
			holder.tvPickUpDate.setText("Yesterday");
		} else if( diff < -1) {
			holder.tvPickUpDate.setText(Math.abs(diff)+" days ago");
		}else {
			holder.tvPickUpDate.setText(DateUtil.dateToString(date, DateUtil.MONTH_DATE_PATTERN));
		}
        date = endDate.getTime();
        diff = DateUtil.daysDiff(new Date(), date);
        if(diff==0){
        	holder.tvDeliveryDate.setText("Today");
        }else if(diff==1){
        	holder.tvDeliveryDate.setText("Tomorrow");
        } else if(diff==-1){
			holder.tvDeliveryDate.setText("Yesterday");
		} else if( diff < -1) {
			holder.tvDeliveryDate.setText(Math.abs(diff)+" days ago");
		}else {
			holder.tvDeliveryDate.setText(DateUtil.dateToString(date, DateUtil.MONTH_DATE_PATTERN));
		}
		if(order.getExtras()==null){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.trancelucent));
    	
		}else if(order.getExtras().equalsIgnoreCase("Employee 1")){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.home_item_pink));
    	
	  		}else if(order.getExtras().equalsIgnoreCase("Employee 2")){
	  			convertView.setBackgroundColor(context.getResources().getColor(R.color.home_item_blue));
      	  
	  		}else if(order.getExtras().equalsIgnoreCase("Employee 3")){
	  			convertView.setBackgroundColor(context.getResources().getColor(R.color.home_item_yellow));
     	 
	  		}else if(order.getExtras().equalsIgnoreCase("Employee 4")){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.home_item_green));

		} else if(order.getExtras().equalsIgnoreCase("Employee 5")){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.orange));

		} else if(order.getExtras().equalsIgnoreCase("Employee 6")){
			convertView.setBackgroundColor(context.getResources().getColor(R.color.iconpressed_red));

		} else {
	  			convertView.setBackgroundColor(context.getResources().getColor(R.color.trancelucent));
	  		
	  		}
		if(order.getStatus().equalsIgnoreCase("pending")){
			
			holder.tvStatus.setText("Pending");
		}else if(order.getStatus().equalsIgnoreCase("accepeted")){
			holder.tvStatus.setText("Pick Up requested");
		}else if(order.getStatus().equalsIgnoreCase("washing")){
			holder.tvStatus.setText("Washing");
		}else if(order.getStatus().equalsIgnoreCase("completed")){
			holder.tvStatus.setText("Delivery required");
		}else if(order.getStatus().equalsIgnoreCase("paid")){
			holder.tvStatus.setText("Payment Collected");
		}else if(order.getStatus().equalsIgnoreCase("cancelled")){
			holder.tvStatus.setText("Order Cancelled");
			convertView.setBackgroundColor(context.getResources().getColor(R.color.alizarin));
		}
		
		convertView.setTag(holder);
		return convertView;
	}
	
	public class ViewHolder{
		TextView tvName,tvContact,tvStatus, tvAddress, tvOrder; 
		TextView tvPickUp,tvPickUpDate,tvPickUpTime, tvDelivery,tvDeliveryDate,tvDeliveryTime;
		
		
	}

}
