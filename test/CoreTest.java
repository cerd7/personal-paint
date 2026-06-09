package test;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.List;

import src.com.cerd.app.core.command.PasteCommand;
import src.com.cerd.app.core.model.ClipboardBuffer;
import src.com.cerd.app.core.model.DrawModel;
import src.com.cerd.app.core.model.Stroke;
import src.com.cerd.app.core.selection.BoundsSelectionPolicy;
import src.com.cerd.app.core.selection.SelectionPolicy;

public class CoreTest {
    private interface TestAction {
        void run();
    }

    private static final class TestCase {
        private final String name;
        private final TestAction action;

        private TestCase(String name, TestAction action) {
            this.name = name;
            this.action = action;
        }
    }

    private static final class TestResult {
        private int passed;
        private int failed;

        private void pass() {
            passed++;
        }

        private void fail() {
            failed++;
        }

        private boolean hasFailures() {
            return failed > 0;
        }
    }

    public static void main(String[] args) {
        TestResult result = run(Arrays.asList(
            test("stroke bounds", CoreTest::strokeBounds),
            test("stroke translate updates points and bounds", CoreTest::strokeTranslate),
            test("stroke protects inserted point", CoreTest::strokeCopiesInputPoint),
            test("selection policy uses stroke bounds", CoreTest::selectionPolicy),
            test("clipboard returns deep clones", CoreTest::clipboardClones),
            test("paste command undo removes pasted strokes", CoreTest::pasteCommandUndo)
        ));

        System.out.println();
        System.out.println("Result: " + result.passed + " passed, " + result.failed + " failed.");

        if (result.hasFailures()) {
            System.exit(1);
        }
    }

    private static TestCase test(String name, TestAction action) {
        return new TestCase(name, action);
    }

    private static TestResult run(List<TestCase> tests) {
        TestResult result = new TestResult();

        for (TestCase test : tests) {
            try {
                test.action.run();
                result.pass();
                System.out.println("[PASS] " + test.name);
            } catch (Throwable error) {
                result.fail();
                System.out.println("[FAIL] " + test.name);
                System.out.println("       " + error.getMessage());
            }
        }

        return result;
    }

    private static void strokeBounds() {
        Stroke stroke = new Stroke();
        stroke.addPoint(new Point(10, 20));
        stroke.addPoint(new Point(30, 40));

        Rectangle bounds = stroke.getBounds();
        assertEquals(10, bounds.x, "bounds.x");
        assertEquals(20, bounds.y, "bounds.y");
        assertEquals(20, bounds.width, "bounds.width");
        assertEquals(20, bounds.height, "bounds.height");
    }

    private static void strokeTranslate() {
        Stroke stroke = new Stroke();
        stroke.addPoint(new Point(1, 1));
        stroke.addPoint(new Point(3, 4));

        stroke.translate(2, 3);

        Point first = stroke.getPoints().get(0);
        Rectangle bounds = stroke.getBounds();

        assertEquals(3, first.x, "translated first point x");
        assertEquals(4, first.y, "translated first point y");
        assertEquals(3, bounds.x, "translated bounds.x");
        assertEquals(4, bounds.y, "translated bounds.y");
        assertEquals(2, bounds.width, "translated bounds.width");
        assertEquals(3, bounds.height, "translated bounds.height");
    }

    private static void strokeCopiesInputPoint() {
        Point source = new Point(5, 5);
        Stroke stroke = new Stroke();
        stroke.addPoint(source);

        source.translate(10, 10);

        Point stored = stroke.getPoints().get(0);
        assertEquals(5, stored.x, "stored point x should not follow source mutation");
        assertEquals(5, stored.y, "stored point y should not follow source mutation");
    }

    private static void selectionPolicy() {
        Stroke inside = new Stroke();
        inside.addPoint(new Point(2, 2));
        inside.addPoint(new Point(8, 8));

        Stroke outside = new Stroke();
        outside.addPoint(new Point(20, 20));
        outside.addPoint(new Point(30, 30));

        SelectionPolicy policy = new BoundsSelectionPolicy();
        List<Stroke> selected = policy.select(Arrays.asList(inside, outside), new Rectangle(0, 0, 10, 10));

        assertEquals(1, selected.size(), "selected stroke count");
        assertSame(inside, selected.get(0), "selected stroke instance");
    }

    private static void clipboardClones() {
        Stroke stroke = new Stroke();
        stroke.addPoint(new Point(5, 5));

        ClipboardBuffer clipboard = new ClipboardBuffer();
        clipboard.set(Arrays.asList(stroke));

        List<Stroke> clones = clipboard.getClones();

        assertEquals(1, clones.size(), "clone count");
        assertNotSame(stroke, clones.get(0), "stroke clone instance");
        assertNotSame(stroke.getPoints().get(0), clones.get(0).getPoints().get(0), "point clone instance");
    }

    private static void pasteCommandUndo() {
        DrawModel model = new DrawModel();
        Stroke stroke = new Stroke();
        stroke.addPoint(new Point(1, 1));

        PasteCommand command = new PasteCommand(model, Arrays.asList(stroke));
        command.execute();
        assertEquals(1, model.getStrokes().size(), "stroke count after paste");

        command.undo();
        assertEquals(0, model.getStrokes().size(), "stroke count after undo");
    }

    private static void assertEquals(int expected, int actual, String label) {
        if (expected != actual) {
            throw new AssertionError(label + ": expected " + expected + ", got " + actual);
        }
    }

    private static void assertSame(Object expected, Object actual, String label) {
        if (expected != actual) {
            throw new AssertionError(label + ": expected same instance");
        }
    }

    private static void assertNotSame(Object unexpected, Object actual, String label) {
        if (unexpected == actual) {
            throw new AssertionError(label + ": expected different instances");
        }
    }
}
