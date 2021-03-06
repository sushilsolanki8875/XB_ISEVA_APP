package com.iseva.app.source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Login_Jobs_Dep extends AppCompatActivity implements TextWatcher {


    // UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private ProgressDialog pd;
    private boolean isEmployee = false;
    private TextView txtDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_jobs_dep);
        // Set up the login form.
        inti();
        mUserView = (AutoCompleteTextView) findViewById(R.id.edtUsername);
        populateAuto();
        mPasswordView = (EditText) findViewById(R.id.password);

        /*mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });*/

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    private void populateAuto(){

        Object_AppConfig obj = new Object_AppConfig(this);
        String[] item = {obj.getuserName()};
        mUserView.addTextChangedListener(this);
        mUserView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item));
    }





    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.

        String userName = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
       /* if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }*/

        // Check for a valid email address.
        if (TextUtils.isEmpty(userName)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        } /*else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            login(userName, password);
            /*Object_AppConfig objConfig = new Object_AppConfig(this);
            objConfig.setboolIslogin(true);
           // Activity_Home.islogIn = true;
            this.finish();*/
        }
    }


    private void login(String username,String password){

        try{
            Object_AppConfig obj = new Object_AppConfig(this);
            obj.setuserName(username);
            pd = Globals.showLoadingDialog(pd, this, false, "Login");
            Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                    Request.Method.POST,
                    Custom_URLs_Params.getURL_EmpLogin(),
                    Custom_URLs_Params.getParams_Login(this, username, password,isEmployee), new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Globals.hideLoadingDialog(pd);
                    //setEmpty();
                    Log.i("SUSHIL", "json Response recieved !!"+response);
                    getLoginResponce(response);

                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError err) {
                    Globals.hideLoadingDialog(pd);
                    Log.i("SUSHIL", "ERROR VolleyError");
                    Globals.showAlert("Error",Globals.MSG_SERVER_ERROR,Activity_Login_Jobs_Dep.this);

                }
            });

            Custom_VolleyAppController.getInstance().addToRequestQueue(
                    jsonObjectRQST);
        }catch (Exception e){
            Globals.hideLoadingDialog(pd);
            e.printStackTrace();
            Globals.showAlert("ERROR", "Some Error, Retry!", Activity_Login_Jobs_Dep.this);
        }
    }

    private void getLoginResponce(JSONObject obj){
        if(obj!=null){
            try{
                if(obj.has("login")) {
                    JSONObject responce = obj.getJSONObject("login");
                    if (responce.has("isValidated")) {
                        int validUser = responce.getInt("isValidated");
                        if (validUser == 0) {
                            String err = "Username & Password did not Match, Retry!";

                            if(responce.has("error_message")){
                                err = responce.getString("error_message");
                            }
                            Globals.showShortToast(this, err);
                        } else {
                        /*if(responce.has("isactive")){
                            String isActive = responce.getString("isactive");
                            if(responce.has("userdata")){
                                JSONObject obj = responce.getJSONObject("userdata");
                                //Log.i("SUSHIL", "userData "+obj);
                                //.i("SUSHIL", "is active Data "+isActive);
                                if(obj!=null)
                                    navigation(obj,isActive);

                            }
                        }*/

                            if (responce.has("empid")) {
                                Globals.showShortToast(this, "You are logged in successfully!");
                                int id = responce.getInt("empid");
                                if(responce.has("isemployee")) {
                                    int isemployee = responce.getInt("isemployee");
                                    Object_AppConfig config = new Object_AppConfig(this);
                                    if(isemployee==1){

                                        config.setEmpId(id);
                                    }else{
                                        config.setEmperId(id);
                                    }
                                }

                                //Commented By Harsh until Edit functionality is addded
                                //navigation(obj);

                                /* Added and commented by harsh
                                if (responce.has("name"))
                                    config.setuserName(responce.getString("name"));
                                if (responce.has("firmname"))
                                    config.setFirmName(responce.getString("firmname"));
                                if (responce.has("image"))
                                    config.setFirmName(responce.getString("image"));
                                    */
                                this.finish();
                            }

                        }
                    }
                }
            }catch(JSONException ex){
                ex.printStackTrace();
            }
        }

    }





    private void inti(){
        Intent i = getIntent();
        isEmployee = i.getBooleanExtra("bool",false);
        TextView txtHeader = (TextView)findViewById(R.id.txtHeader);
        txtHeader.setText("Login");
        txtDes = (TextView)findViewById(R.id.txtDes);
        if(isEmployee){
            txtDes.setText("Employee Login");
        }else{
            txtDes.setText("Employer Login");
        }
        ImageView imgBack = (ImageView) findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity_Login_Jobs_Dep.this.finish();
            }
        });


    }



    private void setEmpty(){
        mUserView.setText("");
        mPasswordView.setText("");
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
    @Override
    public void afterTextChanged(Editable s) {

    }
}
