import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGUI extends JFrame {
    private JButton[][] buttons = new JButton[3][3]; // Buttons representing each cell in the grid
    private char[][] board = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
    };
    private boolean playerATurn = true; // Variable to track turns (true for Player A, false for Player B)

    // Constructor to set up the Tic-Tac-Toe GUI
    public TicTacToeGUI() {
        setTitle("Tic-Tac-Toe"); // Set window title
        setSize(400, 400); // Set window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation for the window
        setLayout(new GridLayout(3, 3)); // Set grid layout for 3x3 Tic-Tac-Toe board

        // Initialize each button in the grid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton button = new JButton("");
                button.setFont(new Font("Arial", Font.PLAIN, 60)); // Set font size for X and O
                buttons[i][j] = button; // Store button reference
                add(button); // Add button to the grid layout
                int row = i;
                int col = j;

                // Add ActionListener to each button for player moves
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Player A's turn (X)
                        if (board[row][col] == ' ' && playerATurn) { // Check if cell is empty
                            board[row][col] = 'X';
                            button.setText("X");
                            playerATurn = false; // Switch turn to Player B

                            // Check if Player A wins or if it's a draw
                            if (checkWin('X')) {
                                JOptionPane.showMessageDialog(null, "Player A (X) wins!");
                                resetBoard();
                            } else if (isBoardFull()) {
                                JOptionPane.showMessageDialog(null, "It's a draw!");
                                resetBoard();
                            } else {
                                computerMove(); // If no win or draw, Player B (computer) makes a move
                            }
                        }
                    }
                });
            }
        }
    }

    // Computer's move using minimax algorithm
    private void computerMove() {
        int[] bestMove = minimaxMove(); // Calculate best move
        board[bestMove[0]][bestMove[1]] = 'O'; // Update board with best move
        buttons[bestMove[0]][bestMove[1]].setText("O"); // Display O in GUI
        playerATurn = true; // Switch turn back to Player A

        // Check if computer wins or if it's a draw
        if (checkWin('O')) {
            JOptionPane.showMessageDialog(null, "Player B (O) wins!");
            resetBoard();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(null, "It's a draw!");
            resetBoard();
        }
    }

    // Minimax algorithm to calculate the best move for the computer
    public int[] minimaxMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[2]; // Array to store best move coordinates

        // Iterate over each cell to evaluate possible moves
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') { // Check if cell is empty
                    board[i][j] = 'O'; // Make the move
                    int score = minimax(false); // Calculate score for this move
                    board[i][j] = ' '; // Undo the move

                    if (score > bestScore) { // Update best score and move if found a better option
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }

    // Minimax algorithm logic for evaluating moves
    public int minimax(boolean isMaximizing) {
        if (checkWin('O')) return 1; // Return score if computer wins
        if (checkWin('X')) return -1; // Return score if player wins
        if (isBoardFull()) return 0; // Return 0 for a draw

        if (isMaximizing) { // Maximizing for the computer
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'O'; // Simulate move for computer
                        int score = minimax(false); // Recursive call
                        board[i][j] = ' '; // Undo move
                        bestScore = Math.max(score, bestScore); // Choose highest score
                    }
                }
            }
            return bestScore;
        } else { // Minimizing for the player
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = 'X'; // Simulate move for player
                        int score = minimax(true); // Recursive call
                        board[i][j] = ' '; // Undo move
                        bestScore = Math.min(score, bestScore); // Choose lowest score
                    }
                }
            }
            return bestScore;
        }
    }

    // Function to check if a player has won
    public boolean checkWin(char player) {
        // Check rows, columns, and diagonals for a win
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
               (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    // Function to check if the board is full (indicating a draw if no win)
    public boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') return false; // Found an empty cell, so not full
            }
        }
        return true;
    }

    // Reset the game board for a new game
    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' '; // Clear board state
                buttons[i][j].setText(""); // Clear button text
            }
        }
        playerATurn = true; // Set turn to Player A for the new game
    }

    // Main function to initialize the game and display GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TicTacToeGUI().setVisible(true); // Create and display the GUI
            }
        });
    }
}
