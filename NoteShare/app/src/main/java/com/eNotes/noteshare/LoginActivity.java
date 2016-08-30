package com.eNotes.noteshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mobiapp.ventures.eNotes.R;


public class LoginActivity extends Activity {

	public Button btnsignUP, btnSignIn;
	public LinearLayout layoutloginusername, layoutloginpassowrd;
	public EditText textusername,textpassowrd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		// LayoutInflater inflater = (LayoutInflater)
		// this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		// View contentView = inflater.inflate(R.layout.login_activity, null,
		// false);
		// /mDrawerLayout.addView(contentView, 0);
		initlizeUIElement(null);

	}

	void initlizeUIElement(View contentview) {
		
		btnSignIn=(Button) findViewById(R.id.btnloginsignin);
		btnsignUP=(Button) findViewById(R.id.btnloginsignup);
		

		layoutloginpassowrd=(LinearLayout)findViewById(R.id.loginpassword);
		layoutloginusername=(LinearLayout)findViewById(R.id.loginusername);
		
		textusername=(EditText)layoutloginusername.findViewById(R.id.editTextlogin);
		textpassowrd=(EditText)layoutloginpassowrd.findViewById(R.id.editTextlogin);
		
		textusername.setHint("User Name");
		textpassowrd.setHint("Password");
		textpassowrd.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD|InputType.TYPE_CLASS_TEXT);
		
		
		addlistners();
		
		
	}
	void addlistners()
	{
		
		btnSignIn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		btnsignUP.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
				finish();
				
			}
		});
		
	}
}
