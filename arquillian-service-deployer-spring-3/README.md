# Arquillian Spring 3 test enricher

## Configuration

Example

```
<?xml version="1.0" encoding="UTF-8"?>
<arquillian xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns="http://jboss.org/schema/arquillian"
            xsi:schemaLocation="http://jboss.org/schema/arquillian http://jboss.org/schema/arquillian/arquillian_1_0.xsd">

    <extension qualifier="spring">

        <!-- The version of Spring artifact, will be used for auto package the spring-context and spring-web,
         default is 3.1.1.RELEASE -->
        <property name="springVersion">3.1.1.RELEASE</property>

        <!-- The version of CGLIB artifact, required by Java-based config, default is 2.2.2 -->
        <property name="cglibVersion">2.2.2</property>

        <!-- Whether to auto package the dependencies, default is true -->
        <property name="autoPackage">true</property>

        <!-- Whether to include the snowdrop in test deployment, default is false -->
        <property name="includeSnowdrop">true</property>

        <!-- The version of the Snowdrop artifact, default is 2.0.3.Final -->
        <property name="snowdropVersion">2.0.3.Final</property>

        <!-- The name of custom context class, optional, when not specified
         then org.springframework.context.support.ClassPathXmlApplicationContext will be used -->
        <property name="customContextClass">org.springframework.context.support.ClassPathXmlApplicationContext</property>

        <!-- The name of custom annotation context class, optional, when not specified then
         org.springframework.context.annotation.AnnotationConfigApplicationContext -->
        <property name="customAnnotationContextClass">org.springframework.context.annotation.AnnotationConfigApplicationContext</property>
    </extension>
</arquillian>
```

## Build

```
mvn clean install
```