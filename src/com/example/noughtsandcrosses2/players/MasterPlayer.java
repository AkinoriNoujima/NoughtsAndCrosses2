package com.example.noughtsandcrosses2.players;

import java.util.Random;

import com.example.noughtsandcrosses2.MarubatsuBoard;
import com.example.noughtsandcrosses2.Player;

public class MasterPlayer implements Player {

	String label;
	String[] board;
	MarubatsuBoard marubatsuBoard = new MarubatsuBoard();

	public MasterPlayer(String label, String[] board) {
		this.label = label;
		this.board = board;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public int playTurn(int n) {
		//既にリーチなら勝ちにいく
		for (int i = 0; i < marubatsuBoard.checkLines.length; i++) {
			int[] ck = marubatsuBoard.checkLines[i];
			if (board[ck[0]].equals(label) && board[ck[1]].equals(label) && board[ck[2]].equals("-")) {
				return ck[2];
			} else if (board[ck[0]].equals(label) && board[ck[2]].equals(label) && board[ck[1]].equals("-")) {
				return ck[1];
			} else if (board[ck[1]].equals(label) && board[ck[2]].equals(label) && board[ck[0]].equals("-")) {
				return ck[0];
			}
		}

		//相手がリーチしていたらブロックしにいく
		for (int i = 0; i < marubatsuBoard.checkLines.length; i++) {
			int[] ck = marubatsuBoard.checkLines[i];
			if (!board[ck[0]].equals(label) && !board[ck[1]].equals(label) &&
					!board[ck[0]].equals("-") && !board[ck[1]].equals("-") && board[ck[2]].equals("-")) {
				return ck[2];
			} else if (!board[ck[0]].equals(label) && !board[ck[2]].equals(label) &&
					!board[ck[0]].equals("-") && !board[ck[2]].equals("-") && board[ck[1]].equals("-")) {
				return ck[1];
			} else if (!board[ck[1]].equals(label) && !board[ck[2]].equals(label) &&
					!board[ck[1]].equals("-") && !board[ck[2]].equals("-") && board[ck[0]].equals("-")) {
				return ck[0];
			}
		}

		//上記以外なら選択していない場所を指定
		int selectNumber = new Random().nextInt(9);
		while (!board[selectNumber].equals("-")) {
			selectNumber = new Random().nextInt(9);
		}
		return selectNumber;
	}
}
