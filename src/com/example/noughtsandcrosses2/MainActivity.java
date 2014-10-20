package com.example.noughtsandcrosses2;

import java.util.Arrays;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.noughtsandcrosses2.players.ManualPlayer;
import com.example.noughtsandcrosses2.players.MasterPlayer;

public class MainActivity extends Activity implements OnClickListener, GamePlayable {

	/** ゲーム盤 */
	private MarubatsuBoard board = new MarubatsuBoard();
	//ボタン入力用
	private int input = 0;
	//勝利した状態用
	private String winner = null;
	//プレーヤー管理用
	private Player p;
	private Player p1;
	private Player p2;
	//ターン数と結果と勝敗数表示用
	private TextView turnText;
	private TextView resultText;
	private TextView winCountText;
	private TextView loseCountText;
	private int winCount1 = 0;
	private int loseCount1 = 0;
	private int winCount2 = 0;
	private int loseCount2 = 0;
	//9マスのボタンと再勝負ボタンと勝敗クリアボタン
	private Button[] gridBtns;
	private Button retryBtn;
	private Button clearBtn;
	//SharedPreference保存用(今までのやり方とちょっと変えた)
	private SharedPreferences pref;
	//ゲームモード管理用
	private int gameMode;
	private TextView gameModeText;
	// 音楽再生用(MediaPlayer)
	private MediaPlayer mediaPlayer;
	// 音楽再生用(SoundPool)
	private SoundPool soundPool;
	private int[] soundIds = new int[SOUND_FILES.length];
	// 効果音の配列
	private static final int[] SOUND_FILES = {
			R.raw.push, R.raw.finish
	};

	/*-----------------------------------------------------------------------*/
	//initSoundPool
	/*-----------------------------------------------------------------------*/
	private void initSoundPool() {
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		for (int i = 0; i < soundIds.length; i++) {
			soundIds[i] = soundPool.load(this, SOUND_FILES[i], 1);
		}
	}

	/*-----------------------------------------------------------------------*/
	//onCreate
	/*-----------------------------------------------------------------------*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//TextView
		turnText = (TextView) findViewById(R.id.turnText);
		resultText = (TextView) findViewById(R.id.resultText);
		winCountText = (TextView) findViewById(R.id.winCountText);
		loseCountText = (TextView) findViewById(R.id.loseCountText);
		gameModeText = (TextView) findViewById(R.id.gameModeText);

		//ButtonView
		gridBtns = new Button[] {
				(Button) findViewById(R.id.btn0),
				(Button) findViewById(R.id.btn1),
				(Button) findViewById(R.id.btn2),
				(Button) findViewById(R.id.btn3),
				(Button) findViewById(R.id.btn4),
				(Button) findViewById(R.id.btn5),
				(Button) findViewById(R.id.btn6),
				(Button) findViewById(R.id.btn7),
				(Button) findViewById(R.id.btn8),
		};
		retryBtn = (Button) findViewById(R.id.retryBtn);
		clearBtn = (Button) findViewById(R.id.clearBtn);

		//各ボタンにOnClickListenerをセット
		for (int i = 0; i < gridBtns.length; i++) {
			gridBtns[i].setOnClickListener(this);
		}
		retryBtn.setOnClickListener(this);
		clearBtn.setOnClickListener(this);

		//Intentで渡ってきたゲームモードの情報を受取る
		Intent intent = getIntent();
		gameMode = (Integer) intent.getSerializableExtra("gameMode");

		//設定のSharedPreferenceを読み込む
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		//データが無いときは0がデフォルトになる
		winCount1 = pref.getInt("winData1", 0);
		loseCount1 = pref.getInt("loseData1", 0);
		winCount2 = pref.getInt("winData2", 0);
		loseCount2 = pref.getInt("loseData2", 0);

		//ゲームモードによって分ける
		if (gameMode == 1) {
			//マニュアル入力 vs 少し賢い自動プレーヤー
			setPlayers(new ManualPlayer("o"), new MasterPlayer("x", board.board));
			//勝敗テキストを設定
			winCountText.setText("自分 " + winCount1 + " 勝");
			loseCountText.setText("CPU " + loseCount1 + " 勝");
			gameModeText.setText("一人でプレイ中！");
//		//最初のターン表示
			turnText.setText(p1.getLabel() + "のターン");
		} else {
			//マニュアル入力 vs マニュアル入力
			setPlayers(new ManualPlayer("o"), new ManualPlayer("x"));
			//勝敗テキストを設定
			winCountText.setText("先手 " + winCount2 + " 勝");
			loseCountText.setText("後手 " + loseCount2 + " 勝");
			gameModeText.setText("二人で対戦中！");
//		//最初のターン表示
			turnText.setText(p1.getLabel() + "のターン");
		}
	}

	/*-----------------------------------------------------------------------*/
	//onResume
	/*-----------------------------------------------------------------------*/
	@Override
	protected void onResume() {
		super.onResume();
		// 音楽再生用(MediaPlayer)
		mediaPlayer = MediaPlayer.create(this, R.raw.play);
		// title音楽再生(ループあり)
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
		//soundPlayer準備
		initSoundPool();
	}

