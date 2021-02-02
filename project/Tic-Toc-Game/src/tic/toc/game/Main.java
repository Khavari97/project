
package tic.toc.game;

import java.util.Scanner;

public class Main {

    public Board board;//class Board
    public Scanner input = new Scanner(System.in);
    //constructor
    public Main() {
        board = new Board();
    }
    //main method
    public static void main(String[] args) {
    	Main ticTacToe = new Main();
        ticTacToe.play();
    }
    //
    public void play () {
        while (true) {
            printGameStatus();
            Move();
            if (board.isGameOver()) {
                printWinner();
                break;
            }
        }
    }
    
    private void printGameStatus () {
        System.out.println("\n" + board + "\n");
        System.out.println(board.getTurn().name() + "'s turn.");
    }

    private void Move () {
        if (board.getTurn() == Board.State.A) {
            getPlayerMove();
        } else {
            alphaBetaAdvanced(board);
        }
    }
    public static void alphaBetaAdvanced (Board board) {
        Algorithm.play(board.getTurn(), board, Double.POSITIVE_INFINITY);
    }

    private void getPlayerMove () {
        System.out.print("Enter index: ");

        int index = input.nextInt();

        if (index < 0 || index >= 3* 3) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe index of the move must be between 0 and "
                    + (3 * 3- 1) + ", inclusive.");
        } else if (!board.move(index)) {
            System.out.println("\nInvalid move.");
            System.out.println("\nThe selected index must be blank.");
        }
    }

    private void printWinner () {
        Board.State winner = board.getWinner();

        System.out.println("\n" + board + "\n");

        if (winner == Board.State.any) {
            System.out.println("The TicTacToe is a Draw.");
        } else {
            System.out.println("Player " + winner.toString() + " wins!");
        }
    }
}
