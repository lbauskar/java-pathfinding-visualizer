package pathfinding_visualizer;

import static org.junit.Assert.*;

import javax.swing.JTextField;
import javax.swing.text.*;

import org.junit.Test;

/**
 * Tests the {@link IntFilter} class.
 */
public class IntFilterTest {

    private JTextField tf;
    private PlainDocument doc;
    private IntFilter filter = new IntFilter(-11, 10);

    /**
     * Sets up the {@code JTextField}, {@code PlainDocument}, and 
     * {@code IntFilter} to be used by the tests.
     */
    public IntFilterTest() {
        tf = new JTextField();
        doc = (PlainDocument) tf.getDocument();
        doc.setDocumentFilter(filter);
    }

    /**
     * Tests that the constructor creates a non-null IntFilter object.
     */
    @Test
    public void makeIntFilter() {
        assertNotNull(new IntFilter(0, 10));
    }

    /**
     * Tests the {@code replace} function of {@code IntFilter}.
     * <p>
     * The following tests are run.
     * <ol>
     * <li> Replace a character in the Document String @{code s} such that {@code min < Integer(s) < max}. Should work.
     * <li> Replace a character in {@code s} such that {@code Integer(s) > max}. Should not work.
     * <li> Replace a character in {@code s} with a letter. Should not work.
     * <li> Replace text outside of the Document. Should throw a (caught) exception.
     * </ol>
     * 
     * @throws BadLocationException one of the first 3 tests placed a String outside the Document
     */
    @Test
    public void replaceTest() throws BadLocationException {
        //replace 5 with a 9 (should work)
        tf.setText("05");
        doc.replace(1, 1, "9", null);
        assertEquals("09", tf.getText());

        //replace 0 with 1 (should not work)
        tf.setText("05");
        doc.replace(0, 1, "1", null);
        assertEquals("05", tf.getText());

        //replace 5 with an 'a' (should not work)
        tf.setText("5");
        doc.replace(0, 1, "a", null);
        assertEquals("5", tf.getText());

        //try text replacement that's longer than document
        tf.setText("10");
        boolean exceptionThrown = false;
        try {
            doc.replace(0, 5, "2", null);
        } catch (BadLocationException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Test the {@code remove} method of {@code IntFilter}.
     * <p>
     * The following tests are run:
     * <ol>
     * <li> Remove a character at the end of the Document String {@code s} such that {@code min < Integer(s) < max}. Should work.
     * <li> Remove all characters from {@code s} resulting in an empty Document String. Should work.
     * <li> Remove negative sign at beginning of {@code s}, such that {@code Integer(s) > max}. Should not work.
     * <li> Remove text outside of the Document. Should throw a (caught) exception.
     * 
     * @throws BadLocationException one of the first 3 tests tried ot remove text outside of the Document
     */
    @Test
    public void removeTest() throws BadLocationException {
        //remove 0 at end (should work)
        tf.setText("10");
        doc.remove(1, 1);
        assertEquals("1", tf.getText());

        //remove all text (should work)
        tf.setText("10");
        doc.remove(0, 2);
        assertEquals("", tf.getText());

        //remove negative sign at beginning (should not work)
        tf.setText("-11");
        doc.remove(0, 1);
        assertEquals("-11", tf.getText());

        //try to remove more characters than are in the document
        tf.setText("10");
        boolean exceptionThrown = false;
        try {
            doc.remove(0, 100);
        } catch (BadLocationException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Test the {@code insert} method of {@code IntFilter}.
     * <p>
     * The following tests are run:
     * <ol>
     * <li> Insert a character at the beginning of the Document String {@code s}, such that {@code min < Integer(s) < max}. Should work.
     * <li> Insert a character into the middle of {@code s}, such that {@code Integer(s) > max}. Should not work.
     * <li> Insert a letter into {@code s}. Should not work.
     * <li> Insert text outside of the Document. Should throw a (caught) exception.
     * 
     * @throws BadLocationException one of the first 3 tests tried to insert text outside of the Document
     */
    @Test
    public void insertTest() throws BadLocationException {

        //insert 1 at beginning (should work)
        tf.setText("0");
        doc.insertString(0, "1", null);
        assertEquals("10", tf.getText());

        //try to add a zero in the middle (should not work)
        tf.setText("10");
        doc.insertString(1, "0", null);
        assertEquals("10", tf.getText());

        //add 'a' to end (should not work)
        tf.setText("0");
        doc.insertString(1, "a", null);
        assertEquals("0", tf.getText());

        //try to insert text outside the document (should not work)
        tf.setText("1");
        boolean exceptionThrown = false;
        try {
            doc.insertString(12, "2", null);
        } catch (BadLocationException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    /**
     * Tests for the edge case where {@code min > max}. In such a case, any attempt
     * at inserting a non-empty string should fail.
     * 
     * @throws BadLocationException the test tried to insert text outside of the Document
     */
    @Test
    public void minHigherThanMax() throws BadLocationException {
        doc.setDocumentFilter(new IntFilter(2, -2));
        tf.setText("");
        doc.insertString(0, "1", null);
        assertEquals("", tf.getText());
    }

}