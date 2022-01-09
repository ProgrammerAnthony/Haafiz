package com.haafiz.core.netty.filter.pre;

import com.haafiz.common.config.DubboServiceInvoker;
import com.haafiz.common.constants.HaafizProtocol;
import com.haafiz.common.constants.ProcessorFilterConstants;
import com.haafiz.core.context.AttributeKey;
import com.haafiz.core.context.Context;
import com.haafiz.core.context.HaafizContext;
import com.haafiz.core.context.HaafizRequest;
import com.haafiz.core.netty.filter.AbstractEntryProcessorFilter;
import com.haafiz.core.netty.filter.Filter;
import com.haafiz.core.netty.filter.FilterConfig;
import com.haafiz.core.netty.filter.ProcessorFilterType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc
 */
@Filter(
        id = ProcessorFilterConstants.TIMEOUT_PRE_FILTER_ID,
        name = ProcessorFilterConstants.TIMEOUT_PRE_FILTER_NAME,
        value = ProcessorFilterType.PRE,
        order = ProcessorFilterConstants.TIMEOUT_PRE_FILTER_ORDER
)
public class TimeoutPreFilter extends AbstractEntryProcessorFilter<TimeoutPreFilter.Config> {

    public TimeoutPreFilter(Class<Config> filterConfigClass) {
        super(TimeoutPreFilter.Config.class);
    }

    @Override
    public void entry(Context ctx, Object... args) throws Throwable {
        try {
            HaafizContext haafizContext = (HaafizContext) ctx;
            String protocol = haafizContext.getProtocol();
            TimeoutPreFilter.Config config = (TimeoutPreFilter.Config) args[0];
            switch (protocol) {
                case HaafizProtocol.HTTP:
                    HaafizRequest haafizRequest = haafizContext.getRequest();
                    haafizRequest.setRequestTimeout(config.getTimeout());
                    break;
                case HaafizProtocol.DUBBO:
                    DubboServiceInvoker dubboServiceInvoker = (DubboServiceInvoker)haafizContext.getRequiredAttribute(AttributeKey.DUBBO_INVOKER);
                    dubboServiceInvoker.setTimeout(config.getTimeout());
                    break;
                default:
                    break;
            }
        } finally {
            super.fireNext(ctx, args);
        }
    }


    @Getter
    @Setter
    public static class Config extends FilterConfig {
        private Integer timeout;
    }
}
