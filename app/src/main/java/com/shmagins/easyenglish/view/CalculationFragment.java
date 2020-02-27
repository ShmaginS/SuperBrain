package com.shmagins.easyenglish.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.disposables.Disposable;

import com.shmagins.easyenglish.R;
import com.shmagins.easyenglish.CalculationsAdapter;
import com.shmagins.easyenglish.WordsViewModel;

public class CalculationFragment extends Fragment {

    private Disposable disposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_calculation, container, false);

        RecyclerView recycler = fragmentView
                .findViewById(R.id.word_card_recycler);
        CalculationsAdapter adapter = new CalculationsAdapter();
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recycler);
        WordsViewModel viewModel = new ViewModelProvider(
                getActivity(),
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()
                )).get(WordsViewModel.class);
        disposable = viewModel.getAll()
                .subscribe(adapter::setCalculations);
        return fragmentView;
    }
}