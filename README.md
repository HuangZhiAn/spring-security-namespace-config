# spring-security-namespace-config
####  Web.xml

```xml
    /*add filter*/
    <filter>
    	<filter-name>springSecurityFilterChain</filter-name>
    	<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>
    <filter-mapping>
    	<filter-name>springSecurityFilterChain</filter-name>
    	<url-pattern>/*</url-pattern>
    </filter-mapping>

```

Add security.xml config file to spring path.There a example at the end.

Custom a security Filter
Office doc: http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#ns-custom-filters

Add blow config to security xml file
```xml
<http>
<custom-filter before="FORM_LOGIN_FILTER" ref="myFilter" />
</http>
<beans:bean id="myFilter" class="com.mycompany.MySpecialAuthenticationFilter"/>
```
_**Notice**: If not only one <http> element,the last <http> pattern should be "/**"_

Add passwordEncoder<br>
Spring security has some inner password encoder method,we can use them like that<br>
```xml
    <password-encoder hash="md5" />
```
If you have used password-encoder your passwords in database or file are supposed to be encoded.
Open xsd file to see the inner password encoder method that we can choose.

Custom a password encoder
Create your password encoder and let it implements the PasswordEncoder interface of framwork.<br>
Config your password encoder in authentication-provider element like that
```xml
<authentication-provider user-service-ref="customUserDetailService"> <!--自定义UserDetailService-->
   <!--自定义password encoder-->
   <password-encoder ref="myPasswordEncoder"></password-encoder>
</authentication-provider>
```
Add session manager
add session listener in web.xml
```xml
<listener>
  <listener-class>org.springframework.security.web.session.HttpSessionEventPublisher</listener-class>
</listener>
Add session management in security config file
<session-management>
  <concurrency-control max-sessions="1" error-if-maximum-exceeded="false"/>
</session-management>
```
## Tips

1. Security congfig xml file is supported to start with "beans:beans" to be include in the application context.

2. The <http> element is responsible for creating a FilterChainProxy and the filter beans

<intercept-url> is to config url access control,when there is more than a <intercept-url> and url config conflicts,the framework will use the first config.As the doc say:

You can use multiple <intercept-url> elements to define different access requirements for different sets of URLs, but they will be evaluated in the order listed and the first match will be used. 

3. We can have multiple <authentication-provider>

4.     <csrf disabled="true"/>
Disable csrf

5.     <form-login login-page="/view/loginPage.html"/>
cannot use "classpath:" to specify a login page

6. use-expressions="true" default is true 
when it's true, we are supported to use expressions like this: access="hasRole('USER')"

7. Customing a UserDetailService, when the user is not exist,we need to throw UsernameNotFoundException.

8.     <authentication-provider><br>
           <!--数据库中取用户数据，自定义sql-->
           <jdbc-user-service data-source-ref="dataSource" users-by-username-query="select username,password,1 as enabled from account WHERE username=?"
                authorities-by-username-query="select u.username, r.name as role from account u,b_user_role ur, b_role r where u.id=ur.user_id and r.id = ur.role_id and u.username=?"/>
       </authentication-provider><code>
when we need to custom sql to fix our database,we can add the provider blow.Don't forget to override the sql.
    



## security.xml Example
```xml
<beans:beans xmlns="http://www.springframework.org/schema/security"
         xmlns:beans="http://www.springframework.org/schema/beans"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
    http://www.springframework.org/schema/security
http://www.springframework.org/schema/security/spring-security.xsd">

<!--配置security为none,访问pattern所包含的url时，将跳过spring security框架-->
<http pattern="/view/loginPage.html" security="none"></http>

<http pattern="/special_resources/**" entry-point-ref="myEntryPoint">  <!--entry-point用于处理抛出验证异常后的跳转动作-->
    <custom-filter before="FORM_LOGIN_FILTER" ref="myFilter" />
</http>
<beans:bean id="myFilter" class="com.hza.filter.MySpecialAccessControlFilter"/>
<beans:bean id="myEntryPoint" class="com.hza.filter.UnauthorizedEntryPoint"/>
<!--http元素用于配置访问控制和登录验证-->
<http use-expressions="true">
    <!--使csrf不可用 TUDO 自定义登录页，使用csrf，不使用模板-->
    <csrf disabled="true"/>
    <intercept-url pattern="/view/loginPage.html" access="isAnonymous()"/>
    <intercept-url pattern="/view/adminPage.html" access="hasRole('ADMIN')" />
    <intercept-url pattern="/**" access="hasRole('USER')" />
    <form-login login-page="/view/loginPage.html" />
    <logout />
    <!--添加session管理 限制同一用户的session数量-->
    <session-management>
        <concurrency-control max-sessions="1" error-if-maximum-exceeded="false"/>
    </session-management>
</http>

<!--配置用户信息来源 TUDO 自定义用户数据来源，从数据库中取数据-->
<authentication-manager>
    <authentication-provider user-service-ref="customUserDetailService"> <!--自定义UserDetailService-->
        <!--自定义password encoder-->
        <password-encoder ref="myPasswordEncoder"></password-encoder>
    </authentication-provider>
    <authentication-provider>
        <!--密码加密 md5需要32位加密-->
        <password-encoder hash="md5" />
        <!--从配置文件中读用户信息-->
        <user-service>
            <user name="jimi" password="0ddbe36716977a9cfe7595b36e4409fe" authorities="ROLE_USER, ROLE_ADMIN" /> <!--jimispassword-->
            <user name="bob" password="12b141f35d58b8b3a46eea65e6ac179e" authorities="ROLE_USER" /> <!--bobspassword-->
        </user-service>
    </authentication-provider>
    <authentication-provider>
        <!--数据库中取用户数据，自定义sql-->
        <jdbc-user-service data-source-ref="dataSource" users-by-username-query="select username,password,1 as enabled from account WHERE username=?"
                                    authorities-by-username-query="select u.username, r.name as role from account u,b_user_role ur, b_role r where u.id=ur.user_id and r.id = ur.role_id and u.username=?"/>
    </authentication-provider>
</authentication-manager>

<beans:import resource="application-context.xml"></beans:import>

</beans:beans>
```
