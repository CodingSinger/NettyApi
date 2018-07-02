
# 1
Java 提供两种不同的类型：引用类型和原始类型（或内置类型）
八大原始类型：
char,boolean,byte,short,int,long,float,double

byte：8位，最大存储数据量是255，存放的数据范围是-128~127之间。

short：16位，最大数据存储量是65536，数据范围是-32768~32767之间。

int：32位，最大数据存储容量是2的32次方减1，数据范围是负的2的31次方到正的2的31次方减1。

long：64位，最大数据存储容量是2的64次方减1，数据范围为负的2的63次方到正的2的63次方减1。

float：32位，数据范围在3.4e-45~1.4e38，直接赋值时必须在数字后加上f或F。

double：64位，数据范围在4.9e-324~1.8e308，赋值时可以加d或D也可以不加。

boolean：只有true和false两个取值。

char：16位，存储Unicode码，用单引号赋值。
除去原始类型的都是引用类型，包括任何数组


```java


  @Test
    public void testPrimitive() {
        int[] i = new int[]{1,2};


        System.out.println(i.getClass().getComponentType());

        int j = 1;
        Object o = j; //这样可以 会自动装箱
        Object o1 = i; //这样也可以 因为i是数组，不是原始数据类型，则是引用类型
//        Object[] obj = i;   //无法强制 因为当i为原始类型的数组时，只有原始类型的数组才可以进行赋值
        Integer[] is = (Integer[]) ReflectionTool .toObjectArray(i); //toObjectArray将原始类型进行包装成Object
        System.out.println(is);
    }

```

面试题：可以把任何一种数据类型的变量赋给Object类型的变量。 答案是正确的，因为基本类型的也会自动装箱。
//        Object[] obj = i;   //无法强制 因为当i为原始类型的数组时，只有原始类型的数组才可以进行赋值，具体为啥不能，请查看[checkcast](https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.checkcast)

