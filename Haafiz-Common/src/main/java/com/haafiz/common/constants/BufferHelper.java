package com.haafiz.common.constants;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc
 */
public interface BufferHelper {

    String FLUSHER = "FLUSHER";

    String MPMC = "MPMC";

    static boolean isMpmc(String bufferType) {
        return MPMC.equals(bufferType);
    }

    static boolean isFlusher(String bufferType) {
        return FLUSHER.equals(bufferType);
    }

}
