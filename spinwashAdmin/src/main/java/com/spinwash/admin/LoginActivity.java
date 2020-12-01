package com.spinwash.admin;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.spinwash.admin.utils.Utils;
import com.spinwash.admin.utils.ZTypeface;
import com.spinwash.admin.vo.OrderVo;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends Activity implements OnClickListener {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.login_layout);
        context = this;
        String name = Utils.getSharedPreference(context, "name");
        if (name != null && name.length() > 3) {
            initSplashLayout();
        } else
            initLayout();

    }

    public void initLayout() {
        try {
            TextView tvVersion = (TextView) findViewById(R.id.tvVersion);
            tvVersion.setVisibility(View.VISIBLE);

            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = "Ver." + pInfo.versionCode;
            tvVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        EditText etName = (EditText) findViewById(R.id.etName);
        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        TextView tvCountry = (TextView) findViewById(R.id.tvCountry);
        Button btGo = (Button) findViewById(R.id.btGo);
        ImageView imLogo = (ImageView) findViewById(R.id.imLogo);
        imLogo.setVisibility(View.GONE);
        tvCountry.setVisibility(View.VISIBLE);
        etName.setVisibility(View.VISIBLE);
        etEmail.setVisibility(View.VISIBLE);
        btGo.setVisibility(View.VISIBLE);
        etName.setTypeface(ZTypeface.UbuntuR(this));
        etEmail.setTypeface(ZTypeface.UbuntuR(this));

        btGo.setTypeface(ZTypeface.UbuntuR(this));
        btGo.setOnClickListener(this);
        tvCountry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btGo:
                login();
                break;
            case R.id.tvCountry:
                showCountryMenu(v);
                break;

            default:
                break;
        }

    }


    public void login() {
        EditText etName = (EditText) findViewById(R.id.etName);
        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        TextView tvCountry = (TextView) findViewById(R.id.tvCountry);

        String name = Utils.getText(etName);
        String password = Utils.getText(etEmail);
        String country = tvCountry.getText().toString();
        if (name == null || name.length() < 2) {
            Toast.makeText(context, "Please enter a valid username", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (password == null || password.length() < 2) {
                Toast.makeText(context, "Please enter a valid password", Toast.LENGTH_SHORT).show();
                return;
            }
            if(country.equalsIgnoreCase("Select Country")){
                Toast.makeText(context, "Please select a valid country", Toast.LENGTH_SHORT).show();
                return;
            }else if(country.equalsIgnoreCase(Utils.COUNTRY_QATAR)){
                if ((name.equalsIgnoreCase(Utils.USERNAME_0) && password.equalsIgnoreCase(Utils.PASSWORD_0)||(name.equalsIgnoreCase("Qatar") && password.equalsIgnoreCase("qwerty1")))) {
                    Utils.setSharedPreference(context, "user", "0");
                } else if (name.equalsIgnoreCase(Utils.USERNAME_1) && password.equalsIgnoreCase(Utils.PASSWORD_1)) {
                    Utils.setSharedPreference(context, "user", "Employee 1");
                } else if (name.equalsIgnoreCase(Utils.USERNAME_2) && password.equalsIgnoreCase(Utils.PASSWORD_2)) {
                    Utils.setSharedPreference(context, "user", "Employee 2");
                } else if (name.equalsIgnoreCase(Utils.USERNAME_3) && password.equalsIgnoreCase(Utils.PASSWORD_3)) {
                    Utils.setSharedPreference(context, "user", "Employee 3");
                } else if (name.equalsIgnoreCase(Utils.USERNAME_4) && password.equalsIgnoreCase(Utils.PASSWORD_4)) {
                    Utils.setSharedPreference(context, "user", "Employee 4");
                } else if (name.equalsIgnoreCase(Utils.USERNAME_5) && password.equalsIgnoreCase(Utils.PASSWORD_5)) {
                    Utils.setSharedPreference(context, "user", "Employee 5");
                } else if (name.equalsIgnoreCase(Utils.USERNAME_6) && password.equalsIgnoreCase(Utils.PASSWORD_6)) {
                    Utils.setSharedPreference(context, "user", "Employee 6");
                } else {
                    Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                Utils.setSharedPreference(context,"country",Utils.COUNTRY_QATAR);
            }else {
                if ((name.equalsIgnoreCase(Utils.USERNAME_0) && password.equalsIgnoreCase(Utils.PASSWORD_0B))||(name.equalsIgnoreCase("oman") && password.equalsIgnoreCase("qwerty1"))) {
                    Utils.setSharedPreference(context, "user", "0");
                } else if (name.equalsIgnoreCase(Utils.USERNAME_1) && password.equalsIgnoreCase(Utils.PASSWORD_1B)) {
                    Utils.setSharedPreference(context, "user", "Employee 1");
                } else if (name.equalsIgnoreCase(Utils.USERNAME_2) && password.equalsIgnoreCase(Utils.PASSWORD_2B)) {
                    Utils.setSharedPreference(context, "user", "Employee 2");
                } else if (name.equalsIgnoreCase(Utils.USERNAME_3) && password.equalsIgnoreCase(Utils.PASSWORD_3B)) {
                    Utils.setSharedPreference(context, "user", "Employee 3");
                } else if (name.equalsIgnoreCase(Utils.USERNAME_4) && password.equalsIgnoreCase(Utils.PASSWORD_4B)) {
                    Utils.setSharedPreference(context, "user", "Employee 4");
                } else if (name.equalsIgnoreCase(Utils.USERNAME_5) && password.equalsIgnoreCase(Utils.PASSWORD_5B)) {
                    Utils.setSharedPreference(context, "user", "Employee 5");
                } else if (name.equalsIgnoreCase(Utils.USERNAME_6) && password.equalsIgnoreCase(Utils.PASSWORD_6B)) {
                    Utils.setSharedPreference(context, "user", "Employee 6");
                } else {
                    Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                Utils.setSharedPreference(context,"country",Utils.COUNTRY_OMAN);

            }


        }

	/*	if(!name.equalsIgnoreCase(Utils.USERNAME)){
			Toast.makeText(context, "Invalid Username", Toast.LENGTH_SHORT).show();
			return;
		}

		else if(!email.equalsIgnoreCase(Utils.PASSWORD)){
			Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show();
			return;
		}*/
        Utils.setSharedPreference(context, "name", name);
        Utils.setSharedPreference(context, "email", password);
        Intent intent = new Intent(context, OrderListActivity.class);

        startActivity(intent);
        finish();
    }

    public void initSplashLayout() {
        try {
            TextView tvVersion = (TextView) findViewById(R.id.tvVersion);
            tvVersion.setVisibility(View.VISIBLE);

            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = "Ver." + pInfo.versionCode;
            tvVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new Thread() {
            public void run() {
                try {
                    sleep(2500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                OrderVo order = Utils.getOrder(context);
                Intent intent;

                intent = new Intent(context, OrderListActivity.class);

                startActivity(intent);
                finish();

            }
        }.start();
    }

    public void showCountryMenu(View v){

        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_country, popup.getMenu());
        popup.setOnMenuItemClickListener(new  PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // TODO Auto-generated method stub
                TextView tvCountry = (TextView) findViewById(R.id.tvCountry);

                switch (item.getItemId()) {
                    case R.id.slot_1:

                        tvCountry.setText(Utils.COUNTRY_QATAR);
                        break;
                    case R.id.slot_2:
                        tvCountry.setText(Utils.COUNTRY_OMAN);

                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        popup.show();
    }
}
