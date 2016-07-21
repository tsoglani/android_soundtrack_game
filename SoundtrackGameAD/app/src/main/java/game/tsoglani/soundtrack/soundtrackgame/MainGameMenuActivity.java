package game.tsoglani.soundtrack.soundtrackgame;

import java.util.ArrayList;


import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainGameMenuActivity extends Activity {
	
	private LinearLayout linear;
	private GameView gameView;
	private static Database database;
	
	/**
 * 
 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		linear= new LinearLayout(this);
		setContentView(linear);
		database= new Database(this);
		SelectEnigmaButton.foundAnsweresId=database.getAllContacts();
		//EnigmaGameMenuActivity.listlevel_1
	
	
		//linear= (LinearLayout)findViewById(R.layout.activity_game_menu);
		gameView= new GameView(this);
		linear.addView(gameView);
		
		
	}


	/**
     * 
     */


	/**
 * 
 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_game_menu, menu);
		return true;
	}
	/**
	 * 
	 */
	@Override
	public void onBackPressed() {
	this.startActivity(new Intent(this,MovieQuiz.class));
	}
}