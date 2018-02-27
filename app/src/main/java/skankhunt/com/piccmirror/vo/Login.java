package skankhunt.com.piccmirror.vo;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by skankhunt on 2018/1/26.
 */

@Entity(primaryKeys = "loginType")
public class Login {

    @SerializedName("login_type")
    @NonNull
    public final String loginType;

    @SerializedName("login_state")
    @NonNull
    public final boolean loginState;

    public Login(String loginType, boolean loginState) {
        this.loginType = loginType;
        this.loginState = loginState;
    }
}
