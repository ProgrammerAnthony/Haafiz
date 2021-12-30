package com.haafiz.core.context;

import com.haafiz.common.config.Rule;
import com.haafiz.common.util.AssertUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * @author Anthony
 * @create 2021/12/29
 * @desc
 */
public class HaafizContext extends BasicContext{
    private final HaafizRequest haafizRequest;

    private HaafizResponse haafizResponse;

    private final Rule rule;

    private HaafizContext(String protocol, ChannelHandlerContext nettyCtx, boolean keepAlive,
                          HaafizRequest haafizRequest, Rule rule) {
        super(protocol, nettyCtx, keepAlive);
        this.haafizRequest = haafizRequest;
        this.rule = rule;
    }


    public static class Builder {

        private String protocol;

        private ChannelHandlerContext nettyCtx;

        private HaafizRequest haafizRequest;

        private Rule rule;

        private boolean keepAlive;

        public Builder() {
        }

        public Builder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder setNettyCtx(ChannelHandlerContext nettyCtx) {
            this.nettyCtx = nettyCtx;
            return this;
        }

        public Builder setRapidRequest(HaafizRequest haafizRequest) {
            this.haafizRequest = haafizRequest;
            return this;
        }

        public Builder setRule(Rule rule) {
            this.rule = rule;
            return this;
        }

        public Builder setKeepAlive(boolean keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        public HaafizContext build() {
            AssertUtil.notNull(protocol, "protocol cannot be empty");
            AssertUtil.notNull(nettyCtx, "nettyCtx cannot be empty");
            AssertUtil.notNull(haafizRequest, "haafizRequest cannot be empty");
            AssertUtil.notNull(rule, "rule cannot be empty");
            return new HaafizContext(protocol, nettyCtx, keepAlive, haafizRequest, rule);
        }
    }


    public <T> T getRequiredAttribute(AttributeKey<T> key) {
        T value = getAttribute(key);
        AssertUtil.notNull(value, "required attribute '" + key + "' is missing !");
        return value;
    }


    @SuppressWarnings("unchecked")
    public <T> T getAttributeOrDefault(AttributeKey<T> key, T defaultValue) {
        return (T) attributes.getOrDefault(key, defaultValue);
    }


    public Rule.FilterConfig getFilterConfig(String filterId) {
        return rule.getFilterConfig(filterId);
    }


    public String getUniqueId() {
        return haafizRequest.getUniqueId();
    }


    public void releaseRequest() {
        if(requestReleased.compareAndSet(false, true)) {
            ReferenceCountUtil.release(haafizRequest.getFullHttpRequest());
        }
    }

    @Override
    public Rule getRule() {
        return rule;
    }

    @Override
    public HaafizRequest getRequest() {
        return haafizRequest;
    }


    public HaafizRequest getOriginRequest() {
        return haafizRequest;
    }


    public HaafizRequest getRequestMutale() {
        return haafizRequest;
    }

    @Override
    public HaafizResponse getResponse() {
        return haafizResponse;
    }

    @Override
    public void setResponse(Object response) {
        this.haafizResponse = (HaafizResponse) response;
    }

}
