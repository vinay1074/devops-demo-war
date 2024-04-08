package com.ak.servlet;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/health")
public class HealthCheckServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final long HEALTH_CHECK_INTERVAL = 30000; // Health check interval in milliseconds
    private boolean isHealthy = true; // Initial health status
    
    @Override
    public void init() throws ServletException {
        super.init();
        
        // Schedule periodic health check task
        Timer timer = new Timer();
        timer.schedule(new HealthCheckTask(), 0, HEALTH_CHECK_INTERVAL);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        
        if (isHealthy) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("OK");
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("ERROR");
        }
    }
    
    private class HealthCheckTask extends TimerTask {
        @Override
        public void run() {
            // Perform health check here
            // Example: check database connection, external service availability, etc.
            boolean isDatabaseHealthy = checkDatabaseHealth();
            boolean isServiceHealthy = checkExternalServiceHealth();
            
            // Update overall health status based on individual checks
            isHealthy = isDatabaseHealthy && isServiceHealthy;
        }
        
        // Example method to check database health
        private boolean checkDatabaseHealth() {
            // Implement your database health check logic here
            return true; // Assuming it's healthy for the example
        }
        
        // Example method to check external service health
        private boolean checkExternalServiceHealth() {
            // Implement your external service health check logic here
            return true; // Assuming it's healthy for the example
        }
    }
}