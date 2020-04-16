package com.shmagins.easyenglish.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shmagins.easyenglish.BrainApplication;
import com.shmagins.easyenglish.R;
import com.shmagins.easyenglish.adapters.ResultTableAdapter;

public class ResultActivity extends AppCompatActivity {
    public static final int REQUEST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ResultTableAdapter adapter = new ResultTableAdapter(((BrainApplication)getApplication()).getCalcGame());
        RecyclerView recycler = findViewById(R.id.result_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void onPlayAgainClick(View view) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
    }

    public void onCancelClick(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
