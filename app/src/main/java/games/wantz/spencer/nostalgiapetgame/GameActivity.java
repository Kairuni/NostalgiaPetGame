package games.wantz.spencer.nostalgiapetgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import games.wantz.spencer.nostalgiapetgame.gameplay.actors.Monster;
import games.wantz.spencer.nostalgiapetgame.gameplay.GameView;

/**
 * The activity used for actually playing the game.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 * @version 1.B, 31 May 2018
 */
public class GameActivity extends AppCompatActivity {

    //Final Field Variables
    /** Tag used for logging. */
    private static final String GAME_ACTIVITY_LOG = "GAME_ACTIVITY";
    /** Used to pass in the monster data via intent. */
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

        final GameView gameView = findViewById(R.id.game_play_view);

        final Monster monster;
        ImageButton buttonShare;

        /* Retrieve the monster from the intent. */
        monster = (Monster) intent.getSerializableExtra(MONSTER_EXTRA);

        buttonShare = findViewById(R.id.button_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Builds the intent to share the status of our creature, then calls that intent. */
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

        findViewById(R.id.button_shower).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.doShower();
            }
        });

        findViewById(R.id.button_bathroom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.doToilet();
            }
        });


        findViewById(R.id.button_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.doGame();
            }
        });


        findViewById(R.id.button_stats).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.toggleStats();
            }
        });


        gameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.doClick();
            }
        });

        // Pass the monster to our GameView, as we don't actually care about it.
        gameView.setMonster(monster);
    }

    /**
     * Called when the app pauses, calls gameView.gameViewPause().
     */
    @Override
    protected void onPause() {
        super.onPause();
        GameView gameView = findViewById(R.id.game_play_view);
        if (gameView != null) {
            gameView.gameViewPause();
            new UpdateMonsterAsyncTask().execute(gameView.buildMonsterURL());
        }
    }

    /**
     * Called when the app resumes, calls gameView.gameViewResume().
     */
    @Override
    protected void onResume() {
        super.onResume();
        GameView gameView = findViewById(R.id.game_play_view);
        if (gameView != null) {
            gameView.gameViewResume();
        }
    }

    /**
     * On restart, transitions back to LoginActivity to prevent assets from breaking.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(GAME_ACTIVITY_LOG, "Restarting, transition back to LoginActivity to reset everything.");
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * UpdateMonsterAsyncTask updates the remote server via the provided URL.
     */
    public class UpdateMonsterAsyncTask extends AsyncTask<String, Void, String> {

        /** Tag for logging. */
        private final String UPDATE_TASK_LOG = "UPDATE_TASK";

        /** Pre-Execute, simply calls super.onPreExecute(). */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Accesses the provided URLs and retrieves the response.
         *
         * @param urls The URLs to access.
         * @return The response from those URLs.
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to update monster, reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * Tests if the request succeeds via JSON objects.
         *
         * @param result The result from the HTTP query.
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            Log.d(UPDATE_TASK_LOG, result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Log.d(UPDATE_TASK_LOG, "Successfully updated.");
                } else {
                    Log.d(UPDATE_TASK_LOG, "Failed to update: " + jsonObject.get("error"));
                }
            } catch (JSONException e) {
                Log.d(UPDATE_TASK_LOG, "Something wrong with the data: " + e.getMessage());
            }
        }
    }
}