package io.quarkus.arc.benchmarks.appbeans;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import io.quarkus.qute.Engine;
import io.quarkus.qute.Template;

public class Generator {

    private static String APP_BEAN = "package io.quarkus.arc.benchmarks.appbeans;\n"
            + "import jakarta.enterprise.context.ApplicationScoped;\n"
            + "import io.quarkus.runtime.Startup;\n"
            + "\n"
            + "@ApplicationScoped\n"
            + "@Startup\n"
            + "public class AppBean{index} {\n"
            + "void ping() { }\n"
            + "}";

    public static void main(String[] args) throws IOException {
        System.out.println("Start generating beans...");
        Engine engine = Engine.builder().addDefaults().build();
        Template template = engine.parse(APP_BEAN);
        int loop = 200;
        File outputDir = new File("target/generated-beans");
        if (outputDir.exists()) {
            Files.walk(outputDir.toPath()).map(Path::toFile).forEach(File::delete);
        }
        outputDir.mkdirs();
        for (int i = 0; i < loop; i++) {
            Path path = new File(outputDir, "AppBean" + i + ".java").toPath();
            Files.write(path,
                    template.data("index", i).render().getBytes(Charset.forName("utf-8")));
        }
        System.out.println("Finished generating beans...");
    }

}
