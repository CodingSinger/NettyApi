package com.zzc.test.Controller;

import com.zzc.test.entity.Person;

/**
 * @author zhengzechao
 * @date 2018/3/31
 */
public class UserController {

    public String get(int uid,int noteid){
        System.out.println(uid);
        System.out.println("change asasssfdddasas");
        return "uid"+uid+"--noteid"+noteid;
    }
    public String getPerson(Person person){
        System.out.println(person.getName());
        return person.toString();
    }

    public String getPersonStrng(Person person,Integer age){
        return person.toString()+" "+age;

    }
}