	/*-----------------------------------------------------------------------*/
	//onPause
	/*-----------------------------------------------------------------------*/
	@Override
	protected void onPause() {
		super.onPause();
		//mediaPlayer解放
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
		//soundPool解放
		if (soundPool != null) {
			soundPool.release();
			soundPool = null;
		}
	}

	/*-----------------------------------------------------------------------*/
	//onClick  各ボタンが押された時の処理
	/*-----------------------------------------------------------------------*/
	@Override
	public void onClick(View v) {
		//押されたボタンがgridBtnsに含まれていたら
		if (Arrays.asList(gridBtns).contains(v)) {
			//効果音再生
			soundPool.play(soundIds[0], 1.0f, 1.0f, 1, 0, 1.0f);
			//押されたボタンの配列添字をinputに代入する
			for (int i = 0; i < gridBtns.length; i++) {
				if (v.equals(gridBtns[i])) {
					input = i;
				}
			}
			//マニュアル入力用
			start();
			//自動プレーヤー用
			if (gameMode == 1) {
				start();
			}
		} else if (v.getId() == R.id.retryBtn) {//再勝負ボタン
			//ゲームをリセットして再勝負
			reset();
		} else {//それ以外(ここでは勝敗クリアボタン)
			//SharedPreferencesをクリアして勝敗を0に戻す
			prefClear();
		}
	}

