package com.washnow.admin.async;

import android.content.Context;
import android.os.AsyncTask;

import com.washnow.admin.InvoiceActivity;
import com.washnow.admin.OrderStatusActivity;
import com.washnow.admin.http.RestClient;
import com.washnow.admin.http.RestClient.RequestMethod;
import com.washnow.admin.utils.Utils;
import com.washnow.admin.vo.TResponse;
import com.washnow.admin.widgets.TCustomProgressDailogue;


public class ProductsUpdateTask extends
		AsyncTask<String, Void, TResponse<String>> {

	private Context ctx;
	private TCustomProgressDailogue pd;
	public ProductsUpdateTask(Context context	) {
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

			restClient.addParam("process", "create_invoice");
			restClient.addParam("order_id", params[0]);
			restClient.addParam("products", params[1]);
			/*JSONArray jArray = new JSONArray(params[1]);
			for(int i =0;i<jArray.length();i++) {
				restClient.addParam("products", jArray.getJSONObject(i).toString());
				Log.e("JSON", jArray.getJSONObject(i).toString());
			}*/



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

		 if (ctx instanceof InvoiceActivity) {
		  ((InvoiceActivity) ctx).updateOrder(result);
		}
	

	}

}
