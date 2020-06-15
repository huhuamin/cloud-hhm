package com.huhuamin.common.oauth2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName : DbTempUser  //类名
 * @Description : 临时demoDbUser  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-12 09:59  //时间
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DbTempUser {
    private Long id;
    private String userName;
    private String password;


}
