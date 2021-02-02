package tic.toc.game;

import java.util.HashSet;

public class Board {

	public enum State {
		any, A, B
	}

	public State[][] board;
	public State playersTurn;//turn
	public State winner;
	public HashSet<Integer> movesAvailable;

	public int moveCount;
	public boolean gameOver;

	Board() {
		board = new State[3][3];
		movesAvailable = new HashSet<>();
		reset();
	}

	private void initialize() {
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				board[row][col] = State.any;
			}
		}

		movesAvailable.clear();

		for (int i = 0; i < 3 * 3; i++) {
			movesAvailable.add(i);
		}
	}

	void reset() {
		moveCount = 0;
		gameOver = false;
		playersTurn = State.A;
		winner = State.any;
		initialize();
	}

	public boolean move(int index) {
		int x = index % 3;
		int y = index / 3;
		if (gameOver) {
			throw new IllegalStateException("TicTacToe is over. No moves can be played.");
		}

		if (board[y][x] == State.any) {
			board[y][x] = playersTurn;
		} else {
			return false;
		}

		moveCount++;
		movesAvailable.remove(y * 3 + x);

		if (moveCount == 3 * 3) {
			winner = State.any;
			gameOver = true;
		}
		checkRow(y);
		checkColumn(x);
		checkDiagonalFromTopLeft(x, y);
		checkDiagonalFromTopRight(x, y);

		playersTurn = (playersTurn == State.A) ? State.B : State.A;
		return true;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	State[][] toArray() {
		return board.clone();
	}

	public State getTurn() {
		return playersTurn;
	}

	public State getWinner() {
		if (!gameOver) {
			throw new IllegalStateException("TicTacToe is not over yet.");
		}
		return winner;
	}

	public HashSet<Integer> getAvailableMoves() {
		return movesAvailable;
	}

	private void checkRow(int row) {
		for (int i = 1; i < 3; i++) {
			if (board[row][i] != board[row][i - 1]) {
				break;
			}
			if (i == 3 - 1) {
				winner = playersTurn;
				gameOver = true;
			}
		}
	}

	private void checkColumn(int column) {
		for (int i = 1; i < 3; i++) {
			if (board[i][column] != board[i - 1][column]) {
				break;
			}
			if (i == 3 - 1) {
				winner = playersTurn;
				gameOver = true;
			}
		}
	}

	private void checkDiagonalFromTopLeft(int x, int y) {
		if (x == y) {
			for (int i = 1; i < 3; i++) {
				if (board[i][i] != board[i - 1][i - 1]) {
					break;
				}
				if (i == 3 - 1) {
					winner = playersTurn;
					gameOver = true;
				}
			}
		}
	}

	private void checkDiagonalFromTopRight(int x, int y) {
		if (3 - 1 - x == y) {
			for (int i = 1; i < 3; i++) {
				if (board[3 - 1 - i][i] != board[3 - i][i - 1]) {
					break;
				}
				if (i == 3 - 1) {
					winner = playersTurn;
					gameOver = true;
				}
			}
		}
	}

	public Board getDeepCopy() {
		Board board = new Board();

		for (int i = 0; i < board.board.length; i++) {
			board.board[i] = this.board[i].clone();
		}

		board.playersTurn = this.playersTurn;
		board.winner = this.winner;
		board.movesAvailable = new HashSet<>();
		board.movesAvailable.addAll(this.movesAvailable);
		board.moveCount = this.moveCount;
		board.gameOver = this.gameOver;
		return board;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {

				if (board[y][x] == State.any) {
					sb.append("-");
				} else {
					sb.append(board[y][x].name());
				}
				sb.append(" ");

			}
			if (y != 3 - 1) {
				sb.append("\n");
			}
		}
		return new String(sb);
	}

}

