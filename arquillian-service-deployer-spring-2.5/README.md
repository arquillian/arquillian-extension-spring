# Arquillian Spring 2.5 test enricher

## Configuration

Example

```
<?xml version="1.0" encoding="UTF-8"?>
<arquillian xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns="http://jboss.org/schema/arquillian"
            xsi:schemaLocation="http://jboss.org/schema/arquillian http://jboss.org/schema/arquillian/arquillian_1_0.xsd">

    <extension qualifier="spring">

        <!-- The version of Spring artifact, will be used for auto package the spring-context and spring-web,
         default is 2.5.6 -->
        <property name="springVersion">2.5.6</property>

        <!-- Whether to auto package the dependencies, default is true -->
        <property name="autoPackage">true</property>

        <!-- Whether to include the snowdrop in test deployment, default is false -->
        <property name="includeSnowdrop">true</property>

        <!-- The version of the Snowdrop artifact, default is 2.0.3.Final -->
        <property name="snowdropVersion">2.0.3.Final</property>

        <!-- The name of custom context class, optional, when not specified
         then org.springframework.context.support.ClassPathXmlApplicationContext will be used -->
        <property name="customContextClass">org.springframework.context.support.ClassPathXmlApplicationContext</property>
    </extension>
</arquillian>
```

## Running the test in JBoss AS

In other to successfully run the Spring 2.5.x in JBoss the tests should rely on Snowdrop application context
instead of Spring's. This can be done by specifying for each test custom context class.

```
@RunWith(Arquillian.class)
@SpringConfiguration(value = {"applicationContext.xml"}, contextClass = VFSClassPathXmlApplicationContext.class)
public class DefaultEmployeeRepositoryTestCase {

    // test code
}
```

Alternatively it is possible to set up the context class for all the tests through arquillian.xml.

```
<?xml version="1.0" encoding="UTF-8"?>
<arquillian xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns="http://jboss.org/schema/arquillian"
            xsi:schemaLocation="http://jboss.org/schema/arquillian http://jboss.org/schema/arquillian/arquillian_1_0.xsd">

    <extension qualifier="spring">

        <!-- The name of custom context class, optional, when not specified
         then org.springframework.context.support.ClassPathXmlApplicationContext will be used -->
        <property name="customContextClass">org.jboss.spring.vfs.context.VFSClassPathXmlApplicationContext</property>

    </extension>
</arquillian>
```

When running a web application custom context classes need to be registered in web.xml.
This done by specifying the init param for dispatcher servlet:

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         version="2.5">

    <servlet>
        <servlet-name>employee</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextClass</param-name>
            <param-value>
                org.jboss.spring.vfs.context.VFSXmlWebApplicationContext
            </param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>employee</servlet-name>
        <url-pattern>*.htm</url-pattern>
    </servlet-mapping>

</web-app>
```

When the VFSXmlWebApplicationContext need to be used with ContextLoaderListener then <context-param/> is used instead.

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
         version="2.5">

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <context-param>
        <param-name>contextClass</param-name>
        <param-value>
            org.jboss.spring.vfs.context.VFSXmlWebApplicationContext
        </param-value>
    </context-param>

</web-app>
```

## Buid

```
mvn clean install
```