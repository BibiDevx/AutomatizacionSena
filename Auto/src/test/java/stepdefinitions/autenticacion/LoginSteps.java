package stepdefinitions.autenticacion;

import tasks.LoginTask;
import utils.DriverFactory;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class LoginSteps {

    // 🚨 CORRECCIÓN 1: Inicializa el driver AQUI para que no sea null
    private WebDriver driver = DriverFactory.getDriver();
    private final Duration TIMEOUT = Duration.ofSeconds(15);

    // CORRECCIÓN 2: Inicializa LoginTask aquí (requiere que driver no sea null)
    private LoginTask loginTask = new LoginTask(driver);

    @Given("el usuario esta en la pagina de inicio de sesion")
    public void user_is_on_login_page() {
        // 🚨 CORRECCIÓN 3: Solo navegamos, no reinicializamos el driver
        driver.get("http://localhost:3000/login");
    }

    @When("el usuario ingresa el {string} y la {string}")
    public void user_enters_username_and_password(String email, String password) {
        loginTask.login(email, password);
    }

    @When("hace clic en el boton de iniciar sesion")
    public void clicks_on_login_button() {
        // Este método está vacío. Asumo que el clic se hace dentro de LoginTask.login().
    }

    @Then("el usuario deberia ver la pagina de inicio del administrador")
    public void user_should_see_homepage_admin() throws InterruptedException{
        // 🚨 CORRECCIÓN 4: Crea el WebDriverWait aquí, si no lo hiciste globalmente
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/admin")
        ));

        System.out.println("Login exitoso en URL: " + driver.getCurrentUrl());

        Thread.sleep(5000);

    }

    @Then("el usuario deberia ver la pagina de inicio")
    public void user_should_see_homepage() throws InterruptedException{
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/perfil")
        ));

        System.out.println("Login exitoso en URL: " + driver.getCurrentUrl());

        Thread.sleep(5000);

    }
}