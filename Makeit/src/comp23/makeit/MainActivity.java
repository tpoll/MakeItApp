package comp23.makeit;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class MainActivity extends Activity implements OnClickListener {

	private Button playBtn, helpBtn, highBtn;
	private String[] levelNames = { "Easy", "Medium", "Hard" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		playBtn = (Button) findViewById(R.id.play_btn);
		helpBtn = (Button) findViewById(R.id.help_btn);
		highBtn = (Button) findViewById(R.id.high_btn);

		playBtn.setOnClickListener(this);
		helpBtn.setOnClickListener(this);
		highBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		
		if (view.getId() == R.id.play_btn) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setTitle("Choose a level").setSingleChoiceItems(levelNames,
					0, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							startPlay(which);
						}
					});
			AlertDialog ad = builder.create();
			ad.show();
			
		} else if (view.getId() == R.id.help_btn) {
			Intent helpIntent = new Intent(this, HowToPlay.class);
			this.startActivity(helpIntent);
			
		} else if (view.getId() == R.id.high_btn) {
			Intent highIntent = new Intent(this, HighScores.class);
			this.startActivity(highIntent);
		}
	}

	private void startPlay(int chosenLevel) {
		Intent playIntent = new Intent(this, PlayGame.class);
		playIntent.putExtra("level", chosenLevel);
		this.startActivity(playIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_main, container,
					false);
			return rootView;
		}
	}

}
