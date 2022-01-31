package wolper.logic;


import org.springframework.context.ApplicationContext;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.support.WebApplicationContextUtils;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;


public class SessionEventListener extends HttpSessionEventPublisher {

    //Установка таймаута сессии и определение имени пользователя
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        super.sessionCreated(event);
        event.getSession().setMaxInactiveInterval(60*3);
    }

    //Удаляем запись об игре из репозитория когда игрок уходит
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        String name=null;
        //----Замысловатый способ найти имя пользователя
        SessionRegistry sessionRegistry = getSessionRegistry(event);
        SessionInformation sessionInfo = (sessionRegistry != null ? sessionRegistry
                .getSessionInformation(event.getSession().getId()) : null);
        UserDetails ud = null;
        if (sessionInfo != null) {
            ud = (UserDetails) sessionInfo.getPrincipal();
        }
        if (ud != null) {
            name=ud.getUsername();
            //Извещаем соперников что мы ушли
            getAllGames(event).removeByName(name);
        }
        //---
        super.sessionDestroyed(event);
    }


    //По другому в слушатель бины не заинжектишь
    public AllGames getAllGames(HttpSessionEvent event){
        HttpSession session = event.getSession();
        ApplicationContext ctx =
                WebApplicationContextUtils.
                        getWebApplicationContext(session.getServletContext());
        return (AllGames) ctx.getBean("allGames");
    }

    //По другому в слушатель бины не заинжектишь
    public SessionRegistry getSessionRegistry(HttpSessionEvent event){
        HttpSession session = event.getSession();
        ApplicationContext ctx =
                WebApplicationContextUtils.
                        getWebApplicationContext(session.getServletContext());
        return (SessionRegistry) ctx.getBean("sessionRegistry");
    }
}

