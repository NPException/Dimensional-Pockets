package net.gtn.dimensionalpocket.oc.api.collect;

import java.util.*;

/**
 * Built around a linkedHashMap, this data structure exists as an non-intrusive, unweighted, undirected graph.
 *
 * non-intrusive: Without relying on the nodes to track their connections.
 *  Note: I don't create an object to represent a node, but LinkedHashMap does.
 *
 *
 *
 * @param <T>
 */
public class Graph<T> {

    protected LinkedHashMap<T, Collection<T>> nodeMap;
    protected int keySize = 0;

    public Graph() {
        nodeMap = new LinkedHashMap<>();
    }

    public boolean addNode(T node) {
        if (containsNode(node))
            return false;
        keySize++;
        nodeMap.put(node, new ArrayList<T>());
        return containsNode(node);
    }

    public boolean removeNode(T node) {
        if (!containsNode(node))
            return false;
        keySize--;
        Collection<T> nodes = nodeMap.remove(node);
        for (T adjacentNode : nodes)
            nodeMap.get(adjacentNode).remove(node);
        return !containsNode(node);
    }

    public boolean containsNode(T node) {
        return node != null && nodeMap.containsKey(node);
    }

    public boolean isAdjacent(T from, T to) {
        return containsNode(from) && containsNode(to) && nodeMap.get(from).contains(to);
    }

    /**
     * Add a connection/edge between two nodes.
     * The graph has to contain both.
     *
     * @return true if the edge was added.
     */
    public boolean addEdge(T from, T to) {
        if (from == to)
            return false;
        if (!(containsNode(from) && containsNode(to)))
            return false;

        Collection<T> fromCollection = nodeMap.get(from);
        if (!fromCollection.contains(to))
            fromCollection.add(to);

        Collection<T> toCollection = nodeMap.get(to);
        if (!toCollection.contains(from))
            toCollection.add(from);
        return true;
    }

    public void clear() {
        keySize = 0;
        nodeMap.clear();
    }

    /**
     * @return number of nodes
     */
    public int size() {
        return keySize;
    }

    /**
     * @return keySize == 0;
     */
    public boolean isEmpty() {
        return keySize == 0;
    }

    /**
     * @param node The node in question.
     * @return The number of adjacent nodes.
     */
    public int connectionSize(T node) {
        return !containsNode(node) ? 0 : nodeMap.get(node).size();
    }

    /**
     * Add the given map to this graph.
     */
    public void addAll(Map<? extends T, ? extends Collection<T>> map) {
        nodeMap.putAll(map);
        keySize = nodeMap.keySet().size();
    }

    /**
     * @param nodes Collection of nodes to remove.
     * @return true if the data structure was altered.
     */
    public boolean removeAll(Collection<? extends T> nodes) {
        return batchRemove(nodes, false);
    }

    /**
     * @param nodes Collection of nodes to retain.
     * @return true if the data structure was changed.
     */
    public boolean retainAll(Collection<? extends T> nodes) {
        return batchRemove(nodes, true);
    }

    protected boolean batchRemove(Collection<? extends T> nodes, boolean compliment) {
        Collection<T> keySet = nodeMap.keySet();
        Iterator<T> iterator = keySet.iterator();
        boolean modified = false;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (nodes.contains(t) != compliment) {
                iterator.remove();
                modified = true;
            }
        }
        keySize = keySet.size();
        return modified;
    }

    /**
     * @return all nodes currently in the network.
     */
    public Collection<T> getNodes() {
        return nodeMap.keySet();
    }

    /**
     * @return an iterable for the adjacent nodes.
     */
    public Collection<T> adjacentTo(T node) {
        return !containsNode(node) ? Collections.<T>emptyList() : nodeMap.get(node);
    }

    /**
     * Changes will NOT reflect, but the objects are references to the objects within this graph.
     *
     * @return A mutable shallow copy of the map.
     */
    public Map<T, Collection<T>> asMap() {
        return new LinkedHashMap<>(nodeMap);
    }

    @Override
    public String toString() {
        return nodeMap.toString();
    }
}
