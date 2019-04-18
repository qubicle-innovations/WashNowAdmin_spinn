package com.washnow.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.washnow.admin.R;
import com.washnow.admin.utils.ZTypeface;
import com.washnow.admin.vo.ProductVo;

import java.util.ArrayList;
import java.util.Calendar;


public class ProductListAdapter extends BaseAdapter{

	private Context context;
	private LayoutInflater inflator;
	private ArrayList<ProductVo> pList ;


	public ProductListAdapter(Context context, ArrayList<ProductVo> pList) {
		super();
		this.context = context;
		this.inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
		this.pList = pList;
		
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pList.size();
	}
	public ArrayList<ProductVo> getList(){
		return pList;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public ProductVo getItembyPos(int pos) {
		// TODO Auto-generated method stub
		return pList.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int postion, View convertView, ViewGroup arg2) {

		ViewHolder holder;
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
	
	 	if(convertView!=null&&convertView.getTag()!= null) {
	 		holder = (ViewHolder) convertView.getTag();
		}else {
			
			holder = new ViewHolder();
			convertView = inflator.inflate(R.layout.products_list_item, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tvDryCount = (TextView) convertView.findViewById(R.id.tvDryCount);
			holder.tvIronCount = (TextView) convertView.findViewById(R.id.tvIronCount);
			holder.tvWashCount = (TextView) convertView.findViewById(R.id.tvWashCount);



			holder.tvName.setTypeface(ZTypeface.UbuntuR(context));
			holder.tvDryCount.setTypeface(ZTypeface.UbuntuR(context));
			holder.tvIronCount.setTypeface(ZTypeface.UbuntuR(context));
			holder.tvWashCount.setTypeface(ZTypeface.UbuntuR(context));

			
			
		}
	 	ProductVo vo = pList.get(postion);
		holder.tvName.setText(vo.getName());

		holder.tvIronCount.setTag(vo);
		holder.tvWashCount.setTag(vo);
		holder.tvDryCount.setTag(vo);

		if(vo.getIronCount()>0) {
			holder.tvIronCount.setSelected(true);
			holder.tvIronCount.setText(String.valueOf(vo.getIronCount()));


		} else{
			holder.tvIronCount.setSelected(false);
			holder.tvIronCount.setText("");

		}
		if(vo.getWashCount()>0){
			holder.tvWashCount.setSelected(true);
			holder.tvWashCount.setText(String.valueOf(vo.getWashCount()));

		} else{
			holder.tvWashCount.setSelected(false);
			holder.tvWashCount.setText("");


		}
		if(vo.getDryCount()>0){
			holder.tvDryCount.setSelected(true);
			holder.tvDryCount.setText(String.valueOf(vo.getDryCount()));

		}else{
			holder.tvDryCount.setSelected(false);
			holder.tvDryCount.setText("");

		}


		holder.tvDryCount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ProductVo vo = (ProductVo) v.getTag();
				int count = vo.getDryCount();
				vo.setDryCount(count+1);
				pList.set(postion,vo);
				notifyDataSetChanged();

			}
		});
		holder.tvIronCount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ProductVo vo = (ProductVo) v.getTag();
				int count = vo.getIronCount();
				vo.setIronCount(count+1);
				pList.set(postion,vo);
				notifyDataSetChanged();

			}
		});
		holder.tvWashCount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ProductVo vo = (ProductVo) v.getTag();
				int count = vo.getWashCount();
				vo.setWashCount(count+1);
				pList.set(postion,vo);
				notifyDataSetChanged();

			}
		});

		holder.tvWashCount.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				ProductVo vo = (ProductVo) v.getTag();
 				vo.setWashCount(0);
				pList.set(postion,vo);
				notifyDataSetChanged();
				return false;
			}
		});
		holder.tvIronCount.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				ProductVo vo = (ProductVo) v.getTag();
				vo.setIronCount(0);
				pList.set(postion,vo);
				notifyDataSetChanged();
				return false;
			}
		});
		holder.tvDryCount.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				ProductVo vo = (ProductVo) v.getTag();
				vo.setDryCount(0);
				pList.set(postion,vo);
				notifyDataSetChanged();
				return false;
			}
		});

		convertView.setTag(holder);
		return convertView;
	}
	
	public class ViewHolder{
		TextView tvName,tvIronCount,tvWashCount, tvDryCount;

		
	}

}
