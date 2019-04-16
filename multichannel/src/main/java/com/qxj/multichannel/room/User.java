package com.qxj.multichannel.room;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.graphics.Bitmap;

/**
 * 这里需要使用@Entity来注解该类
 * 至少要有一个主键@PrimaryKey
 */
//primaryKeys复合主键  tableName 表名默认为实体类的名字
@Entity(tableName = "users"
//        primaryKeys = {"first_name", "last_name"}
        //indices 数据索引
//        indices = {@Index("first_name"),
//                @Index(value = {"last_name", "last_name"}),
//                //unique = true 强制唯一属性，防止表有两行数据在列firstName和lastName拥有相同值
//                @Index(value = {"first_name", "last_name"}, unique = true)}
) //表
public class User {
    @PrimaryKey//(autoGenerate = true)//自增，默认false   //主键
    public int uid;
    @ColumnInfo(name = "first_name")//大小写敏感 默认为属性名
    public String firstName;
    @ColumnInfo(name = "last_name")
    public String lastName;

    //复合域  表中还包含列 street state city post_code
    @Embedded
    Address address;
    //如果实体拥有多个相同类型的嵌套域, 你可以通过设置prefix属性保留每一列唯一.
    //然后Room给嵌套对象的每一个列名的起始处添加prefix设置的给定值

    @Ignore//不想持久化的用Ignore注解掉
    public Bitmap picture;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
