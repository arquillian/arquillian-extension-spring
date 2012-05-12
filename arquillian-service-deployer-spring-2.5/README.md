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

## Buid

```
mvn clean install
```