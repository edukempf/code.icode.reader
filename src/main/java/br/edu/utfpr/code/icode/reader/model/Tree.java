package br.edu.utfpr.code.icode.reader.model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Tree<T> {

	private final Map<T, T> nodeParent = new HashMap<T, T>();
    private final LinkedHashSet<T> nodeList = new LinkedHashSet<T>();

    public boolean add (T parent, T node) {

        boolean added = nodeList.add(node);
        nodeList.add(parent);
        if (added) {
            nodeParent.put(node, parent);
        }
        return added;
    }

    public boolean remove (T node, boolean cascade) {
        if (!nodeList.contains(node)) {
            return false;
        }
        if (cascade) {
            for (T child : getChildren(node)) {
                remove(child, true);
            }
        } else {
            for (T child : getChildren(node)) {
                nodeParent.remove(child);
            }
        }
        nodeList.remove(node);
        return true;
    }

    public List<T> getRoots () {
        return getChildren(null);
    }

    public T getParent (T node) {
        return nodeParent.get(node);
    }

    public List<T> getChildren (T node) {
        List<T> children = new LinkedList<T>();
        for (T n : nodeList) {
            T parent = nodeParent.get(n);
            if (node == null && parent == null) {
                children.add(n);
            } else if (node != null && parent != null && parent.equals(node)) {
                children.add(n);
            }
        }
        return children;
    }

    public String toString () {
        StringBuilder builder = new StringBuilder();
        dumpNodeStructure(builder, null, "- ");
        return builder.toString();
    }

    private void dumpNodeStructure (StringBuilder builder, T node, String prefix) {
        if (node != null) {
            builder.append(prefix);
            builder.append(node.toString());
            builder.append('\n');
            prefix = "    " + prefix;
        }
        for (T child : getChildren(node)) {
            dumpNodeStructure(builder, child, prefix);
        }
    }
}
