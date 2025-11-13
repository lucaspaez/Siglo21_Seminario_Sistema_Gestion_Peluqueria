package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.TurnoCalendar;
import com.seminario.siglo21.sistemapeluqueria.persistencia.TurnoDAO;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import java.sql.SQLException;

public class TurnoController implements Initializable {

    // Constantes de configuración
    private static final LocalTime START_TIME = LocalTime.of(8, 0); // Inicio de la jornada
    private static final LocalTime END_TIME = LocalTime.of(20, 0);  // Fin de la jornada
    private static final int TIME_INTERVAL_MINUTES = 30;           // Intervalo de la cuadrícula
    private static final double ROW_HEIGHT = 40.0;                 // Altura en píxeles de una fila de 30 min

    // Propiedades FXML
    @FXML private DatePicker datePicker;
    @FXML private Label currentViewLabel;
    @FXML private ToggleGroup viewToggleGroup;
    @FXML private GridPane calendarGrid;
    @FXML private ScrollPane calendarScrollPane;
    @FXML private ToggleButton btnDayView;
    @FXML private ToggleButton btnWeekView;
    @FXML private CheckBox chkShowCompleted;
    @FXML private CheckBox chkShowCancelled;

    // Estado del Controlador
    private ObjectProperty<LocalDate> currentDate = new SimpleObjectProperty<>(LocalDate.now());
    private String currentView = "DAY"; // Por defecto
    private ObservableList<TurnoCalendar> currentAppointments = FXCollections.observableArrayList();

    private TurnoDAO turnoDAO = new TurnoDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Enlazar el DatePicker a la propiedad del controlador
        datePicker.valueProperty().bindBidirectional(currentDate);

        // Listener para la fecha: Cuando cambia la fecha, recargar la vista
        currentDate.addListener((obs, oldDate, newDate) -> {
            drawCalendarGrid();
            loadAppointments();
        });

