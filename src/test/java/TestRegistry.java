import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.zzc.nettyapi.apiutil.ApiRegistry;
import com.zzc.nettyapi.apiutil.Result;
import com.zzc.test.Controller.UserController;
import com.zzc.utils.Condition;
import org.junit.Test;

import java.lang.reflect.Method;
import java.nio.channels.AlreadyBoundException;
import java.util.*;
import java.util.stream.Collectors;
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
    public void name13() throws Exception {
        Arrays.asList(1,2,3,4).stream().forEach(integer -> System.out.println(integer));
        Arrays.asList(1,2,3,4).stream().forEach(System.out::println);

    }

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
        HashMap hs1 = new HashMap();
        System.out.println(hs == hs1);
        System.out.println(hs.getClass() == hs1.getClass());
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
    public void name11() throws Exception {
        ImmutableMap.Builder<String,Object> builder = new ImmutableMap.Builder();
        builder.put("as","sas");
        builder.put("sas",1);
        ImmutableMap<String, Object> build = builder.build();
        System.out.println(build);
        System.out.println(build.get("as"));
//        build.put("sa","sa");
    }

    @Test
    public void name12() throws Exception {
    }


    @Test
    public void name10() throws Exception {
        System.out.println(Number.class.isAssignableFrom(Integer.class));
        Object obj = null;
        System.out.println(Condition.and(1==1,obj == null,"asas"=="asas"));
        System.out.println(Condition.or(1==2,obj!=null,"as"=="as"));
        System.out.println(Condition.and(1==1,obj!=null));

    }

    @Test
    public void name6() throws Exception {
        String[] s = new String[10];
        System.out.println(s.getClass());
//        try {
//            int i = 1/0;
//        } catch (Exception e) {
//            throw e;
//
//        }
        Object o = 1;
        System.out.println(o instanceof Number);

    }

    @Test
    public void name15() throws Exception {
        List<Integer> list1 = new LinkedList<>();
        list1.add(1);
        List<Integer> list = Arrays.asList(1,2,3,4);
        final Map<String, Integer> collect = list.stream().collect(Collectors.toMap(String::valueOf, t -> t));
        System.out.println(collect);
    }

    @Test
    public void test16() {
        Object[] obj = null;
        Integer[] its = new Integer[]{1,2};
        System.out.println(its );
        obj = its;
        System.out.println(Arrays.toString(obj));
    }

    @Test
    public void test_exception() {
//        try{
////            int i = 1/0;
////            throw new RuntimeException("sas");
//
//        }catch (AlreadyBoundException e){
//            System.out.println("catch - "+e.getMessage());
//        }
    }

    @Test

    public void name8(){
        Integer i = new Integer(2);
        Integer i2 = 2;
        System.out.println(i == i2);
        System.out.println(CharSequence.class.isAssignableFrom(String.class));
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