package com.pfe.cabinet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

import static com.pfe.cabinet.CommonUtilities.SENDER_ID;
import static com.pfe.cabinet.CommonUtilities.displayMessage;


public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /** Method called on device registered**/
    @Override
    protected void onRegistered(Context context,String registrationId) {
       	  	    	
         ServerUtilities.register(context, registrationId);
	   
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
               
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {

        /*
        String id_news = intent.getExtras().getString("id_news");   
        String titre = intent.getExtras().getString("title_news");  
        


           */
        displayMessage(context, "Success");
        String message = intent.getExtras().getString("message");
        generateNotification(context, message);
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
//        Log.i(TAG, "Received deleted messages notification");
//        String message = getString(R.string.gcm_deleted, total);
//        displayMessage(context, message);
//        // notifies user
//        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
       
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
       
     
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    @SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message) {
    	
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        

        
//        if(lang.equals("0") || (application.getCurrent_langue().equals("ar")&& lang.equals("1")) || (application.getCurrent_langue().equals("fr")&& lang.equals("2"))){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);        
       

        
        

        
        Notification notification = new Notification(icon, message, when);

        //String title = context.getString(R.string.app_name);

        Intent notificationIntent;
        notificationIntent = new Intent(context, SubActivity.class);





        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, notificationIntent, 0);

        Notification.Builder builder = new Notification.Builder(context);

        builder.setAutoCancel(false);
        builder.setTicker(message);
        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(message);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);

        builder.setNumber(100);
        builder.build();


        Notification myNotication;
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(context, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;



        myNotication = builder.getNotification();
        notificationManager.notify(11, myNotication);

        }
//    }
    
 
    
 
    
  

}
