package annotation;

import java.lang.annotation.*;

/*
注解中可以定义多个参数，参数的定义有以下特点：
访问修饰符必须为public，不写默认为public
该元素的类型只能是基本数据类型、String、Class、枚举类型、注解类型（体现了注解的嵌套效果）以及上述类型的一位数组
该元素的名称一般定义为名词，如果注解中只有一个元素，请把名字起为value（后面使用会带来便利操作）
参数名称后面的()不是定义方法参数的地方，也不能在括号中定义任何参数，仅仅只是一个特殊的语
default代表默认值，值必须和第2点定义的类型一致
如果没有默认值，代表后续使用注解时必须给该类型元素赋值

@Target注解定义注解的使用范围
自定义注解上也可以不使用@Target注解，如果不使用，表示自定义注解可以用在任何地方。

首先元注解@Inherited作为一个元注解，只能修饰其他注解类型（由@Target(ElementType.ANNOTATION_TYPE)决定）。
当用户在一个程序元素类上，使用AnnotatedElement的相关注解查询方法，
查询元注解Inherited修饰的其他注解类型A时，如果这个类本身并没有被注解A修饰，
那么会自动查询这个类的父类是否被注解A修饰。查询过程会沿着类继承链一直向上查找，直到注解A被找到，或者到达继承链顶层（Object）。


 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Ann1 {

}

@Ann1
class UseAnnotation1 {
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Ann2 {
    String name();
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Ann6 {
    String value();
}

@Ann2(name = "我是路人")
public class UseAnnotation {
    @Ann6("年龄")
    static int age;

    public static void main(String[] args) throws NoSuchFieldException {
        for (Annotation annotation : UseAnnotation.class.getAnnotations()) {
            System.out.println(annotation);
        }

        for (Annotation annotation : UseAnnotation.class.getDeclaredField("age").getDeclaredAnnotations()) {
            System.out.println(annotation);
        }
        Ann6 ann6 = UseAnnotation.class.getDeclaredField("age").getAnnotation(Ann6.class);
        System.out.println(ann6.value());
        for (Annotation annotation : UseAnnotation.class.getAnnotationsByType(Ann2.class)) {
            System.out.println(annotation);
        }
    }
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Ann3 {
    String value();
}

@Ann3("我")
class UseAnnotation3 {

}


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Ann4 {
    String[] value();
}

@Ann4("我")
class UseAnnotation4 {

}


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Ann5 {
    String[] name() default {"fkjds", "kk"};

    int[] score() default 1;

    int age() default 20;
}

@Ann4("我")
class UseAnnotation5 {

}