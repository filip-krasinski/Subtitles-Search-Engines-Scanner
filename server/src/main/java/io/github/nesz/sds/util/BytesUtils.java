package io.github.nesz.sds.util;

public final class BytesUtils {

    private BytesUtils() {}

    public static int indexOf(byte[] outerArray, byte[] smallerArray) {
        for (int i = 0; i < outerArray.length - smallerArray.length+1; ++i) {
            boolean found = true;
            for(int j = 0; j < smallerArray.length; ++j) {
                if (outerArray[i+j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return i;
        }
        return -1;
    }

    public static boolean startsWith(byte[] src, byte[] with) {
        for (int i = 0; i < with.length; ++i) {
            if (src[i] != with[i]) {
                return false;
            }
        }

        return true;
    }
}
