package games.wantz.spencer.nostalgiapetgame.gameplay.data;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;

import java.util.List;
import java.util.ArrayList;


import games.wantz.spencer.nostalgiapetgame.R;
import games.wantz.spencer.nostalgiapetgame.gameplay.actors.Monster;

/**
 * A class to interface with a SQLite database for storing a monster on this device.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 * @version 1.B, 31 May 2018
 */
public class MonsterDB {

    // Constants
    /**
     * The database's name.
     */
    private static final String DB_NAME = "Monster.db";
    /**
     * The table's name.
     */
    private static final String MONSTER_TABLE = "Monsters";
    /**
     * The version of the database.
     */
    private static final int DB_VERSION = 1;

    //Non-Final Field Variables
    /**
     * A helper used to ensure the db exists, and to get the writable form of the db.
     */
    private MonsterDBHelper mMonsterDBHelper;
    /** The actual SQLite db.  */
    private SQLiteDatabase mSQLiteDatabase;

    /**
     * Creates a new MonsterDB.
     *
     * @param context The application context.
     */
    public MonsterDB(Context context) {
        mMonsterDBHelper = new MonsterDBHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mMonsterDBHelper.getWritableDatabase();
    }

    /**
     * Delete all the data from the MONSTER_TABLE
     */
    private void deleteMonsters() {
        mSQLiteDatabase.delete(MONSTER_TABLE, null, null);
    }

    /**
     * Inserts the monster into the sqlite table, returns true on success.
     *
     * @param theMonster the monster to insert into the database.
     */
    public boolean insertMonster(Monster theMonster) {
        // First, delete all the existing bits. We only want one monster in the table for now.
        deleteMonsters();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UID", theMonster.getUID());
        contentValues.put("Breed", theMonster.getBreed());
        contentValues.put("Hatched", theMonster.getHatched());
        contentValues.put("MaxHealth", theMonster.getMaxHealth());
        contentValues.put("Health", theMonster.getHealth());
        contentValues.put("MaxStamina", theMonster.getMaxStamina());
        contentValues.put("Stamina", theMonster.getStamina());
        contentValues.put("MaxHunger", theMonster.getMaxHunger());
        contentValues.put("Hunger", theMonster.getHunger());
        contentValues.put("MaxBladder", theMonster.getMaxBladder());
        contentValues.put("Bladder", theMonster.getBladder());
        contentValues.put("Fun", theMonster.getFun());
        contentValues.put("Dirty", theMonster.getDirty());
        contentValues.put("LAST_ACCESS", System.currentTimeMillis());
        long rowId = mSQLiteDatabase.insert(MONSTER_TABLE, null, contentValues);
        return rowId != -1;
    }

    /**
     * Returns the stored monster, may return more than one monster in the future.
     *
     * @return list
     */
    public Monster getMonster() {
        String[] columns = {
                "*"
        };
        Cursor c = mSQLiteDatabase.query(
                MONSTER_TABLE,  // The table to query
                columns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();

        Monster mon = new Monster(
                c.getString(0),
                c.getInt(1),
                c.getInt(2) == 1,
                c.getFloat(3),
                c.getFloat(4),
                c.getFloat(5),
                c.getFloat(6),
                c.getFloat(7),
                c.getFloat(8),
                c.getFloat(9),
                c.getFloat(10),
                c.getFloat(11),
                c.getFloat(12),
                c.getLong(13)
        );
        return mon;
    }

    /**
     * Helper class for making sure the database exists.
     */
    class MonsterDBHelper extends SQLiteOpenHelper {

        /**
         * The SQL to use if we need to create the monster db.
         */
        private final String CREATE_MONSTER_SQL;
        /**
         * The SQL to use to drop the monster DB.
         */
        private final String DROP_MONSTER_SQL;

        public MonsterDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                               int version) {
            super(context, name, factory, version);

            CREATE_MONSTER_SQL = context.getString(R.string.CREATE_MONSTER_SQL);
            DROP_MONSTER_SQL = context.getString(R.string.DROP_MONSTER_SQL);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_MONSTER_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_MONSTER_SQL);
            onCreate(sqLiteDatabase);
        }
    }
}