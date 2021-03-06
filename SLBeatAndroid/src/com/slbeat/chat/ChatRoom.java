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
import android.widget.EditText;

public class ChatRoom extends Activity {

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
            	
            }
        }

        public void onServiceDisconnected(ComponentName className) {
        	chatService = null;
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_room);
		
		bindService(new Intent(ChatRoom.this, ChatService.class), mConnection,
		        Context.BIND_AUTO_CREATE);
		
		final EditText textMessage = (EditText)findViewById(R.id.textMessage);
		Button chat = (Button)findViewById(R.id.btnSend);
		
		chat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chatService.sendMessage(textMessage.getText().toString());
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_room, menu);
		return true;
	}

}
