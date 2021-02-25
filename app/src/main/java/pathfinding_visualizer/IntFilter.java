package pathfinding_visualizer;

import javax.swing.text.*;

/**
 * DocumentFilter that accepts integers between {@code min} and {@code max}, inclusive.
 */
public class IntFilter extends DocumentFilter {
    /**
     * Minimum accepted integer value
     */
    private int min;
    /**
     * Maximum accepted integer value
     */
    private int max;

    /**
     * Creates a filter that only accepts integers between {@code min} and {@code max}, inclusive.
     * @param min minimum accepted integer value
     * @param max maximum accepted integer value
     */
    IntFilter(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Handles the insertion of text. Checks if insertion results in a valid string before allowing it.
     * 
     * @param fb FilterBypass that can be used to modify the Document
     * @param offset offset from the beginning, cannot be negative
     * @param string String you want to insert
     * @param attr AttributeSet indicating attributes of {@code string}, or null 
     * 
     * @throws BadLocationException - {@code offset} is not a valid postion in the Document
     */
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (valid(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    /**
     * Handles the replacement of text. Checks if replacement results in a valid string before allowing it.
     * 
     * @param fb FilterBypass that can be used to modify the Document
     * @param offset offset from the beginning, cannot be negative
     * @param length length of text in the Document to delete
     * @param text String to insert, or null
     * @param attrs AttributeSet of {@code text}, or null
     * 
     * @throws BadLocationException {@code offset} is not a valid location in the Document
     */
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if (valid(sb.toString())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    /**
     * Handles the removal of text. Checks if removal results in a valid string before allowing it.
     * 
     * @param fb FilterBypass that can be used to modify the Document
     * @param offset offset from the beginning, cannot be negative
     * @param length number of characters to remove, cannot be negative
     * 
     * @throws BadLocationException part of the removal range is not a valid part of the Document
     */
    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if (valid(sb.toString())) {
            super.remove(fb, offset, length);
        }
    }

    /**
     * Checks if the String {@code s} is an integer that falls between {@code min} and {@code max} or if {@code s} is empty.
     * @param s String to be checked
     * @return {@code true} if {@code s} satisfies the constraints in the description, false otherwise.
     */
    private boolean valid(String s) {
        if (s.isEmpty()) {
            return true;
        }

        try {
            int x = Integer.parseInt(s);
            return x >= min && x <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}