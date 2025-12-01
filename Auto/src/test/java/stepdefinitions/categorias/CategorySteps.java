package stepdefinitions.categorias;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.junit.Assert;
import java.time.Duration;

public class CategorySteps {

    // 🚨 IMPORTANTE: Necesitas inicializar este driver con la instancia de Selenium activa.
    // Asume que esta instancia del driver se obtiene después del Login/Hooks.
    private WebDriver driver;
    private final Duration TIMEOUT = Duration.ofSeconds(15);
    private WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);

    // --- MÉTODOS AUXILIARES ---

    private void clickButtonInRow(String categoryName, String buttonText) {
        // Busca la fila (tr) que contiene el nombre y dentro de esa fila, el botón.
        String xpath = String.format("//tr[td[text()='%s']]//button[contains(., '%s')]", categoryName, buttonText);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).click();
    }

    private void assertCategoryPresence(String categoryName, boolean shouldBePresent) {
        // Busca la categoría en la tabla
        String xpath = String.format("//tbody//td[text()='%s']", categoryName);

        try {
            driver.findElement(By.xpath(xpath));
            // Si encuentra el elemento
            if (!shouldBePresent) {
                // Si la categoría NO debería estar presente, la prueba falla
                Assert.fail("Error: La categoría '" + categoryName + "' sigue visible en la tabla y debería haber sido eliminada.");
            }
        } catch (org.openqa.selenium.NoSuchElementException e) {
            // Si NO encuentra el elemento (NoSuchElementException)
            if (shouldBePresent) {
                // Si la categoría SÍ debería estar presente, la prueba falla
                Assert.fail("Error: La categoría '" + categoryName + "' no fue encontrada en la tabla.");
            }
        }
    }


    // =================================================================
    // GIVEN: PREPARACIÓN Y NAVEGACIÓN
    // =================================================================

    @Given("el usuario esta logueado como Admin y en la pagina de administracion de categorias")
    public void user_is_on_category_admin_page() {
        wait = new WebDriverWait(driver, TIMEOUT);

        // 1. Simular clic en el menú desplegable "Gestión" (SidebarAdmin.js)
        // Busca el div con el texto 'Gestión' que actúa como trigger.
        WebElement gestionMenu = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class, 'nav-link')][contains(., 'Gestión')]")
                )
        );
        gestionMenu.click();

        // 2. Simular clic en el enlace "Categorías" dentro del submenú
        // Busca el enlace (Link) con la URL de Categorías.
        WebElement categoryLink = wait.until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//a[@href='/admin/categorias'][contains(., 'Categorías')]")
                )
        );
        categoryLink.click();

        // 3. Esperar a que la página de Categorías cargue y el título sea visible
        wait.until(ExpectedConditions.urlContains("/admin/categorias"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(., 'Administrar Categorías')]")));

        // Espera opcional a que desaparezca el spinner de carga
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@role='status']")));
    }

    @Given("la categoria {string} ya existe en la base de datos")
    public void category_exists_in_database(String categoryName) {
        // Verifica que la categoría está presente antes de intentar editarla/eliminarla.
        assertCategoryPresence(categoryName, true);
    }

    // =================================================================
    // WHEN/AND: ACCIONES
    // =================================================================

    @When("el administrador hace clic en el boton {string}")
    public void admin_clicks_on_add_button(String buttonText) {
        // Se usa para el botón principal "Agregar Categoría"
        driver.findElement(By.xpath("//button[contains(., '" + buttonText + "')]")).click();

        // Esperamos a que el título del modal sea visible para confirmar que se abrió
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(., 'Categoría')]")));
    }

    @And("introduce el nombre {string} en el formulario")
    public void introduces_name_in_form(String categoryName) {
        // Selector de tu componente React: id="nombreCategoria"
        WebElement inputField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nombreCategoria")));
        inputField.clear(); // Limpiamos primero por si el campo ya tenía el nombre de edición
        inputField.sendKeys(categoryName);
    }

    @And("hace clic en el boton {string}")
    public void clicks_on_save_button(String buttonText) {
        // Busca el botón de Guardar/Cerrar en el modal-footer.
        driver.findElement(By.xpath("//div[@class='modal-footer rounded-bottom-3']//button[contains(., '" + buttonText + "')]")).click();
        // Espera a que termine la acción de guardado
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//button[contains(., 'Guardando...')]")));
    }

    @When("el administrador edita la categoria {string} a {string}")
    public void admin_edits_category(String oldName, String newName) {
        // 1. Clic en EDITAR en la fila de la categoría antigua
        clickButtonInRow(oldName, "Editar");

        // 2. Introduce el nuevo nombre (reutiliza el método)
        introduces_name_in_form(newName);

        // 3. Clic en GUARDAR (reutiliza el método)
        clicks_on_save_button("Guardar");
    }

    @When("el administrador hace clic en eliminar la categoria {string}")
    public void admin_clicks_on_delete_category(String categoryName) {
        // 1. Clic en ELIMINAR en la fila de la categoría para abrir el modal de confirmación
        clickButtonInRow(categoryName, "Eliminar");

        // 2. Espera a que aparezca el título del modal de confirmación
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[contains(., 'Confirmar Eliminación')]")));
    }

    @And("confirma la eliminacion en el modal de confirmacion")
    public void confirms_delete_modal() {
        // Clic en el botón "Eliminar" DENTRO del modal de confirmación (botón peligroso)
        driver.findElement(By.xpath("//div[contains(@class, 'modal-footer')]//button[contains(., 'Eliminar')]")).click();

        // Espera a que el modal de confirmación desaparezca
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//h5[contains(., 'Confirmar Eliminación')]")));
    }


    // =================================================================
    // THEN: VERIFICACIÓN
    // =================================================================

    @Then("el mensaje de exito {string} es visible en el modal")
    public void success_message_is_visible_in_modal(String successMessage) {
        // Busca el mensaje de éxito dentro del modal (div alert)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert'][contains(., '" + successMessage + "')]")));

        // Opcional: Cierra el modal después de un éxito para continuar
        driver.findElement(By.xpath("//button[contains(., 'Cerrar')]")).click();
    }

    @And("la categoria {string} aparece en la tabla de categorias")
    public void category_appears_in_table(String categoryName) {
        assertCategoryPresence(categoryName, true);
    }

    @Then("la categoria {string} ya no debe aparecer en la tabla de categorias")
    public void category_should_not_appear_in_table(String categoryName) {
        assertCategoryPresence(categoryName, false);
    }
}