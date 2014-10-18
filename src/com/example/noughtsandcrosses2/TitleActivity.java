package com.example.noughtsandcrosses2;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

public class TitleActivity extends Activity implements OnClickListener {

	private TextView infoText;
	private Button onePlayBtn;
	private Button twoPlayBtn;

	// 音楽再生用(MediaPlayer)
	private MediaPlayer mediaPlayer;

	/*-----------------------------------------------------------------------*/
	//onCreate
	/*-----------------------------------------------------------------------*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);

		infoText = (TextView) findViewById(R.id.infoText);
		onePlayBtn = (Button) findViewById(R.id.onePlayBtn);
		twoPlayBtn = (Button) findViewById(R.id.twoPlayBtn);
		onePlayBtn.setOnClickListener(this);
		twoPlayBtn.setOnClickListener(this);

		// 透明にするアニメーションを作成
		AlphaAnimation animation_alpha = new AlphaAnimation(1, 0);
		// アニメーション実行時間を指定する（ms）
		animation_alpha.setDuration(1500);
		// アニメーションの起動
		this.infoText.startAnimation(animation_alpha);
		//アニメーションを繰り返す
		animation_alpha.setRepeatCount(Animation.INFINITE);
	}

	/*-----------------------------------------------------------------------*/
	//onResume
	/*-----------------------------------------------------------------------*/
	@Override
	protected void onResume() {
		super.onResume();
		// 音楽再生用(MediaPlayer)
		mediaPlayer = MediaPlayer.create(this, R.raw.opening);
		// title音楽再生(ループあり)
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
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
	}

	/*-----------------------------------------------------------------------*/
	//onClick
	/*-----------------------------------------------------------------------*/
	@Override
	public void onClick(View v) {
		Intent intent;
		//ゲームモードの情報を保持して画面遷移させる
		switch (v.getId()) {
		case R.id.onePlayBtn:
			intent = new Intent(TitleActivity.this, MainActivity.class);
			intent.putExtra("gameMode", 1);
			startActivity(intent);
			break;

		case R.id.twoPlayBtn:
			intent = new Intent(TitleActivity.this, MainActivity.class);
			intent.putExtra("gameMode", 2);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
