

package com.washnow.admin;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.washnow.admin.loader.ImageLoader;
import com.washnow.admin.utils.ZTypeface;

import java.util.ArrayList;
 
public class BaseActivity extends AppCompatActivity implements OnClickListener {
	ActionBar actionBar;
	Context context;
	InputMethodManager imm;
	ImageLoader mLoader;
	public static int running = 0;
	private ResideMenu resideMenu;
	private ResideMenuItem itemBlank;
    private ResideMenuItem itemPrice;
    private ResideMenuItem itemNewOrder;
    private ResideMenuItem itemShare;
	
	  
	    public boolean isSmallScreen=false;
		public ArrayList<String> 	mDrawerMenu;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		running=1;
		super.onCreate(savedInstanceState);
		 setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		actionBar = this.getSupportActionBar();
		context=this;
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		//Utils.hideSoftKeyBoard(this);
		  imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			   mLoader = new ImageLoader(this);
				
	}
	
	public void setMenu(){
		  resideMenu = new ResideMenu(this);
	        resideMenu.setBackground(R.drawable.wash_splash_bg);
	        resideMenu.attachToActivity(this);
	      //  resideMenu.setMenuListener(menuListener);
	        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip. 
	        resideMenu.setScaleValue(0.6f);
	        
	        
	        itemNewOrder     = new ResideMenuItem(context);
	        itemShare     = new ResideMenuItem(context);
	        itemBlank     = new ResideMenuItem(context);
	        itemPrice     = new ResideMenuItem(context);
		        
	        itemNewOrder.setTitle("NEW ORDER");
	        itemShare.setTitle( "SHARE" );
	        itemPrice.setTitle("PRICING");
	        itemBlank.setTitle( "" );

	        itemNewOrder.setOnClickListener(this);
	        itemPrice.setOnClickListener(this);
	        itemShare.setOnClickListener(this);

	        itemBlank.setBorder(View.INVISIBLE);
	        
	        resideMenu.addMenuItem(itemBlank, ResideMenu.DIRECTION_LEFT);
	        resideMenu.addMenuItem(itemNewOrder, ResideMenu.DIRECTION_LEFT);
	        resideMenu.addMenuItem(itemPrice, ResideMenu.DIRECTION_LEFT);
	        resideMenu.addMenuItem(itemShare, ResideMenu.DIRECTION_LEFT);

	        // You can disable a direction by setting ->
	        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

	        findViewById(R.id.im_logo).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
	            }
	        });
	        itemPrice.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
				 Intent shareIntent = new Intent(BaseActivity.this, WebViewActivity.class);
		       startActivity(shareIntent); 
		       resideMenu.closeMenu();
				}
			});
		 itemShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String q="Laundry & Dry Cleaning Service in QATAR \n";
 				 
					q=q+"https://play.google.com/store/apps/details?id=com.washnow";
	 				
				 
				
				
				 Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, q);
            startActivity(Intent.createChooser(shareIntent, "Spread the word")); 
            resideMenu.closeMenu();
			}
		});
		 itemNewOrder.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				
				 Intent shareIntent = new Intent(BaseActivity.this, RequestPickupActivity.class);
               startActivity(shareIntent); 
               resideMenu.closeMenu();
				}
			});
			

	}
	
public void setTitle(String Title){
		
		TextView tvTitle = (TextView) findViewById(R.id.tv_actionbar_title);
		
		tvTitle.setText(Title);
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

    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
   // 	FlurryAgent.onStartSession(this, Config.flurryKey);
    	
    }
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		//FlurryAgent.onEndSession(this);
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
     
}