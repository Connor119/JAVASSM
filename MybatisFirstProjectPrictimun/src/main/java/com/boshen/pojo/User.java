package com.boshen.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//data这个注解可以帮助我们生成get，set，tostring，hashcode，equal等方法
@Data
//添加有参构造，注意添加完有参构造之后无参构造会自动的消失
@AllArgsConstructor
//在这里添加无参构造
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private String possword;

}
