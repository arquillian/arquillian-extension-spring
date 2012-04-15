# Arquillian Spring integration, embedded container and enrichers
* Injection of Spring beans into test classes
* Configuration from both XML and Java-based config
* Injecting beans configured in web application (in ContextListener or ContextServlet) for test annotated with @SpringWebConfiguration
* Support for both Spring(@Autowired, @Qualifier, @Required) and JSR-330(@Inject, @Named) annotations
* Bean initialization support (@PostConstruct)
* Auto packaging the spring-context artifact.

## Code Example

The test which requires the dependencies to be injected through Spring should be annotated with @SpringConfiguration.
The annotations provides the information where to look for the spring configuration

### XML configuration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.1.xsd">

    <context:component-scan base-package="org.jboss.arquillian.spring.testsuite.beans.repository.impl"/>
    <context:component-scan base-package="org.jboss.arquillian.spring.testsuite.beans.service.impl"/>

</beans>
```

*Test example*

```java
@RunWith(Arquillian.class)
@SpringConfiguration({"applicationContext.xml"})
public class DefaultEmployeeRepositoryTestCase {

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "spring-test.jar")
                .addClasses(Employee.class,
                        EmployeeRepository.class, DefaultEmployeeRepository.class, NullEmployeeRepository.class)
                .addAsResource(DefaultEmployeeRepositoryTestCase.class.getResource("/applicationContext.xml"),
                        "applicationContext.xml");
    }

    @Autowired
    @Qualifier("defaultEmployeeRepository")
    private EmployeeRepository employeeRepository;

    @Test
    public void testGetEmployees() throws Exception {

        List<Employee> result = employeeRepository.getEmployees();

        assertNotNull("Method returned null list as result.", result);
        assertEquals("Two employees were expected.", 2, result.size());
    }
}
```

### Java-based configuration

```java
@Configuration
public class AppConfig {

    @Bean
    public EmployeeRepository defaultEmployeeRepository() {

        return new DefaultEmployeeRepository();
    }

    @Bean
    public EmployeeService defaultEmployeeService() {

        return new DefaultEmployeeService();
    }
}
```

*Test example*

```java
@RunWith(Arquillian.class)
@SpringConfiguration(classes = {AppConfig.class})
public class AnnotatedConfigurationTestCase {

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "spring-test.jar")
                .addClasses(Employee.class,
                        EmployeeService.class, DefaultEmployeeService.class,
                        EmployeeRepository.class, DefaultEmployeeRepository.class, NullEmployeeRepository.class,
                        AppConfig.class);
    }

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void testGetEmployees() throws Exception {

        List<Employee> result = employeeService.getEmployees();

        assertNotNull("Method returned null list as result.", result);
        assertEquals("Two employees were expected.", 2, result.size());
    }
}
```

### Testing web apps

The above examples allowed testing seperate classes injected through Spring without configuring entire web application,
fallowing example demonstrates how to test a simple MVC application instead.

*Simple annotated controller*

```java
@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/Employees.htm")
    public String getEmployees(Model model) {

        model.addAttribute("employees", employeeService.getEmployees());

        return "employeeList";
    }
}
```

*Web.xml*

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         version="3.0">

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>
            /WEB-INF/applicationContext.xml
        </param-value>
    </context-param>

    <servlet>
        <servlet-name>employee</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>employee</servlet-name>
        <url-pattern>*.htm</url-pattern>
    </servlet-mapping>

</web-app>
```

Note: The ContextLoaderListener is required here, mostly because each dispatcher servlet has it own application context
that is inaccessible from outside.

*Configuration files*

In this example the Spring context is configured through a xml file.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.1.xsd http://www.springframework.org/schema/tool http://www.springframework.org/schema/tool/spring-tool-3.1.xsd http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-3.1.xsd">

    <context:component-scan base-package="org.jboss.arquillian.spring.testsuite.beans.repository.impl"/>
    <context:component-scan base-package="org.jboss.arquillian.spring.testsuite.beans.service.impl"/>
    <context:component-scan base-package="org.jboss.arquillian.spring.testsuite.beans.controller"/>

</beans>
```

*Test*

It is possible to inject into the test fully configured Spring MVC controller and run on it simple tests in
the container.

```java
@RunWith(Arquillian.class)
@SpringWebConfiguration
public class EmployeeControlerTestCase {

    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "spring-test.war")
                .addClasses(Employee.class,
                        EmployeeService.class, DefaultEmployeeService.class,
                        EmployeeRepository.class, DefaultEmployeeRepository.class, NullEmployeeRepository.class,
                        EmployeeController.class)
                .addAsLibraries(springDependencies())
                .addAsLibraries(mockitoDependencies())
                .addAsWebInfResource(EmployeeControlerTestCase.class.getResource("/mvc/web.xml"),
                        "web.xml")
                .addAsWebInfResource(EmployeeControlerTestCase.class.getResource("/mvc/employee-servlet.xml"),
                        "employee-servlet.xml")
                .addAsWebInfResource(EmployeeControlerTestCase.class.getResource("/mvc/applicationContext.xml"),
                        "applicationContext.xml");
    }

    @Autowired
    private EmployeeController employeeController;

    @Test
    public void testGetEmployees() {

        String result;
        Model model;
        ArgumentCaptor<List> argument;

        assertNotNull("The controller hasn't been injected.", employeeController);

        model = mock(Model.class);
        argument = ArgumentCaptor.forClass(List.class);

        result = employeeController.getEmployees(model);

        verify(model).addAttribute(eq("employees"), argument.capture());
        assertEquals("The controller returned invalid view name, 'employeeList' was expected.", "employeeList", result);
        assertEquals("Two employees should be returned from model.", 2, argument.getValue().size());
    }
}
```

## TODO

* Unit tests
* Configuring the extension through arquillian.xml for specifying the Spring artifact version
* Arquillian Persistance Extension integration - wrapper for Spring transaction manager