package com.example.noughtsandcrosses2.players;

import com.example.noughtsandcrosses2.MainActivity;
import com.example.noughtsandcrosses2.Player;

public class ManualPlayer implements Player {

	MainActivity ma = new MainActivity();
	String label;

	//文字列labelを引数とするコンストラクタ
	public ManualPlayer(String label) {
		this.label = label;
	}

	//コンストラクタで渡されたlabelを返却するメソッド
	@Override
	public String getLabel() {
		return label;
	}

	//マニュアルプレーヤーが入力を行った時の処理
	@Override
	public int playTurn(int n) {
		return n;
	}
}
