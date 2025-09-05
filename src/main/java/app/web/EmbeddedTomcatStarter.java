package app.web;

import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmbeddedTomcatStarter {
    private static final String CONTEXTS = "classpath:/web-app-context.xml";

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Logger.getLogger("").setLevel(Level.FINEST);
        SLF4JBridgeHandler.install();
        new EmbeddedTomcat(CONTEXTS).startServer();
    }
}
