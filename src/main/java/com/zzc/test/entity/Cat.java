package com.zzc.test.entity;

/**
 * @author zhengzechao
 * @date 2018/7/3
 * Email ooczzoo@gmail.com
 */
public class Cat {
    private String name;
    private CatHouse catHouse;
    private Integer age;

    public CatHouse getCatHouse() {
        return catHouse;
    }

    public void setCatHouse(CatHouse catHouse) {
        this.catHouse = catHouse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + name + '\'' +
                ", catHouse=" + catHouse +
                ", age=" + age +
                '}';
    }

    public static class CatHouse{
        private String address;

        @Override
        public String toString() {
            return "CatHouse{" +
                    "address='" + address + '\'' +
                    '}';
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
