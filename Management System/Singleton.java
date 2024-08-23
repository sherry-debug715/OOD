package Management System;

public class Singleton {
        /**
     * @return: The same instance of this class every time
     */
    /*
    Eager singleton 1 
    */
    /*
    * instance is created when the class is loaded by JVM. 
    This happens immediately when the class Solution is referenced in any way 
    (e.g., when calling Solution.getInstance()).
    * This is the simplest and most common form of the Eager Singleton. 
    It works well when the instance creation is simple and doesn’t require complex logic.
    */
    private static final Solution instance = new Solution();
    // private constructor to avoid instantiation from other classes 
    private Solution() {}
    // public method to provide access to the instance 
    public static Solution getInstance() {
       return instance;
    }

    /*
    Eager singleton 2
    */
    /*
    * The instance is also created when the class is loaded, 
    but it is initialized in a static block instead of inline. 
    This means the static block runs when the class is loaded, 
    and the instance is created at that time.
    * This approach is more flexible and allows for additional logic in the static block, 
    which might be useful if instance creation depends on some configuration or other setup steps.
    However, for simple Singletons, this added complexity is usually unnecessary.
    */
    // private static Solution instance;
    // static {
    //     instance = new Solution();
    // }
    // private Solution() {}
    // // public method to provide access to the instance 
    // public static Solution getInstance() {
    //    return instance;
    // }

    /* 
    Lazy singleton 1
    */
    // private static Solution instance;
    // private Solution() {}
    // public static Solution getInstance() {
    //     if (instance == null) {
    //         instance = new Solution();
    //     }
    //     return instance;
    // }

    /* 
    Lazy singleton 2
    */
    //The volatile keyword ensures that changes to the instance variable are visible to all threads immediately after they are made.
    // private static volatile Solution instance;
    // private Solution() {}
    // public static Solution getInstance() {
    //     if (instance == null) {
    //         synchronized(Solution.class) { // synchronized method's logger is the class itself
    //             instance = new Solution();
    //         }
    //     }
    //     return instance;
    // }

    /* 
    Static inner class singleton
    */
    private Solution() {}
    // Static inner class responsible for holding the Singleton instance
    private static class InnerSingleton {
        private static final Solution INSTANCE = new Solution();
    }
    public static Solution getInstance() {
        return InnerSingleton.INSTANCE;
    }
}
