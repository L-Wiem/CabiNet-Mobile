package com.pfe.cabinet;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.amigold.fundapter.interfaces.ItemClickListener;
import com.google.android.gcm.GCMRegistrar;
import com.kosalgeek.android.json.JsonConverter;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import static com.pfe.cabinet.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.pfe.cabinet.CommonUtilities.SENDER_ID;

public class SubActivity extends AppCompatActivity implements AsyncResponse, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SwipeRefreshLayout swipe_refresh_layout;
    private ListView lvMessagePatient;
    private FunDapter<MessagePatient> adapter;
    private ArrayList<MessagePatient> listMessagePatient;

    private AsyncTask<Void, Void, Void> mRegisterTask;
    private String device_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SubActivity.this, Main3Activity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       // PostResponseAsyncTask ttask = new PostResponseAsyncTask(SubActivity.this, this);

        HashMap postData = new HashMap();

        postData.put("txtUsername", Config.LOGGEDIN_SHARED_PREF );

        PostResponseAsyncTask task = new PostResponseAsyncTask(SubActivity.this,postData, this);

       task.execute("http://YOUR_PROJECT_HOST/api/list-demande-rdv");

        swipe_refresh_layout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setOnRefreshListener(this);

        lvMessagePatient = (ListView)findViewById(R.id.lvMessagePatient);

        registerForContextMenu(lvMessagePatient);

        getDeviceToken();
        /*
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //succès de régistration
                    String token = intent.getStringExtra("token");
                  //  Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                    //echec de registration
                    Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                } else {

                }
            }
        };

        //vérifier l'existance et le bon travail du google play service
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if(ConnectionResult.SUCCESS != resultCode) {
            //vérifier le type d'erreur
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                //donc notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            //travail de device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }

        */
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sub_activity_context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final MessagePatient selectedMessagePatient = adapter.getItem(info.position);

		
		// pour assuer le modification et la suppression de rdv
        if(item.getItemId() == R.id.menuUpdate){//si on selection le menu update (modifier)

            Intent in = new Intent(SubActivity.this, DetailActivity.class); //on va rediriger authomatiquement à détailActivity 
            in.putExtra("messagePatient", selectedMessagePatient);
            startActivity(in);
        }
		//suppression
        else if(item.getItemId() == R.id.menuRemove){//si on selection le menu remove (supprimer)
			
		
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("vous voulez supprimer ça?");//confirmation de suppression
            alert.setPositiveButton("Supprier", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listMessagePatient.remove(selectedMessagePatient);

                    SubActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                    HashMap postData = new HashMap();
                    postData.put("mobile", "android");
                    postData.put("id", "" + selectedMessagePatient.id);
                    // traitement de suppression
                    PostResponseAsyncTask taskRemove =
                            new PostResponseAsyncTask(SubActivity.this, postData, new AsyncResponse() {
                                @Override
                                public void processFinish(String s) {
                                    if(s.contains("Succé")){
                                        Toast.makeText(SubActivity.this, "Removed", Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(SubActivity.this, "Something Wrong.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    taskRemove.execute("http://YOUR_PROJECT_HOST/api/supprimer-demande-rdv");


                }
            });
            alert.setNegativeButton("Cancel", null);
            alert.show();
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


	
	
    @Override
    public void processFinish(String jsonText) {


        listMessagePatient = new JsonConverter<MessagePatient>().toArrayList(jsonText, MessagePatient.class);

        BindDictionary<MessagePatient> dict = new BindDictionary<MessagePatient>();

        //nom
        dict.addStringField(R.id.tvnom, new StringExtractor<MessagePatient>() {
            @Override
            public String getStringValue(MessagePatient messagePatient, int position) {
                return Config.LOGGEDIN_SHARED_PREF;
            }

        }).onClick(new ItemClickListener<MessagePatient>() {
            @Override
            public void onClick(MessagePatient item, int position, View view) {
                Toast.makeText(SubActivity.this, item.nom, Toast.LENGTH_LONG).show();
            }
        });


        dict.addStringField(R.id.tvrdtime, new StringExtractor<MessagePatient>() {
            @Override
            public String getStringValue(MessagePatient messagePatient, int position) {
                return "" + messagePatient.date_rdv;
            }
        });

        dict.addStringField(R.id.tvrddate, new StringExtractor<MessagePatient>() {
            @Override
            public String getStringValue(MessagePatient messagePatient, int position) {
                return "" + messagePatient.heure_rdv;
            }
        });



        adapter = new FunDapter<>(SubActivity.this,
                listMessagePatient, R.layout.message_patient, dict);

        lvMessagePatient.setAdapter(adapter);
        lvMessagePatient.setOnItemClickListener(this);

    }


    @Override
	// la méthode que nous permet d'assuer le refresh du page
    public void onRefresh() {
        swipe_refresh_layout.setRefreshing(true);

        HashMap postData = new HashMap();

        postData.put("txtUsername", Config.LOGGEDIN_SHARED_PREF );
        PostResponseAsyncTask task = new PostResponseAsyncTask(SubActivity.this,postData, this);

        task.execute("http://YOUR_PROJECT_HOST/api/list-demande-rdv");

        swipe_refresh_layout.setRefreshing(false);
    }

	
	
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MessagePatient messagePat = listMessagePatient.get(position);

        Intent in = new Intent(SubActivity.this, DetailActivity.class);
        in.putExtra("messagePatient", messagePat);
        startActivity(in);
    }

    @Override
    protected void onResume() {
        super.onResume();//Always call the superclass method first
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }

    @Override
	// onPause une méthode qui permet de 
	//Stop animations or other ongoing actions that could consume CPU
    protected void onPause() {
        super.onPause();//Always call the superclass method first
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }




    /****************************************************** for notification *************************************/

    private void getDeviceToken() {
        // TODO Auto-generated method stub

        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
        device_token = GCMRegistrar.getRegistrationId(this);


        if (device_token.equals("")) {
            GCMRegistrar.register(this, SENDER_ID);

        }




        mRegisterTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                // Register on our server
                // On server creates a new user
                ServerUtilities.register(SubActivity.this, device_token);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mRegisterTask = null;
            }

        };
        mRegisterTask.execute(null, null, null);

    }

    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
            // Releasing wake lock
            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {

        }

    }




}
