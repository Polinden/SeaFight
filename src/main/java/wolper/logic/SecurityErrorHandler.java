package wolper.logic;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityErrorHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception.getClass().isAssignableFrom(SessionAuthenticationException.class)) {
            request.getRequestDispatcher("/double_reg/"+request.getParameterValues("username")[0]).forward(request, response);
        }
        else if (exception.getClass().isAssignableFrom(BadCredentialsException.class)) {
            request.getRequestDispatcher("/error").forward(request, response);}
        else super.onAuthenticationFailure(request, response, exception);
    }
}


