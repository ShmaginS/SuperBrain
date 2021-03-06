package com.shmagins.superbrain.calcgame;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;

import com.shmagins.superbrain.common.GameEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class CalcGame {
    private List<? extends Expression<? extends Number>> expressions;
    private int milliseconds = 0;
    private long startTime = 0;
    private PublishSubject<Pair<Integer, GameEvent>> events;
    private Set<Integer> solved;
    private Set<Integer> failed;
    private CompositeDisposable disposable;

    public static class Rules {
        public static final int SEC_PER_EXPR = 7;

        public static int getStarsForResult(int time, int total, int errors) {
            int stars = 3;
            if (total * SEC_PER_EXPR < time)
                stars--;
            if ((float) (total - errors) * 100.0f / total < 90)
                stars--;
            if ((float) (total - errors) * 100.0f / total < 50)
                stars = 0;
            return stars;
        }
    }

    public int rightCount() {
        return solved.size();
    }

    public int wrongCount() {
        return failed.size();
    }

    public Set<Integer> getSolved() {
        return solved;
    }

    public Set<Integer> getFailed() {
        return failed;
    }

    public void subscribe(Consumer<Pair<Integer, GameEvent>> consumer) {
        disposable.add(events.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer));
    }

    public Expression<? extends Number> getExpression(int i) {
        return expressions.get(i);
    }

    public int getCalculationCount() {
        return expressions.size();
    }

    public CalcGame() {
        this.expressions = new ArrayList<>();
        events = PublishSubject.create();
        solved = new HashSet<>();
        failed = new HashSet<>();
        disposable = new CompositeDisposable();
    }

    public void setExpressions(List<? extends Expression<? extends Number>> expressions) {
        Log.d("CalcGame", "setExpressions " + expressions);
        this.expressions = expressions;
        events.onNext(new Pair<>(this.expressions.size(), GameEvent.UPDATE_ALL));
    }

    public void startGame() {
        startTime = SystemClock.elapsedRealtime();
    }

    public void pauseGame() {
        milliseconds += SystemClock.elapsedRealtime() - startTime;
    }

    public void answer(int i, Number answer) {
        Log.d("CalcGame", "answer: " + i + " " + answer);
        Expression<? extends Number> expression = expressions.get(i);
        if (expression.getValue().equals(answer)) {
            solved.add(i);
        } else {
            failed.add(i);
        }
        if (isFinished()) {
            milliseconds += SystemClock.elapsedRealtime() - startTime;
            events.onNext(new Pair<>(milliseconds, GameEvent.WIN));
            new Handler()
                    .postDelayed(() -> {
                        disposable.dispose();
                    }, 2000);
        }
    }

    public boolean isFinished() {
        Log.d("CalcGame", "isFinished: " + solved.size() + " " + failed.size() + " " + expressions.size());
        return solved.size() + failed.size() == expressions.size();
    }

}
