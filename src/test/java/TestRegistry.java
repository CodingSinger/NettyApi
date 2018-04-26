import com.alibaba.fastjson.JSONObject;
import com.zzc.nettyapi.apiutil.ApiRegistry;
import com.zzc.nettyapi.apiutil.Result;
import com.zzc.test.Controller.UserController;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */
public class TestRegistry {
    int i =10;
    public A a = new A(10);
//    public TestRegistry(int i) {
//    }

    @Test
    public void name() throws Exception {
        ApiRegistry registry = new ApiRegistry();
        System.out.println(ApiRegistry.urlRegistrys);
    }

    @Test
    public void name1() throws Exception {
        Object[] o = new Object[10];
        System.out.println(o.hashCode());


    }

    @Test
    public void name2() throws Exception {
        StringBuilder builder = new StringBuilder();

        builder.append("sas");
        builder.append("hello");
        System.out.println(builder.toString());
    }

    @Test
    public void name3() throws Exception {
        HashMap hs = new HashMap();
        hs.put("1",1);
        hs.put("2",2);
        System.out.println(hs.values().getClass());

    }

    @Test
    public void name5() throws Exception {
        //使用FastJson的必须要有get set方法
        String js = JSONObject.toJSONString(new Result(10,"sas"));
        System.out.println(js);
    }

    @Test
    public void name4() throws Exception {
        Class clzz = UserController.class;
        Method method = Arrays.stream(clzz.getDeclaredMethods()).filter((t)->t.getName().equals("get")).findFirst().get();
        Object instance = clzz.newInstance();
        System.out.println(method.invoke(instance,new Object[]{1,2}));

    }

    public void test(Object ... args) throws Exception {
        System.out.println();


    }

    @Test
    public void name6() throws Exception {
        String[] s = new String[10];
        System.out.println(s.getClass());
        try {
            int i = 1/0;
        } catch (Exception e) {
            throw e;

        }
        Object o = 1;
        System.out.println(o instanceof Number);

    }


    @Test

    public void name8(){

        Integer i = new Integer(2);
        Integer i2 = 2;
        System.out.println(i == i2);
    }

    @Test
    public void name7() throws Exception {
        System.out.println(Stream.of(1,2,3).map(Math::incrementExact));
    }

    @Test
    public void name9() throws Exception {
        int[] is = new int[]{1,2,3};
        Arrays.stream(is).map(i->i*i).forEach(System.out::println);
    }

    public static void main(String[] args) {

//        new TestRegistry();
    }

//
//    public TestRegistry() {
//        //此时会自动将实例变量实例化过程都放到TestRegistry过程中去。
//        this(10);
//    }
}


class A{
    int i ;
    public A(int i) {
        this.i = i;
    }
}

class B extends A{

    int i = 10;
    public B() {

        this(1);
    }

    public B(int i){
        super(i);
    }

}