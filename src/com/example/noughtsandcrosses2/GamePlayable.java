package com.example.noughtsandcrosses2;

/**
 * ゲームとしてプレイ可能なことを示すインタフェース
 */
public interface GamePlayable {
	/** ゲームを開始する */
	public void start();
	/** ゲームを開始していない状態にリセットする */
	public void reset();
}