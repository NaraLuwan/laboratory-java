package com.github.luwan.code.analysis.google.guava.cache;

/**
 * 构建模式demo
 *
 * @author hechao
 * @date 2020/4/19
 */
public class PersonBuilderDemo {

    private String name;
    private int age;
    private boolean sex;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isSex() {
        return sex;
    }

    public static class Builder {
        private String name;
        private int age;
        private boolean sex;

        public Builder name(String n) {
            name = n;
            return this;
        }

        public Builder age(int a) {
            age = a;
            return this;
        }

        public Builder sex(boolean s) {
            sex = s;
            return this;
        }

        public PersonBuilderDemo build() {
            return new PersonBuilderDemo(this);
        }
    }

    private PersonBuilderDemo(Builder builder) {
        name = builder.name;
        age = builder.age;
        sex = builder.sex;
    }
}
