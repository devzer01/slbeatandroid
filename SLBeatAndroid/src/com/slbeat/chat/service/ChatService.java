package com.slbeat.chat.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.muc.MultiUserChat;

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
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
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
		config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

		connection = new XMPPConnection(config);

		new Thread(new Runnable() {
			@Override
			public void run() {
	
				Looper.prepare();
				
				try {
					connection.connect();
					connection.login(user, pass);
					if(connection.isConnected()){
						
						Intent intent = new Intent("com.slbeat.chat.LOGIN");
						ChatService.this.sendBroadcast(intent);
						
						Toast.makeText(getApplicationContext(), "connected to server", Toast.LENGTH_LONG).show();
						authenticated = true;
						
						MultiUserChat muc2 = new MultiUserChat(connection, "lanka@conference.slbeat.com");

						muc2.join("AndroidBiatch");
						
						muc2.sendMessage("kohomada lamai");
						
						muc2.addParticipantListener(new PacketListener() {

							@Override
							public void processPacket(Packet arg0) {
								System.out.println(arg0.getFrom());
							}
							
						});
						
					} else {
						authenticated = false;
					}
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				
				Looper.loop();
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
