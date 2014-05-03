package com.prysmradio.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.prysmradio.R;
import com.prysmradio.adapters.PrysmListAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by fxoxe_000 on 03/05/2014.
 */
public abstract class PrysmListFragment<E> extends PrysmFragment implements AdapterView.OnItemClickListener {

    @InjectView(R.id.listView)
    ListView listView;
    @InjectView(R.id.list_progressBar)
    ProgressBar progressBar;

    protected List<E> items;
    protected PrysmListAdapter<E> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list, container, false);

        ButterKnife.inject(this, rootView);

        return rootView;
    }

    protected void notifyDataSetChanged(List<E> newItems){
        items = newItems;
        adapter.addAll(items);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onItemClicked(position);
    }

    abstract void onItemClicked(int position);
}
