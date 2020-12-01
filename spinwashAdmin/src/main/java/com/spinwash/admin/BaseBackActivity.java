

package com.spinwash.admin;
 

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spinwash.admin.utils.ZTypeface;
 
public class BaseBackActivity extends AppCompatActivity {
    protected Context context;
    ActionBar actionBar;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        actionBar = getSupportActionBar();
		actionBar.setCustomView(R.layout.actionbar_backnav);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		
		ImageView imlogo = (ImageView) findViewById(R.id.im_logo);
		imlogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_logo));
		imlogo.setVisibility(View.INVISIBLE);
		findViewById(R.id.im_bck).setVisibility(View.VISIBLE);
		
		 findViewById(R.id.ll_logo).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                 onBackPressed();
	            }
	        });
		       
    }
    
    public void setTitle(String title){
    	TextView tvTitle = (TextView) findViewById(R.id.tv_actionbar_title);
    	tvTitle.setText(title);
    	tvTitle.setTypeface(ZTypeface.UbuntuR(context));
		
    }
	 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
     //   inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
     
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
   //     boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
    //    menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case android.R.id.home:
            onBackPressed(); 
        	return true;
        default:
            return super.onOptionsItemSelected(item);
    }
	 
       
    }

   

     
}