package com.movie.quiz;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MovieQuiz extends Activity {
	private Database database;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new Database(this);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		addMainMenu();
		
		
	
		
		
	}
		
		
		private void testSound(String txt){
			AssetFileDescriptor afd;
			
			try {
				afd = getAssets().openFd( txt);

				MediaPlayer	player = new MediaPlayer();
				player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
						afd.getLength());
				player.prepare();
				player.start();}catch(Exception e){
					e.printStackTrace();
				}
		}

	/**
 * 
 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_movie_quiz, menu);
		return true;

	}

	/**
 * 
 */
	private void addMainMenu() {
		setContentView(R.layout.activity_movie_quiz);
		Button movieButton = (Button) this.findViewById(R.id.moviesButton);
		Button resetButton = (Button) this.findViewById(R.id.reset_button);
		mainMenuIconPreferences(movieButton, R.drawable.gm_mn);
		movieButton.setOnClickListener(goToGameMenuListener);
		if (database.getCorrectCount() == 0) {
			resetButton.setEnabled(false);
		}
		resetButton.setOnClickListener(resetListener);
	}

	/**
 * 
 */
	private OnClickListener goToGameMenuListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(MovieQuiz.this, MainGameMenuActivity.class));
		}
	};

	/**
		 * 
		 */

	private OnClickListener resetListener = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			

			vibrate(1000);
			AlertDialog.Builder alert;
			alert = new AlertDialog.Builder(MovieQuiz.this);
			alert.setIcon(android.R.drawable.alert_dark_frame);
			alert.setTitle("Reset game");
			alert.setMessage("are you sure you want to reset the game ?");
			alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					while (!reset()) {
					}
					Button button = (Button) v;
					button.setEnabled(false);
					Toast.makeText(MovieQuiz.this,"reset succesfull", Toast.LENGTH_SHORT).show();
				}

			});
			alert.setNegativeButton("No", null);
			alert.show();

		}
	};
	
	private void vibrate(int time){
		try{
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		// Vibrate for ... milliseconds
		v.vibrate(time);
	}catch(Exception e){}
		}

	/**
		 * 
		 */
	private boolean reset() {
		database.removeAll2();
		return (database.getCorrectCount() == 0);
	}

	/**
	 * 
	 * @param button
	 * @param id
	 * @param text
	 */
	private void mainMenuIconPreferences(Button button, int id) {
		button.setTextColor(getResources().getColor(R.color.GreenYellow));
		button.setTextSize(30);
		button.setBackgroundResource(id);
	}

	public void onBackPressed() {
		AlertDialog.Builder alert;
		alert = new AlertDialog.Builder(this);
		alert.setIcon(android.R.drawable.ic_dialog_alert);
		alert.setTitle("Closing Activity");
		alert.setMessage("are you sure you want to close the application ?");

		alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			
				System.runFinalization();
				System.exit(0);
				
			}

		});
		alert.setNegativeButton("No", null);
		alert.show();
	}

}
