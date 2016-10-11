package starter.kit.rx.app.network.service;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import starter.kit.rx.app.model.entity.User;

/**
 * Created by YuGang Yang on 06 29, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public interface AuthService {

  /**
   * 登录接口
   *
   * @param phone 手机号码
   * @param password 密码
   * @return Call
   */
  @FormUrlEncoded @POST("/auth/login") Observable<User>
  login(@Field("phone") String phone,
      @Field("password") String password);
}
