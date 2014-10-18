package com.example.noughtsandcrosses2;

public class MarubatsuBoard {

	// 012
	// 345
	// 678
	/** ゲーム盤の選択状況 */
	public String[] board = { "-", "-", "-", "-", "-", "-", "-", "-", "-" };
	/** 現在のターン数 */
	public int turn = 0;

	/**
	 * ゲーム盤のnで指定された箇所が空欄であるか？
	 * @param n 0～8までの数値
	 * @return 空欄の場合true, 誰かが選択済みの場合false
	 */
	public boolean isBlank(int n) {
		// TODO フェーズ１で記述
		//一度選択しているとboard[]の中はラベルが入るので"-"だとまだ未選択、それ以外は選択済み
		if (board[n].equals("-")) {
			return true;
		}
		return false;
	}

	/**
	 * ゲーム盤のnで指定された箇所を選択する
	 * @param n 0～8までの数値
	 * @param player nの箇所を選択するプレイヤー
	 */
	public void select(int n, Player player) {
		// TODO フェーズ１で記述
		//盤board[]のn番目にその時のターンのプレーヤーのラベルを入れる
		board[n] = player.getLabel();
	}

	/**
	 * 指定プレイヤーが勝利しているか、ゲーム盤の状態を確認する
	 * @param player 勝利を確認するプレイヤー
	 * @return 勝利している場合にtrue, それ以外はfalse
	 */
	//masterPlayerで使用するためにpublicでここに記載
	public int[][] checkLines = {
			{ 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 },
			{ 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
			{ 0, 4, 8 }, { 2, 4, 6 }
	};

	public String isWin(Player player) {
		// TODO フェーズ１で記述
		//isWinの中身は元のmarubatsuを少し改変
		//現時点ターンでのプレーヤーのラベルを取得してcheckLinesと照合して戻り値を返す
		String winnerLabel = player.getLabel();
		for (int i = 0; i < checkLines.length; i++) {
			int[] ck = checkLines[i];
			if (board[ck[0]].equals(winnerLabel) && board[ck[1]].equals(winnerLabel) && board[ck[2]].equals(winnerLabel)) {
				return "" + ck[0] + ck[1] + ck[2];
			}
		}
		return null;
	}
}
