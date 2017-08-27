package game.tictactoe;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TTTGame extends JFrame {
    //creating an object of a singleton
    private static TTTGame instance = new TTTGame();
    private String [] board = new String[9];
    private Player [] player = new Player[2];
    private JPanel boardGUI;
    private JPanel panel;
    private JTextArea logDetails;
    private Player currentPlayer;
    private Square[][] square;
    private Square currentSquare;
    private final String [] MARKS = {"X", "O"};
    private final Color [] COLOR = {Color.BLUE, Color.PINK};
    private String gameStatus;
    //making constructor private so that this class can't be instantiated
    private TTTGame(){
        gameStatus = "running";
        logDetails = new JTextArea(4, 30);
        logDetails.setEditable(false);
        this.add(new JScrollPane(logDetails), BorderLayout.SOUTH);
        boardGUI = new JPanel(new GridLayout(3,3,2,2));//create 3 by 3 board

        square = new Square[3][3];
        initBoard();

        panel = new JPanel();// create a new panel to hold board
        panel.add(boardGUI); // attach TicTacToe board to panel
        add(panel, BorderLayout.NORTH);
        setSize(250,225);
        setVisible(true);

        //creating players
        for(int i = 0; i < 2; i++){
            player[i] = new Player(" ", i);
        }
        currentPlayer = player[0];
    }
    public void initBoard(){
        for(int row = 0; row < 3; row++){
            for(int col = 0; col < 3; col++){
                square[row][col] = new Square(" ", row * 3 + col);
                boardGUI.add(square[row][col]);
            }
        }
        logDetails.append("\nGame Initializing ");
    }
    //get only one instance of the object;
    public static TTTGame getInstance(){
        return instance;
    }

    public void updateBoard(int location) {
        if(!isOccupied(location)){
            String mark = currentPlayer.getMark();
            board[location] = mark;
            currentSquare.setMark(mark);
            logDetails.append("\nMove by " + currentPlayer.getMark());
            if(hasWon(currentPlayer.getMark())){
                gameStatus = GameStatus.GAMEOVER.toString();
                logDetails.append("\nGame Own by: " + currentPlayer.getMark());
            }else if (isDraw()){
                gameStatus = GameStatus.GAMEOVER.toString();
                logDetails.append("\nGame Draw: ");
            }else{
                currentPlayer = player[(currentPlayer.getPlayerNumber() + 1) % 2];
                gameStatus = GameStatus.RUNNING.toString();
            }
        }else{
            logDetails.append("\nInvalid Move!");
        }
    }

    private boolean hasWon(String mark){
        return isMarkInLocation(mark, 0,1,2) || isMarkInLocation(mark, 3,4,5) || isMarkInLocation(mark, 6,7,8)
                || isMarkInLocation(mark,0,3,6) || isMarkInLocation(mark, 1,4,7) || isMarkInLocation(mark, 2,5,8)
                || isMarkInLocation(mark,0,4,8) || isMarkInLocation(mark,2,4,6);
    }

    private boolean isMarkInLocation(String mark, int l1,int l2,int l3){
        return mark.equals(board[l1]) && mark.equals(board[l2]) && mark.equals(board[l3]);
    }

    private boolean isDraw(){
        for(int i = 0; i < board.length; i++){
            if(!isOccupied(i)){
                return false;
            }
        }
        return true;
    }

    private boolean isOccupied(int location){
        return player[0].getMark().equals(board[location]) || player[1].getMark().equals(board[location]);
    }

    public void setCurrentSquare(Square square){
        currentSquare = square;
    }
    public class Player{
        private String name;
        private String mark;
        private int playerNumber;
        private Color color;
        Player(String name, int playerNumber){
            this.name = name;
            this.playerNumber = playerNumber;
            this.mark = MARKS[playerNumber];
            this.color = COLOR[playerNumber];
        }

        public String getMark(){
            return mark;
        }
        public Color getColor(){
            return color;
        }
        public int getPlayerNumber(){
            return playerNumber;
        }
    }
    class Square extends JPanel {

        private String mark;
        private int location;

        public Square(String mark, int location){
            this.mark = mark;
            this.location = location;
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if(gameStatus.equals("running")) {
                        setCurrentSquare(Square.this);
                        updateBoard(getSquareLocation());
                    }
                }
            });
        }

        private int getSquareLocation(){
            return location;
        }
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.setColor(Color.WHITE);
            g.fillRect(0,0,29,29);
            g.setColor(currentPlayer.getColor());
            g.drawString(mark, 11,20);
        }
        @Override
        public Dimension getPreferredSize(){
            return new Dimension(30,30);
        }

        public void setMark(String mark){
            this.mark = mark;
            repaint();
        }
    }

}

