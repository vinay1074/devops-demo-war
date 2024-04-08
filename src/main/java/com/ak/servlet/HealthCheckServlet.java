package com.ak.servlet;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/health")
public class HealthCheckServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(HealthCheckServlet.class.getName());
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

        logger.info("Outgoing response to " + req.getRemoteAddr() + ":" + req.getRemotePort() + " with " + resp.getStatus() + " status code");
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
            boolean isSuccess = performHealthCheck();
            isHealthy = isSuccess;
        }
        
        private boolean performHealthCheck() {
            HttpURLConnection connection = null;
            try {
                // Example: Check an external service health
                URL url = new URL("http://localhost:8080/health"); // Replace with your endpoint
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                
                int responseCode = connection.getResponseCode();
                
                return responseCode >= 200 && responseCode < 400;
            } catch (IOException e) {
                return false;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}