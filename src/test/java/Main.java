/**
 * @author zhengzechao
 * @date 2018/5/4
 * Email ooczzoo@gmail.com
 */
public class Main {
    public static void main(String[] args) {
        Object o = new Object();
        synchronized (o){

        }
        hello();
    }


    public synchronized static void hello(){

    }
}
