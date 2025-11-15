package com.seminario.siglo21.sistemapeluqueria.controlador;

import com.seminario.siglo21.sistemapeluqueria.modelo.TurnoCalendar;
import com.seminario.siglo21.sistemapeluqueria.persistencia.TurnoDAO;
import com.seminario.siglo21.sistemapeluqueria.util.VistaUtil;
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

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import java.sql.SQLException;

public class TurnoController implements Initializable {

    // Constantes de configuración
    private static final LocalTime HORA_INICIO = LocalTime.of(8, 0); // Inicio de la jornada
    private static final LocalTime HORA_FIN = LocalTime.of(20, 0);  // Fin de la jornada
    private static final int INTERVALO_MINUTOS = 30;           // Intervalo de la cuadrícula
    private static final double ALTO_FILA = 40.0;                 // Altura en píxeles de una fila de 30 min

    // CONSTANTE NUEVA PARA TRADUCIR
    private static final DateTimeFormatter FORMATO_DIA_SEMANA =
            DateTimeFormatter.ofPattern("EEEE", new Locale("es", "AR"));

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
    private ObjectProperty<LocalDate> fechaActual = new SimpleObjectProperty<>(LocalDate.now());

    // CAMBIO: Variable y valor por defecto traducidos
    private String vistaActual = "DIA"; // Por defecto

    private ObservableList<TurnoCalendar> turnosActuales = FXCollections.observableArrayList();

    private TurnoDAO turnoDAO = new TurnoDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Enlazar el DatePicker a la propiedad del controlador
        datePicker.valueProperty().bindBidirectional(fechaActual);

