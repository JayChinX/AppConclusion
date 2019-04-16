package com.qxj.multichannel.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Loan {
    @ColumnInfo(name = "user_id")
    public int userId;
    @PrimaryKey
    @ColumnInfo(name = "book_id")
    public int bookId;
}
