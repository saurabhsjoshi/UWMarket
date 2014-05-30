package com.collegecode.fragments.additem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.collegecode.fragments.Market;
import com.collegecode.uwmarket.Home;
import com.collegecode.uwmarket.R;

/**
 * Created by saurabh on 5/30/14.
 */
public class ChooseItemFragment extends Fragment {
    Spinner spn_items;
    ArrayAdapter<CharSequence> adapter_items;

    @Override
    public void onCreate(Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_item,container, false);
        getActivity().setTitle("Choose item");

        spn_items = (Spinner) view.findViewById(R.id.spn_ch_item);
        adapter_items = ArrayAdapter.createFromResource(getActivity(),R.array.lbl_spn_items, android.R.layout.simple_spinner_item);
        adapter_items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_items.setAdapter(adapter_items);

        (view.findViewById(R.id.btn_next)).setOnClickListener(new View.OnClickListener() {
            Fragment fragment;
            @Override
            public void onClick(View view) {

                switch (spn_items.getSelectedItemPosition()){
                    //Book
                    case 0:
                        fragment = new AddBookFragment();
                        break;
                    case 1:
                        fragment = new AddClickerFragment();
                        break;
                    default:
                        fragment = new AddBookFragment();
                        break;
                }
                ((Home) getActivity()).changeFragmentwithAnim(fragment, R.anim.enter, R.anim.exit);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_cancel, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fragment_cancel:
                getActivity().setTitle("Market");
                ((Home) getActivity()).changeFragmentwithAnim(new Market(), R.anim.slide_in,R.anim.slide_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
