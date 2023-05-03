package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.humbleui.skija.*;
import lombok.Getter;
import misc.*;
import panels.PanelControl;
import panels.PanelLog;
import panels.PanelRendering;

import java.util.ArrayList;
import java.util.Objects;

import static app.Colors.*;

/**
 *  Глобальные проблемы:
 *
 *  1. Добавить рисование штриховки в случае сторон, параллельных OY
 *
 *  2. Что-то сделать при начале добавление ручного ввода, а потом по случайным значениям (они некорректно добавляются)
 *
 *
 */

/**
 * Класс задачи
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Task {
    /**
     * Флаг, решена ли задача
     */
    private boolean solved;
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            На плоскости задано множество прямоугольников.\040
            Найти такую пару пересекающихся прямоугольников,\040
            что площадь фигуры, находящейся внутри обоих\040
            прямоугольников, будет максимальна. В качестве ответа:\040
            выделить эту пару прямоугольников, выделить контур\040
            фигуры, находящейся внутри обоих треугольников,\040
            желательно "залить цветом" внутреннее пространство этой\040
            фигуры.""";


    /**
     * Вещественная система координат задачи
     */
    @Getter
    private final CoordinateSystem2d ownCS;
    /**
     * Список точек
     */
    @Getter
    private final ArrayList<Point> points;
    /**
     * список отрезков
     */
    @Getter
    private final ArrayList<Line> lines;
    /**
     * список отрезков штриховки
     */
    @Getter
    private final ArrayList<Line> hatchingLines;
    /**
     * Список прямоугольников
     */
    @Getter
    private final ArrayList<Rectangle> rectangles;
    /**
     * Какая опорная точка вводится
     */
    @Getter
    @JsonIgnore
    private int COUNT_POINT_CREATING_RECT = 1;
    /**
     * Первая опорная точка
     */
    @Getter
    @JsonIgnore
    private Vector2d FIRST_POINT;
    /**
     * Вторая опорная точка
     */
    @Getter
    @JsonIgnore
    private Vector2d SECOND_POINT;
    /**
     * Опорный отрезок
     */
    @Getter
    @JsonIgnore
    private Line FIRST_LINE;
    /**
     * Первый прямоугольник ответа
     */
    @Getter
    @JsonIgnore
    private Rectangle ansRectF;
    /**
     * Второй прямоугольник ответа
     */
    @Getter
    @JsonIgnore
    private Rectangle ansRectS;
    /**
     * последняя СК окна
     */
    protected CoordinateSystem2i lastWindowCS;
    /**
     * Порядок разделителя сетки, т.е. раз в сколько отсечек
     * будет нарисована увеличенная
     */
    private static final int DELIMITER_ORDER = 10;
    /**
     * коэффициент колёсика мыши
     */
    private static final float WHEEL_SENSITIVE = 0.001f;

    /**
     * Задача
     *
     * @param ownCS  СК задачи
     * @param points массив точек
     */
    @JsonCreator
    public Task(
            @JsonProperty("ownCS") CoordinateSystem2d ownCS,
            @JsonProperty("points") ArrayList<Point> points,
            @JsonProperty("lines") ArrayList<Line> lines,
            @JsonProperty("rectangles") ArrayList<Rectangle> rectangles,
            @JsonProperty("hatchingLines") ArrayList<Line> hatchingLines
    ) {
        this.ownCS = ownCS;
        this.points = points;
        this.lines = lines;
        this.ansRectF = Rectangle.nullRectangle();
        this.ansRectS = Rectangle.nullRectangle();
        this.rectangles = rectangles;
        this.hatchingLines = hatchingLines;
    }

    /**
     * Масштабирование области просмотра задачи
     *
     * @param delta  прокрутка колеса
     * @param center центр масштабирования
     */
    public void scale(float delta, Vector2i center) {
        if (lastWindowCS == null) return;
        // получаем координаты центра масштабирования в СК задачи
        Vector2d realCenter = ownCS.getCoords(center, lastWindowCS);
        // выполняем масштабирование
        ownCS.scale(1 + -delta * WHEEL_SENSITIVE, realCenter);
    }

    /**
     * Рисование задачи
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    private void renderTask(Canvas canvas, CoordinateSystem2i windowCS) {
        canvas.save();
        // создаём перо
        try (var paint = new Paint()) {
            for (Point p : points) {
                paint.setColor(p.getColor());
                // y-координату разворачиваем, потому что у СК окна ось y направлена вниз,
                // а в классическом представлении - вверх
                Vector2i windowPos = windowCS.getCoords(p.pos.x, p.pos.y, ownCS);
                // рисуем точку
                canvas.drawRRect(RRect.makeXYWH(windowPos.x - 3, windowPos.y - 3, 6, 6, 3), paint);
            }
            for (Line l : lines) {
                paint.setColor(l.getColor());
                // y-координату разворачиваем, потому что у СК окна ось y направлена вниз,
                // а в классическом представлении - вверх
                Vector2i windowPos1 = windowCS.getCoords(l.pointA.x, l.pointA.y, ownCS);
                Vector2i windowPos2 = windowCS.getCoords(l.pointB.x, l.pointB.y, ownCS);
                // рисуем точку
                canvas.drawLine(windowPos1.x, windowPos1.y, windowPos2.x, windowPos2.y, paint);
            }
            for (Line l : hatchingLines) {
                paint.setColor(l.getColor());
                // y-координату разворачиваем, потому что у СК окна ось y направлена вниз,
                // а в классическом представлении - вверх
                Vector2i windowPos1 = windowCS.getCoords(l.pointA.x, l.pointA.y, ownCS);
                Vector2i windowPos2 = windowCS.getCoords(l.pointB.x, l.pointB.y, ownCS);
                // рисуем точку
                canvas.drawLine(windowPos1.x, windowPos1.y, windowPos2.x, windowPos2.y, paint);
            }
        }
        canvas.restore();
    }

    /**
     * Рисование
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        // Сохраняем последнюю СК
        lastWindowCS = windowCS;
        // рисуем координатную сетку
        renderGrid(canvas, lastWindowCS);
        // рисуем задачу
        renderTask(canvas, windowCS);
    }

    /**
     * Клик мыши по пространству задачи
     *
     * @param pos         положение мыши
     */
    public void click(Vector2i pos) {
        if (lastWindowCS == null) return;
        // получаем положение на экране
        Vector2d taskPos = ownCS.getCoords(pos, lastWindowCS);
        // добавляем, исходя из того, какая по счёта опорная точка
        add(taskPos);
    }
    /**
     * добавить точку с рисованием остального
     */
    public void add(Vector2d taskPos) {
        if (COUNT_POINT_CREATING_RECT == 1) {
            addPoint(taskPos);
            FIRST_POINT = taskPos;
            COUNT_POINT_CREATING_RECT = 2;
        } else if (COUNT_POINT_CREATING_RECT == 2) {
            addPoint(taskPos);
            SECOND_POINT = taskPos;
            COUNT_POINT_CREATING_RECT = 3;
            addLine(FIRST_POINT, SECOND_POINT);
            FIRST_LINE = new Line(FIRST_POINT, SECOND_POINT);
        } else {
            addPoint(taskPos);
            // рассчитываем расстояние от прямой до точки
            double dist = FIRST_LINE.getDistance(taskPos);
            // рассчитываем векторы для векторного умножения
            Vector2d AB = Vector2d.subtract(SECOND_POINT, FIRST_POINT);
            Vector2d AP = Vector2d.subtract(taskPos, FIRST_POINT);
            // определяем направление смещения
            double direction = Math.signum(AB.cross(AP));
            // получаем вектор смещения
            Vector2d offset = AB.rotated(Math.PI / 2 * direction).norm().mult(dist);

            // находим координаты вторых двух вершин прямоугольника
            Vector2d pointC = Vector2d.sum(SECOND_POINT, offset);
            addPoint(pointC);
            Vector2d pointD = Vector2d.sum(FIRST_POINT, offset);
            addPoint(pointD);

            // рисуем его стороны
            Line BC = new Line(SECOND_POINT, pointC);
            addLine(SECOND_POINT, pointC);
            Line CD = new Line(pointC, pointD);
            addLine(pointC, pointD);
            Line DA = new Line(pointD, FIRST_POINT);
            addLine(pointD, FIRST_POINT);
            COUNT_POINT_CREATING_RECT = 1;

            // создаём

            addRectangle(FIRST_POINT, SECOND_POINT, pointC, pointD,
                    FIRST_LINE, BC, CD, DA);
        }
    }

    /**
     * Добавить точку
     *
     * @param pos положение
     */
    public void addPoint(Vector2d pos) {
        cancel();
        Point newPoint = new Point(pos);
        points.add(newPoint);
        // Добавляем в лог запись информации
        PanelLog.info("точка " + newPoint + " создана");
    }

    /**
     * Добавить точку
     *
     * @param pos положение
     */
    public void addPointInter(Vector2d pos) {
        Point newPoint = new Point(pos);
        newPoint.intersect();
        points.add(newPoint);
        // Добавляем в лог запись информации
        PanelLog.info("точка " + newPoint + " создана");
    }

    /**
     * добавить пересечения отрезок
     */
    public void addLineInter(Vector2d first, Vector2d second) {
        Line newLine = new Line(first, second);
        newLine.addToAns();
        lines.add(newLine);
        PanelLog.info("отрезок " + newLine + " создан");
    }

    /**
     * добавить отрезок штриховки
     */
    public void addLineHatching(Vector2d first, Vector2d second) {
        Line newLine = new Line(first, second);
        newLine.addToAns();
        hatchingLines.add(newLine);
        PanelLog.info("отрезок штриховки " + newLine + " создан");
    }

    /**
     * Добавить случайные точки
     *
     * @param cnt кол-во случайных точек
     */
    public void addRandom(int cnt) {
        // если создавать точки с полностью случайными координатами,
        // то вероятность того, что они совпадут крайне мала
        // поэтому нужно создать вспомогательную малую целочисленную ОСК
        // для получения случайной точки мы будем запрашивать случайную
        // координату этой решётки (их всего 30х30=900).
        // после нам останется только перевести координаты на решётке
        // в координаты СК задачи
        CoordinateSystem2i addGrid = new CoordinateSystem2i(30, 30);

        // повторяем заданное количество раз
        for (int i = 0; i < cnt; i++) {
            for (int j = 0; j < 3; j++) {
                // получаем случайные координаты на решётке
                Vector2i gridPos = addGrid.getRandomCoords();
                // получаем координаты в СК задачи
                Vector2d taskPos = ownCS.getCoords(gridPos, addGrid);
                // сработает примерно в половине случаев
                add(taskPos);
            }
        }
    }

    /**
     * Добавить отрезок
     */
    public void addLine(Vector2d fps, Vector2d sps) {
        cancel();
        Line newLine = new Line(fps, sps);
        lines.add(newLine);
        // Добавляем в лог запись информации
        PanelLog.info("отрезок " + newLine + " создан");
    }

    /**
     * Добавить прямоугольник
     */
    public void addRectangle(Vector2d A, Vector2d B, Vector2d C, Vector2d D, Line AB, Line BC, Line CD, Line DA) {
        cancel();
        Rectangle newRectangle = new Rectangle(A, B, C, D, AB, BC, CD, DA);
        rectangles.add(newRectangle);
        // Добавляем в лог запись информации
        PanelLog.info("прямоугольник " + newRectangle + " создан");
    }

    /**
     * сортировка точек для выпуклой фигуры
     *
     * @param mass список точек
     * @return отсортированный список
     */
    public ArrayList<Vector2d> sortForCF(ArrayList<Vector2d> mass) {
        ArrayList<Vector2d> ans = new ArrayList<>();
        if (mass.size() != 0) {
            Vector2d first = mass.get(0);
            for (Vector2d point : mass) {
                if (point.x < first.x ||
                        point.x == first.x && point.y > first.y) {
                    first = point;
                }
            }
            ans.add(first);
            mass.remove(first);
            while (mass.size() != 0) {
                Vector2d Point = mass.get(0);
                for (Vector2d point : mass) {
                    double a1 = Math.atan((point.x - first.x) / (first.y - point.y));
                    double a2 = Math.atan((Point.x - first.x) / (first.y - Point.y));
                    if (a1 < 0) {
                        a1 += Math.PI;
                    }
                    if (a2 < 0) {
                        a2 += Math.PI;
                    }
                    if (a1 < a2) {
                        Point = point;
                    }
                }
                ans.add(Point);
                mass.remove(Point);
            }
        }
        return ans;
    }

    /**
     * нахождение фигуры пересечения
     */
    public ArrayList<Vector2d> getIntersRect(Rectangle first, Rectangle second) {
        ArrayList<Vector2d> ans = new ArrayList<>();
        for (Line lineF : first.getlistlines()) {
            for (Line lineS : second.getlistlines()) {
                if (lineF.isIntersLines(lineS) && lineF.getIntersLines(lineS).isBelongSegment(lineF, lineS)) {
                    ans.add(lineF.getIntersLines(lineS));
                    PanelLog.info("Точка пересечения " + lineF.getIntersLines(lineS) + " обработана\n");
                    addPoint(lineF.getIntersLines(lineS));
                }
            }
        }
        for (Vector2d point : first.getlistpoints()) {
            if (point.isIntersRect(second)) {
                ans.add(point);
                PanelLog.info("Точка объединения " + point + " обработана\n");
                addPoint(point);
            }
        }
        for (Vector2d point : second.getlistpoints()) {
            if (point.isIntersRect(first)) {
                ans.add(point);
                PanelLog.info("Точка объединения " + point + " обработана\n");
                addPoint(point);
            }
        }
        PanelLog.info("индексировано пересечение " + ans);
        return sortForCF(ans);
    }

    /**
     * нахождение площади пересечения
     */
    public double getAreaIntersRect(Rectangle first, Rectangle second) {
        double s = 0;
        ArrayList<Vector2d> intersection = getIntersRect(first, second);
        if (intersection.size() != 0) {
            intersection.add(intersection.get(0));
            //Вычисление площади пересечения по методу Гаусса
            for (int i = 0; i < intersection.size() - 1; i++) {
                s += intersection.get(i).x * -intersection.get(i + 1).y - intersection.get(i + 1).x * -intersection.get(i).y;
            }
        }

        return Math.abs(s / 2);
    }
    /**
     * получить экстремальные значения
     *
     * @return минимум по X, минимум по Y, максимум по X, максимум по Y
     */
    public ArrayList<Double> getExtremum(ArrayList<Vector2d> rect) {

        double minX = rect.get(0).x, minY = rect.get(0).y, maxX = rect.get(0).x, maxY = rect.get(0).y;

        for (Vector2d point : rect) {
            if (point.x < minX) { minX = point.x; }
            if (point.y < minY) { minY = point.y; }
            if (point.x > maxX) { maxX = point.x; }
            if (point.y > maxY) { maxY = point.y; }
        }

        ArrayList<Double> res = new ArrayList<>(); res.add(minX); res.add(minY); res.add(maxX); res.add(maxY);
        return res;
    }

    /**
     * Очистить задачу
     */
    public void clear() {
        points.clear();
        lines.clear();
        rectangles.clear();
        hatchingLines.clear();
        COUNT_POINT_CREATING_RECT = 1;
        solved = false;
        ansRectF = Rectangle.nullRectangle();
        ansRectS = Rectangle.nullRectangle();
        PanelControl.solve.changeText(1);
    }

    /**
     * Решить задачу
     */
    public void solve() {

        cancel();

        double sMax = 0;
        // перебираем пары прямоугольников
        for (int i = 0; i < rectangles.size() - 1; i++) {
            for (int j = i + 1; j < rectangles.size(); j++) {
                double s = getAreaIntersRect(rectangles.get(i), rectangles.get(j));
                if (s > sMax) {
                    sMax = s;
                    ansRectF = rectangles.get(i);
                    ansRectS = rectangles.get(j);
                }
            }
        }
        ArrayList<Vector2d> ans = sortForCF(getIntersRect(ansRectF, ansRectS));
        if (ans.size() != 0) {
            ans.add(ans.get(0));
            // выделяем пару
            for (int i = 0; i < ans.size() - 1; i++) {
                addPointInter(ans.get(i));
                addLineInter(ans.get(i), ans.get(i + 1));
            }
            //штриховка
            ArrayList<Double> ext = getExtremum(ans);
            double minX = ext.get(0), minY = ext.get(1), maxX = ext.get(2), maxY = ext.get(3);
            for (double i = minY + minX; i <= maxY + maxX; i += 0.3) {
                double a = 1, b = 1, c = -i;
                Vector2d firstPoint=null; Vector2d secondPoint=null;
                for (int j = 0; j < ans.size() - 1; j++) {
                    Line l = new Line(ans.get(j), ans.get(j + 1));
                    if (l.isInterLines(a, b, c)) {
                        if (firstPoint == null) {
                            firstPoint = l.getInterLines(a, b, c);
                        }
                        else{
                            secondPoint = l.getInterLines(a, b, c);
                        }
                    }
                }
                if (firstPoint != null && secondPoint != null) {
                    addLineHatching(firstPoint, secondPoint);
                }

            }

        } else {
            PanelLog.info("Пересечений прямоугольников нет\n");
        }

        // задача решена
        solved = true;
    }

    /**
     * Отмена решения задачи
     */
    public void cancel() {
        for (Line l : lines) {
            l.RemoveFromAns();
            hatchingLines.clear();
        }
        String s = "Вызов отмены решения через функцию";
        PanelLog.success(s);
        PanelControl.solve.changeText(1);
        solved = false;
    }

    /**
     * Рисование сетки
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void renderGrid(Canvas canvas, CoordinateSystem2i windowCS) {
        // сохраняем область рисования
        canvas.save();
        // получаем ширину штриха(т.е. по факту толщину линии)
        float strokeWidth = 0.03f / (float) ownCS.getSimilarity(windowCS).y + 0.5f;
        // создаём перо соответствующей толщины
        try (var paint = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(strokeWidth).setColor(TASK_GRID_COLOR)) {
            // перебираем все целочисленные отсчёты нашей СК по оси X
            for (int i = (int) (ownCS.getMin().x); i <= (int) (ownCS.getMax().x); i++) {
                // находим положение этих штрихов на экране
                Vector2i windowPos = windowCS.getCoords(i, 0, ownCS);
                // каждый 10 штрих увеличенного размера
                float strokeHeight = i % DELIMITER_ORDER == 0 ? 5 : 2;
                // рисуем вертикальный штрих
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x, windowPos.y + strokeHeight, paint);
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x, windowPos.y - strokeHeight, paint);
            }
            // перебираем все целочисленные отсчёты нашей СК по оси Y
            for (int i = (int) (ownCS.getMin().y); i <= (int) (ownCS.getMax().y); i++) {
                // находим положение этих штрихов на экране
                Vector2i windowPos = windowCS.getCoords(0, i, ownCS);
                // каждый 10 штрих увеличенного размера
                float strokeHeight = i % 10 == 0 ? 5 : 2;
                // рисуем горизонтальный штрих
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x + strokeHeight, windowPos.y, paint);
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x - strokeHeight, windowPos.y, paint);
            }
        }
        // восстанавливаем область рисования
        canvas.restore();
    }

    /**
     * проверка, решена ли задача
     *
     * @return флаг
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Получить положение курсора мыши в СК задачи
     *
     * @param x        координата X курсора
     * @param y        координата Y курсора
     * @param windowCS СК окна
     * @return вещественный вектор положения в СК задачи
     */
    @JsonIgnore
    public Vector2d getRealPos(int x, int y, CoordinateSystem2i windowCS) {
        return ownCS.getCoords(x, y, windowCS);
    }

    /**
     * Рисование курсора мыши
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     * @param font     шрифт
     * @param pos      положение курсора мыши
     */
    public void paintMouse(Canvas canvas, CoordinateSystem2i windowCS, Font font, Vector2i pos) {
        // создаём перо
        try (var paint = new Paint().setColor(TASK_GRID_COLOR)) {
            // сохраняем область рисования
            canvas.save();
            // рисуем перекрестие
            canvas.drawRect(Rect.makeXYWH(0, pos.y - 1, windowCS.getSize().x, 2), paint);
            canvas.drawRect(Rect.makeXYWH(pos.x - 1, 0, 2, windowCS.getSize().y), paint);
            // смещаемся немного для красивого вывода текста
            canvas.translate(pos.x + 3, pos.y - 5);
            // положение курсора в пространстве задачи
            Vector2d realPos = getRealPos(pos.x, pos.y, lastWindowCS);
            // выводим координаты
            canvas.drawString(realPos.toString(), 0, 0, font, paint);
            // восстанавливаем область рисования
            canvas.restore();
        }
    }


}