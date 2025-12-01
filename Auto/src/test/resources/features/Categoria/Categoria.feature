Feature: Administracion de Categorias de Componentes (CRUD)

  # @Category es la etiqueta que usarás en el Runner para ejecutar esta Suite
  Background:
    # El usuario debe estar en la página de administración de categorías ANTES de cada escenario.
    Given el usuario esta logueado como Admin y en la pagina de administracion de categorias

  @CRUD @Create @Category
  Scenario: 1. Crear una nueva categoria con datos validos
    When el administrador hace clic en el boton "Agregar Categoria"
    And introduce el nombre "Accesorios" en el formulario
    And hace clic en el boton "Guardar"
    Then el mensaje de exito "Categoría agregada correctamente." es visible en el modal
    And la categoria "Accesorios" aparece en la tabla de categorias

  @CRUD @Edit @Category
  Scenario: 2. Editar el nombre de una categoria existente
    Given la categoria "Accesorios" ya existe en la base de datos
    When el administrador edita la categoria "Accesorios" a "Accesorios gaming"
    Then el mensaje de exito "Categoría actualizada correctamente." es visible en el modal
    And la categoria "Accesorios gaming" aparece en la tabla de categorias

  @CRUD @Delete @Category
  Scenario: 3. Eliminar una categoria del sistema
    Given la categoria "Accesorios" existe
    When el administrador hace clic en eliminar la categoria "Accesorios"
    And confirma la eliminacion en el modal de confirmacion
    Then la categoria "Accesorios" ya no debe aparecer en la tabla de categorias