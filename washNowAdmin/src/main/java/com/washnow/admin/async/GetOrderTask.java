package com.washnow.admin.async;

import java.util.List;

import org.apache.http.NameValuePair;

import com.washnow.admin.OrderStatusActivity;
import com.washnow.admin.http.RestClient;
import com.washnow.admin.http.RestClient.RequestMethod;
import com.washnow.admin.utils.Utils;
import com.washnow.admin.vo.TResponse;
import com.washnow.admin.widgets.TCustomProgressDailogue;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
 

public class GetOrderTask extends
		AsyncTask<String, Void, TResponse<String>> {

	private Context ctx;
	private TCustomProgressDailogue pd;
	public GetOrderTask(Context context	) {
		 this.ctx = context;
		this.pd = new TCustomProgressDailogue(ctx);
		this.pd.setCancelable(false);
	}

	@Override
	protected void onPreExecute() {
		pd.show();
	}

	@Override
	protected TResponse<String> doInBackground(String... params) {
		TResponse<String> response =  new TResponse<String>();

		try {
			String result=null;
			RestClient restClient = new RestClient(Utils.URL_SERVER+"?process=selectid&id="+params[0]);
			 
	//			restClient.addParam("id", this.params);
			//	Log.i("id", this.params);
			 
			restClient.execute(RequestMethod.GET);
			result = restClient.getResponseString();
			 response.setResponseContent(result);
			response.setHasError(false);

		} catch (Exception e) {
			 
			response.setResponseContent(e.toString());
			response.setHasError(true);
			e.printStackTrace();
		}

		return response;
	}

	@Override
	protected void onPostExecute(TResponse<String> result) {

		 
		if (pd.isShowing()){
			pd.dismiss();
		}

		if (ctx instanceof OrderStatusActivity) {
		  ((OrderStatusActivity) ctx).updateOrder(result);
			 Log.v("response", result.getResponseContent());
		}
	

	}

}
