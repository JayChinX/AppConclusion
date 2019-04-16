package com.qxj.multichannel.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

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
