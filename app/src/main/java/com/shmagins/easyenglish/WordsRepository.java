package com.shmagins.easyenglish;

import android.os.AsyncTask;
import android.util.Log;

import com.shmagins.easyenglish.db.Calculation;
import com.shmagins.easyenglish.db.CalculationDatabase;

import java.util.List;


import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WordsRepository {
    CalculationDatabase wdb;
    @Inject
    public WordsRepository(CalculationDatabase wdb) {
        this.wdb = wdb;
    }

    public Observable<List<Calculation>> getAllWords(){
        return wdb.calculationDao().getAll();
    }
    public void insertWords(List<Calculation> calculations) {
        Observable.fromIterable(calculations)
                .subscribeOn(Schedulers.newThread())
                .subscribe(word -> wdb.calculationDao().insert(word),
                        throwable -> Log.d("happy", "insertWords: insertion error"));
    }

    public void updateWords(List<Calculation> calculations){
        Observable.fromIterable(calculations)
                .subscribeOn(Schedulers.newThread())
                .subscribe(word -> wdb.calculationDao().update(word),
                        throwable -> Log.d("happy", "updateWords: update error"));
    }

    public void updateWord(Calculation calculation){
        Observable.just(calculation)
                .subscribeOn(Schedulers.newThread())
                .subscribe(word -> wdb.calculationDao().update(word),
                        throwable -> Log.d("happy", "updateWord: update error"));
    }



    public void deleteAll() {
        DeleteAllTask task = new DeleteAllTask(wdb);
        task.execute();

    }
}

class DeleteAllTask extends AsyncTask<Void, Void, Void> {
    CalculationDatabase wdb;

    public DeleteAllTask(CalculationDatabase wdb) {
        this.wdb = wdb;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        wdb.calculationDao().deleteAll();
        return null;
    }
}