package panels;

import app.Point;
import app.Task;
import java.util.ArrayList;

import controls.*;
import io.github.humbleui.jwm.*;
import io.github.humbleui.skija.Canvas;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;

import java.util.List;

import static app.Application.PANEL_PADDING;
import static app.Colors.FIELD_BACKGROUND_COLOR;
import static app.Colors.FIELD_TEXT_COLOR;

/**
 * Панель управления
 */
public class PanelControl extends GridPanel {
    /**
     * Текст задания
     */
    MultiLineLabel task;
    /**
     * Заголовки
     */
    public List<Label> labels;
    /**
     * Поля ввода
     */
    public List<Input> inputs;
    /**
     * Кнопки
     */
    public List<Button> buttons;
    /**
     * Кнопка решить
     */
    public static Button solve;

    /**
     * Панель управления
     *
     * @param window     окно
     * @param drawBG     флаг, нужно ли рисовать подложку
     * @param color      цвет подложки
     * @param padding    отступы
     * @param gridWidth  кол-во ячеек сетки по ширине
     * @param gridHeight кол-во ячеек сетки по высоте
     * @param gridX      координата в сетке x
     * @param gridY      координата в сетке y
     * @param colspan    кол-во колонок, занимаемых панелью
     * @param rowspan    кол-во строк, занимаемых панелью
     */
    public PanelControl(
            Window window, boolean drawBG, int color, int padding, int gridWidth, int gridHeight,
            int gridX, int gridY, int colspan, int rowspan
    ) {
        super(window, drawBG, color, padding, gridWidth, gridHeight, gridX, gridY, colspan, rowspan);

        // создаём списки
        inputs = new ArrayList<>();
        labels = new ArrayList<>();
        buttons = new ArrayList<>();

        // задание
        task = new MultiLineLabel(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 0, 0, 6, 2, Task.TASK_TEXT,
                false, true);
        // добавление вручную
        Label xLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 7, 0, 2, 1, 1, "X", true, true);
        labels.add(xLabel);
        Input xField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 7, 1, 2, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(xField);
        Label yLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 7, 3, 2, 1, 1, "Y", true, true);
        labels.add(yLabel);
        Input yField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 7, 4, 2, 2, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(yField);
        Button addToSet = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 0, 3, 3, 1, "Добавить",
                true, true);
        addToSet.setOnClick(() -> {
            // если числа введены верно
            if (!xField.hasValidDoubleValue()) {
                PanelLog.warning("X координата введена неверно");
            } else if (!yField.hasValidDoubleValue())
                PanelLog.warning("Y координата введена неверно");
            else
                PanelRendering.task.add(
                        new Vector2d(xField.doubleValue(), yField.doubleValue())
                );
        });
        buttons.add(addToSet);

        // случайное добавление
        Label cntLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 7, 0, 4, 1, 1, "Кол-во", true, true);
        labels.add(cntLabel);

        Input cntField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 7, 1, 4, 2, 1, "2", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(cntField);

        Button addPoints = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 3, 4, 3, 1, "Добавить\nслучайные прямоугольники",
                true, true);
        addPoints.setOnClick(() -> {
            // если числа введены верно
            if (!cntField.hasValidIntValue()) {
                PanelLog.warning("кол-во точек указано неверно");
            } else
                PanelRendering.task.addRandom(cntField.intValue());
        });
        buttons.add(addPoints);
        // управление
        Button load = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 0, 5, 3, 1, "Загрузить",
                true, true);
        load.setOnClick(() -> {
            PanelRendering.load();
            PanelRendering.task.cancel();
        });
        buttons.add(load);

        Button save = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 3, 5, 3, 1, "Сохранить",
                true, true);
        save.setOnClick(PanelRendering::save);
        buttons.add(save);

        Button clear = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 0, 6, 3, 1, "Очистить",
                true, true);
        clear.setOnClick(() -> PanelRendering.task.clear());
        buttons.add(clear);

        solve = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 7, 3, 6, 3, 1, "Решить",
                true, true, 1, "Сбросить");
        solve.setOnClick(() -> {
            if (!PanelRendering.task.isSolved()) {
                String s = "Вызов решения";
                PanelLog.success(s);
                PanelRendering.task.solve();
                //String s = "Задача решена\n";// +
                        //"Пара прямоугольников: " + PanelRendering.task.getIntersRect() + "\n" +
                        //"Площадь: " + PanelRendering.task.getAreaIntersRect();

                solve.changeText(2);
            } else {
                //String s = "Вызов отмены решения через кнопку";
                solve.changeText(1);
                //PanelLog.success(s);
                PanelRendering.task.cancel();
            }
            window.requestFrame();
        });
        buttons.add(solve);
    }



    /**
     * Обработчик событий
     *
     * @param e событие
     */
    @Override
    public void accept(Event e) {
        // вызываем обработчик предка
        super.accept(e);
        // событие движения мыши
        if (e instanceof EventMouseMove ee) {
            for (Input input : inputs)
                input.accept(ee);
            for (Button button : buttons) {
                if (lastWindowCS != null)
                    button.checkOver(lastWindowCS.getRelativePos(new Vector2i(ee)));
            }

            // событие нажатия мыши
        } else if (e instanceof EventMouseButton ee) {
            if (!lastInside || !ee.isPressed())
                return;

            Vector2i relPos = lastWindowCS.getRelativePos(lastMove);

            // пробуем кликнуть по всем кнопкам
            for (Button button : buttons) {
                button.click(relPos);
            }

            // перебираем поля ввода
            for (Input input : inputs) {
                // если клик внутри этого поля
                if (input.contains(relPos)) {
                    // переводим фокус на это поле ввода
                    input.setFocus();
                }
            }
            // перерисовываем окно
            window.requestFrame();
            // обработчик ввода текста
        } else if (e instanceof EventTextInput ee) {
            for (Input input : inputs) {
                if (input.isFocused()) {
                    input.accept(ee);
                }
            }
            // перерисовываем окно
            window.requestFrame();
            // обработчик ввода клавиш
        } else if (e instanceof EventKey ee) {
            for (Input input : inputs) {
                if (input.isFocused()) {
                    input.accept(ee);
                }
            }
            // перерисовываем окно
            window.requestFrame();
        }
    }

    /**
     * Метод под рисование в конкретной реализации
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    @Override
    public void paintImpl(Canvas canvas, CoordinateSystem2i windowCS) {
        // выводим текст задачи
        task.paint(canvas, windowCS);

        // выводим кнопки
        for (Button button : buttons) {
            button.paint(canvas, windowCS);
        }
        // выводим поля ввода
        for (Input input : inputs) {
            input.paint(canvas, windowCS);
        }
        // выводим поля ввода
        for (Label label : labels) {
            label.paint(canvas, windowCS);
        }
    }

}
