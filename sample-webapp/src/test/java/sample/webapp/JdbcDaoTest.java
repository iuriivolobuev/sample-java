package sample.webapp;

import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ContextConfiguration("classpath:/dao-context.xml")
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface JdbcDaoTest {}
