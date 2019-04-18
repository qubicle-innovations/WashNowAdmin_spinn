package com.washnow.admin.widgets;





import com.washnow.admin.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window; 

public class TCustomProgressDailogue extends Dialog {

	public TCustomProgressDailogue(Context context) {
		super(context, R.style.Theme_CustomProgress);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_progress);

	}

}
