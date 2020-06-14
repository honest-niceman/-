public class TestJNI {
    public native static String bitwiseNot(String str);

    static {
        System.load("C:\\Users\\MyDLL.dll");
    }

    public TestJNI(){ }
}

