package com.viauc.mobapp_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button new_game = (Button) findViewById(R.id.button1);
        Button mode = (Button) findViewById(R.id.button2);
             
        new_game.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
	            intent.setClass(MainActivity.this, GameActivity.class);
				startActivity(intent);
			}
		});           

        mode.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				game_mode();			
			}
		});        
    }
  

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    

    public void game_mode()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Game mode");
        builder.setNegativeButton("2 Players", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Toast.makeText(getApplicationContext(), "Game mode : 2 Players", Toast.LENGTH_SHORT).show();
				GameActivity.game_mode = Constants.MODE_2P;
				GameActivity.score_player_1 = 0;
				GameActivity.score_player_2 = 0;
			}
		});
		builder.setPositiveButton("1 Player", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Toast.makeText(getApplicationContext(), "Game mode : 1 Player", Toast.LENGTH_SHORT).show();
				GameActivity.game_mode = Constants.MODE_1P;
				GameActivity.score_player_1 = 0;
				GameActivity.score_player_2 = 0;
			}
		});
        builder.show();
        return;
    }

}