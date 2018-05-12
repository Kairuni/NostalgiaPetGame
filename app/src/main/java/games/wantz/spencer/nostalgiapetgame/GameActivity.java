package games.wantz.spencer.nostalgiapetgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {
    public final static String UID = "UID";
    public final static String BREED = "Breed";
    public final static String MAX_HEALTH = "MaxHealth";
    public final static String HEALTH = "Health";
    public final static String MAX_STAMINA = "MaxStamina";
    public final static String STAMINA = "Stamina";
    public final static String MAX_HUNGER = "MaxHunger";
    public final static String HUNGER = "Hunger";
    public final static String MAX_BLADDER = "MaxBladder";
    public final static String BLADDER = "Bladder";

    private Monster mMonster;
    public Monster getMonster() {return mMonster;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();

        Log.d("Game", "Creating monster.");
        /*    public Monster(String mUID, int mBreed, float mMaxHealth, float mHealth, float mMaxStamina,
                   float mStamina, float mMaxHunger, float mHunger, float mMaxBladder, float mBladder) {*/
        mMonster = new Monster(intent.getStringExtra(UID), intent.getIntExtra(BREED, 0),
                               intent.getFloatExtra(MAX_HEALTH, 0.0f), intent.getFloatExtra(HEALTH, 0.0f),
                               intent.getFloatExtra(MAX_STAMINA, 0.0f), intent.getFloatExtra(STAMINA, 0.0f),
                               intent.getFloatExtra(MAX_HUNGER, 0.0f), intent.getFloatExtra(HUNGER, 0.0f),
                               intent.getFloatExtra(MAX_BLADDER, 0.0f), intent.getFloatExtra(BLADDER, 0.0f));
        Log.d("Game", "Created monster.");

    }



}