        // Listener para la fecha: Cuando cambia la fecha, recargar la vista
        fechaActual.addListener((obs, oldDate, newDate) -> {
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
        // CAMBIO: Comparación con valores en español
        if (vistaActual.equals("DIA")) {
            fechaActual.set(fechaActual.get().minusDays(1));
        } else if (vistaActual.equals("SEMANA")) {
            fechaActual.set(fechaActual.get().minusWeeks(1));
        }
    }

    @FXML
    private void handleNextDate() {
        // CAMBIO: Comparación con valores en español
        if (vistaActual.equals("DIA")) {
            fechaActual.set(fechaActual.get().plusDays(1));
        } else if (vistaActual.equals("SEMANA")) {
            fechaActual.set(fechaActual.get().plusWeeks(1));
        }
    }

    @FXML
    private void handleDateSelection() {
        // El listener en fechaActual ya maneja la recarga.
    }

    @FXML
    private void handleChangeView(javafx.event.ActionEvent event) {
        ToggleButton selected = (ToggleButton) viewToggleGroup.getSelectedToggle();
        if (selected != null) {
            // CAMBIO: Se obtiene el userData en español ("DIA" o "SEMANA")
            vistaActual = (String) selected.getUserData();
        } else {
            // Asegurar que siempre haya una vista seleccionada
            btnDayView.setSelected(true);
            vistaActual = "DIA";
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
        LocalTime currentTime = HORA_INICIO;
        while (currentTime.isBefore(HORA_FIN) || currentTime.equals(HORA_FIN)) {
            // Restricción de Fila
            RowConstraints rowConstraints = new RowConstraints(ALTO_FILA);
            calendarGrid.getRowConstraints().add(rowConstraints);

            // Marcador de Hora (Columna 0)
            Label timeLabel = new Label(currentTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            timeLabel.getStyleClass().add("time-label");
            calendarGrid.add(timeLabel, 0, row);
            GridPane.setValignment(timeLabel, VPos.TOP); // Alineación para que coincida con la línea

            // Agregar listeners de DragOver y DragDropped a todas las celdas (para el Drag and Drop)
            addDropTargetToRow(row, datesToShow.size());

            currentTime = currentTime.plusMinutes(INTERVALO_MINUTOS);
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

        Label dayName = new Label(date.format(FORMATO_DIA_SEMANA).toUpperCase());
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
        // CAMBIO: Comparación con valores en español
        if (vistaActual.equals("DIA")) {
            dates.add(fechaActual.get());
        } else if (vistaActual.equals("SEMANA")) {
            // Empieza en Lunes
            LocalDate startOfWeek = fechaActual.get().with(DayOfWeek.MONDAY);
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

        // CAMBIO: Comparación con valores en español
        if (vistaActual.equals("DIA")) {
            labelText = datesToShow.get(0).format(DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM yyyy"));
        } else { // SEMANA
            LocalDate start = datesToShow.get(0);
            LocalDate end = datesToShow.get(datesToShow.size() - 1);
            labelText = "Semana del " + start.format(dtf) + " al " + end.format(dtf);
        }
        currentViewLabel.setText(labelText);
    }

    // --- Lógica de Carga y Renderizado de Turnos ---

    @FXML
    public void loadAppointments() {
        // Limpiar turnos viejos de la cuadrícula
        // Este filtro solo borra los VBox de turnos (que tienen este estilo).
        List<Node> nodesToRemove = calendarGrid.getChildren().stream()
                .filter(node -> node.getStyleClass().contains("appointment-block"))
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
            System.err.println("Error al cargar turnos desde la base de datos.");
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
        long minutesFromStart = HORA_INICIO.until(time, java.time.temporal.ChronoUnit.MINUTES);
        return (int) (minutesFromStart / INTERVALO_MINUTOS) + 1; // +1 por la fila 0 de encabezado
    }

    /**
     * Calcula cuántas filas (rowSpan) ocupa la cita.
     */
    private int calculateRowSpan(int durationMinutes) {
        return Math.max(1, (int) Math.ceil((double) durationMinutes / INTERVALO_MINUTOS));
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

    // --- Lógica de Arrastrar y Soltar (Drag and Drop) ---

    /**
     * Configura el bloque de cita como fuente de arrastre (Drag Source).
     */
    private void setupDragSource(VBox appointmentPane, TurnoCalendar turno) {
        appointmentPane.setOnDragDetected(event -> {
            Dragboard db = appointmentPane.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();

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

            // Hacemos que el panel sea transparente (no captura clics)
            // pero sí esté presente para el D&D.
            //dropTarget.setPickOnBounds(false);

            calendarGrid.add(dropTarget, col, row);

            // Este manejador es ESENCIAL. Es el "saludo" que le
            // dice al sistema que esta celda ACEPTA un 'Drop'.
            dropTarget.setOnDragOver(event -> {
                // Acepta el gesto de 'MOVER' si el origen no es él mismo
                if (event.getGestureSource() != dropTarget && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            });

            dropTarget.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;

                if (db.hasString()) {
                    String[] data = db.getString().split(",");
                    int turnoId = Integer.parseInt(data[0]);

                    int newRow = GridPane.getRowIndex(dropTarget);
                    int newCol = GridPane.getColumnIndex(dropTarget);

                    LocalDate newDate = getDatesForCurrentView().get(newCol - 1);
                    LocalTime newTime = HORA_INICIO.plusMinutes((newRow - 1) * INTERVALO_MINUTOS);

                    try {
                        turnoDAO.moverTurno(turnoId, newDate, newTime);
                        System.out.println("Turno " + turnoId + " movido exitosamente a: " + newDate + " " + newTime);
                        success = true;
                    } catch (SQLException e) {
                        System.err.println("Error al mover el turno en la DB: " + e.getMessage());
                    }

                    loadAppointments(); // Recargar la vista
                }
                event.setDropCompleted(success);
                event.consume();
            });
        }
    }

    // --- Lógica de CRUD (Event Handlers) ---

    @FXML
    public void handleNewAppointment() throws IOException {
        System.out.println("Abrir formulario para nuevo turno...");

        VistaUtil.mostrarVentanaModal(
                "/com/seminario/siglo21/sistemapeluqueria/DialogoTurno.fxml",
                "Agendar un turno"
        );

    }

    private void handleEditAppointment(TurnoCalendar turno) {
        System.out.println("Abrir formulario para editar turno: " + turno.getIdTurno());
        // Lógica para abrir una nueva ventana modal con los datos del turno
    }
}