package com.qxj.commondata.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

//ForeignKey  定义了和实体user的关系
@Entity(foreignKeys = @ForeignKey(entity = User.class, parentColumns = "uid", childColumns = "user_id"))
public class Book {
    @PrimaryKey
    @ColumnInfo(name = "book_id")
    public int bookId;

    public String title;

    @ColumnInfo(name = "user_id")
    public int userId;
}
