package com.movie.quiz;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class GameView extends ViewGroup {
	private SelectDifficultyButton[] level = new SelectDifficultyButton[5];
	private Context context;
	private Button reset;

	/**
	 * 
	 * @param context
	 */
	public GameView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	/**
 * 
 */
	private void init() {

		for (int i = 0; i < level.length; i++) {
			level[i] = new SelectDifficultyButton(context, i);
			level[i].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					EnigmaGameMenuActivity
							.setCalledby((String) (((SelectDifficultyButton) v)
									.getText()));
					context.startActivity(new Intent(context,
							EnigmaGameMenuActivity.class));

				}
			});
			addView(level[i]);
			
		}

	}

	/**
	 * 
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = this.getChildCount();
		for (int i = 0; i < childCount; i++) {
			if (getChildAt(i) instanceof SelectDifficultyButton) {
				SelectDifficultyButton btn = (SelectDifficultyButton) getChildAt(i);
				btn.layout(0, i * getHeight() / 6+getHeight()/40, getWidth(), i * getHeight()
						/ 6 + getHeight()/6+getHeight()/55);

			}
		}

	}

}
