package com.haafiz.common.util;

import com.haafiz.common.exception.HaafizBasicException;
import com.haafiz.common.exception.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * @author Anthony
 * @create 2021/12/16
 * @desc
 */
@Slf4j
public class IPMatcher {

	/**
	 * <B>方法名称：</B>match<BR>
	 * <B>概要说明：</B>概要说明：子网掩码格式匹配 如192.168.1.0/24 or 202.24.0.0/14<BR>
	 * @param rule
	 * @param val
	 * @return
	 */
    public boolean match(String rule, String val) {
        if (StringUtils.isEmpty(val) || StringUtils.isEmpty(rule)) {
            return false;
        }
        val = val.trim();
        rule = rule.trim();

        int nMaskBits;
        InetAddress requiredAddress;

        if (rule.indexOf('/') > 0) {
            String[] addressAndMask = StringUtils.split(rule, "/");
            rule = addressAndMask[0];
            nMaskBits = Integer.parseInt(addressAndMask[1]);
        }
        else {
            nMaskBits = -1;
        }
        requiredAddress = parseAddress(rule);
        AssertUtil.isTrue(requiredAddress.getAddress().length * 8 >= nMaskBits,
                String.format("IP address %s is too short for bitmask of length %d", rule, nMaskBits));

        val = val.indexOf(':') > -1 ? val.substring(0, val.indexOf(':')) : val;
        return matches(requiredAddress, nMaskBits, val);
    }

    public boolean matches(InetAddress requiredAddress, int nMaskBits, String address) {

        InetAddress remoteAddress = parseAddress(address);

        if (!requiredAddress.getClass().equals(remoteAddress.getClass())) {
            return false;
        }

        if (nMaskBits < 0) {
            return remoteAddress.equals(requiredAddress);
        }

        byte[] remAddr = remoteAddress.getAddress();
        byte[] reqAddr = requiredAddress.getAddress();

        int nMaskFullBytes = nMaskBits / 8;
        byte finalByte = (byte) (0xFF00 >> (nMaskBits & 0x07));

        for (int i = 0; i < nMaskFullBytes; i++) {
            if (remAddr[i] != reqAddr[i]) {
                return false;
            }
        }

        if (finalByte != 0) {
            return (remAddr[nMaskFullBytes] & finalByte) == (reqAddr[nMaskFullBytes] & finalByte);
        }

        return true;
    }


    private InetAddress parseAddress(String address) {
        try {
            return InetAddress.getByName(address);
        }
        catch (UnknownHostException e) {
            log.warn("Failed to parse address:{}", address);
            throw new HaafizBasicException(ResponseCode.VERIFICATION_FAILED, e);
        }
    }
}
