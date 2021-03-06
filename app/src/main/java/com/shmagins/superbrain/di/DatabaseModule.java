package com.shmagins.superbrain.di;

import android.content.Context;

import androidx.room.Room;

import com.shmagins.superbrain.common.GameRepository;
import com.shmagins.superbrain.common.DatabaseCallback;
import com.shmagins.superbrain.common.GameDatabase;
import com.shmagins.superbrain.common.GameDao;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    public DatabaseModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    GameDatabase provideGameDatabase() {
        return Room.databaseBuilder(
                appContext,
                GameDatabase.class,
                databaseName
        ).fallbackToDestructiveMigration()
                .addCallback(new DatabaseCallback(appContext))
                .build();
    }

    @Provides
    @Singleton
    GameDao provideGameDao(GameDatabase db) {
        return db.gameDao();
    }

    @Provides
    GameRepository provideGameRepository(GameDatabase gd) {
        return new GameRepository(gd);
    }

    @Component(modules = {
            DatabaseModule.class
    })
    @Singleton
    public interface DatabaseComponent {
        GameRepository getGameRepository();
    }

    private Context appContext;
    private final String databaseName = "super_brain_database.db";
}
