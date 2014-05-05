package comp23.makeit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import java.util.Arrays;
import java.util.Random;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import android.content.SharedPreferences;

@SuppressLint("SimpleDateFormat")
public class PlayGame extends Activity implements OnClickListener {

	private int level = 0, score = 0, target = 0, current = 0, path_score = 11,
			num1Count = 0, num2Count = 0;
	private final String ADD_OPERATOR = "+", SUBTRACT_OPERATOR = "-",
			MULTIPLY_OPERATOR = "*", DIVIDE_OPERATOR = "÷", POW_OPERATOR = "^";
	private String[] operators = { "+", "-", "*", "÷", "^" };
	private boolean opIn = false;
	private Random random;
	private SharedPreferences gamePrefs;
	public static final String GAME_PREFS = "ArithmeticFile";

	private TextView question, answerTxt, scoreTxt;
	private ImageView response;
	private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9,
			btnPlus, btnSub, btnMult, btnDiv, btnPow, btn0, enterBtn, clearBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playgame);

		question = (TextView) findViewById(R.id.question);
		answerTxt = (TextView) findViewById(R.id.answer);
		response = (ImageView) findViewById(R.id.response);
		scoreTxt = (TextView) findViewById(R.id.score);

		response.setVisibility(View.INVISIBLE);

		// init buttons
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		btn3 = (Button) findViewById(R.id.btn3);
		btn4 = (Button) findViewById(R.id.btn4);
		btn5 = (Button) findViewById(R.id.btn5);
		btn6 = (Button) findViewById(R.id.btn6);
		btn7 = (Button) findViewById(R.id.btn7);
		btn8 = (Button) findViewById(R.id.btn8);
		btn9 = (Button) findViewById(R.id.btn9);
		btn0 = (Button) findViewById(R.id.btn0);
		btnPlus = (Button) findViewById(R.id.btnPlus);
		btnSub = (Button) findViewById(R.id.btnSub);
		btnMult = (Button) findViewById(R.id.btnMult);
		btnDiv = (Button) findViewById(R.id.btnDiv);
		btnPow = (Button) findViewById(R.id.btnPow);
		enterBtn = (Button) findViewById(R.id.enter);
		clearBtn = (Button) findViewById(R.id.clear);

		// listen to buttons
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn9.setOnClickListener(this);
		btn0.setOnClickListener(this);
		btnPlus.setOnClickListener(this);
		btnSub.setOnClickListener(this);
		btnMult.setOnClickListener(this);
		btnDiv.setOnClickListener(this);
		btnPow.setOnClickListener(this);
		enterBtn.setOnClickListener(this);
		clearBtn.setOnClickListener(this);

		if (savedInstanceState != null) {
			level = savedInstanceState.getInt("level");
			int exScore = savedInstanceState.getInt("score");
			scoreTxt.setText("Score: " + exScore);
		} else {
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				int passedLevel = extras.getInt("level", -1);
				if (passedLevel >= 0)
					level = passedLevel;
			}
		}

		random = new Random();
		gamePrefs = getSharedPreferences(GAME_PREFS, 0);

		chooseQuestion();
	}

	// Happens every time a new question is needed
	private void chooseQuestion() {
		answerTxt.setText("= ");
		// easy
		if (level == 0) {
			target = random.nextInt(150 - 0) + 0;
			// medium
		} else if (level == 1) {
			target = random.nextInt(300 - 150) + 150;
			// hard
		} else {
			target = random.nextInt(1200 - 300) + 300;
		}
		path_score = (level + 2) * 3;
		question.setText(Integer.toString(target));
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.enter) {
			String statement = answerTxt.getText().toString();
			current = parseString(statement);

			// reset restriction
			num1Count = 0;
			num2Count = 0;
			opIn = false;

			// Check how parsing went and if user has reached target
			if (current == target) {
				answerTxt.setText("= ");
				scoreTxt.setText("Score: " + (score += path_score));
				response.setVisibility(View.VISIBLE);
				path_score = (level + 2) * 3;
				chooseQuestion();
			} else if (current != 0) {
				path_score -= 1;
				answerTxt.setText("= " + Integer.toString(current));
			} else {
				answerTxt.setText("= ");
			}
			// clear the response
		} else if (view.getId() == R.id.clear) {
			answerTxt.setText("= ");

			// reset restriction
			num1Count = 0;
			num2Count = 0;
			opIn = false;

		} else {
			response.setVisibility(View.INVISIBLE);
			String enteredNum = (view.getTag().toString());

			if (Arrays.asList(operators).contains(enteredNum) && !opIn) {
				opIn = true;
				answerTxt.append("" + enteredNum);
			} else if (!opIn && num1Count < 2) {
				answerTxt.append("" + enteredNum);
				num1Count += 1;
			} else if (opIn && num2Count < 2
					&& !Arrays.asList(operators).contains(enteredNum)) {
				answerTxt.append("" + enteredNum);
				num2Count += 1;
			}
		}
	}

	// Parses response string and evaluates equation
	private int parseString(String statement) {
		int result = 0, stateLength = statement.length();
		String num1 = "", num2 = "", op = "";

		Character[] chars = toCharacterArray(statement);
		boolean opOn = false;
		boolean hasOp = false;

		// Check for bad input
		for (int i = 0; i < stateLength; i++) {
			if (Arrays.asList(operators).contains(String.valueOf(chars[i]))) {
				hasOp = true;
			}
		}

		if (statement == "= " || !hasOp) {
			return 0;
		}

		for (int i = 0; i < stateLength; i++) {
			if (!opOn && Character.isDigit(chars[i])) {
				num1 += String.valueOf(chars[i]);
			} else if (!opOn
					&& Arrays.asList(operators).contains(
							String.valueOf(chars[i]))) {
				opOn = true;
				op = String.valueOf(chars[i]);
			} else if (opOn && Character.isDigit(chars[i])) {
				num2 += String.valueOf(chars[i]);
			}
		}

		// Check for numbers
		if (num1 == "" || num2 == "") {
			return 0;
		}

		int first = Integer.parseInt(num1);
		int second = Integer.parseInt(num2);

		switch (op) {
		case ADD_OPERATOR:
			result = first + second;
			break;
		case SUBTRACT_OPERATOR:
			result = first - second;
			break;
		case MULTIPLY_OPERATOR:
			result = first * second;
			break;
		case DIVIDE_OPERATOR:
			if (second != 0) {
				result = first / second;
			} else {
				return 0;
			}
			break;
		case POW_OPERATOR:
			result = (int) Math.pow(first, second);
		default:
			break;
		}
		if (result > 1000000) {
			return 0;
		}
		return result;
	}

	private void setHighScore() {
		if (score > 0) {

			SharedPreferences.Editor scoreEdit = gamePrefs.edit();
			DateFormat dateForm = new SimpleDateFormat("dd MMMM yyyy");
			String dateOutput = dateForm.format(new Date());
			String scores = gamePrefs.getString("highScores", "");

			if (scores.length() > 0) {
				List<Score> scoreStrings = new ArrayList<Score>();
				String[] exScores = scores.split("\\|");

				for (String eSc : exScores) {
					String[] parts = eSc.split(" - ");
					scoreStrings.add(new Score(parts[0], Integer
							.parseInt(parts[1])));
				}

				Score newScore = new Score(dateOutput, score);
				scoreStrings.add(newScore);
				Collections.sort(scoreStrings);

				StringBuilder scoreBuild = new StringBuilder("");
				for (int s = 0; s < scoreStrings.size(); s++) {
					if (s >= 10)
						break;// only want ten
					if (s > 0)
						scoreBuild.append("|");// pipe separate the score
												// strings
					scoreBuild.append(scoreStrings.get(s).getScoreText());
				}
				// write to prefs
				scoreEdit.putString("highScores", scoreBuild.toString());
				scoreEdit.commit();

			} else {
				scoreEdit.putString("highScores", "" + dateOutput + " - "
						+ score);
				scoreEdit.commit();
			}
		}

	}

	@SuppressLint("UseValueOf")
	private Character[] toCharacterArray(String s) {
		if (s == null) {
			return null;
		}
		Character[] array = new Character[s.length()];
		for (int i = 0; i < s.length(); i++) {
			array[i] = new Character(s.charAt(i));
		}
		return array;
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		int exScore = score;
		savedInstanceState.putInt("score", exScore);
		savedInstanceState.putInt("level", level);
		super.onSaveInstanceState(savedInstanceState);
	}

	protected void onDestroy() {
		setHighScore();
		super.onDestroy();
	}

}
