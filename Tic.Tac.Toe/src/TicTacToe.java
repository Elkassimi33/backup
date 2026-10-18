/**
 * @author elkay
 **/
import javax.swing.JFrame;
import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;


public class TicTacToe {
    int bordWidth = 600;
    int bordHeight = 650;
    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JButton [][] board = new JButton[3][3];
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = "X";
    boolean gameOver = false;
    int turns = 0;
    JButton restart = new JButton("Restart");



    public TicTacToe(){
        frame.setVisible(true);
        frame.setSize(bordWidth,bordHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial",Font.BOLD,50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic Tac Toe");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel,BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3,3));
        boardPanel.setBackground(Color.darkGray);
        frame.add(boardPanel);

        for(int r=0 ;r<3;r++){
            for(int c=0 ;c<3;c++){
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(Color.DARK_GRAY);
                tile.setForeground(Color.green);
                tile.setFont(new Font("Arial",Font.BOLD,120));
                tile.setFocusable(false);

                tile.addActionListener(e -> {
                    if(gameOver){
                        return;
                    }
                    JButton tile1 = (JButton)e.getSource();
                    if(tile1.getText().isEmpty()){

                        tile1.setText(currentPlayer);
                        turns++;
                        checkWinner();
                        if(!gameOver) {
                            currentPlayer = currentPlayer.equals( playerX )? playerO : playerX;
                            textLabel.setText(currentPlayer + "'s turn");
                        }
                    }
                });

            }
        }

        restart.setBackground(Color.darkGray);
        restart.setForeground(Color.white);
        restart.setFont(new Font("Arial",Font.BOLD,70));
        frame.add(restart,BorderLayout.SOUTH);
        restart.addActionListener(e -> {
            gameOver = false;
            for(int r=0 ;r<3;r++){
                for(int c=0 ;c<3 ; c++){
                    board[r][c].setBackground(Color.DARK_GRAY);
                    board[r][c].setForeground(Color.green);
                    board[r][c].setText("");
                }
            }
            playerX = "X";
            playerO = "O";
            currentPlayer = "X";
            textLabel.setText("Tic Tac Toe");
            turns = 0;
        });
    }
    void checkWinner() {
        //horizontal
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText().isEmpty()) continue;
            if (board[r][0].getText().equals(board[r][1].getText()) && board[r][1].getText().equals(board[r][2].getText())) {
                gameOver = true;
                for (int i = 0; i < 3; i++) {
                    setWinner(board[r][i]);
                }
                return;
            }
        }
        //vertical
        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText().isEmpty()) continue;
            if (board[0][c].getText().equals(board[1][c].getText()) && board[1][c].getText().equals(board[2][c].getText())) {
                gameOver = true;
                for (int i = 0; i < 3; i++) {
                    setWinner(board[i][c]);
                }
            }

        }
        //diagonally
        if (board[0][0].getText().equals(board[1][1].getText()) &&
                board[1][1].getText().equals(board[2][2].getText())&&
                !board[0][0].getText().isEmpty()){
            gameOver = true;
            for (int i = 0; i<3 ;i++) {
                setWinner(board[i][i]);
            }
            return;
        }
        //anti-diagonally
        if (board[0][2].getText().equals(board[1][1].getText()) &&
                board[1][1].getText().equals(board[2][0].getText())&&
                !board[0][2].getText().isEmpty()){
                     gameOver = true;
                     setWinner(board[0][2]);
                     setWinner(board[1][1]);
                     setWinner(board[2][0]);
                     return;

        }
        if(turns == 9){
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    setTie(board[r][c]);
                }
            }
            gameOver = true;
        }

    }
    void setWinner(JButton tile ){
        tile.setForeground(Color.white);
        tile.setBackground(Color.gray);
        textLabel.setText(currentPlayer + " is the winner");
    }
    void setTie(JButton tile ){
        tile.setForeground(Color.orange);
        tile.setBackground(Color.gray);
        textLabel.setText("Tie");
    }
}
