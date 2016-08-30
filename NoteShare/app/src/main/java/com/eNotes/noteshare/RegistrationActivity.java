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


public class RegistrationActivity extends Activity {

	public Button btnsignUP, btnSignIn;
	public LinearLayout layoutloginusername, layoutloginpassowrd,
			layoutloginfirstname, layoutloginlastname,
			layoutloginconfirmpassowrd;
	public EditText textusername, textpassowrd, textFirstname, textlastname,
			textconfirmpassword;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_activity);

		// LayoutInflater inflater = (LayoutInflater)
		// this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// inflate your activity layout here!
		// View contentView = inflater.inflate(R.layout.login_activity, null,
		// false);
		// /mDrawerLayout.addView(contentView, 0);
		initlizeUIElement(null);

	}

	void initlizeUIElement(View contentview) {

		btnSignIn = (Button) findViewById(R.id.btnregistersignin);
		btnsignUP = (Button) findViewById(R.id.btnregistersignup);

		layoutloginpassowrd = (LinearLayout) findViewById(R.id.registerpassword);
		layoutloginusername = (LinearLayout) findViewById(R.id.registeruseremail);
		layoutloginconfirmpassowrd = (LinearLayout) findViewById(R.id.registerconfirmpassword);
		layoutloginfirstname = (LinearLayout) findViewById(R.id.registerfirstname);
		layoutloginlastname = (LinearLayout) findViewById(R.id.registerlastname);

		textusername = (EditText) layoutloginusername
				.findViewById(R.id.editTextlogin);
		textpassowrd = (EditText) layoutloginpassowrd
				.findViewById(R.id.editTextlogin);

		textconfirmpassword = (EditText) layoutloginconfirmpassowrd
				.findViewById(R.id.editTextlogin);
		textFirstname = (EditText) layoutloginfirstname
				.findViewById(R.id.editTextlogin);
		textlastname = (EditText) layoutloginlastname
				.findViewById(R.id.editTextlogin);

		textconfirmpassword.setHint("Confirm Password");
		textFirstname.setHint("First Name");
		textlastname.setHint("Last Name");
		textusername.setHint("Email");
		textpassowrd.setHint("Password");
		textpassowrd.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD
				| InputType.TYPE_CLASS_TEXT);
		textconfirmpassword
				.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD
						| InputType.TYPE_CLASS_TEXT);
		
		
		
		
		addlistners();

	}

	void addlistners() {

		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		btnsignUP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent newIntent = new Intent(getApplicationContext(),UserProfileActivity.class);
				newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(newIntent);
				finish();
				
				//startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));

			}
		});
		
		

	}
}
