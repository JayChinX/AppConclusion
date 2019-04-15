package com.qxj.commondata.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Loan {
    @ColumnInfo(name = "user_id")
    public int userId;
    @PrimaryKey
    @ColumnInfo(name = "book_id")
    public int bookId;
}