	/*-----------------------------------------------------------------------*/
	//setPlayers  プレイヤー２名をセットする
	/*-----------------------------------------------------------------------*/
	public void setPlayers(Player p1, Player p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	/*-----------------------------------------------------------------------*/
	//start
	/*-----------------------------------------------------------------------*/
	@Override
	public void start() {
		if (board.turn < 9 && winner == null) {
			p = (board.turn % 2 == 0) ? p1 : p2;
			input = p.playTurn(input);
			//選んだ場所がすでに選択済みの場合
			if (!board.isBlank(input)) {
				resultText.setText("ERROR: 既に選択済みです");
				return;
			}
			//selectメソッドで処理
			board.select(input, p);
			gridBtns[input].setText(p.getLabel());
			//プレーヤーによってボタン表示の色を変更(ここではプレーヤー○は青、プレーヤー☓は赤)
			if (board.turn % 2 == 0) {
				gridBtns[input].setTextColor(Color.rgb(0, 0, 255));
			} else {
				gridBtns[input].setTextColor(Color.rgb(255, 0, 0));
			}
			//結果表示欄はプレイ中
			resultText.setText("プレイ中");
			//決着が付いているかをチェック
			winner = board.isWin(p);
			if (winner != null) {//winnerにnull以外が返ってきたら
				//決着済み
				//効果音再生
				soundPool.play(soundIds[1], 1.0f, 1.0f, 1, 0, 1.0f);
				//ひとまず9マスのボタンを押せなくする
				for (int i = 0; i < gridBtns.length; i++) {
					gridBtns[i].setOnClickListener(null);
				}
				//結果表示欄に勝負結果を表示
				resultText.setText("Player： " + p.getLabel() + " の勝ちです");
				//SharedPreferencesに勝敗結果を保存する
				if (p.equals(p1)) {
					if (gameMode == 1) {
						winCount1++;
						winCountText.setText("自分 " + winCount1 + " 勝");
						//SharedPreferencesを更新
						pref.edit().putInt("winData1", winCount1).commit();
					} else {
						winCount2++;
						winCountText.setText("先手 " + winCount2 + " 勝");
						//SharedPreferencesを更新
						pref.edit().putInt("winData2", winCount2).commit();
					}
				} else {
					if (gameMode == 1) {
						loseCount1++;
						loseCountText.setText("CPU " + loseCount1 + " 勝");
						//SharedPreferencesを更新
						pref.edit().putInt("loseData1", loseCount1).commit();
					} else {
						loseCount2++;
						loseCountText.setText("後手 " + loseCount2 + " 勝");
						//SharedPreferencesを更新
						pref.edit().putInt("loseData2", loseCount2).commit();
					}
				}
				//揃ったラインの色を変えてみる
				for (int i = 0; i < winner.length(); i++) {
					gridBtns[Integer.parseInt(winner.substring(i, i + 1))].setBackgroundColor(Color.GREEN);
				}
				return;
			}
			//ターン数を１増やす
			board.turn++;
			if (board.turn == 9) {
				resultText.setText("引き分けです");
				for (int i = 0; i < gridBtns.length; i++) {
					gridBtns[i].setOnClickListener(null);
				}
			}
			//次のプレーヤーのターン表示
			p = (board.turn % 2 == 0) ? p1 : p2;
			turnText.setText(p.getLabel() + "のターン");
		}
	}

	/*-----------------------------------------------------------------------*/
	//reset
	/*-----------------------------------------------------------------------*/
	@Override
	public void reset() {
		//MarubatsuBoardのインスタンスを新しく作りなおす
		board = new MarubatsuBoard();
		//新しく作ったboardを引数にしてプレイヤーもリセットする
		if (gameMode == 1) {
			setPlayers(new ManualPlayer("o"), new MasterPlayer("x", board.board));
		} else {
			setPlayers(new ManualPlayer("o"), new ManualPlayer("x"));
		}
		//新しくターン表示
		turnText.setText(p1.getLabel() + "のターン");
		resultText.setText("リセットしました");
		//ゲーム開始初期状態に戻す
		winner = null;
		for (int i = 0; i < gridBtns.length; i++) {
			gridBtns[i].setOnClickListener(this);
			gridBtns[i].setText("-");
			gridBtns[i].setTextColor(Color.rgb(0, 0, 0));
			gridBtns[i].setBackgroundColor(Color.rgb(204, 204, 204));
		}
	}

	/*-----------------------------------------------------------------------*/
	//reset  SharedPreferencesをクリアする
	/*-----------------------------------------------------------------------*/
	public void prefClear() {
		//ダイアログを表示して本当に処理するか確認
		new AlertDialog.Builder(MainActivity.this)
				.setTitle("本当に勝敗データをクリアしますか？")
				.setNegativeButton("はい", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						if (gameMode == 1) {
							pref.edit().remove("winData1").commit();
							pref.edit().remove("loseData1").commit();
							winCount1 = 0;
							loseCount1 = 0;
							winCountText.setText("自分 " + winCount1 + " 勝");
							loseCountText.setText("CPU " + loseCount1 + " 勝");
						} else {
							pref.edit().remove("winData2").commit();
							pref.edit().remove("loseData2").commit();
							winCount2 = 0;
							loseCount2 = 0;
							winCountText.setText("先手 " + winCount2 + " 勝");
							loseCountText.setText("後手 " + loseCount2 + " 勝");
						}
					}
				})
				.setPositiveButton("いいえ", null)
				.show();
	}
}