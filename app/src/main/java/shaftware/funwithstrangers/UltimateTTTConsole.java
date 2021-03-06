package shaftware.funwithstrangers;

import java.util.Scanner;

import static shaftware.funwithstrangers.TttLogicBase.Piece;
import static shaftware.funwithstrangers.TttLogicBase.Winner;

public class UltimateTTTConsole {
    public UltimateTTTLogic game;

    public UltimateTTTConsole() {
        game = new UltimateTTTLogic(Piece.X);
    }

    public static void main(String[] args) {
        UltimateTTTConsole consoleDriver = new UltimateTTTConsole();

        consoleDriver.drawBoard();

        Scanner scan = new Scanner(System.in);
        int x;
        int y;

        while (consoleDriver.game.checkWinner() == Winner.IN_PROGRESS) {
            do {
                x = scan.nextInt();
                y = scan.nextInt();
            } while (!consoleDriver.game.pickSpot(x ,y));

            //consoleDriver.game.swapPiece();
            consoleDriver.drawBoard();
        }
    }

    private void drawBoard() {
        System.out.print("   0 1 2    3 4 5    6 7 8");
        System.out.print("\n");
        System.out.print("\n");

        for (int i = 0; i < 9; i++) {
            System.out.print(i + "  ");

            for (int j = 0; j < 9; j++) {
                if (game.getBoardPiece(i, j) == Piece.O) {
                    System.out.print("O ");
                } else if (game.getBoardPiece(i, j) == Piece.X) {
                    System.out.print("X ");
                } else if (game.getBoardPiece(i, j) == Piece.OPEN) {
                    System.out.print("B ");
                } else {
                    System.out.print("D ");
                }

                if ((j + 1) % 3 == 0) {
                    System.out.print(" | ");
                }
            }

            System.out.print("\n");
            if ((i + 1) % 3 == 0) {
                System.out.print("   -------------------------");
                System.out.print("\n");
            }
        }
    }
}
