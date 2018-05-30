package games.wantz.spencer.nostalgiapetgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

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

    public Monster mMon;

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
        mMon = (Monster) intent.getSerializableExtra(MONSTER_EXTRA);
        gameView.setMonster(mMon);

        button_misc = findViewById((R.id.button_misc));
        button_misc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                int shBreed = mMon.getBreed();
                Float shHealth = mMon.getHealthPerc();
                Float shStamina = mMon.getStaminaPerc();
                Float shHunger = mMon.getHungerPerc();
                Float shBladder = mMon.getBladderPerc();
                Float shFun = mMon.getFunPerc();
                Float shDirty = mMon.getDirtyPerc();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shBreed);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shHealth);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shStamina);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shHunger);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shBladder);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shFun);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shDirty);
                startActivity(Intent.createChooser(shareIntent, "Share using:"));
            }
        });
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