/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * NTopicBootX插件配置
 *
 * @author obullxl 2021年03月20日: 新增
 */
@Configuration
public class NTBootPluginConfig {

    /**
     * NTConfigX:缓存最大数量
     */
    @Value("${ntopic.plugin.NTConfigX.cache.maximum_size}")
    private int ntConfigMaximumSize;

    /**
     * NTConfigX:缓存有效时间(单位：秒)
     */
    @Value("${ntopic.plugin.NTConfigX.cache.expire_seconds}")
    private int ntConfigExpireSeconds;

    /**
     * NTopicBootX分布式锁插件
     */
//    @Bean(BeanConstants.NT_LOCK_X)
//    public NTLockX ntLockX(@Qualifier("ntJdbcTemplate") JdbcTemplate jdbcTemplate) {
//        return new NTLockX(jdbcTemplate);
//    }

    /**
     * NTopicBootX配置插件
     */
//    @Bean(BeanConstants.NT_CONFIG_X)
//    @Autowired
//    public NTConfigX ntConfigX(@Qualifier("ntJdbcTemplate") JdbcTemplate jdbcTemplate) {
//        return new NTConfigX(jdbcTemplate, this.ntConfigMaximumSize, this.ntConfigExpireSeconds);
//    }

    /**
     * NTopicBootX序列插件
     */
//    @Bean(BeanConstants.NT_SEQUENCE_X)
//    @Autowired
//    public NTSequenceX ntSequenceX(@Qualifier("ntJdbcTemplate") JdbcTemplate jdbcTemplate) {
//        NTSequenceX ntSequenceX = new NTSequenceX(jdbcTemplate);
//
//        // 序列初始化
//        for (NTSequenceEnum seqEnum : NTSequenceEnum.values()) {
//            ntSequenceX.init(seqEnum.getCode(), seqEnum.getMinValue(), seqEnum.getMaxValue(), seqEnum.getIncStep());
//        }
//
//        return ntSequenceX;
//    }

}
