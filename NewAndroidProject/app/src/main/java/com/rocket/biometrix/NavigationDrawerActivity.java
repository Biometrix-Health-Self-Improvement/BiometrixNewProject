package com.rocket.biometrix;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rocket.biometrix.DietModule.DietEntry;
import com.rocket.biometrix.DietModule.DietParent;
import com.rocket.biometrix.ExerciseModule.ExerciseEntry;
import com.rocket.biometrix.ExerciseModule.ExerciseParent;
import com.rocket.biometrix.Login.CreateLogin;
import com.rocket.biometrix.Login.GetLogin;
import com.rocket.biometrix.Login.GoogleLogin;
import com.rocket.biometrix.Login.LocalAccount;
import com.rocket.biometrix.MedicationModule.MedicationEntry;
import com.rocket.biometrix.MedicationModule.MedicationParent;
import com.rocket.biometrix.MoodModule.MoodEntry;
import com.rocket.biometrix.MoodModule.MoodParent;
import com.rocket.biometrix.SleepModule.SleepEntry;
import com.rocket.biometrix.SleepModule.SleepParent;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //keeps track of the currently active fragment
    public Fragment activeFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigatoin_drawer_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment frag;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        frag = new HomeScreen();
        transaction.replace(R.id.navigation_drawer_fragment_content, frag, "home");
        transaction.addToBackStack(null);
        transaction.commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment frag = new HomeScreen(); //intialize to homescreen in case something goes wrong it will not crash and just go back to home

        if (id == R.id.nav_mood_module) {
            frag = new MoodParent();
        } else if (id == R.id.nav_sleep_module) {
            frag = new SleepParent();
        } else if (id == R.id.nav_exercise_module) {
            frag = new ExerciseParent();
        } else if (id == R.id.nav_diet_module) {
            frag = new DietParent();
        } else if (id == R.id.nav_medication_module) {
            frag = new MedicationParent();
        } else if (id == R.id.nav_analytics) { //TODO: menu open analytics fragment

        } else if (id == R.id.nav_settings) { //TODO: menu open settings fragment

        } else if (id == R.id.nav_login) {
            frag = new GetLogin();
        } else if (id == R.id.nav_create_account){
            frag = new CreateLogin();
        } else if (id == R.id.nav_google_login){
            frag = new GoogleLogin();
        } else if (id == R.id.nav_logout)
        {
           LogoutUser();
        }
        replaceFragment(frag);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**************************************************************************
     * Changes the action bar text to the string passed in
     * @param title The title the fragment wants the Action Bar to be set to
     **************************************************************************/
    public void setActionBarTitleFromFragment(int title){
        try {
            getSupportActionBar().setTitle(getResources().getString(title));
        } catch (Exception e){}
    }

    /**************************************************************************
     * Replaces the current active fragment with the fragment passed in
     * @param frag a new fragment that has been created before the function is called
     **************************************************************************/
    public void replaceFragment(Fragment frag){ //TODO:addToBackStack tags
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.navigation_drawer_fragment_content, frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**************************************************************************
     *  Manages onClick events for each modules CreateEntry button.
     *  The current fragment is retrieved and identified and replaces with its create entry fragment
     * @param v
     **************************************************************************/
    public void CreateEntryOnClick(View v) {
        //Initialize to home screen in case the fragment active is not found in the following, it will not crash and just go back to home
        Fragment newFragment = new HomeScreen();

        //if fragment exists
        if (activeFragment != null && activeFragment.isVisible()) {

            //Determines which module parent activity is active and then replaces it with its child Entry fragment
            if(activeFragment.getClass() == MoodParent.class) {
                newFragment = new MoodEntry();
            } else if (activeFragment.getClass() == SleepParent.class){
                newFragment = new SleepEntry();
            } else if (activeFragment.getClass() == ExerciseParent.class){
                newFragment = new ExerciseEntry();
            } else if (activeFragment.getClass() == DietParent.class){
                newFragment = new DietEntry();
            } else if (activeFragment.getClass() == MedicationParent.class){
                newFragment = new MedicationEntry();
            }

            //replaces the current fragment with the entry fragment
            replaceFragment(newFragment);
        }
    }

    /**************************************************************************
     *  Manages onClick events for each modules Entry done button.
     *  The current fragment is retrieved and identified and replaces with its parent fragment
     * @param v
     **************************************************************************/
    public void EntryDoneOnClick(View v) {
        //Initialize to home screen in case the fragment active is not found in the following, it will not crash and just go back to home
        Fragment newFragment = new HomeScreen();

        //if fragment exists
        if (activeFragment != null && activeFragment.isVisible()) {
            //Determines which module entry activity is active and then replaces it with its parent fragment
            if(activeFragment.getClass() == MoodEntry.class) {
                ((MoodEntry) activeFragment).onDoneClick(v);
                newFragment = new MoodParent();
            } else if (activeFragment.getClass() == SleepEntry.class){
                ((SleepEntry) activeFragment).onDoneClick(v);
                newFragment = new SleepParent();
            } else if (activeFragment.getClass() == ExerciseEntry.class){
                //TODO: Please implement Callback interface so I'm not forced to conform in a bad way
                //TODO: let's not be a little bitch about it
                ((ExerciseEntry) activeFragment).onDoneClick(v);
                newFragment = new ExerciseParent();
            } else if (activeFragment.getClass() == DietEntry.class){

                newFragment = new DietParent();
            } else if (activeFragment.getClass() == MedicationEntry.class){

                newFragment = new MedicationParent();
            }

            //replaces the current fragment with the parent fragment
            replaceFragment(newFragment);
        }
    }

    public void resetPasswordButtonClick(View v){
        ((com.rocket.biometrix.Login.GetLogin)activeFragment).resetPasswordClick();
    }
    public  void passwordSignIn(View v){
        ((com.rocket.biometrix.Login.GetLogin)activeFragment).okayButtonClick(v);
    }

    public void cancelButton(View v){
        replaceFragment(new HomeScreen());
    }

    public void createAccountButtonClick(View v){
        ((CreateLogin)activeFragment).createAccount();
    }

    /**
     * If the current user is logged in with Biometrix, sign them out. If they are logged in via
     * google this directs them to that page instead.
     */
    private void LogoutUser()
    {
        if (LocalAccount.isLoggedIn())
        {
            if (LocalAccount.isGoogleAccountSignedIn())
            {
                Toast.makeText(getApplicationContext(), "Please logout from the google login page", Toast.LENGTH_LONG).show();
            }
            else
            {
                LocalAccount.Logout();
                Toast.makeText(getApplicationContext(), "Account logged out", Toast.LENGTH_LONG).show();
            }
        }
    }
}
