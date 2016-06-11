package com.pfe.cabinet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText pnom, pprenom, pdateNaissance, pgenre,pusername,ppassword,pconfpassword;
    private TextInputLayout inputLayoutNom,inputLayoutPrenom,inputLayoutDateN,inputLayoutGenre,inputLayoutUsername,inputLayoutPassword,inputLayoutConfPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputLayoutNom = (TextInputLayout)findViewById(R.id.input_layout_nom);
        inputLayoutPrenom = (TextInputLayout)findViewById(R.id.input_layout_prenom);
        inputLayoutDateN = (TextInputLayout)findViewById(R.id.input_layout_date_naissance);
        inputLayoutGenre = (TextInputLayout)findViewById(R.id.input_layout_genre);
        inputLayoutUsername = (TextInputLayout)findViewById(R.id.input_layout_username);
        inputLayoutPassword = (TextInputLayout)findViewById(R.id.input_layout_date_password);
        inputLayoutConfPassword = (TextInputLayout)findViewById(R.id.input_layout_confpassword);
        pnom = (EditText)findViewById(R.id.pnom);
        pprenom = (EditText)findViewById(R.id.pprenom);
        pdateNaissance = (EditText)findViewById(R.id.pdateNaissance);
        pgenre = (EditText)findViewById(R.id.pgenre);
        pusername = (EditText)findViewById(R.id.pusername);
        ppassword = (EditText)findViewById(R.id.ppassword);
        pconfpassword = (EditText)findViewById(R.id.pconfpassword);
        Button btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        pnom.addTextChangedListener(new MyTextWatcher(pnom));
        pprenom.addTextChangedListener(new MyTextWatcher(pprenom));
        pdateNaissance.addTextChangedListener(new MyTextWatcher(pdateNaissance));
        pgenre.addTextChangedListener(new MyTextWatcher(pgenre));
        pusername.addTextChangedListener(new MyTextWatcher(pusername));
        ppassword.addTextChangedListener(new MyTextWatcher(ppassword));
        pconfpassword.addTextChangedListener(new MyTextWatcher(pconfpassword));


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    @Override
    public void onClick(View v) {



    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }



        if (!validatePassword()) {
            return;
        }

        HashMap postData = new HashMap();
        postData.put("txtnom", pnom.getText().toString());
        postData.put("txtprenom", pprenom.getText().toString());
        postData.put("txtdateNaissance", pdateNaissance.getText().toString());
        postData.put("txtgenre", pdateNaissance.getText().toString());
        postData.put("txtusername", pusername.getText().toString());
        postData.put("txtpassword", ppassword.getText().toString());
        postData.put("txtconfpassword", pconfpassword.getText().toString());
        postData.put("mobile", "android");




        PostResponseAsyncTask task = new PostResponseAsyncTask(
                SignupActivity.this,
                postData,
                new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        Toast.makeText(SignupActivity.this, s, Toast.LENGTH_LONG).show();
                        if(s.contains("Succe")){
                            Config.LOGGEDIN_SHARED_PREF ="0";
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));

                        }

                    }
                });
        task.execute("http://YOUR_PROJECT_HOST/api/ajout-mobile-user");
    }

    private boolean validateName() {
        if (pnom.getText().toString().trim().isEmpty()) {
            inputLayoutNom.setError(getString(R.string.err_msg_name));
            requestFocus(pnom);
            return false;
        } else {
            inputLayoutNom.setErrorEnabled(false);
        }


        if (pprenom.getText().toString().trim().isEmpty()) {
            inputLayoutPrenom.setError(getString(R.string.err_msg_surnme));
            requestFocus(pprenom);
            return false;
        } else {
            inputLayoutPrenom.setErrorEnabled(false);
        }


        if (pdateNaissance.getText().toString().trim().isEmpty()) {
            inputLayoutDateN.setError(getString(R.string.err_msg_date));
            requestFocus(pdateNaissance);
            return false;
        } else {
            inputLayoutDateN.setErrorEnabled(false);
        }



        if (pgenre.getText().toString().trim().isEmpty()) {
            inputLayoutGenre.setError(getString(R.string.err_msg_gender));
            requestFocus(inputLayoutGenre);
            return false;
        }  else {
            inputLayoutGenre.setErrorEnabled(false);
        }

        if (pusername.getText().toString().trim().isEmpty()) {
            inputLayoutUsername.setError(getString(R.string.err_msg_username));
            requestFocus(pusername);
            return false;
        }else {
            inputLayoutUsername.setErrorEnabled(false);
        }



        return true;
    }



    private boolean validatePassword() {
        if (ppassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(ppassword);
            return false;
        } else {
            inputLayoutPassword.setError(null);
        }
        if (pconfpassword.getText().toString().trim().isEmpty()) {
            inputLayoutConfPassword.setError(getString(R.string.err_msg_copassword));
            requestFocus(pconfpassword);
            return false;
        } else {
            inputLayoutConfPassword.setError(null);
        }
        if (!((pconfpassword.getText().toString()).equals(ppassword.getText().toString()))) {
            inputLayoutConfPassword.setError(getString(R.string.err_msg_confpassword));
            requestFocus(pconfpassword);
            return false;
        } else {
            inputLayoutConfPassword.setError(null);
        }

        return true;
    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_layout_nom:
                    validateName();
                    break;
                case R.id.input_layout_prenom:
                    validateName();
                    break;

                case R.id.input_layout_date_naissance:
                    validateName();
                    break;

                case R.id.input_layout_genre:
                    validateName();
                    break;
                case R.id.input_layout_username:
                    validateName();
                    break;
                case R.id.input_layout_password:
                    validatePassword();
                    break;
                case R.id.input_layout_confpassword:
                    validatePassword();
                    break;
            }
        }
    }
}
