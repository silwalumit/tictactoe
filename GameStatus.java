package game.tictactoe;

public enum GameStatus{
    GAMEOVER("game_over"),
    RUNNING("running");
    private String status;

    private GameStatus(String status){
        this.status = status;
    }

    public String toString(){
        return status;
    }
}
