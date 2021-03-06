package com.rocket.biometrix.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.rocket.biometrix.Login.LocalAccount;

import java.util.LinkedList;
import java.util.List;

/**
 * Exercise Module's implementation of the SQLite database adapter: LocalStorageAccessBase_OLD
 */
public class LocalStorageAccessExercise{

    //Strings that represent table and column names in the database for Exercise X
    public static final String TABLE_NAME = "Exercise";
    public static final String LOCAL_EXERCISE_ID = "LocalExerciseID";//ID used for primary key
    public static final String USER_NAME = "UserName";//Username from shared preferences
    public static final String WEB_EXERCISE_ID = "WebExerciseID";
    public static final String TITLE = "Title";//Title will help co-determine the module mode e.g. Simple mode (yay I walked to the fridge), Gainz mode (weight and reps etc.)
    public static final String TYPE = "Type";//light, cardio, etc.
    public static final String MINUTES = "Minutes";//minutes exercised
    public static final String INTY = "Inty";
    public static final String NOTES = "Notes";
    public static final String DATE = "DateEx";
    public static final String TIME = "TimeEx";

    // All the columns above, see getColumns() below
    public static final String[] columns = {LOCAL_EXERCISE_ID, USER_NAME, WEB_EXERCISE_ID,
            TITLE, TYPE, MINUTES, INTY, NOTES, DATE, TIME};
    //Later, we'll hopefully get to a shared preferences class that stores BMI and weight information.

    public LocalStorageAccessExercise(Context context) {    }

    protected static String createTable() {
        //The SQL to create the table goes here
        return "CREATE TABLE " + TABLE_NAME + " ( " +
                LOCAL_EXERCISE_ID + " integer primary key autoincrement, " +
                USER_NAME + " varchar(50) Not Null, " +
                WEB_EXERCISE_ID + " int Null, " +
                TITLE + " varchar(255) Null, " +
                TYPE + " varchar(140) Null, " +
                MINUTES + " tinyint Null, " +
                INTY + " tinyint Null, " +
                NOTES + " varchar(255) Null, " +
                DATE + " date Not Null, " +
                TIME + " time Not Null" +");";
    }


    //Prints all column names and returns a string array with them in it.
    public static String[] getColumns() {
        return columns;
    }

    /**
     * Makes a call to the base class with the needed parameters to pull out the last primary key
     * entered
     * @param c
     * @return The integer value of the last primary key entered.
     */
    public static int GetLastID(Context c)
    {
        return LocalStorageAccess.getInstance(c).GetLastID(c, LOCAL_EXERCISE_ID, TABLE_NAME);
    }

    /**
     * Tests the passed in ContentValues against the private Strings
     * that represent columns in this class. Then calls parent's insert method
     * <p>
     * If the key values of the passed in cv EXACTLY match your column titles,
     * they will be placed into a new cv that is used as a parameter for the
     * safeInsert method.
     *
     * @param  cv  a ContentValues map with key values that match the private String column names
     * {@see safeInsert} in parent is called from
     */
    public void insertFromContentValues(ContentValues cv, Context c) {

        //Real ContentValues that will be passed to the base class' insert method.
        ContentValues dataToBeInserted = new ContentValues();

        //This is inefficient for more than 100 columns, but we've got a glorified file system so we'll be fine.
        //for each String returned by getColumns method, check if the parameter cv contains a Column String  as a key
        for (String columnName : getColumns()) {
            if (cv.containsKey(columnName)) {
                //if the key pulled out of the parameter cv is equal to any string inside columns:
                dataToBeInserted.put(columnName,cv.getAsString(columnName)); //put the key and its value into the new CV
            }
            else{
                Log.d("insertFromContentValues"," Key " + columnName+" not found");
            }
        }

        //WHERE THE MAGIC HAPPENS //Table name is a string above "Exercise", columns[1] is just any column that can be null, then we pass in the clean cv
        LocalStorageAccess.getInstance(c).safeInsert(TABLE_NAME, columns[1], dataToBeInserted);

    }//end insert

    /**
     * Deletes a the row with the local key that has the passed in value
     * @param context A reference to the context, used to grab database access
     * @param value The value of the key on the row to delete
     * @return The number of rows deleted. Should be 0 on fail and 1 on success. Greater than 1
     * means something went wrong.
     */
    public static int deleteByLocalKeyValue(Context context, int value)
    {
        return LocalStorageAccess.deleteEntryByID(context, TABLE_NAME, LOCAL_EXERCISE_ID, value);
    }

    /**
     * Retrieves the web primary key associated with the local primary key that is associated with
     * value.
     * @param context The current context. Used for database access
     * @param value The value of the local primary key on the desired row
     * @return -1 on failure. Otherwise returns the web primary key
     */
    public static int getWebKeyFromLocalKey(Context context, int value)
    {
        return LocalStorageAccess.getWebKeyFromLocalKey(context, TABLE_NAME, LOCAL_EXERCISE_ID, WEB_EXERCISE_ID, value);
    }

    /**
     * Calls an update for this table using the passed in params
     * @param contentValues The content values that determine the new values for the row
     * @param context The current context, used for database access
     * @param localPrimaryKey The value of the primary key on the row to update
     * @return The number of rows that were updated. Should be either 1 or 0.
     */
    public static int updateFromContentValues(ContentValues contentValues, Context context, Integer localPrimaryKey)
    {
        return LocalStorageAccess.updateTableFromContentValues(context, contentValues, localPrimaryKey, TABLE_NAME, LOCAL_EXERCISE_ID);
    }


