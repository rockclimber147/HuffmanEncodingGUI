package org.example.huffmanencodinggui;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanTreeGenerator {
    private String sourceText;

    public HuffmanTreeGenerator(final String sourceText) {
        this.sourceText = sourceText;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public TreeNode getHuffmanTree(final PriorityQueue<Map.Entry<TreeNode, Integer>> priorityQueue) {
        TreeNode root = new TreeNode();
        if (priorityQueue.size() == 1) {
            root.setLeft(new TreeNode(priorityQueue.poll().getKey().getCharacter()));
            return root;
        } else if (priorityQueue.isEmpty()) {
            return root;
        }
        while (priorityQueue.size() > 2) {
            Map.Entry<TreeNode, Integer> left = priorityQueue.poll();
            Map.Entry<TreeNode, Integer> right = priorityQueue.poll();
            TreeNode temp = new TreeNode(left.getKey(), right.getKey());
            temp.generateCodeValue();
            HashMap<TreeNode, Integer> temp2 = new HashMap<>();
            temp2.put(temp, left.getValue() + right.getValue());
            priorityQueue.addAll(temp2.entrySet());
        }

        root.setLeft(priorityQueue.poll().getKey());
        root.setRight(priorityQueue.poll().getKey());
        root.generateCodeValue();

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
//        System.out.println("CHARACTER FREQUENCIES:\n" + charFrequencyMap);
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


    public static void main(String[] args) {
        HuffmanTreeGenerator gen = new HuffmanTreeGenerator("ANNA AND DANNY");
        HashMap<Character, Integer> map = gen.getCharFrequencyMap();
        System.out.println("CFM: " + map + "\n");

        PriorityQueue<Map.Entry<TreeNode, Integer>> queue = gen.getPriorityQueue(map);
        System.out.println(queue);
        TreeNode root = gen.getHuffmanTree(queue);
        root.generateCodeValue();
//        System.out.println(root.getXML());
        System.out.println(gen.getCodeTable(root));
    }
}
