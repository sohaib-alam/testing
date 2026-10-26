package lab;

/** Optional JNI fixture. The memory corruption happens inside native C code. */
public class NativeOverflow {
    static {
        System.loadLibrary("scannerfixture");
    }

    private static native void copyInput(String input);

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: NativeOverflow <input>");
            return;
        }
        copyInput(args[0]);
    }
}
