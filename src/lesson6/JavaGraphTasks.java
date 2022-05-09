package lesson6;

import kotlin.NotImplementedError;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     *
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     *
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     *
     * Трудоемкость: O(vertices + edges)
     * Ресурсоемкость: O(edges)
     *
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        //проврерка на эйлеровость
        for (Graph.Vertex vertex : graph.getVertices()) {
            Set<Graph.Vertex> neighbours = graph.getNeighbors(vertex);
            if (neighbours.size() == 0 || neighbours.size() % 2 != 0)
                return Collections.emptyList();
        }
        // задаем первую вершину
        Optional<Graph.Vertex> start = graph.getVertices().stream().findFirst();
        if (start.isEmpty())
            return Collections.emptyList();

        Stack<Graph.Edge> loop = new Stack<>();
        Set<Graph.Edge> traversedEdges = new HashSet<>();
        // рекурсивная функция, которая ищет путь
        eulerLoopDFS(start.get(), null, traversedEdges, loop, graph);
        return loop;
    }

    private static Set<Graph.Vertex> neighboursForUntraveledEdges(Graph.Vertex vertex, Set<Graph.Edge> traversedEdges, Graph graph) {
        Set<Graph.Vertex> neighbours = new HashSet<>();
        for (Graph.Vertex neighbour : graph.getNeighbors(vertex)) {
            Graph.Edge edgeToNeighbour = graph.getConnection(vertex, neighbour);
            if (!traversedEdges.contains(edgeToNeighbour))
                neighbours.add(neighbour);
        }
        return neighbours;
    }

    private static void eulerLoopDFS(Graph.Vertex vertex, Graph.Vertex wentFrom, Set<Graph.Edge> traversedEdges, Stack<Graph.Edge> loop, Graph graph) {
        if (wentFrom != null)
            traversedEdges.add(graph.getConnection(wentFrom, vertex));
        Set<Graph.Vertex> neighbours;
        while (!(neighbours = neighboursForUntraveledEdges(vertex, traversedEdges, graph)).isEmpty()) {
            Graph.Vertex neighbour = neighbours.stream().findFirst().get();
            eulerLoopDFS(neighbour, vertex, traversedEdges, loop, graph);
        }
        if (wentFrom != null)
            loop.push(graph.getConnection(wentFrom, vertex));
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     *
     * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ:
     *
     *      G    H
     *      |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     *
     * Дан граф без циклов (получатель), например
     *
     *      G -- H -- J
     *      |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     *
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     *
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     *
     * В данном случае ответ (A, E, F, D, G, J)
     *
     * Если на входе граф с циклами, бросить IllegalArgumentException
     */
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Наидлиннейший простой путь.
     * Сложная
     *
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     *
     * Пример:
     *
     *      G -- H
     *      |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     *
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     *
     * Трудоемкость: O(!vertices)
     * Ресурсоемкость: O(!vertices)
     *
     */
    public static Path longestSimplePath(Graph graph) {
        int max = -1;
        Path path = new Path();
        Deque<Path> deque = new ArrayDeque<>(); //все списки из одной вершины
        for (Graph.Vertex vertex : graph.getVertices())
            deque.add(new Path(vertex));
        while (!deque.isEmpty()) {
            Path current = deque.pollFirst();
            if (current.toString().length() > max) {
                max = current.getLength();
                path = current;
                if (max == graph.getVertices().size())
                    break;
            }
            Iterator<Graph.Vertex> iter = current.getVertices().iterator();
            Graph.Vertex last = iter.next();
            while (iter.hasNext())
                last = iter.next();
            for (Graph.Vertex neighbour : graph.getNeighbors(last)) {
                if (!current.contains(neighbour))
                    deque.add(new Path(current, graph, neighbour));
            }
        }
        return path;
    }


    /**
     * Балда
     * Сложная
     *
     * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
     * поэтому задача присутствует в этом разделе
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        throw new NotImplementedError();
    }
}
