package com.polygraphene.remoteglass;

import android.util.Log;

import java.nio.ByteBuffer;

class SrtReceiverThread extends MainActivity.ReceiverThread {
    private static final String TAG = "SrtReceiverThread";

    static {
        System.loadLibrary("srt");
        System.loadLibrary("native-lib");
    }

    SrtReceiverThread(NALParser nalParser, StatisticsCounter counter, MainActivity activity) {
        super(nalParser, counter, activity);
    }

    public boolean isStopped(){
        return mainActivity.isStopped();
    }

    @Override
    public void run() {
        setName(SrtReceiverThread.class.getName());

        try {
            int ret = initializeSocket(host, port);
            if (ret != 0) {
                Log.e(TAG, "Error on initialize srt socket. Code=" + ret + ".");
                return;
            }

            runLoop();
        } finally {
            closeSocket();
        }


        Log.v(TAG, "SrtReceiverThread stopped.");
    }

    native int initializeSocket(String host, int port);

    native void closeSocket();

    native void runLoop();

    native int send(byte[] buf, int length);

    native int getNalListSize();

    native NAL getNal();

    native void flushNALList();
}