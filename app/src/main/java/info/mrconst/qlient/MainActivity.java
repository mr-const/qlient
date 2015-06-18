package info.mrconst.qlient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = MainActivity.class.getName();
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    QuestKeyboard mQuestKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mQuestKeyboard = new QuestKeyboard(this, R.id.keyboardView,
                new int[] {R.xml.numbers_kbd, R.xml.braille_kbd, R.xml.morse_kbd});

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        _showFragment(position);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_anagram);
                break;
            case 2:
                mTitle = getString(R.string.title_tools);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
    public void onBackPressed() {
        if( mQuestKeyboard.isCustomKeyboardVisible() ) mQuestKeyboard.hideCustomKeyboard();
        else
            super.onBackPressed();
    }

    public QuestKeyboard getQuestKeyboard() {
        return mQuestKeyboard;
    }

    class FragmentInfo {
        public Class<? extends Fragment> cls;
        public FragmentInfo(Class<? extends Fragment> cls) {
            this.cls = cls;
        }
    }

    FragmentInfo[] mFragments = {
            new FragmentInfo(AnagramFragment.class),
            new FragmentInfo(ToolFragment.class)
    };

    private void _showFragment(int id) {
        if (id >= mFragments.length) throw new AssertionError();

        Bundle args = new Bundle();
        Fragment fragment;

        try {
            fragment = mFragments[id].cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            Log.e(TAG, "Failed to create fragment instance", e);
            return;
        }

        fragment.setArguments(args);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, fragment)
                .commit();
    }
}
