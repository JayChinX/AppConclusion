package com.qxj.multichannel.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import android.database.Cursor;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Insert//可以返回一个long, 表示新插入项的rowId; 如果参数是数组或者集合, 同时地, 它应该返回long[]或者List<Long>.
    void insertAll(User... users);

    @Delete//可以将这个方法返回int值, 表示从数据库中删除的行数.
    void delete(User user);

    @Update//可以返回一个int值, 表示在数据库中被修改的行数.
    void update(User... users);

    @Query("SELECT first_name, last_name FROM users")
    List<NameTuple> loadFullName();//Room明白: 查询返回了列first_name和last_name, 这些值能够映射到NameTuple为的域中.

    /**
     * LiveData
     *
     * 在执行查询的时候, 经常想要在数据发生改变的时候自动更新UI.
     * 要达到这个目的, 需要在查询方法描述中返回LiveData类型的值.
     * 在数据库更新的时候, Room生成所有必要的代码以更新LiveData.
     */
    @Query("SELECT first_name, last_name FROM users WHERE uid IN (:userIds)")
    LiveData<List<NameTuple>> loadUsersFromRegionsSync(int[] userIds);

    /**
     * RxJava
     *
     *在build.gradle文件中添加依赖: android.arch.persistence.room:rxjava2.
     * 之后, 你可以返回在RxJava2中定义的数据类型
     */
    @Query("SELECT * FROM users WHERE uid = :id LIMIT 1")
    Flowable<User> loadUserById(int id);

    /**
     * 游标查询
     */
    @Query("SELECT * FROM users WHERE uid > :minId LIMIT 5")
    Cursor loadRawUsersOlderThan(int minId);

    /**
     * 查询多个表
     *
     * 执行表联接, 以合并包含借书用户的表和包含在借书数据的表的信息
     */
    @Query("SELECT * FROM book " +
            "INNER JOIN loan ON loan.book_id = book.book_id " +
            "INNER JOIN users ON users.uid = loan.user_id " +
            "WHERE users.last_name LIKE :userName")
    List<Book> findBooksBorrowedByNameSync(String userName);

    @Query("SELECT users.uid AS userId, loan.book_id AS bookId " +
            "FROM users, loan " +
            "WHERE users.uid = loan.user_id")
    LiveData<List<UserLoan>> loadUserAndLoan();


    class UserLoan {
        public String userId;
        public String bookId;
    }

}
