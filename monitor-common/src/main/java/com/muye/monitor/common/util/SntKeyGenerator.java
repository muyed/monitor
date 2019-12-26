package com.muye.monitor.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;

public class SntKeyGenerator {

    private static SntKeyGenerator sntKeyGenerator = new SntKeyGenerator();

    public static final long EPOCH;
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_BITS = 10L;
    private static final long SEQUENCE_MASK = 4095L;
    private static final long WORKER_ID_LEFT_SHIFT_BITS = 12L;
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = 22L;
    private static final long WORKER_ID_MAX_VALUE = 1024L;
    private static long workerId;
    private long sequence;
    private long lastTime;

    /**
     * 方法需要私有化，防止被误用
     */
    private SntKeyGenerator() {
        long initWorkId = this.initWorkerId();
        workerId = this.initWorkerId();

    }

    public static SntKeyGenerator getInstance(){
        return sntKeyGenerator;
    }

    public static void setWorkerId(long initWorkId) {
        workerId = workerId;
    }

    public synchronized Number generateKey() {
        long currentMillis = getCurrentMillis();
        if (this.lastTime == currentMillis) {
            if (0L == (this.sequence = ++this.sequence & 4095L)) {
                currentMillis = this.waitUntilNextTime(currentMillis);
            }
        } else {
            this.sequence = 0L;
        }


        this.lastTime = currentMillis;

        return currentMillis - EPOCH << 22 | workerId << 12 | this.sequence;
    }

    private long waitUntilNextTime(long lastTime) {
        long time;
        for(time = getCurrentMillis(); time <= lastTime; time = getCurrentMillis()) {
            ;
        }

        return time;
    }

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 10, 1);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        EPOCH = calendar.getTimeInMillis();
    }

    protected long initWorkerId() {
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }
        byte[] ipAddressByteArray = address.getAddress();
        long workerId = 0L;
        if (ipAddressByteArray.length == 4) {
            for (byte byteNum : ipAddressByteArray) {
                workerId += byteNum & 0xFF;
            }
        } else if (ipAddressByteArray.length == 16) {
            for (byte byteNum : ipAddressByteArray) {
                workerId += byteNum & 0B111111;
            }
        } else {
            throw new IllegalStateException("Bad LocalHost InetAddress, please check your network!");
        }
        return workerId;
    }

    private long initWorkerIdHostName() {
        InetAddress address;
        Long workerId;
        try {
            address = InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            throw new IllegalStateException("Cannot get LocalHost InetAddress, please check your network!");
        }
        String hostName = address.getHostName();
        try {
            workerId = Long.valueOf(hostName.replace(hostName.replaceAll("\\d+$", ""), ""));
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Wrong hostname:%s, hostname must be end with number!", hostName));
        }
        return workerId;
    }

    private long getCurrentMillis() {
        return System.currentTimeMillis();
    }
}
