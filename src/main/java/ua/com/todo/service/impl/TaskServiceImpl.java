package ua.com.todo.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.todo.model.StatisticDate;
import ua.com.todo.model.Status;
import ua.com.todo.model.Task;
import ua.com.todo.repository.TaskRepository;
import ua.com.todo.service.TaskService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private static final String SHEET = "Tasks";
    private static final float TITLE_HEIGHT_IN_POINTS = 30;
    private static final int COLUMNS_WIDTH = 15 * 256;
    private static final String TITLE = "title";
    private static final String HEADER = "header";
    private static final String TABLE = "table";
    private static final String FILTER = "filter";
    private static final String NUMBERS = "numbers";
    private final TaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        task.setStartTask(LocalDateTime.now());
        task.setStatus(Status.NEW);
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> getByName(String name) {
        return taskRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    @Override
    public Task getById(Long id) {
        Optional<Task> taskFromDB = taskRepository.findById(id);
        return taskFromDB.orElseThrow(() -> new IllegalArgumentException("Task with such id doesn't exist!"));
    }

    @Override
    public Task update(Task task, Long taskId) {
        Task taskFromDB = getById(taskId);
        taskFromDB.setName(task.getName());
        taskFromDB.setDescription(task.getDescription());
        taskRepository.save(taskFromDB);
        return taskFromDB;
    }

    @Override
    public void delete(Long id) {
        Task task = getById(id);
        taskRepository.delete(task);
    }

    @Override
    public Task changeStatus(Long id, Status status) {
        Task task = getById(id);
        if (!task.getStatus().equals(Status.FINISHED)) {
            task.setStatus(status);
            if (Status.FINISHED.equals(task.getStatus())) {
                task.setFinishTask(LocalDateTime.now());
            }
            taskRepository.save(task);
        }
        return task;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> getStatistic(StatisticDate date, int num) {
        switch (date) {
            case DAY:
                LocalDateTime minusDays = LocalDateTime.now().minusDays(num);
                return getResultTasks(minusDays);
            case WEEK:
                LocalDateTime minusWeeks = LocalDateTime.now().minusWeeks(num);
                return getResultTasks(minusWeeks);
            case MONTH:
                LocalDateTime minusMonths = LocalDateTime.now().minusMonths(num);
                return getResultTasks(minusMonths);
            case YEAR:
                LocalDateTime minusYears = LocalDateTime.now().minusYears(num);
                return getResultTasks(minusYears);
            default:
                return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Task> getAllByTimePeriod(LocalDateTime from, LocalDateTime to) {

        boolean isFromAndToPresent = Objects.nonNull(from) && Objects.nonNull(to);
        boolean isFromPresent = Objects.nonNull(from);
        boolean isToPresent = Objects.nonNull(to);

        return isFromAndToPresent ? taskRepository.findByFinishTaskBetween(from, to) :
                isFromPresent ? taskRepository.findByFinishTaskLessThanEqual(from) :
                        isToPresent ? taskRepository.findByFinishTaskGreaterThanEqual(to) : taskRepository.findByFinishTaskIsNotNull();
    }

    @Transactional(readOnly = true)
    @Override
    public ByteArrayInputStream loadTaskReport(LocalDateTime from, LocalDateTime to) {
        List<Task> tasks = getAllByTimePeriod(from, to);
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
            sheet.setColumnWidth(0, COLUMNS_WIDTH);
            sheet.setColumnWidth(1, COLUMNS_WIDTH);
            sheet.setColumnWidth(2, COLUMNS_WIDTH);
            sheet.setColumnWidth(3, COLUMNS_WIDTH);
            sheet.setColumnWidth(4, COLUMNS_WIDTH);

            var styles = createStylesMap(workbook);

            Row tableHeaderRow = sheet.createRow(1);
            createCell(tableHeaderRow, 0, styles.get(HEADER)).setCellValue("id");
            createCell(tableHeaderRow, 1, styles.get(HEADER)).setCellValue("name");
            createCell(tableHeaderRow, 2, styles.get(HEADER)).setCellValue("status");
            createCell(tableHeaderRow, 3, styles.get(HEADER)).setCellValue("description");
            createCell(tableHeaderRow, 4, styles.get(HEADER)).setCellValue("importance");

            writeTasksToExcel(tasks, sheet, styles);

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    private void writeTasksToExcel(List<Task> tasks, Sheet sheet, Map<String, XSSFCellStyle> styles) {
        int rowCount = 1;

        for(Task t : tasks) {
            Row contentRow = sheet.createRow(++rowCount);
            createCell(contentRow, 0, styles.get(TABLE)).setCellValue(t.getId());
            createCell(contentRow, 1, styles.get(TABLE)).setCellValue(t.getName());
            createCell(contentRow, 2, styles.get(TABLE)).setCellValue(t.getStatus().toString());
            createCell(contentRow, 3, styles.get(TABLE)).setCellValue(t.getDescription());
            createCell(contentRow, 4, styles.get(TABLE)).setCellValue(t.getImportance());
        }
    }

    private Cell createCell(Row row, int column, XSSFCellStyle style) {
        var cell = row.createCell(column);
        cell.setCellStyle(style);
        return cell;
    }

    private Map<String, XSSFCellStyle> createStylesMap(XSSFWorkbook workbook) {
        Map<String, XSSFCellStyle> styles = new HashMap<>();
        styles.put(FILTER, createCommonStyle(workbook));

        var style = createCommonStyle(workbook);
        addBoldFont(style);
        styles.put(TITLE, style);

        style = createCommonStyle(workbook);
        addBorder(style);
        styles.put(TABLE, style);

        style = createCommonStyle(workbook);
        addBoldFont(style);
        addBorder(style);
        styles.put(HEADER, style);

        style = createCommonStyle(workbook);
        addBorder(style);
        DataFormat dataFormat = workbook.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("# ##0"));
        styles.put(NUMBERS, style);

        return styles;
    }

    private XSSFCellStyle createCommonStyle(XSSFWorkbook book) {
        XSSFCellStyle style = book.createCellStyle();
        XSSFFont font = book.createFont();

        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        font.setFontHeightInPoints((short) 9);
        font.setFontName("Arial");
        style.setFont(font);

        return style;
    }

    private void addBoldFont(XSSFCellStyle style) {
        var font = style.getFont();
        font.setBold(true);
        style.setFont(font);
    }

    private void addBorder(XSSFCellStyle style) {
        BorderStyle border = BorderStyle.THIN;
        style.setBorderTop(border);
        style.setBorderBottom(border);
        style.setBorderRight(border);
        style.setBorderLeft(border);
    }

    private List<Task> getResultTasks(LocalDateTime minusDays) {
        return taskRepository.findAll().stream().filter(e -> Objects.nonNull(e.getFinishTask())).filter(e -> minusDays.compareTo(e.getFinishTask()) < 0).collect(Collectors.toList());
    }


}
