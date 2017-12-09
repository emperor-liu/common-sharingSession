/**
 * Project Name session
 * File Name package-info.java
 * Package Name com.lljqiu.tools.session.session.utils
 * Create Time 2017年12月9日
 * Create by name：liujie -- email: liujie@lljqiu.com
 * Copyright © 2015, 2017, www.lljqiu.com. All rights reserved.
 */
package com.lljqiu.tools.session.session.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

/** 
 * ClassName: UUID.java <br>
 * Description: 生成随机 ID<br>
 * @author name：liujie <br>email: liujie@lljqiu.com <br>
 * Create Time: 2017年12月9日<br>
 */
public class UUID {
    private boolean       noCase;
    private String        instanceId;
    private AtomicInteger counter;
    private static byte[] machineId;
    static {
        machineId = getLocalHostAddress();
    }

    public UUID() {
        this(true);
    }

    public UUID(boolean noCase) {
        byte[] jvmId = getRandomizedTime();

        this.instanceId = StringUtil.bytesToString(machineId, noCase) + "-"
                + StringUtil.bytesToString(jvmId, noCase);

        this.counter = new AtomicInteger();

        this.noCase = noCase;
    }

    private static byte[] getLocalHostAddress() {
        Method getHardwareAddress;

        try {
            getHardwareAddress = NetworkInterface.class.getMethod("getHardwareAddress");
        } catch (Exception e) {
            getHardwareAddress = null;
        }

        byte[] addr;

        try {
            InetAddress localHost = InetAddress.getLocalHost();

            if (getHardwareAddress != null) {
                addr = (byte[]) getHardwareAddress
                        .invoke(NetworkInterface.getByInetAddress(localHost)); // maybe null
            } else {
                addr = localHost.getAddress();
            }
        } catch (Exception e) {
            addr = null;
        }

        if (addr == null) {
            addr = new byte[] { 127, 0, 0, 1 };
        }

        return addr;
    }

    private byte[] getRandomizedTime() {
        long jvmId = System.currentTimeMillis();
        long random = new SecureRandom().nextLong();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeLong(jvmId);
            dos.writeLong(random);
        } catch (Exception e) {
        }

        return baos.toByteArray();
    }

    public String nextID() {
        return instanceId + "-" + StringUtil.longToString(System.currentTimeMillis(), noCase) + "-"
                + StringUtil.longToString(counter.getAndIncrement(), noCase);
    }
}