        // Inicializar
        drawCalendarGrid();
        loadAppointments();
    }

    // --- Métodos de Navegación y Vista ---

    @FXML
    private void handlePreviousDate() {
        if (currentView.equals("DAY")) {
            currentDate.set(currentDate.get().minusDays(1));
        } else if (currentView.equals("WEEK")) {
            currentDate.set(currentDate.get().minusWeeks(1));
        }
    }

    @FXML
    private void handleNextDate() {
        if (currentView.equals("DAY")) {
            currentDate.set(currentDate.get().plusDays(1));
        } else if (currentView.equals("WEEK")) {
            currentDate.set(currentDate.get().plusWeeks(1));
        }
    }

    @FXML
    private void handleDateSelection() {
        // El listener en currentDate ya maneja la recarga.
    }

    @FXML
    private void handleChangeView(javafx.event.ActionEvent event) {
        ToggleButton selected = (ToggleButton) viewToggleGroup.getSelectedToggle();
        if (selected != null) {
            currentView = (String) selected.getUserData();
        } else {
            // Asegurar que siempre haya una vista seleccionada
            btnDayView.setSelected(true);
            currentView = "DAY";
        }
        drawCalendarGrid();
        loadAppointments();
    }

    // --- Lógica del Calendario (Dibujado) ---

    /**
     * Dibuja la cuadrícula del calendario (encabezados de días y filas de tiempo).
     */
    private void drawCalendarGrid() {
        calendarGrid.getChildren().clear();
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();

        List<LocalDate> datesToShow = getDatesForCurrentView();

        // 1. Dibuja las restricciones de columna
        // Columna 0: Horas (Fija)
        ColumnConstraints timeColumn = new ColumnConstraints(80);
        timeColumn.setHalignment(HPos.RIGHT);
        calendarGrid.getColumnConstraints().add(timeColumn);

        // Columnas de Días (Dinámicas)
        for (int i = 0; i < datesToShow.size(); i++) {
            ColumnConstraints dayColumn = new ColumnConstraints();
            dayColumn.setHgrow(Priority.ALWAYS);
            dayColumn.setMinWidth(120);
            calendarGrid.getColumnConstraints().add(dayColumn);

            // Agregar el encabezado del día
            VBox header = createDayHeader(datesToShow.get(i));
            calendarGrid.add(header, i + 1, 0);
        }

        // 2. Dibuja las filas de tiempo y los marcadores de hora (Columna 0)
        int row = 1;
        LocalTime currentTime = START_TIME;
        while (currentTime.isBefore(END_TIME) || currentTime.equals(END_TIME)) {
            // Restricción de Fila
            RowConstraints rowConstraints = new RowConstraints(ROW_HEIGHT);
            calendarGrid.getRowConstraints().add(rowConstraints);

            // Marcador de Hora (Columna 0)
            Label timeLabel = new Label(currentTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            timeLabel.getStyleClass().add("time-label");
            calendarGrid.add(timeLabel, 0, row);
            GridPane.setValignment(timeLabel, VPos.TOP); // Alineación para que coincida con la línea

            // Agregar listeners de DragOver y DragDropped a todas las celdas (para el Drag and Drop)
            addDropTargetToRow(row, datesToShow.size());

            currentTime = currentTime.plusMinutes(TIME_INTERVAL_MINUTES);
            row++;
        }

        updateViewLabel(datesToShow);
    }

    /**
     * Crea el encabezado de día (Ej: MIÉRCOLES 13)
     */
    private VBox createDayHeader(LocalDate date) {
        VBox header = new VBox(5);
        header.setAlignment(javafx.geometry.Pos.CENTER);
        header.getStyleClass().add("day-header");

        Label dayName = new Label(date.getDayOfWeek().toString());
        dayName.setStyle("-fx-font-weight: bold;");

        Label dayNum = new Label(date.format(DateTimeFormatter.ofPattern("dd/MM")));

        header.getChildren().addAll(dayName, dayNum);
        return header;
    }

    /**
     * Calcula las fechas a mostrar según la vista (Día o Semana).
     */
    private List<LocalDate> getDatesForCurrentView() {
        List<LocalDate> dates = new ArrayList<>();
        if (currentView.equals("DAY")) {
            dates.add(currentDate.get());
        } else if (currentView.equals("WEEK")) {
            // Empieza en Lunes
            LocalDate startOfWeek = currentDate.get().with(DayOfWeek.MONDAY);
            for (int i = 0; i < 7; i++) {
                dates.add(startOfWeek.plusDays(i));
            }
        }
        return dates;
    }

    /**
     * Actualiza la etiqueta de la vista (Ej: "Semana del 11 al 17 de Noviembre").
     */
    private void updateViewLabel(List<LocalDate> datesToShow) {
        if (datesToShow.isEmpty()) return;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd 'de' MMM");
        String labelText;

        if (currentView.equals("DAY")) {
            labelText = datesToShow.get(0).format(DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM yyyy"));
        } else { // WEEK
            LocalDate start = datesToShow.get(0);
            LocalDate end = datesToShow.get(datesToShow.size() - 1);
            labelText = "Semana del " + start.format(dtf) + " al " + end.format(dtf);
        }
        currentViewLabel.setText(labelText);
    }

    // --- Lógica de Carga y Renderizado de Turnos ---

    /**
     * ⚠️ SIMULACIÓN de la carga de Turnos.
     * En producción, esta función llamaría a TurnoDAO.obtenerTurnosPorRango().
     */
    // --- Lógica de Carga y Renderizado de Turnos ---

    @FXML
    private void loadAppointments() {
        // Limpiar turnos viejos de la cuadrícula
        List<Node> nodesToRemove = calendarGrid.getChildren().stream()
                // ⚠️ CORRECCIÓN: Primero verificar si los índices son NULOS
                .filter(node -> {
                    Integer col = GridPane.getColumnIndex(node);
                    Integer row = GridPane.getRowIndex(node);

                    // Asegurarse de que ambos índices no son nulos y que no son parte de los encabezados (Col 0 o Row 0)
                    // Columna 0 = Horas
                    // Fila 0 = Encabezados de Días
                    return col != null && row != null && col.intValue() > 0 && row.intValue() > 0;
                })
                .toList();

        calendarGrid.getChildren().removeAll(nodesToRemove);

        // 1. Determinar el rango de fechas
        List<LocalDate> datesToShow = getDatesForCurrentView();
        if (datesToShow.isEmpty()) return;

        LocalDate fechaInicio = datesToShow.get(0);
        LocalDate fechaFin = datesToShow.get(datesToShow.size() - 1);

        try {
            // 2. 📞 LLAMADA REAL AL DAO
            List<TurnoCalendar> turnos = turnoDAO.obtenerTurnosPorRango(fechaInicio, fechaFin);

            // 3. Renderiza cada turno
            for (TurnoCalendar turno : turnos) {
                renderAppointment(turno, datesToShow);
            }

        } catch (SQLException e) {
            // Manejo de errores de base de datos
            System.err.println("Error al cargar turnos desde la base de datos.");
            // ⚠️ Aquí puedes mostrar una alerta al usuario
            e.printStackTrace();
        }
    }

    /**
     * Renderiza un único bloque de cita en el GridPane.
     */
    private void renderAppointment(TurnoCalendar turno, List<LocalDate> visibleDates) {
        // 1. Ocultar si el filtro está activo
        if ((turno.getEstado().equalsIgnoreCase("REALIZADO") && !chkShowCompleted.isSelected()) ||
                (turno.getEstado().equalsIgnoreCase("CANCELADO") && !chkShowCancelled.isSelected())) {
            return;
        }

        // 2. Calcular la posición (columna/día)
        int colIndex = -1;
        for (int i = 0; i < visibleDates.size(); i++) {
            if (visibleDates.get(i).equals(turno.getFecha())) {
                colIndex = i + 1; // +1 porque la columna 0 es para las horas
                break;
            }
        }

        if (colIndex == -1) return; // La cita no está en el rango visible

        // 3. Calcular la posición (fila) y extensión (altura)
        int startRow = calculateRowIndex(turno.getHora());
        int rowSpan = calculateRowSpan(turno.getDuracionTotalMinutos());

        // 4. Crear el bloque visual (VBox con información)
        VBox appointmentPane = createAppointmentPane(turno);

        // 5. Agregar el bloque al GridPane
        calendarGrid.add(appointmentPane, colIndex, startRow, 1, rowSpan);

        // 6. Configurar Drag and Drop para el bloque (Permitir mover la cita)
        setupDragSource(appointmentPane, turno);
    }

    /**
     * Calcula el índice de fila inicial basado en la hora.
     */
    private int calculateRowIndex(LocalTime time) {
        long minutesFromStart = START_TIME.until(time, java.time.temporal.ChronoUnit.MINUTES);
        return (int) (minutesFromStart / TIME_INTERVAL_MINUTES) + 1; // +1 por la fila 0 de encabezado
    }

    /**
     * Calcula cuántas filas (rowSpan) ocupa la cita.
     */
    private int calculateRowSpan(int durationMinutes) {
        return Math.max(1, (int) Math.ceil((double) durationMinutes / TIME_INTERVAL_MINUTES));
    }

    /**
     * Crea el VBox que representa visualmente el turno.
     */
    private VBox createAppointmentPane(TurnoCalendar turno) {
        VBox pane = new VBox(2);
        pane.getStyleClass().addAll("appointment-block", turno.getEstado().toLowerCase() + "-turno");

        // Información del turno
        Label timeLabel = new Label(turno.getHora().format(DateTimeFormatter.ofPattern("HH:mm")) + " (" + turno.getDuracionTotalMinutos() + " min)");
        Label clientLabel = new Label(turno.getNombreCompletoCliente());
        Text serviceText = new Text(turno.getServiciosDescripcion());

        timeLabel.getStyleClass().add("appointment-time");
        clientLabel.getStyleClass().add("appointment-client");
        serviceText.getStyleClass().add("appointment-service");

        pane.getChildren().addAll(timeLabel, clientLabel, serviceText);

        // Acción de doble clic para editar
        pane.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                handleEditAppointment(turno);
            }
        });

        return pane;
    }

    // --- Drag and Drop Logic ---

    /**
     * Configura el bloque de cita como fuente de arrastre (Drag Source).
     */
    private void setupDragSource(VBox appointmentPane, TurnoCalendar turno) {
        appointmentPane.setOnDragDetected(event -> {
            Dragboard db = appointmentPane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();

            // ⚠️ ENVIAMOS EL ID Y LA DURACIÓN EN UNA CADENA SEPARADA POR COMA
            String dragData = String.valueOf(turno.getIdTurno()) + "," + String.valueOf(turno.getDuracionTotalMinutos());
            content.putString(dragData);

            db.setContent(content);
            event.consume();
        });
    }

    /**
     * Agrega el Drop Target (objetivo de soltar) a cada celda de la cuadrícula.
     */
    private void addDropTargetToRow(int row, int numDayColumns) {
        for (int col = 1; col <= numDayColumns; col++) {
            Pane dropTarget = new Pane();
            calendarGrid.add(dropTarget, col, row);

            // ... (DragOver se mantiene igual) ...

            dropTarget.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasString()) {
                    // ⚠️ RECUPERAR ID Y DURACIÓN DEL CLIPBOARD
                    String[] data = db.getString().split(",");
                    int turnoId = Integer.parseInt(data[0]);
                    // int durationMinutes = Integer.parseInt(data[1]); // Ya no se necesita si el DAO calcula la hora fin

                    int newRow = GridPane.getRowIndex(dropTarget);
                    int newCol = GridPane.getColumnIndex(dropTarget);

                    LocalDate newDate = getDatesForCurrentView().get(newCol - 1);
                    LocalTime newTime = START_TIME.plusMinutes((newRow - 1) * TIME_INTERVAL_MINUTES);

                    try {
                        // 📞 LLAMADA ACTUALIZADA AL DAO (sólo requiere ID, fecha y nueva hora de inicio)
                        turnoDAO.moverTurno(turnoId, newDate, newTime);
                        System.out.println("Turno " + turnoId + " movido exitosamente a: " + newDate + " " + newTime);
                        success = true;
                    } catch (SQLException e) {
                        System.err.println("Error al mover el turno en la DB: " + e.getMessage());
                        // Muestra un error al usuario
                    }

                    loadAppointments();
                }
                event.setDropCompleted(success);
                event.consume();
            });
        }
    }

    // --- Lógica de CRUD (Event Handlers) ---

    @FXML
    private void handleNewAppointment() {
        System.out.println("Abrir formulario para nuevo turno...");
        // Lógica para abrir una nueva ventana modal de creación de turno
    }

    private void handleEditAppointment(TurnoCalendar turno) {
        System.out.println("Abrir formulario para editar turno: " + turno.getIdTurno());
        // Lógica para abrir una nueva ventana modal con los datos del turno
    }

}
