package shaftware.funwithstrangers;

import android.app.Application;

public class Globals extends Application {
    enum Mode
    {
        CHESS, CHECKERS, HANGMAN, TICTACTOE;
    }
    private Mode mode;

    public void setMode(Mode s){
       mode = s;
    }

    public Mode getMode(){
        return mode;
    }
}