package com.example.tictactoe.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.R;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];
    private TextView playerX;
    private TextView playerO;
    private TextView playerXScore;
    private TextView playerOScore;
    private TextView playerXNotifier;
    private TextView playerONotifier;
    private Button restart;
    private Button quit;
    private Button nextRound;
    private boolean playerXTurn = true;
    private int roundCount = 0;
    private String playerXName;
    private String playerOName;
    private int playerXPoints = 0;
    private int playerOPoints = 0;
    private char winner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        playerX = findViewById(R.id.activity_play_player_x_tv);
        playerO = findViewById(R.id.activity_play_player_o_tv);
        playerXScore = findViewById(R.id.activity_play_player_x_score_tv);
        playerOScore = findViewById(R.id.activity_play_player_o_score_tv);
        playerXNotifier = findViewById(R.id.activity_play_player_x_notifier_tv);
        playerONotifier = findViewById(R.id.activity_play_player_o_notifier_tv);
        restart = findViewById(R.id.activity_play_restart_button);
        quit = findViewById(R.id.activity_player_quit_button);
        nextRound = findViewById(R.id.activity_play_next_round_button);

        Intent incomingIntent = getIntent();
        Bundle players = incomingIntent.getBundleExtra("play");
        if (players != null) {
            playerXName = players.getString("playerX");
            playerOName = players.getString("playerO");
            playerX.setText(playerXName);
            playerO.setText(playerOName);
        } else
            Log.e("player activity", "bundle is empty");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "btn_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });
    }

    public void resetGame() {
        playerXScore.setText("");
        playerOScore.setText("");
        nextRound.setVisibility(View.INVISIBLE);
        playerXPoints = 0;
        playerOPoints = 0;
        resetPlayground();
    }

    @Override
    public void onClick(View view) {
        if (!((Button) view).getText().toString().equals("")) {
            return;
        }

        if (playerXTurn) {
            ((Button) view).setText("X");
            setNotifiers(1);
            // ((Button) view).setCompoundDrawables(getResources().getDrawable(R.drawable.ic_cross_80dp),null,null,null);
        } else {
            setNotifiers(2);
            ((Button) view).setText("O");
            // ((Button) view).setCompoundDrawables(getResources().getDrawable(R.drawable.ic_circle_80dp), null, null, null);
        }

        roundCount++;

        if (checkWin()) {
            if (playerXTurn)
                playerXWins();
            else
                playerOWins();
        } else if (roundCount == 9)
            draw();
        else
            playerXTurn = !playerXTurn;
    }

    public void setNotifiers(int state) {
        switch (state) {
            case (0):
                playerXNotifier.setTextColor(getResources().getColor(R.color.colorBlack));
                playerONotifier.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case (1):
                playerXNotifier.setTextColor(getResources().getColor(R.color.colorPrimary));
                playerONotifier.setTextColor(getResources().getColor(R.color.colorBlack));
                break;
            case (2):
                playerXNotifier.setTextColor(getResources().getColor(R.color.colorBlack));
                playerONotifier.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @SuppressLint("SetTextI18n")
    private void playerXWins() {
        Toast.makeText(this, playerXName + " " + getString(R.string.won), Toast.LENGTH_SHORT).show();
        lockButtons();
        playerXPoints++;
        playerXScore.setText(R.string.winner);
        winner = 'X';
        nextRound.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void playerOWins() {
        Toast.makeText(this, playerOName + " " + getString(R.string.won), Toast.LENGTH_SHORT).show();
        lockButtons();
        playerOPoints++;
        playerOScore.setText(R.string.winner);
        winner = 'O';
        nextRound.setVisibility(View.VISIBLE);
    }

    private void draw() {
        Toast.makeText(this, R.string.draw, Toast.LENGTH_SHORT).show();
        lockButtons();
        nextRound.setVisibility(View.VISIBLE);
    }

    private void lockButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setClickable(false);
            }
        }
    }

    private void unlockButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setClickable(true);
            }
        }
    }

    private void resetButtonContents() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }

    private void resetPlayground() {
        resetButtonContents();
        playerXTurn = true;
        roundCount = 0;
        unlockButtons();

    }

    private boolean checkWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (field[0][j].equals(field[1][j])
                    && field[0][j].equals(field[2][j])
                    && !field[0][j].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        return field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("");
    }

    @SuppressLint("SetTextI18n")
    public void nextRound(View view) {
        playerXScore.setVisibility(View.VISIBLE);
        playerOScore.setVisibility(View.VISIBLE);
        playerXScore.setText(Integer.toString(playerXPoints));
        playerOScore.setText(Integer.toString(playerOPoints));
        resetPlayground();
        playerXTurn = winner == 'X';
    }

    public void quit(View view) {
        finishAffinity();
        System.exit(0);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount", roundCount);
        outState.putInt("playerXPoints", playerXPoints);
        outState.putInt("playerOPoints", playerOPoints);
        outState.putBoolean("playerXTurn", playerXTurn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCount");
        playerXPoints = savedInstanceState.getInt("playerXPoints");
        playerOPoints = savedInstanceState.getInt("playerOPoints");
        playerXTurn = savedInstanceState.getBoolean("playerXTurn");
    }
}
