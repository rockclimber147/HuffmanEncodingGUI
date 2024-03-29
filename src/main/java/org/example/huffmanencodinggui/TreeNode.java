package org.example.huffmanencodinggui;

public class TreeNode {
    private static int xmlIndentCountIncrement = 2;
    private TreeNode left;
    private TreeNode right;
    private char character;
    private String codeValue;

    public TreeNode(final char character) {
        this.character = character;
    }

    public TreeNode(final TreeNode left, final TreeNode right) {
        this.left = left;
        this.right = right;
        this.character = Character.MAX_VALUE;
    }

    public TreeNode() {
        this.character = Character.MAX_VALUE;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public static void setXmlIndentCountIncrement(int newIncrement) {
        if (newIncrement > 0) {
            xmlIndentCountIncrement = newIncrement;
        }
    }

    public boolean isLeaf() {
        return this.left == null && this.right == null;
    }

    public void generateCodeValue() {
        generateCodeValue("");
    }

    private void generateCodeValue(final String parentCodeValue) {
        this.codeValue = parentCodeValue;
        if (this.left != null) {
            left.generateCodeValue(this.codeValue + "0");
        }
        if (this.right != null) {
            right.generateCodeValue(this.codeValue + "1");
        }
    }

    public String getXML(int indentCount) {
        return " ".repeat(indentCount) + "<Node>\n"
                + " ".repeat(indentCount + xmlIndentCountIncrement) + "<value> " + this.character + " </value>\n"
                + " ".repeat(indentCount + xmlIndentCountIncrement) + "<codeValue> " + this.codeValue + " </codeValue>\n"
                + " ".repeat(indentCount + xmlIndentCountIncrement) + "<LeftChild>\n"
                + ((this.left == null)
                ? " ".repeat(indentCount + xmlIndentCountIncrement) + "null\n"
                : this.left.getXML(indentCount + 2 * xmlIndentCountIncrement))
                + " ".repeat(indentCount + xmlIndentCountIncrement) + "</LeftChild>\n"
                + " ".repeat(indentCount + xmlIndentCountIncrement) + "<RightChild>\n"
                + ((this.right == null)
                ? " ".repeat(indentCount + xmlIndentCountIncrement) + "null\n"
                : this.right.getXML(indentCount + 2 * xmlIndentCountIncrement))
                + " ".repeat(indentCount + xmlIndentCountIncrement) + "</RightChild>\n"
                + " ".repeat(indentCount) + "<Node>\n";
    }



    public String getXML() {
        return this.getXML(0);
    }
    public String toString() {
        return getAbridgedString(0);
    }

//    public String toString() {
//        return "Node {"
//                + "\ncharacter=" + this.character
//                + "\ncodeValue=" + this.codeValue
//                + "\nleft=" + this.left
//                + "\nright=" + this.right
//                + "\n}";
//    }

    public String getAbridgedString(int indentCount) {
        return
                ((this.left == null) ? "\n" : ("\n" + this.left.getAbridgedString(indentCount + 2 * xmlIndentCountIncrement)))
                + " ".repeat(indentCount + xmlIndentCountIncrement) + "|" + this.character + "|" + this.codeValue
                        + ((this.right == null) ? "\n" : (this.right.getAbridgedString(indentCount + 2 * xmlIndentCountIncrement)));
    }

    public static void main(String[] args) {
        TreeNode A = new TreeNode('A');
        TreeNode B = new TreeNode('B');
        TreeNode C = new TreeNode('C');
        TreeNode D = new TreeNode('D');
        TreeNode E = new TreeNode('E');
        TreeNode F = new TreeNode('F');

        TreeNode AB = new TreeNode(A,B);
        TreeNode DE = new TreeNode(D,E);
        TreeNode AB_C = new TreeNode(AB, C);
        TreeNode F_ = new TreeNode(F, null);
        TreeNode DE_F_ = new TreeNode(DE, F_);
        TreeNode root = new TreeNode(AB_C, DE_F_);
        System.out.println(root.getXML());
        root.generateCodeValue("");
        System.out.println(root.getXML());
    }
}
