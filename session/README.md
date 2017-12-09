使用说明
### web.xml
```
	<!-- 分布式session -->
	
	<filter>
		<filter-name>sessionAnywhereFilter</filter-name>
		<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
		<!-- <filter-class>com.bbcp.common.session.SessionFilter</filter-class> -->
		<init-param>
			<param-name>targetFilterLifecycle</param-name>
			<param-value>true</param-value>
		</init-param>
	</filter>
	
	<filter-mapping>
		<filter-name>sessionAnywhereFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
	
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath*:spring/spring-service.xml</param-value>
	</context-param>
```
### spring-service.xml
```

	<context:component-scan base-package="com.lljqiu.tools" />
	<context:annotation-config/>  
	<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
		<property name="locations">
			<list>
				<value>classpath:config/datasource.properties</value>
				<value>classpath:config/redis.properties</value>
			</list>
		</property>
	</bean>
	
	<import resource="classpath:spring/spring-datasource.xml" />
	<import resource="classpath:spring/spring-redis.xml" />
	<import resource="classpath:spring/spring-session.xml" />
```
spring-redis.xml
```

	<bean id="redisClient" class="com.lljqiu.tools.session.cache.redis.ShardedRedisClient"
		init-method="init">
		<property name="servers" value="${redis.servers}" />
		<property name="configs">
			<map>
                <entry key="poolMaxSize" value="300" />
            </map>
		</property>
	</bean>
	<!--设置对象过期时间-->
	<bean name="persistenceHolder" class="com.lljqiu.tools.session.cache.PersistenceHolder">
		<property name="typeExpiredMap">   
          <map key-type="java.lang.String">   
             	<!-- orm objectdata -->
                <entry key="com.lljqiu.tools.dao.dal.dataobject.TesoDO" value="-1"/>           
                
                <entry key="java.lang.Boolean" value="-1"/>           
                <entry key="java.lang.String" value="-1"/>           
                <entry key="java.lang.Integer" value="-1"/>
                <entry key="java.util.List" value="-1"/>
                <entry key="java.util.Map" value="-1"/>
                <entry key="com.alibaba.fastjson.JSONObject" value="-1"/>           
             </map>  
   		</property>
	</bean>
```
spring-session.xml
```

	<bean name="sessionAnywhereFilter" class="com.asdc.vss.session.session.SessionFilter">
		<property name="sessionTimeouts" value="30" />
		<property name="sessionStore">
			<!-- sessionStore的实现 -->
          	<bean class="com.lljqiu.api.commons.session.DatabaseSessionStore">
              	
            </bean>
        </property> 
	</bean>
```
