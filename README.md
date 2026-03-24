
# 📱 DistribuidorApp

Aplicación móvil desarrollada en **Android Studio** para el cálculo de despacho en base a la ubicación del cliente/usuario respecto de la distribuidora.

---

## 🏗️ Arquitectura del Proyecto

- **Lenguaje principal:** Java  
- **Estructura de proyecto:** Android Studio con Gradle  
- **Paquete principal:** `com.example.distribuidora`  

### 1. Configuración de Proyecto
- **Gradle Scripts**
  - `settings.gradle` → configuración de módulos.  
  - `build.gradle` (app) → dependencias, configuración de SDK y librerías.  

### 2. Actividades Principales
- **LoginActivity.java**  
  - Maneja el inicio de sesión del usuario.  
  - Validación de credenciales.  
- **MenuActivity.java**  
  - Pantalla principal posterior al login.  
  - Permite acceder a funcionalidades del sistema (ejemplo: calcular despacho, ubicación, etc.).  

### 3. Recursos
- **Layouts (XML)**  
  - `activity_login.xml` → interfaz de login.  
  - `activity_menu.xml` → interfaz principal con menú de opciones.  
- **Drawables**  
  - Íconos de la aplicación.  
- **Values**  
  - Definición de estilos, colores y textos.  

### 4. Manifest
- **AndroidManifest.xml**  
  - Configuración de permisos (ubicación, internet, etc.).  
  - Declaración de actividades de la aplicación.  

### 5. Funcionalidades Clave
- Autenticación básica de usuarios.  
- Obtención de la ubicación del cliente/usuario.  
- Cálculo del despacho en base a la distancia con la distribuidora.  
- Navegación entre pantallas mediante menú principal.  

---

## 👥 Historias de Usuario

1. **Como usuario**, quiero poder iniciar sesión con mis credenciales para acceder de forma segura a la aplicación.  

2. **Como usuario**, quiero que la aplicación detecte mi ubicación actual, para que el cálculo de despacho sea preciso.  

3. **Como cliente**, quiero visualizar el costo de despacho según mi ubicación, para decidir si realizo el pedido.  

4. **Como distribuidor**, quiero que el sistema me muestre un listado de pedidos pendientes con su ubicación, para planificar mis entregas.  

5. **Como usuario**, quiero navegar fácilmente desde el menú principal hacia las diferentes funciones (pedido, cálculo, historial), para tener una mejor experiencia.  
