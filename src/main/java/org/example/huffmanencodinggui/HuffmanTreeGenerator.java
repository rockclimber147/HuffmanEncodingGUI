package org.example.huffmanencodinggui;

import java.util.*;

public class HuffmanTreeGenerator {
    private String sourceText;

    public HuffmanTreeGenerator(final String sourceText) {
        this.sourceText = sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getSourceText() {
        return sourceText;
    }

    public TreeNode getHuffmanTree(final PriorityQueue<Map.Entry<TreeNode, Integer>> priorityQueue) {
        TreeNode root = new TreeNode();
        if (priorityQueue.size() == 1) {
            root.setLeft(new TreeNode(priorityQueue.poll().getKey().getCharacter()));
        } else if (priorityQueue.size() >= 2){

            while (priorityQueue.size() > 2) {
                Map.Entry<TreeNode, Integer> left = priorityQueue.poll();
                Map.Entry<TreeNode, Integer> right = priorityQueue.poll();
                TreeNode temp = new TreeNode(left.getKey(), Objects.requireNonNull(right).getKey());

                Map.Entry<TreeNode,Integer> entry =
                        new AbstractMap.SimpleEntry<>(temp, left.getValue() + right.getValue());
                priorityQueue.add(entry);
            }

            root.setLeft(Objects.requireNonNull(priorityQueue.poll()).getKey());
            root.setRight(Objects.requireNonNull(priorityQueue.poll()).getKey());
            root.generateCodeValue();
        }
        return root;
    }

    public TreeNode getHuffmanTree() {
        return getHuffmanTree(getPriorityQueue(getCharFrequencyMap()));
    }

    public HashMap<Character, Integer> getCharFrequencyMap() {
        HashMap<Character, Integer> charFrequencyMap = new HashMap<>();
        for (int i = 0; i < this.sourceText.length(); i++) {
            charFrequencyMap.merge(this.sourceText.charAt(i), 1, Integer::sum);
        }
        return charFrequencyMap;
    }

    public PriorityQueue<Map.Entry<TreeNode, Integer>> getPriorityQueue(HashMap<Character, Integer> charFrequencyMap) {

        PriorityQueue<Map.Entry<TreeNode, Integer>> queue = new PriorityQueue<>((first , second)
                -> first.getValue().equals(second.getValue())
                ? Integer.compare(first.getKey().getCharacter(),
                second.getKey().getCharacter())
                : Integer.compare(first.getValue(),
                second.getValue()));

        HashMap<TreeNode, Integer> nodeFrequencyMap = new HashMap<>();
        for (var entry: charFrequencyMap.entrySet()) {
            nodeFrequencyMap.put(new TreeNode(entry.getKey()), entry.getValue());
        }
        queue.addAll(nodeFrequencyMap.entrySet());
        return queue;
    }

    public HashMap<Character, String> getCodeTable(final TreeNode root) {
        HashMap<Character, String> translationTable = new HashMap<>();
        populateTable(translationTable, root);
        return translationTable;
    }

    private void populateTable(final HashMap<Character, String> table, TreeNode current) {
        TreeNode left = current.getLeft();
        TreeNode right = current.getRight();
        if (left == null && right == null) {
            table.put(current.getCharacter(), current.getCodeValue());
        }
        if (left != null) {
            populateTable(table, left);
        }
        if (right != null) {
            populateTable(table, right);
        }
    }
}
