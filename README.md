# Arquillian Spring integration, embedded container and enrichers
* Injection of Spring beans into test classes
* Configuration from both XML and Java-based config
* Injecting beans configured in web application (i.e. DispatcherServlet) for tests annotated with @SpringWebConfiguration
* Support for both Spring(@Autowired, @Qualifier, @Required) and JSR-330(@Inject, @Named) annotations
* Bean initialization support (@PostConstruct)
* Auto packaging the spring-context artifact.

## Code Example

The test which requires the dependencies to be injected through Spring should be annotated with @SpringConfiguration.
The annotations provides the information where to look for the spring configuration

### Testing Spring beans

#### XML configuration

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

#### Java-based configuration

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

#### XML configuration
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
that is inaccessible from outside. To overcome this limitation the extensions requires that the web application will
create Root Web Application Context by defining ContextLoaderListener or ContextLoaderServlet in it's web.xml.

*Configuration files*

In this example the Spring context is configured through a xml file.

*employee-servlet.xml*

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.1.xsd http://www.springframework.org/schema/tool http://www.springframework.org/schema/tool/spring-tool-3.1.xsd http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-3.1.xsd">

    <context:component-scan base-package="org.jboss.arquillian.spring.testsuite.beans.repository.impl"/>
    <context:component-scan base-package="org.jboss.arquillian.spring.testsuite.beans.service.impl"/>
    <context:component-scan base-package="org.jboss.arquillian.spring.testsuite.beans.controller"/>

    <mvc:annotation-driven />

    <bean id="viewResolver"
          class="org.springframework.web.servlet.view.UrlBasedViewResolver">
        <property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/>
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

</beans>
```

*Test*

It is possible to inject into the test fully configured Spring MVC controller and run on it simple tests in
the container.

```java
@RunWith(Arquillian.class)
@SpringWebConfiguration(servletName = "employee")
public class EmployeeControlerTestCase {

    @Deployment
    @OverProtocol("Servlet 3.0")
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

    /**
     * The injected {@link EmployeeController}.
     */
    @Autowired
    private EmployeeController employeeController;

    /**
     * Tests {@link EmployeeController#getEmployees(org.springframework.ui.Model)} method.
     */
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

#### Java-based configuration (Servlet 3.0 only)

Again, the above example can be also achieved without any piece of XML (even without a web.xml descriptor).
All thanks to WebApplicationInitializer which allows to set up the servlets with code.

Instead of defining web.xml it is possible to write a simple class:

```java
public class EmployeeWebInitializer implements WebApplicationInitializer {

    /**
     * {@inheritDoc}
     */
    public void onStartup(ServletContext servletContext) throws ServletException {

        // creates the root web app context
        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(WebAppConfig.class);

        // registers context load listener
        servletContext.addListener(new ContextLoaderListener(new AnnotationConfigWebApplicationContext()));

        // adds a dispatch servlet, the servlet will be configured from root web app context
        ServletRegistration.Dynamic servletConfig = servletContext.addServlet("employee",
                new DispatcherServlet(webContext));
        servletConfig.setLoadOnStartup(1);
        servletConfig.addMapping("*.htm");
    }
}
```

Only what is left to do is define a configuration for the Spring context.

```java
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "org.jboss.arquillian.spring.testsuite.beans.repository.impl",
        "org.jboss.arquillian.spring.testsuite.beans.service.impl",
        "org.jboss.arquillian.spring.testsuite.beans.controller"})
public class WebAppConfig {

    /**
     * <p>Retrieves instance of {@link ViewResolver}.</p>
     *
     * @return instance of {@link ViewResolver}
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}
```

*Test code*
Now the test deployment doesn't require any kind of descriptors.

```java
@RunWith(Arquillian.class)
@SpringWebConfiguration(servletName = "employee")
public class EmployeeControllerWebInitTestCase {

    @Deployment
    @OverProtocol("Servlet 3.0")
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "spring-test.war")
                .addClasses(Employee.class,
                        EmployeeService.class, DefaultEmployeeService.class,
                        EmployeeRepository.class, DefaultEmployeeRepository.class, NullEmployeeRepository.class,
                        EmployeeController.class, WebAppConfig.class, EmployeeWebInitializer.class)
                .addAsLibraries(springDependencies())
                .addAsLibraries(mockitoDependencies());
    }

    /**
     * The injected {@link EmployeeController}.
     */
    @Autowired
    private EmployeeController employeeController;

    /**
     * Tests {@link EmployeeController#getEmployees(org.springframework.ui.Model)} method.
     */
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