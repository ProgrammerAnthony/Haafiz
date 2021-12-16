package com.haafiz.common.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc
 */
public class NetUtils {

	/**
     * 	IP正则
     */
    public static Pattern pattern =
        Pattern.compile("(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\." + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\."
            + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})\\." + "(1\\d{1,2}|2[0-4]\\d|25[0-5]|\\d{1,2})");

    public static boolean validate(List<String> ipList) {
        if (null != ipList) {
            for (String ip : ipList) {
                if (!pattern.matcher(ip).matches()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean validateRule(List<String> ipList) {
        if (null != ipList) {
            for (String ip : ipList) {
                int nMaskBits = 1;
                if (ip.indexOf('/') > 0) {
                    String[] addressAndMask = StringUtils.split(ip, "/");
                    ip = addressAndMask[0];
                    nMaskBits = Integer.parseInt(addressAndMask[1]);
                }
                if (!pattern.matcher(ip).matches() || nMaskBits < 0 ||nMaskBits>32) {
                    return false;
                }
            }
        }
        return true;

    }

    public static boolean isHttpUrl(String urls) {
        //	设置正则表达式
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";
        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(urls.trim());
        return mat.matches();
    }

    public static String normalizeAddress(String address) {
        String[] blocks = address.split("[:]");
        if (blocks.length > 2) {
            throw new IllegalArgumentException(address + " is invalid");
        }
        String host = blocks[0];
        int port = 80;
        if (blocks.length > 1) {
            port = Integer.valueOf(blocks[1]);
        } else {
            address += ":" + port; // use default 80
        }
        String serverAddr = String.format("%s:%d", host, port);
        return serverAddr;
    }

    public static String getLocalAddress(String address) {
        String[] blocks = address.split("[:]");
        if (blocks.length != 2) {
            throw new IllegalArgumentException(address + " is invalid address");
        }
        String host = blocks[0];
        int port = Integer.valueOf(blocks[1]);

        if ("0.0.0.0".equals(host)) {
            return String.format("%s:%d", NetUtils.getLocalIp(), port);
        }
        return address;
    }

    private static int matchedIndex(String ip, String[] prefix) {
        for (int i = 0; i < prefix.length; i++) {
            String p = prefix[i];
            if ("*".equals(p)) { // *, assumed to be IP
                if (ip.startsWith("127.") || ip.startsWith("10.") || ip.startsWith("172.") || ip.startsWith("192.")) {
                    continue;
                }
                return i;
            } else {
                if (ip.startsWith(p)) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static String getLocalIp(String ipPreference) {
        if (ipPreference == null) {
            ipPreference = "*>10>172>192>127";
        }
        String[] prefix = ipPreference.split("[> ]+");
        try {
            Pattern pattern = Pattern.compile("[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+");
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            String matchedIp = null;
            int matchedIdx = -1;
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                //	跳过虚拟网卡
                if(ni.isLoopback() || ni.isVirtual()) {
                	continue;
                }
                Enumeration<InetAddress> en = ni.getInetAddresses();
                // 	跳过虚拟网卡
                while (en.hasMoreElements()) {
                    InetAddress addr = en.nextElement();
                    if(addr.isLoopbackAddress() || !addr.isSiteLocalAddress() || addr.isAnyLocalAddress()) {
                    	continue;
                    }
                    String ip = addr.getHostAddress();
                    Matcher matcher = pattern.matcher(ip);
                    if (matcher.matches()) {
                        int idx = matchedIndex(ip, prefix);
                        if (idx == -1)
                            continue;
                        if (matchedIdx == -1) {
                            matchedIdx = idx;
                            matchedIp = ip;
                        } else {
                            if (matchedIdx > idx) {
                                matchedIdx = idx;
                                matchedIp = ip;
                            }
                        }
                    }
                }
            }
            if (matchedIp != null)
                return matchedIp;
            return "127.0.0.1";
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }

    public static String getLocalIp() {
        return getLocalIp("*>10>172>192>127");
    }

    public static String remoteAddress(SocketChannel channel) {
        SocketAddress addr = channel.socket().getRemoteSocketAddress();
        String res = String.format("%s", addr);
        return res;
    }

    public static String localAddress(SocketChannel channel) {
        SocketAddress addr = channel.socket().getLocalSocketAddress();
        String res = String.format("%s", addr);
        return addr == null ? res : res.substring(1);
    }

    public static String getPid() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();
        int index = name.indexOf("@");
        if (index != -1) {
            return name.substring(0, index);
        }
        return null;
    }

    public static String getLocalHostName() {
        try {
            return (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException uhe) {
            String host = uhe.getMessage();
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            return "UnknownHost";
        }
    }

}
