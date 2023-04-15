package zhouhongwei;

public class Main {
    public static void main(String[] args) {
        Consumer consumer = new Consumer("admin","admin","tcp://127.0.0.1:61616",500);
        consumer.run();
    }

}