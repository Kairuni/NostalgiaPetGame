package games.wantz.spencer.nostalgiapetgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import games.wantz.spencer.nostalgiapetgame.gameplay.actors.Monster;
import games.wantz.spencer.nostalgiapetgame.gameplay.GameView;

/**
 * The activity used for actually playing the game.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 * @version 0.1, 11 May 2018
 */
public class GameActivity extends AppCompatActivity {
    private static final String GAME_ACTIVITY_LOG = "GAME_ACTIVITY";

    public static final String MONSTER_EXTRA = "MONSTER_EXTRA";

    public Button button_misc;

    /**
     * Makes a new monster using the provided intent.
     *
     * @param savedInstanceState The previously saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Intent intent = getIntent();

        Log.d("Game", "Creating monster.");
        /* public Monster(String mUID, int mBreed, float mMaxHealth, float mHealth, float mMaxStamina,
                float mStamina, float mMaxHunger, float mHunger, float mMaxBladder, float mBladder) {*/

        final GameView gameView = findViewById(R.id.game_play_view);


        final Monster monster;
        ImageButton buttonShare;

        monster = (Monster) intent.getSerializableExtra(MONSTER_EXTRA);

        buttonShare = findViewById(R.id.button_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                        "In Nostalgia Pet, I have a monster of breed " + monster.getBreed() +
                                " that is " + monster.getHealthPercent() + "% healthy, " +
                                monster.getStaminaPercent() + "% ready to roll, " +
                                monster.getHungerPercent() + "% hungry, and " +
                                (100.0f - monster.getBladderPercent()) + "% in need of using the bathroom!"

                );

                shareIntent.setType("text/plain");
                startActivityForResult(Intent.createChooser(shareIntent, "Share using:"), 0);

            }
        });

        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences =
                        getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                // We want this to commit immediately, ignore the error.
                sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                        .commit();
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        findViewById(R.id.button_feed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.doFeed();
            }
        });

        gameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.doHatch();
            }
        });


        // Pass the monster to our GameView, as we don't actually care about it.
        gameView.setMonster(monster);
    }

    @Override
    protected void onPause() {
        super.onPause();

        GameView gameView = findViewById(R.id.game_play_view);
        if (gameView != null) {
            // TODO: Make this save the monster to the databases.
            gameView.gameViewPause();
        }

        // And now, actually go back to the
    }

    @Override
    protected void onResume() {
        super.onResume();

        GameView gameView = findViewById(R.id.game_play_view);
        if (gameView != null) {
            gameView.gameViewResume();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(GAME_ACTIVITY_LOG, "Restarting, transition back to LoginActivity to reset everything.");
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);

        startActivity(intent);
        finish();
    }
}