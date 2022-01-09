package com.haafiz.core.netty.filter;

import com.github.benmanes.caffeine.cache.Cache;
import com.haafiz.common.config.Rule;
import com.haafiz.common.constants.BasicConst;
import com.haafiz.common.util.JSONUtil;
import com.haafiz.core.context.Context;
import com.haafiz.core.netty.cache.DefaultCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Anthony
 * @create 2022/1/9
 * @desc
 */
@Slf4j
public abstract class AbstractEntryProcessorFilter<FilterConfigClass> extends AbstractLinkedProcessorFilter<Context> {
    protected Filter filterAnnotation;

    protected Cache<String, FilterConfigClass> cache;

    protected final Class<FilterConfigClass> filterConfigClass;

    public AbstractEntryProcessorFilter(Class<FilterConfigClass> filterConfigClass) {
        //load filter from subclass annotation
        this.filterAnnotation = this.getClass().getAnnotation(Filter.class);
        this.filterConfigClass = filterConfigClass;
        this.cache = DefaultCacheManager.getInstance().create(DefaultCacheManager.FILTER_CONFIG_CACHE_ID);
    }

    @Override
    public boolean check(Context ctx) throws Throwable {
        return ctx.getRule().hashId(filterAnnotation.id());
    }

    @Override
    public void transformEntry(Context ctx, Object... args) throws Throwable {
        FilterConfigClass filterConfigClass = dynamicLoadCache(ctx, args);
        super.transformEntry(ctx, filterConfigClass);
    }


    private FilterConfigClass dynamicLoadCache(Context ctx, Object[] args) {
        //load rule by context,and get filter config by rule with specific filterId
        Rule.FilterConfig filterConfig = ctx.getRule().getFilterConfig(filterAnnotation.id());

        //	define a cacheKey
        String ruleId = ctx.getRule().getId();
        String cacheKey = ruleId + BasicConst.DOLLAR_SEPARATOR + filterAnnotation.id();

        FilterConfigClass fcc = cache.getIfPresent(cacheKey);
        if (fcc == null) {
            if (filterConfig != null && StringUtils.isNotEmpty(filterConfig.getConfig())) {
                String configStr = filterConfig.getConfig();
                try {
                    fcc = JSONUtil.parse(configStr, filterConfigClass);
                    cache.put(cacheKey, fcc);
                } catch (Exception e) {
                    log.error("#AbstractEntryProcessorFilter# dynamicLoadCache filterId: {}, config parse error: {}",
                            filterAnnotation.id(),
                            configStr,
                            e);
                }
            }
        }
        return fcc;
    }

}
