package sbrt.preppy.lesson_18;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sbrt.preppy.lesson_18.service.ServiceBook;

@SpringBootApplication
public class Lesson18Application {

    public static void main(String[] args) {
//        ApplicationContext context = SpringApplication.run(Lesson18Application.class, args);
//        ServiceBook book = context.getBean(ServiceBook.class);
//        book.start();

        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        ServiceBook RecipeBook = context.getBean("RecipeBook", ServiceBook.class);
        RecipeBook.start();
    }
}
