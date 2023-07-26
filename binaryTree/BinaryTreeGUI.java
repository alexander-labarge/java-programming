import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;


public class BinaryTreeGUI extends JFrame implements ActionListener {
    private JTextField inputField;
    private JTextArea outputArea;

    public BinaryTreeGUI() {
        super("Binary Tree GUI");

        // Create input panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel inputLabel = new JLabel("Enter binary tree:");
        inputField = new JTextField(20);
        JButton makeTreeButton = new JButton("Make Tree");
        makeTreeButton.addActionListener(this);
        inputPanel.add(inputLabel);
        inputPanel.add(inputField);
        inputPanel.add(makeTreeButton);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3));
        JButton isBalancedButton = new JButton("Is Balanced");
        isBalancedButton.addActionListener(this);
        JButton isFullButton = new JButton("Is Full");
        isFullButton.addActionListener(this);
        JButton isProperButton = new JButton("Is Proper");
        isProperButton.addActionListener(this);
        JButton getHeightButton = new JButton("Get Height");
        getHeightButton.addActionListener(this);
        JButton countNodesButton = new JButton("Count Nodes");
        countNodesButton.addActionListener(this);
        JButton inorderTraversalButton = new JButton("Inorder Traversal");
        inorderTraversalButton.addActionListener(this);
        JButton layerTreeButton = new JButton("Layer Tree");
        layerTreeButton.addActionListener(this);
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        buttonPanel.add(isBalancedButton);
        buttonPanel.add(isFullButton);
        buttonPanel.add(isProperButton);
        buttonPanel.add(getHeightButton);
        buttonPanel.add(countNodesButton);
        buttonPanel.add(inorderTraversalButton);
        buttonPanel.add(layerTreeButton);
        buttonPanel.add(clearButton);

        // Create output panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputArea = new JTextArea(10, 20);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        // Add panels to frame
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(buttonPanel, BorderLayout.CENTER);
        contentPane.add(outputPanel, BorderLayout.SOUTH);

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String input = inputField.getText();
        BinaryTree tree;

        try {
            tree = new BinaryTree(input);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid tree syntax: " + ex.getMessage());
            return;
        }

        String output;

        switch (e.getActionCommand()) {
            case "Make Tree":
                output = "Binary tree constructed.";
                break;
            case "Is Balanced":
                output = "Is the tree balanced? " + tree.isBalanced();
                break;
            case "Is Full":
                output = "Is the tree full? " + tree.isFull();
                break;
            case "Is Proper":
                output = "Is the tree proper? " + tree.isProper();
                break;
            case "Get Height":
                output = "Height of the tree: " + tree.getHeight();
                break;
            case "Count Nodes":
                output = "Number of nodes in the tree: " + tree.countNodes();
                break;
            case "Inorder Traversal":
                output = "Inorder traversal: " + tree.getInorderTraversal();
                break;
            case "Layer Tree":
                output = "Layer tree:\n" + tree.getLayerTree();
                break;
            case "Clear":
                inputField.setText("");
                outputArea.setText("");
                return;
            default:
                output = "";
                break;
        }

        outputArea.setText(output);
    }

    public static void main(String[] args) {
        new BinaryTreeGUI();
    }
   
}

class BinaryTree {
    static class Node {
        String value;
        Node left;
        Node right;

        Node(String value) {
            this.value = value;
        }
    }

    private Node root;

    BinaryTree(String input) throws InvalidTreeSyntax {
        root = parseTree(input);
    }

    private Node parseTree(String input) throws InvalidTreeSyntax {
        if (input.length() == 0) {
            throw new InvalidTreeSyntax("Empty input string.");
        }

        if (input.charAt(0) != '(') {
            throw new InvalidTreeSyntax("Input string must start with '('.");
        }

        if (input.charAt(input.length() - 1) != ')') {
            throw new InvalidTreeSyntax("Input string must end with ')'.");
        }

        int i = 1;

        while (i < input.length() && input.charAt(i) != '(' && input.charAt(i) != ')') {
            i++;
        }

        if (i == input.length()) {
            throw new InvalidTreeSyntax("Input string must contain at least one node.");
        }

        Node node = new Node(input.substring(1, i));

        if (i < input.length() - 1) {
            int j = i + 1;
            int count = 1;

            while (j < input.length() - 1 && count > 0) {
                if (input.charAt(j) == '(') {
                    count++;
                } else if (input.charAt(j) == ')') {
                    count--;
                }

                j++;
            }

            if (count != 0) {
                throw new InvalidTreeSyntax("Mismatched parentheses.");
            }

            node.left = parseTree(input.substring(i, j));
            node.right = parseTree(input.substring(j, input.length() - 1));
        }

        return node;
    }

    public boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(Node node) {
        if (node == null) {
            return true;
        }

        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);

        return Math.abs(leftHeight - rightHeight) <= 1 && isBalanced(node.left) && isBalanced(node.right);
    }

    public boolean isFull() {
        return isFull(root);
    }

    private boolean isFull(Node node) {
        if (node == null) {
            return true;
        }

        if (node.left == null && node.right == null) {
            return true;
        }

        if (node.left != null && node.right != null) {
            return isFull(node.left) && isFull(node.right);
        }

        return false;
    }

    public boolean isProper() {
        return isProper(root);
    }

    private boolean isProper(Node node) {
        if (node == null) {
            return true;
        }

        if (node.left == null && node.right == null) {
            return true;
        }

        if (node.left != null && node.right != null) {
            return isProper(node.left) && isProper(node.right);
        }

        return false;
    }

    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(Node node) {
        if (node == null) {
            return 0;
        }

        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);

        return 1 + Math.max(leftHeight, rightHeight);
    }

    public int countNodes() {
        return countNodes(root);
    }

    private int countNodes(Node node) {
        if (node == null) {
            return 0;
        }

        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    public String getInorderTraversal() {
        if (root == null) {
            return "Tree is empty";
        }

        StringBuilder sb = new StringBuilder();
        getInorderTraversal(root, sb);
        return sb.toString();
    }

    private void getInorderTraversal(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }

        if (node.left != null) {
            sb.append("(");
            getInorderTraversal(node.left, sb);
            sb.append(")");
        }

        sb.append(" ");
        sb.append(node.value);
        sb.append(" ");

        if (node.right != null) {
            sb.append("(");
            getInorderTraversal(node.right, sb);
            sb.append(")");
        }
    }


    public String getLayerTree() {
        if (root == null) {
            return "Tree is empty";
        }

        StringBuilder sb = new StringBuilder();
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                Node node = queue.poll();
                sb.append(node.value);
                sb.append(" ");
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public class InvalidTreeSyntax extends Exception {
        public InvalidTreeSyntax(String message) {
            super(message);
        }
    }
}
