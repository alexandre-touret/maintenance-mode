package info.touret.spring.maintenancemode.filter;

import info.touret.spring.maintenancemode.exception.MaintenanceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static info.touret.spring.maintenancemode.controller.MaintenanceController.API_MAINTENANCE_URI;

/**
 * Controls access to the application by returning an error message regarding the maintenance status
 *
 * @see ApplicationAvailability
 */
@Component
public class CheckMaintenanceFilter implements Filter {
    private final static Logger LOGGER = LoggerFactory.getLogger(CheckMaintenanceFilter.class);
    @Autowired
    private ApplicationAvailability availability;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver exceptionHandler;

    /**
     * Checks if the application is under maintenance. If it is and if the requested URI is not '/api/maintenance', it throws a <code>MaintenanceException</code>
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     * @throws info.touret.spring.maintenancemode.exception.MaintenanceException the application is under maintenance
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (availability.getReadinessState().equals(ReadinessState.REFUSING_TRAFFIC) &&
                !((HttpServletRequest) request).getRequestURI().equals(API_MAINTENANCE_URI)) {
            LOGGER.warn("Message handled during maintenance [{}]", ((HttpServletRequest) request).getRequestURI());
            exceptionHandler.resolveException((HttpServletRequest) request, (HttpServletResponse) response, null, new MaintenanceException("Service currently in maintenance"));
        } else {
            chain.doFilter(request, response);
        }
    }

}
