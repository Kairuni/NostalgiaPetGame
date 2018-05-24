package games.wantz.spencer.nostalgiapetgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import games.wantz.spencer.nostalgiapetgame.drawing.GameView;

/**
 * The activity used for actually playing the game.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 * @version 0.1, 11 May 2018
 */
public class GameActivity extends AppCompatActivity {

    /** Value of the JSON return for UID. */
    public final static String UID = "UID";
    /** Value of the JSON return for Breed. */
    public final static String BREED = "Breed";
    /** Value of the JSON return for MaxHealth. */
    public final static String MAX_HEALTH = "MaxHealth";
    /** Value of the JSON return for Health. */
    public final static String HEALTH = "Health";
    /** Value of the JSON return for MaxStamina. */
    public final static String MAX_STAMINA = "MaxStamina";
    /** Value of the JSON return for Stamina. */
    public final static String STAMINA = "Stamina";
    /** Value of the JSON return for MaxHunger. */
    public final static String MAX_HUNGER = "MaxHunger";
    /** Value of the JSON return for Hunger. */
    public final static String HUNGER = "Hunger";
    /** Value of the JSON return for MaxBladder. */
    public final static String MAX_BLADDER = "MaxBladder";
    /** Value of the JSON return for Bladder. */
    public final static String BLADDER = "Bladder";

    /** A reference to the player's monster. */
    private Monster mMonster;

    /**
     * Get a reference to the player's monster.
     */
    public Monster getMonster() {
        return mMonster;
    }

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
        mMonster = new Monster(intent.getStringExtra(UID), intent.getIntExtra(BREED, 0),
                intent.getFloatExtra(MAX_HEALTH, 0.0f),
                intent.getFloatExtra(HEALTH, 0.0f),
                intent.getFloatExtra(MAX_STAMINA, 0.0f),
                intent.getFloatExtra(STAMINA, 0.0f),
                intent.getFloatExtra(MAX_HUNGER, 0.0f),
                intent.getFloatExtra(HUNGER, 0.0f),
                intent.getFloatExtra(MAX_BLADDER, 0.0f),
                intent.getFloatExtra(BLADDER, 0.0f));
        Log.d("Game", "Created monster.");
        //View v = getLayoutInflater().inflate(R.layout.activity_game, null, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GameView gvp = findViewById(R.id.game_play_view);
        if (gvp != null) {
            gvp.gameViewPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GameView gvr = findViewById(R.id.game_play_view);
        if (gvr != null) {
            gvr.gameViewResume();
        }
    }
}