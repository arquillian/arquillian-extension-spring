# Arquillian Spring integration, embedded container and enrichers
* Injection of Spring beans into test classes
* Configuration from both XML and Java-based config
* Support for both Spring(@Autowired, @Qualifier, @Required) and JSR-330(@Inject, @Name) annotations
* Bean initialization support (@PostConstruct)
* Auto packaging the spring-context artifact.

## Code Example

The test which requires the dependencies to be injected through Spring should be annotated with @SpringConfiguration.
The annotations provides the information where to look for the spring configuration

### XML configuration

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.1.xsd">

    <context:component-scan base-package="org.jboss.arquillian.spring.testsuite.beans.repository.impl"/>
    <context:component-scan base-package="org.jboss.arquillian.spring.testsuite.beans.service.impl"/>

</beans>
```

Test example

```
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

```
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

Test example

```
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

## TODO

* Unit tests
* Configuring the extension through arquillian.xml for specifying the Spring artifact version
* Arquillian Persistance Extension integration - wrapper for Spring transaction manager