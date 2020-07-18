package com.washnow.admin.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.washnow.admin.OrderListActivity;
import com.washnow.admin.http.RestClient;
import com.washnow.admin.http.RestClient.RequestMethod;
import com.washnow.admin.utils.Utils;
import com.washnow.admin.vo.TResponse;
import com.washnow.admin.widgets.TCustomProgressDailogue;
 

public class GetOrderListTask extends
		AsyncTask<String, Void, TResponse<String>> {

	private Context ctx;
	private TCustomProgressDailogue pd;
	public GetOrderListTask(Context context	) {
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
			RestClient restClient = new RestClient(Utils.URL_SERVER);
		//	http://qubicle.me/washnow_admin_test/orderapi.php
	 			restClient.addParam("authorization","AAAAQQXlDD8:APA91bEzxEocN_TgmgI1xiZzCFj_OYT4MHtrtEVcTT7CLvb9PmbiqjX_fsHtFbonHwkRh6xSc0-23Hmfp8QaVpFS4_1DQ7uYq1-qS6Ok1ZrZUcXkOYdFRxc3Y0kLRfYqjNfWfciahsZJ"  );
			//	Log.i("id", this.params);
			 restClient.addParam("country", Utils.getSharedPreference(ctx,"country"));
			restClient.execute(RequestMethod.POST);
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

		if (ctx instanceof OrderListActivity) {
		  ((OrderListActivity) ctx).setOrderList(result);
			 Log.v("response", result.getResponseContent());
		}
	

	}

}
