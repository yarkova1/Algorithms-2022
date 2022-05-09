package lesson7;

import kotlin.NotImplementedError;
import kotlin.jvm.internal.Intrinsics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     *
     * Трудоемкость: O(first_length * second_length)
     * Ресурсоемкость: O(first_length * second_length)
     *
     */
    public static String longestCommonSubSequence(String first, String second) {
        int fl = 0;
        int sl = 0;
        int first_length = first.length();
        int second_length = second.length();
        int[][] arr = new int[first_length + 1][second_length + 1];
        StringBuilder builder = new StringBuilder();

        for (int i = first_length; i >= 0 ; i--) {
            for (int j = second_length; j >= 0; j--) {
                if (i == first_length || j == second_length)
                    arr[i][j] = 0;
                else if (first.charAt(i) == second.charAt(j))
                    arr[i][j] = arr[i + 1][j + 1] + 1;
                else {
                    int a = Math.max(arr[i][j + 1], arr[i + 1][j]);
                    arr[i][j] = a;
                }
            }
        }
        while (fl < first_length && sl < second_length) {
            if (first.charAt(fl) == second.charAt(sl)) {
                builder.append(first.charAt(fl));
                fl++;
                sl++;
            } else if (arr[fl + 1][sl] >= arr[fl][sl + 1])
                fl++;
            else
                sl++;
        }
        return builder.toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     *
     * Трудоемкость: O(N * log N)
     * Ресурсоемкость: O(N)
     * где N = list.size()
     *
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        int size = list.size();
        int[] prev = new int[size];
        int[] pos = new int[size + 1];
        int length = 0;
        int l;

        for (int i = size - 1; i >= 0; --i) {
            l = 1;
            int r = length;
            while(l <= r) {
                int m = (l + r) / 2;
                if (list.get(pos[m]) < list.get(i)) {
                    r = m - 1;
                } else
                    l = m + 1;
            }
            prev[i] = pos[l - 1];
            pos[l] = i;
            if (l > length) {
                length = l;
            }
        }

        ArrayList<Integer> answer = new ArrayList<>();
        int p = pos[length];

        for (int l1 = length - 1; l1 >= 0; --l1) {
            answer.add(list.get(p));
            p = prev[p];
        }

        return answer;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }
}
