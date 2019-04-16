package com.qxj.multichannel.room;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import android.content.Context;
import androidx.annotation.NonNull;

@Database(entities = {User.class, Book.class, Loan.class}, version = 1, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    private static final String DB_NAME = "UserDatabase.db";

    public abstract UserDao getUserDao();

    private static volatile UserDatabase instance;

    static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static UserDatabase create(final Context context) {
        return Room.databaseBuilder(context,
                UserDatabase.class,
                DB_NAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        //在数据库创建时的操作
                    }
                })
                .allowMainThreadQueries()//允许在主线程查询数据
                .addMigrations(migration)//数据库迁移
                .fallbackToDestructiveMigration()//迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
                .build();
    }

    //数据库升级用的
    static Migration migration = new Migration(1, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            System.out.println("migrate============" + database.getVersion());
            database.execSQL("ALTER TABLE User " + " ADD COLUMN address TEXT");
        }
    };

}
