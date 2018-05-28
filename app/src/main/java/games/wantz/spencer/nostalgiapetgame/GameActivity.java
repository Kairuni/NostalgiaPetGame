package games.wantz.spencer.nostalgiapetgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

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


        // Pass the monster to our GameView, as we don't actually care about it.
        GameView gameView = findViewById(R.id.game_play_view);
        gameView.setMonster((Monster) intent.getSerializableExtra(MONSTER_EXTRA));
    }

    @Override
    protected void onPause() {
        super.onPause();

        GameView gameView = findViewById(R.id.game_play_view);
        if (gameView != null) {
            gameView.gameViewPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        GameView gameView = findViewById(R.id.game_play_view);
        if (gameView != null) {
            gameView.gameViewResume();
        }
    }

    protected void onFeed() {

    }

    protected void onShower() {

    }

    protected void onBathroom() {

    }

    protected void onStats() {

    }
}