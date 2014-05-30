package com.collegecode.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.collegecode.fragments.additem.ChooseItemFragment;
import com.collegecode.uwmarket.Home;
import com.collegecode.uwmarket.R;

/**
 * Created by saurabh on 5/20/14.
 */
public class Market extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sell:
                ((Home) getActivity()).changeFragmentwithAnim(new ChooseItemFragment(), R.anim.slide_in, R.anim.slide_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
