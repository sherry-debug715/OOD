package Management System;

public class CommonSingletonPatternClass {
    public String toString() {
        return "This is a Single Instance Class.";
    }
    // Lazy Initialization
    // write your code here
    private static SingleClass instance;
    // The constructor private SingleClass() is private, so no other class can create a new instance of SingleClass directly.
    private SingleClass() {}
    public static SingleClass getInstance() {
        //The instance is created lazily, meaning it is not created until it is needed.
        if (instance == null) {
            synchronized (SingleClass.class) {
                instance = new SingleClass();
            }
        }
        return instance;
    }
}
