package com.muye.monitor.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class MybatisConfig implements EnvironmentAware {

    private Environment environment;

    @Bean(name = "dataSource")
    public DataSource dataSource(){
        String url = environment.getProperty("spring.datasource.url");
        String driverClassName = environment.getProperty("spring.datasource.driver-class-name");
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setPassword(password);
        dataSource.setUsername(username);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(100);
        dataSource.setMaxIdle(100);
        dataSource.setValidationQuery("select 1 from dual");
        dataSource.setMaxWait(100000);

        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(300);
        dataSource.setLogAbandoned(false);

        return dataSource;
    }



    @Bean
    @ConditionalOnMissingBean //当容器没有指定的Bean的情况下创建该对象
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("dataSource") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        //设置数据源
        sqlSessionFactoryBean.setDataSource(dataSource);
        //设置mybatis的主配置文件
        ResourcePatternResolver resourcePatternResolver=new PathMatchingResourcePatternResolver();
        Resource mybatisXml=resourcePatternResolver.getResource("classpath:mybatis/mybatis-config.xml");
        sqlSessionFactoryBean.setConfigLocation(mybatisXml);
        // 配置mapper的扫描，找到所有的mapper.xml映射文件
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:mybatis/mapper/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);
        //设置别名包
        sqlSessionFactoryBean.setTypeAliasesPackage("com.muye.monitor.DO");
        return sqlSessionFactoryBean;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
