package com.example.noughtsandcrosses2;

/**
 * プレイヤーの挙動を定義したインタフェース
 */
public interface Player {
	/** ラベル(○や×)を示す１文字の文字列 */
	public abstract String getLabel();
	/** 自分のターンで選択するマス(0〜8)を返す */
	public abstract int playTurn(int n);
}
