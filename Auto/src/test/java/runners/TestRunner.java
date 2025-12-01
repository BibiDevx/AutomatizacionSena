package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features", // Ruta de los archivos .feature
        glue = "stepdefinitions",                // Paquete de los Step Definitions
        plugin = {"pretty", "html:target/cucumber-reports.html"}, // Plugins para reportes
        monochrome = true                        // Formatea la salida de la consola
)
public class TestRunner {
    // Clase vacía, configurada por las anotaciones.
}