/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * NTopic启动器
 *
 * @author obullxl 2021年06月05日: 新增
 */
@SpringBootApplication
@MapperScan(basePackages = "cn.ntopic.das..**.dao", sqlSessionFactoryRef = "ntSqlSessionFactory")
public class NTBootApplication {

    /**
     * SpringBoot启动
     */
    public static void main(String[] args) {
        SpringApplication.run(NTBootApplication.class, args);
    }

    /**
     * DataSource配置
     */
    @Bean(name = "ntDataSource", initMethod = "init", destroyMethod = "close")
    public DruidDataSource ntDataSource(@Value("${ntopic.datasource.url}") String url
            , @Value("${ntopic.datasource.userName}") String userName
            , @Value("${ntopic.datasource.password}") String password) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        dataSource.setInitialSize(0);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(5);
        dataSource.setMaxWait(3000L);

        return dataSource;
    }

    /**
     * JdbcTemplate配置
     */
    @Bean("ntJdbcTemplate")
    public JdbcTemplate ntJdbcTemplate(@Qualifier("ntDataSource") DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);
        jdbcTemplate.setLazyInit(true);
        jdbcTemplate.setQueryTimeout(3);

        return jdbcTemplate;
    }

    /**
     * DB事务管理器：若当前存在事务则加入当前事务，否则新建一个新事务
     *
     * @see org.springframework.transaction.TransactionDefinition#PROPAGATION_REQUIRED
     */
    @Bean("ntTxTemplate")
    public TransactionTemplate ntTxTemplate(@Qualifier("ntDataSource") DataSource dataSource) {
        TransactionTemplate txTemplate = new TransactionTemplate();
        txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        txTemplate.setTransactionManager(new DataSourceTransactionManager(dataSource));

        return txTemplate;
    }

    /**
     * DB事务管理器：新建一个新事务，若当前存在事务则挂起当前事务
     *
     * @see org.springframework.transaction.TransactionDefinition#PROPAGATION_REQUIRES_NEW
     */
    @Bean("ntTxTemplateNew")
    public TransactionTemplate ntTxTemplateNew(@Qualifier("ntDataSource") DataSource dataSource) {
        TransactionTemplate txTemplate = new TransactionTemplate();
        txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        txTemplate.setTransactionManager(new DataSourceTransactionManager(dataSource));

        return txTemplate;
    }

    /**
     * MyBatis事务配置
     */
    @Bean("ntSqlSessionFactory")
    public SqlSessionFactoryBean ntSqlSessionFactory(@Qualifier("ntDataSource") DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        Configuration configuration = new Configuration();
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean;
    }

}
