# ST-Connect

**Tu Asistente ST** es una aplicación móvil diseñada para estudiantes de **Santo Tomás**, que permite acceder de manera rápida y organizada a información académica clave como horarios, calificaciones, evaluaciones y notificaciones. Esta app busca complementar el sistema institucional actual, facilitando el acceso y la organización del estudiante.  

---

## Problema que resuelve  
Actualmente, los estudiantes deben acceder al sistema institucional para consultar notas, horarios o evaluaciones, lo que puede resultar poco práctico.  
**Tu Asistente ST** centraliza toda esta información en una app intuitiva, rápida y con notificaciones inteligentes.  

---

## Funcionalidades principales  

### Pantallas iniciales  
- **Horario (vista principal)**: Visualización clara de asignaturas, horarios, salas y profesores.  
- **Calificaciones (vista)**: Listado de notas obtenidas por asignatura.  
- **Evaluaciones (junto al horario)**: Fechas y detalles de próximas evaluaciones.  
- **Vista emergente (popup)**: Al seleccionar una fecha en el horario se despliega información como sala, profesor y si hay evaluación.  
- **Acceso al aula virtual**: Link directo que abre la página web de la asignatura.  
- **Notificaciones (vista)**: Avisos sobre próximas clases, cambios de sala o evaluaciones.  

---

## Navegación entre pantallas  
- Uso de **Intents** para moverse entre:  
  - Pantalla principal  
  - Horario  
  - Calificaciones  
  - Evaluaciones  
  - Notificaciones  
  - Aula virtual (WebView)  

- **Datos en extras**:  
  - Desde horario → asignatura, sala, hora, profesor, si hay evaluación.  
  - Desde evaluaciones → fecha, asignatura, tipo de evaluación.  
  - Desde notificaciones → datos de la clase (sala, horario, asignatura).  

---

## Componentes de Android utilizados  

- **Activities** → cada vista principal (Horario, Calificaciones, Evaluaciones, Notificaciones).  
- **Fragments** → para dividir y organizar la vista de horario.  
- **RecyclerView** → para mostrar listas dinámicas de horarios, evaluaciones y notas.  
- **WebView** → para abrir el aula virtual.  
- **Notifications** → alertas sobre clases o evaluaciones.  
- **Service** → programación de notificaciones en segundo plano.  
- **BroadcastReceiver** → para manejar alarmas del calendario y notificaciones programadas.  

---

## Datos manejados  

- **Internos:**  
  - Horario de clases  
  - Notas y calificaciones  
  - Fechas de evaluaciones  
  - Salas, profesores y horas  

- **Externos:**  
  - Link al aula virtual  
  - Posible integración futura con API institucional  

---

## Riesgos y desafíos iniciales  
- Crear pantallas claras e intuitivas para el usuario.  
- Manejo de notificaciones en segundo plano.  
- Sincronización con información institucional externa (si se habilita).  

---

## Hitos de avance  

- **Semana 1:** Crear estructura base del proyecto en Android con Activities y navegación mediante Intents.  
- **Semana 2:** Implementar pantallas de horario y calificaciones con datos de prueba.  
- **Semana 3:** Implementar notificaciones locales para clases y evaluaciones, más el acceso a aulas virtuales.  

---

##  Tecnologías utilizadas  
- **Lenguaje:** Java (Android Studio)  
- **Framework:** Android SDK  
- **Diseño:** XML para interfaces  
- **Notificaciones y Servicios:** Android Services + BroadcastReceiver  

---

## Estado del proyecto  
 *En desarrollo*  

## Instalaccion
**Descargar el repositorio en el dispositivo:** git clone https://github.com/SyntaxInfinity/ST-Connect.git
<img width="687" height="212" alt="image" src="https://github.com/user-attachments/assets/46fe6d6c-a247-488c-ad61-d99e16992851" />

Abrir Android Studio y abrin un nuevo projecto 
<img width="519" height="857" alt="image" src="https://github.com/user-attachments/assets/a4a9d805-bee4-474f-b08d-a96dbd7ba720" />
Buscar el projecto clonado, abrirlo y esperar
<img width="947" height="581" alt="image" src="https://github.com/user-attachments/assets/3eb2fb73-c7e5-42ba-a1be-b7b17b2765df" />
Despues ejecutar el emulador del android studio para ejecutar el programa
<img width="1864" height="474" alt="image" src="https://github.com/user-attachments/assets/c80725ed-72fd-421e-ac09-0c8e2ce7f7a1" />
una vez ejecutada la simulacion usar el programa normalmente
<img width="491" height="1020" alt="image" src="https://github.com/user-attachments/assets/8da7e0ea-78a3-4bfe-86d5-0d816bf926b4" />

 ## Integrantes del equipo 
 Jordan Murillo - Benjamin Ponce

