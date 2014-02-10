package com.slbeat.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class ChatService extends Service {

	XMPPConnection connection;
	
	boolean authenticated;
	
	@Override
    public void onCreate() {
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStart(Intent intent, int startId) {
    	// For time consuming an long tasks you can launch a new thread here...
        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

    }
	
	@Override
	public IBinder onBind(Intent arg0) {
		LocalBinder mBinder = new LocalBinder (this);
		return mBinder;
	}

	public class LocalBinder extends Binder {
		
		ChatService service;

		public LocalBinder (ChatService service)
		{
			this.service = service;
		}

		public ChatService getService (){
			return service;
		}

	}

	public void connect(final String user, final String pass) {
		ConnectionConfiguration config = new ConnectionConfiguration("slbeat.com",5222);

		connection = new XMPPConnection(config);

		new Thread(new Runnable() {
			@Override
			public void run() {
	
				try {
					connection.connect();
					connection.login(user, pass);
					if(connection.isConnected()){
						authenticated = true;
					} else {
						authenticated = false;
					}
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
		
	public void disconnect(){
		if(connection.isConnected()){
			connection.disconnect();
		}  else{
			Toast.makeText(getApplicationContext(), "not connected", Toast.LENGTH_LONG).show();
		}
	}

	public boolean isUserAuthenticated() {
		return authenticated;
	}

}
