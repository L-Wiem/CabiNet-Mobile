package com.pfe.cabinet;

import android.content.Intent;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;


import java.util.HashMap;

public class Main3Activity extends AppCompatActivity implements View.OnClickListener {

    EditText  pdate_rdv,pheure_rdv,pprefernces;
    private TextInputLayout  inputLayoutDRDV,inputLayoutHRDV,inputLayoutPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputLayoutHRDV = (TextInputLayout)findViewById(R.id.input_layout_date_heure_rdv);
        inputLayoutDRDV = (TextInputLayout)findViewById(R.id.input_layout_date_date_rdv);
        inputLayoutPref = (TextInputLayout)findViewById(R.id.input_layout_date_preference);

        pdate_rdv = (EditText)findViewById(R.id.pdate_rdv);
        pheure_rdv = (EditText)findViewById(R.id.pheure_rdv);
        pprefernces = (EditText)findViewById(R.id.pprefernces);
        Button btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);




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
        HashMap postData = new HashMap();

        postData.put("txtUsername", Config.LOGGEDIN_SHARED_PREF);
        postData.put("txtdate_rdv", pdate_rdv.getText().toString());
        postData.put("txtheure_rdv", pheure_rdv.getText().toString());
        postData.put("txtpreferences", pprefernces.getText().toString());
        postData.put("mobile", "android");

        PostResponseAsyncTask task = new PostResponseAsyncTask(
                Main3Activity.this,
                postData,
                new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        Toast.makeText(Main3Activity.this, s, Toast.LENGTH_LONG).show();
                        if(s.contains("Succé")){
                            startActivity(new Intent(Main3Activity.this, SubActivity.class));

                        }

                    }
                });
        task.execute("http://YOUR_PROJECT_HOST/api/ajout-demande-rdv");
    }

    private boolean validateName() {




        if (pdate_rdv.getText().toString().trim().isEmpty()) {
            inputLayoutDRDV.setError(getString(R.string.err_msg_obli));
            requestFocus(pdate_rdv);
            return false;
        } else {
            inputLayoutDRDV.setError(null);
        }


        if (pheure_rdv.getText().toString().trim().isEmpty()) {
            inputLayoutHRDV.setError(getString(R.string.err_msg_obli));
            requestFocus(pheure_rdv);
            return false;
        }else {
            inputLayoutHRDV.setErrorEnabled(false);
        }


        if (pprefernces.getText().toString().trim().isEmpty()) {
            inputLayoutPref.setError(getString(R.string.err_msg_obli));
            requestFocus(pprefernces);
            return false;
        } else {
            inputLayoutPref.setError(null);
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


                case R.id.input_layout_date_date_rdv:
                    validateName();
                    break;
                case R.id.input_layout_date_heure_rdv:
                    validateName();
                    break;
                case R.id.input_layout_date_preference:
                    validateName();

            }
        }
    }
}
