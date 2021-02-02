package tic.toc.game;

class Algorithm {

    public static double maxPly;
    //constructor
    public Algorithm() {}

    public static void play (Board.State player, Board board, double maxPly) {
        Algorithm.maxPly = maxPly;
        alphaBetaPruning(player, board, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
    }

    public static int alphaBetaPruning (Board.State player, Board board, double alpha, double beta, int currentPly) {
        if (currentPly++ == maxPly || board.isGameOver()) {
            return score(player, board, currentPly);
        }

        if (board.getTurn() == player) {
            return getMax(player, board, alpha, beta, currentPly);
        } else {
            return getMin(player, board, alpha, beta, currentPly);
        }
    }

    public static int getMax (Board.State player, Board board, double alpha, double beta, int currentPly) {
        int indexOfBestMove = -1;

        for (Integer theMove : board.getAvailableMoves()) {

            Board modifiedBoard = board.getDeepCopy();
            modifiedBoard.move(theMove);
            int score = alphaBetaPruning(player, modifiedBoard, alpha, beta, currentPly);

            if (score > alpha) {
                alpha = score;
                indexOfBestMove = theMove;
            }

            if (alpha >= beta) {
                break;
            }
        }

        if (indexOfBestMove != -1) {
            board.move(indexOfBestMove);
        }
        return (int)alpha;
    }

    public static int getMin (Board.State player, Board board, double alpha, double beta, int currentPly) {
        int indexOfBestMove = -1;

        for (Integer theMove : board.getAvailableMoves()) {

            Board modifiedBoard = board.getDeepCopy();
            modifiedBoard.move(theMove);

            int score = alphaBetaPruning(player, modifiedBoard, alpha, beta, currentPly);

            if (score < beta) {
                beta = score;
                indexOfBestMove = theMove;
            }

            if (alpha >= beta) {
                break;
            }
        }

        if (indexOfBestMove != -1) {
            board.move(indexOfBestMove);
        }
        return (int)beta;
    }

    public static int score (Board.State player, Board board, int currentPly) {

        Board.State opponent = (player == Board.State.A) ? Board.State.B : Board.State.A;

        if (board.isGameOver() && board.getWinner() == player) {
            return 10 - currentPly;
        } else if (board.isGameOver() && board.getWinner() == opponent) {
            return -10 + currentPly;
        } else {
            return 0;
        }
    }

}

