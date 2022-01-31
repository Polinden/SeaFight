package wolper.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import wolper.logic.SessionEventListener;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class PolynaApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer  {


    // Стандартная настройка сервлета и контекста
    @Override
    protected Class<?>[] getRootConfigClasses() {
            return new Class<?>[] {StompCOnfiguration.class, ServiceConfiguration.class, SecurityConfiguration.class};
        }

    @Override
    protected Class<?>[] getServletConfigClasses() {
            return new Class<?>[] {MvcConfiguration.class, FebFlowConfiguration.class};}

    @Override
    protected String[] getServletMappings() {
            return new String[] {"/"};
        }


    // Настройка аплоада файлов
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(new MultipartConfigElement("$OPENSHIFT_TMP_DIR", 2097152, 2097152*100, 0));
    }

    // Настройка слушателя сессии
    @Override
    protected void registerDispatcherServlet(ServletContext servletContext) {
        super.registerDispatcherServlet(servletContext);
        servletContext.addListener(new SessionEventListener());
    }

}

