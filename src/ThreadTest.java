import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadTest {
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public static void main(String[] args) {
        new ThreadTest().test();
    }
   public void test(){
        new threadA().start();
        new threadB().start();
    }

    class threadA extends  Thread{
        @Override
        public void run() {
            for (int i = 'a';i <= 'z';i++){
                lock.lock();
                System.out.print((char)i);
                condition.signalAll();
                try {
                    condition.await();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }
            }
        }
    }

    class threadB extends Thread{
        @Override
        public void run() {
            for (int i =1; i <= 26;i++){
                lock.lock();
                System.out.println(i);
                condition.signalAll();
                try {
                    condition.await();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                    //为了避免Lock对象的lock()后，在后续执行流程中抛出异常而无法解除锁定，
                    // 一定要在finally中调用Lock对象的unlock().
                }
            }
        }
    }
}
