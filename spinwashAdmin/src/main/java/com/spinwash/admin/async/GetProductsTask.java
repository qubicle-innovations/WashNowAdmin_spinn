package com.spinwash.admin.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.spinwash.admin.InvoiceActivity;
import com.spinwash.admin.http.RestClient;
import com.spinwash.admin.http.RestClient.RequestMethod;
import com.spinwash.admin.utils.Utils;
import com.spinwash.admin.vo.TResponse;
import com.spinwash.admin.widgets.TCustomProgressDailogue;


public class GetProductsTask extends
		AsyncTask<String, Void, TResponse<String>> {

	private Context ctx;
	private TCustomProgressDailogue pd;
	public GetProductsTask(Context context	) {
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
			RestClient restClient = new RestClient(Utils.URL_SERVER+"?process=fetchproducts&country="+Utils.getSharedPreference(ctx,"country"));
			 
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

		if (ctx instanceof InvoiceActivity) {
		  ((InvoiceActivity) ctx).fetchProducts(result);
			 Log.v("response", result.getResponseContent());
		}
	

	}

}