    //Get all rows that match date YYYY-MM-DD (pass in date to search, then table you are looking at...)
    public static Cursor selectByDate(String date, Context context){
        return LocalStorageAccess.getInstance(context).selectByDate(date, TABLE_NAME, DATE);
    }

    public static Cursor selectAllDatabyDateRange(String startDate, String endDate, Context context){

        return LocalStorageAccess.getInstance(context).selectAllDatabyDateRange(TABLE_NAME, DATE, startDate, endDate);
    }

    public static String selectAllasStrings(Context context){
        return LocalStorageAccess.getInstance(context).selectALLasStrings(TABLE_NAME, getColumns(), LOCAL_EXERCISE_ID);
    }

    public static List<String[]> getEntries(Context c)
    {
        SQLiteDatabase db = LocalStorageAccess.getInstance(c).getReadableDatabase();

        //Select DATE, TIME, TITLE, TYPE FROM TABLE_NAME ORDER BY DATE DESC
        Cursor cursor = db.query(TABLE_NAME, new String[]{DATE, TIME, TITLE, TYPE}, null, null, null, null, DATE + " DESC, " + TIME + " DESC");

        List<String[]> lst = new LinkedList<String[]>();

        String date, time, title, type;

        //If there is a valid entry move to it
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast())
            {
                date = cursor.getString(0);
                time = cursor.getString(1);
                title = cursor.getString(2);
                type = cursor.getString(3);

                String[] data = {date, time, title, type};
                lst.add(data);

                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return lst;
    }

    /**
     * Updates the ID that is stored locally for reference to the entry on the webserver
     * @param localID The ID number locally
     * @param webID The ID number on the web
     * @param context The context for database updates
     * @param makeToasts Whether to make toasts on failure or not
     * @return True if succeeded, false otherwise
     */
    public static boolean updateWebIDReference(Integer localID, Integer webID, Context context, boolean makeToasts)
    {
        boolean success = true;
        SQLiteDatabase db = LocalStorageAccess.getInstance(context).getWritableDatabase();

        ContentValues webCV = new ContentValues();

        webCV.put(WEB_EXERCISE_ID, webID);

        int num_rows = db.update(TABLE_NAME, webCV, LOCAL_EXERCISE_ID + " = ?", new String[]{localID.toString()});
        db.close();

        if (num_rows < 1)
        {
            Toast.makeText(context, "Could not create reference between web database and local database", Toast.LENGTH_LONG).show();
        }
        else
        {
            if (!LocalStorageAccess.getInstance(context).deleteEntryFromSyncTable(context, TABLE_NAME, localID, true) )
            {
                success = false;
                if(makeToasts) Toast.makeText(context, "Could not update synchronization table", Toast.LENGTH_LONG).show();
            }
        }
        return success;
    }

    /**
     * Returns all rows for the currently logged in user. If no user is logged in, returns the
     * columns for the user "default"
     * @param c The current context
     * @param curUserOnly A boolean value representing whether all users should be displayed (false)
     *                    or only the currently logged in user (true)
     * @return A Cursor to all of the columns for the sleep table for the current user
     */
    public static Cursor selectAll(Context c, boolean curUserOnly)
    {
        return LocalStorageAccess.selectAllEntries(c, TABLE_NAME, DATE + " DESC, " + TIME + " DESC", curUserOnly);
    }

    public static Cursor getMonthEntries(Context c, int year, int month)
    {
        String date = year + "-";
        if(month <10)
            date +="0";
        date += month + "-01";

        //They warned me this would happen, but I always thought "yeah but, it won't happen to {me}"
        String username = LocalAccount.DEFAULT_NAME;
        if (LocalAccount.isLoggedIn()) {
            username = LocalAccount.GetInstance().GetUsername();
        }

        SQLiteDatabase db = LocalStorageAccess.getInstance(c).getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{DATE, TIME, TITLE, TYPE, MINUTES, INTY},
                DATE + " BETWEEN (date(?)) AND (date(?, '+1 month','-1 day')) AND UserName = ?",
                new String[]{date, date, username}, null, null, DATE);

        return cursor;
    }

    /**
     * Slight change to the above method, this returns an array with the information in the
     * column passed in
     * @param c Current context used for database access
     * @param year The year as an int
     * @param month Current month of year as an int
     * @param columnName The name of the column to return
     * @return Returns a cursor that contains the date in column one and the requested column in
     * column index 1
     */
    public static Cursor getMonthEntriesForColumn(Context c, int year, int month, String columnName) {
        String date = year + "-";
        if(month <10)
            date +="0";
        date += month + "-01";

        //Evolution is a 'only good enough' solution.
        String username = LocalAccount.DEFAULT_NAME;
        if (LocalAccount.isLoggedIn()) {
            username = LocalAccount.GetInstance().GetUsername();
        }

        SQLiteDatabase db = LocalStorageAccess.getInstance(c).getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{DATE, columnName},
                DATE + " BETWEEN (date(?)) AND (date(?, '+1 month','-1 day')) AND UserName = ?",
                new String[]{date, date, username}, DATE, null, DATE);

        return cursor;
    }
}
