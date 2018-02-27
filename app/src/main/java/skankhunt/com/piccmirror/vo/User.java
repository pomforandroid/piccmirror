package skankhunt.com.piccmirror.vo;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

/**
 * Created by skankhunt on 2018/1/20.
 */
@Entity(primaryKeys = "id")
public class User {

    @NonNull
    private int id;
    @NonNull
    private String name;
    @NonNull
    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}
