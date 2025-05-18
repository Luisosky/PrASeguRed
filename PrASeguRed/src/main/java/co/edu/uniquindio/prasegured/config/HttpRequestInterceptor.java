package co.edu.uniquindio.prasegured.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Collections;
import java.util.Enumeration;

@Component
public class HttpRequestInterceptor implements HandlerInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Recibida solicitud HTTP {} a la ruta: {}", request.getMethod(), request.getRequestURI());
        
        // Log de headers
        logger.info("Headers de la solicitud:");
        Enumeration<String> headerNames = request.getHeaderNames();
        Collections.list(headerNames).forEach(headerName -> 
            logger.info("{} : {}", headerName, request.getHeader(headerName))
        );
        
        // Log de parámetros (sólo para métodos GET)
        if ("GET".equals(request.getMethod())) {
            logger.info("Parámetros de la solicitud:");
            request.getParameterMap().forEach((key, value) -> 
                logger.info("{} : {}", key, String.join(", ", value))
            );
        }
        
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info("Respuesta de solicitud HTTP {} a {}: status {}", 
                   request.getMethod(), request.getRequestURI(), response.getStatus());
    }
}