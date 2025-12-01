Feature: Proceso de inicio de sesión de usuario

  Scenario: Inicio de sesión exitoso con credenciales validas
    Given el usuario esta en la pagina de inicio de sesion
    When el usuario ingresa el "perez@example.com" y la "123456"
    And hace clic en el boton de iniciar sesion
    Then el usuario deberia ver la pagina de inicio

  Scenario: Login exitoso de un administrador
    Given el usuario esta en la pagina de inicio de sesion
    When el usuario ingresa el "admin@admin.com" y la "123456"
    And hace clic en el boton de iniciar sesion
    Then el usuario deberia ver la pagina de inicio del administrador