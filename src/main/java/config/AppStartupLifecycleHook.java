package config;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.service.GameServiceMongo;
import org.jboss.logging.Logger;


@ApplicationScoped
public class AppStartupLifecycleHook {
    private static final Logger LOG = Logger.getLogger(AppStartupLifecycleHook.class);
    @Startup
    void init() {
        LOG.infof("Welcome! , games-store-service API started!!, Service API build version=%s", getBuildVersion());
    }

    public static String getBuildVersion() {
        return System.getenv("BUILD_VERSION") == null ? "" : System.getenv("BUILD_VERSION");
    }
}
