package concurrency;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(10);

        DateFormatter runnable = new DateFormatter();
        for(int i = 0; i < 100; i++) {
            service.submit(runnable);
        }

        service.shutdown();
    }

    public static class DateFormatter implements Runnable {
        private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        private final ReentrantLock lock = new ReentrantLock();

        /** without locks or synchronized it is highly probable that parse method will throw
         * NumberFormatException because SimpleDateFormat is not thread safe */
        @Override
        public void run() {
            try {
                lock.lock();
                String dateStr = "2022-06-22T10:00:00";
                Date date = df.parse(dateStr);
                System.out.println("Parsed Date " + date);
            } catch (ParseException e) {
                System.out.println("ParseError " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
