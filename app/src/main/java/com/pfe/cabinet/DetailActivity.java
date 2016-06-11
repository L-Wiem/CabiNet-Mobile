package com.pfe.cabinet;

import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    EditText    pdate_rdv, pheure_rdv, ppreferences;
    Button btnUpdate;
    private TextInputLayout inputLayoutDRDV,inputLayoutHRDV,inputLayoutPref;

    MessagePatient messagePatient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        inputLayoutHRDV = (TextInputLayout)findViewById(R.id.input_layout_date_heure_rdv);
        inputLayoutDRDV = (TextInputLayout)findViewById(R.id.input_layout_date_date_rdv);
        inputLayoutPref = (TextInputLayout)findViewById(R.id.input_layout_date_preference);

        pdate_rdv = (EditText) findViewById(R.id.pdate_rdv);
        pheure_rdv = (EditText) findViewById(R.id.pheure_rdv);
        ppreferences = (EditText) findViewById(R.id.pprefernces);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        messagePatient = (MessagePatient) getIntent().getSerializableExtra("messagePatient");
        if (messagePatient == null) {
            return;
        }


        pdate_rdv.setText(messagePatient.date_rdv);
        pheure_rdv.setText(messagePatient.heure_rdv);
        ppreferences.setText(messagePatient.preferences);


        pdate_rdv.addTextChangedListener(new MyTextWatcher(pdate_rdv));
        pheure_rdv.addTextChangedListener(new MyTextWatcher(pheure_rdv));
        ppreferences.addTextChangedListener(new MyTextWatcher(ppreferences));


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnUpdate) {

            if (messagePatient == null) {
                return;
            }
            HashMap postData = new HashMap();
            postData.put("mobile", "android");
            postData.put("txtid", "" + messagePatient.id);

            postData.put("txtdate_rdv", pdate_rdv.getText().toString());
            postData.put("txtheure_rdv", pheure_rdv.getText().toString());
            postData.put("txtpreferences", ppreferences.getText().toString());

            PostResponseAsyncTask taskUpdate = new PostResponseAsyncTask(this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    Toast.makeText(DetailActivity.this, s, Toast.LENGTH_LONG).show();
                    if (s.contains("Succé")) {
                        Toast.makeText(DetailActivity.this, "Mise a jours avec succé", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(DetailActivity.this, SubActivity.class);
                        startActivity(in);
                    } else {
                        Toast.makeText(DetailActivity.this, "Probléme de mise a jours.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            taskUpdate.execute("http://YOUR_PROJECT_HOST/api/modifier-demande-rdv");
        }



    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (messagePatient == null) {
            return;
        }
        HashMap postData = new HashMap();
        postData.put("mobile", "android");
        postData.put("txtid", "" + messagePatient.id);

        postData.put("txtdate_rdv", pdate_rdv.getText().toString());
        postData.put("txtheure_rdv", pheure_rdv.getText().toString());
        postData.put("txtpreferences", ppreferences.getText().toString());

        PostResponseAsyncTask taskUpdate = new PostResponseAsyncTask(this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                Toast.makeText(DetailActivity.this, s, Toast.LENGTH_LONG).show();
                if (s.contains("Succé")) {
                    Toast.makeText(DetailActivity.this, "Mise a jours avec succé", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(DetailActivity.this, SubActivity.class);
                    startActivity(in);
                } else {
                    Toast.makeText(DetailActivity.this, "Probléme de mise a jours.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        taskUpdate.execute("http://YOUR_PROJECT_HOST/api/modifier-demande-rdv");
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


        if (ppreferences.getText().toString().trim().isEmpty()) {
            inputLayoutPref.setError(getString(R.string.err_msg_obli));
            requestFocus(ppreferences);
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
