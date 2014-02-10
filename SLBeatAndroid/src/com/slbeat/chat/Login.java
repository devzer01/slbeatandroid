package com.slbeat.chat;

import com.slbeat.chat.service.ChatService;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Login extends Activity {
	
	ChatService chatService = null;
	
	private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
        	chatService = ((ChatService.LocalBinder)service).getService();  
            
            if (chatService.isUserAuthenticated() == true)
            {
            	Intent i = new Intent(Login.this, ChatRoom.class);																
				startActivity(i);
				Login.this.finish();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
        	chatService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent = new Intent(Login.this, ChatRoom.class);
		//startActivity(intent);
		startService(new Intent(Login.this, ChatService.class));
		bindService(new Intent(Login.this, ChatService.class), mConnection,
		        Context.BIND_AUTO_CREATE);
        
        Button loginButton = (Button)findViewById(R.id.btnLogin);
        
        loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				chatService.connect("devzer0", "xxx");
			}
        	
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
