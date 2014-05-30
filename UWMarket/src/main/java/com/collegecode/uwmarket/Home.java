package com.collegecode.uwmarket;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.collegecode.adapters.DrawerListAdapter;
import com.collegecode.barcodescanner.IntentIntegrator;
import com.collegecode.barcodescanner.IntentResult;
import com.collegecode.fragments.Market;
import com.collegecode.fragments.additem.AddBookFragment;
import com.collegecode.fragments.additem.AddClickerFragment;


public class Home extends ActionBarActivity {

    public static int FRAGMENT_MARKET = 0;
    public static int MY_ITEMS = 1;
    public static int MY_REQUESTS = 2;
    public static int ACTIVITY_SETTINGS = 4;
    public static int FRAGMENT_ADD_BOOK = 3;

    private String[] titles;
    private int[] imgs;
    public DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private boolean isActive = true;
    //Titles
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        titles = getResources().getStringArray(R.array.DrawerTitles);
        imgs = new int[]{R.drawable.ic_action_cloud,R.drawable.ic_action_view_as_list,R.drawable.ic_action_forward,R.drawable.ic_action_unread,R.drawable.ic_action_settings};

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        final Activity a = this;
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close)
        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                ActivityCompat.invalidateOptionsMenu(a); // creates call to onPrepareOptionsMenu()
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new DrawerListAdapter(this, titles, imgs));
        //Set listner for actionbar drawer click
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            if(position != ACTIVITY_SETTINGS)
                setTitle(titles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
            //Use Handler to avoid lag in the transaction
            selectItem(position);
        }
    }

    private void selectItem(final int position){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isActive)
                    selectItem_Async(position);
            }
        }, 250);
    }

    public void selectItem_Async(int position) {
        Fragment fragment;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (position){
            case (3):
                fragment = new AddBookFragment();
                break;
            default:
                fragment = new Market();
                break;
        }

        ft.replace(R.id.content_frame, fragment);
        ft.commitAllowingStateLoss();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.action_add) {
            selectItem(FRAGMENT_ADD_BOOK);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Handler handler = new Handler();
        switch (requestCode) {
            case IntentIntegrator.REQUEST_CODE:
                Log.i("BARCODE", "GOT IT!");
                IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode,
                        resultCode, data);
                if (scanResult == null) {
                    return;
                }
                final String result = scanResult.getContents();
                if (result != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            AddBookFragment nb = (AddBookFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                            nb.txt_isbn.setText(result);
                        }
                    });
                }
                break;

            case 214:
                AddBookFragment nb = (AddBookFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if(resultCode == RESULT_OK)
                {
                    final boolean isCamera;
                    if(data == null)
                    {
                        isCamera = true;
                    }
                    else
                    {
                        final String action = data.getAction();
                        if(action == null)
                        {
                            isCamera = false;
                        }
                        else
                        {
                            isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                    }

                    Uri selectedImageUri;
                    if(isCamera)
                    {

                        selectedImageUri = nb.outputFileUri;
                    }
                    else
                    {
                        selectedImageUri = data.getData();
                    }
                    nb.final_path = selectedImageUri;
                    nb.img.setImageBitmap(nb.decodeSampledBitmapFromResource(selectedImageUri, 128, 128));
                    nb.btn_img.setText(" ↻ ");
                }break;

            case 215:
                AddClickerFragment ac = (AddClickerFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if(resultCode == RESULT_OK)
                {
                    final boolean isCamera;
                    if(data == null)
                    {
                        isCamera = true;
                    }
                    else
                    {
                        final String action = data.getAction();
                        if(action == null)
                        {
                            isCamera = false;
                        }
                        else
                        {
                            isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                    }

                    Uri selectedImageUri;
                    if(isCamera)
                    {

                        selectedImageUri = ac.outputFileUri;
                    }
                    else
                    {
                        selectedImageUri = data.getData();
                    }
                    ac.final_path = selectedImageUri;
                    ac.img.setImageBitmap(ac.decodeSampledBitmapFromResource(selectedImageUri, 128, 128));
                    ac.btn_img.setText(" ↻ ");
                }break;
        }
    }

}
