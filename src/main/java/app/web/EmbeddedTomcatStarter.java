package app.web;

import java.util.Locale;

public class EmbeddedTomcatStarter {
    private static final String CONTEXTS = "classpath:/web-app-context.xml";

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        new EmbeddedTomcat(CONTEXTS).startServer();
    }
}
