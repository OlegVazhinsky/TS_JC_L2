package ru.geekbrains.lesson2;

import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final int FIELD_SIZE_X = 5; // Размерность игрового поля
    private static final int FIELD_SIZE_Y = 5; // Размерность игрового поля
    private static final int WIN_COUNT = 4; // Выигрышная комбинация
    private static final char DOT_HUMAN = 'X'; // Фишка игрока - человек
    private static final char DOT_AI = '0'; // Фишка игрока - компьютер
    private static final char DOT_EMPTY = '*'; // Признак пустого поля
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static char[][] field; // Двумерный массив хранит текущее состояние игрового поля
    private static int winFlag; // keeps sum of marked cells on the field

    public static void main(String[] args) {
        field = new char[3][];
        while (true){
            initialize();
            printField();
            while (true){
                humanTurn();
                printField();
                if (checkGameState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkGameState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.print("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }
    /**
     * Инициализация объектов игры
     */
    private static void initialize(){
        field = new char[FIELD_SIZE_X][FIELD_SIZE_Y];
        for (int x = 0; x < FIELD_SIZE_X; x++){
            for (int y = 0; y < FIELD_SIZE_Y; y++){
                field[x][y] = DOT_EMPTY;
            }
        }

    }
    /**
     * Отрисовка игрового поля
     *
     *     +-1-2-3-
     *     1|*|X|0|
     *     2|*|*|0|
     *     3|*|*|0|
     *     --------
     */
    private static void printField(){
        System.out.print("+");
        for (int x = 0; x < FIELD_SIZE_X * 2 + 1; x++){
            System.out.print((x % 2 == 0) ? "-" : x / 2 + 1);
        }
        System.out.println();
        for (int x = 0; x < FIELD_SIZE_X; x++){
            System.out.print(x + 1 + "|");
            for (int y = 0; y < FIELD_SIZE_Y; y++){
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }
        for (int x = 0; x < FIELD_SIZE_X * 2 + 2; x++){
            System.out.print("-");
        }
        System.out.println();
    }
    /**
     * Обработка хода игрока (человек)
     */
    private static void humanTurn(){
        int x, y;
        do {
            while (true){
                System.out.print("Введите координату хода X (от 1 до " + FIELD_SIZE_X +"): ");
                if (scanner.hasNextInt()){
                    x = scanner.nextInt() - 1;
                    scanner.nextLine();
                    break;
                }
                else {
                    System.out.println("Некорректное число, повторите попытку ввода.");
                    scanner.nextLine();
                }
            }

            while (true){
                System.out.print("Введите координату хода Y (от 1 до " + FIELD_SIZE_Y + "): ");
                if (scanner.hasNextInt()){
                    y = scanner.nextInt() - 1;
                    scanner.nextLine();
                    break;
                }
                else {
                    System.out.println("Некорректное число, повторите попытку ввода.");
                    scanner.nextLine();
                }
            }
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }
    /**
     * Проверка, ячейка является пустой (DOT_EMPTY)
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }
    /**
     * Проверка корректности ввода
     * (координаты хода не должны превышать размерность игрового поля)
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellValid(int x, int y){
        return x >= 0 && x < FIELD_SIZE_X && y >= 0 && y < FIELD_SIZE_Y;
    }
    /**
     * Обработка хода компьютера
     */
    private static void aiTurn(){
        for (int x = 0; x < FIELD_SIZE_X; x++) {
            for (int y = 0; y < FIELD_SIZE_Y; y++) {
                if (isCellEmpty(x, y)) {
                    // if miserable human is going to win next turn
                    field[x][y] = DOT_HUMAN;
                    if (checkWin(DOT_HUMAN) == true) {
                        field[x][y] = DOT_AI;
                        break;
                    }
                    // if AI is going to win next turn, set DOT_AI on X and Y position
                    field[x][y] = DOT_AI;
                    if (checkWin(DOT_AI) == true) {
                        break;
                    }
                    field[x][y] = DOT_EMPTY;
                }
            }
        }
        // otherwise set DOT_AI somewhere
        int x, y;
        do {
            x = random.nextInt(FIELD_SIZE_X);
            y = random.nextInt(FIELD_SIZE_Y);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_AI;
    }
    /**
     * Проверка состояния игры
     * @param c фишка игрока
     * @param s победный слоган
     * @return
     */
    private static boolean checkGameState(char c, String s){
        if (checkWin(c)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false; // Игра продолжается
    }
    /**
     * Проверка победы
     * @param mark
     * @return
     */
    private static boolean checkWin(char mark) {
        // go through all field
        for (int x =0; x < FIELD_SIZE_X; x++) {
            for (int y = 0; y < FIELD_SIZE_Y; y++) {
                // check horizontal case (to right)
                if (y <= (FIELD_SIZE_Y - WIN_COUNT)) {
                    winFlag = 0;
                    for (int i = y; i <= WIN_COUNT + y - 1; i++) {
                        if (field[x][i] == mark) winFlag++;
                    }
                    if (winFlag == WIN_COUNT) return true;
                }
                // check vertical case (down)
                if (x <= (FIELD_SIZE_X - WIN_COUNT)) {
                    winFlag = 0;
                    for (int i = x; i <= WIN_COUNT + x - 1; i++) {
                        if (field[i][y] == mark) winFlag++;
                    }
                    if (winFlag == WIN_COUNT) return true;
                }
                // check diagonal case (to right and down)
                if (x <= (FIELD_SIZE_X - WIN_COUNT) && y <= (FIELD_SIZE_Y - WIN_COUNT)) {
                    winFlag = 0;
                    int j = y;
                    for (int i = x; i <= WIN_COUNT + x - 1; i++) {
                        if (field[j][i] == mark) winFlag++;
                        j++;
                    }
                    if (winFlag == WIN_COUNT) return true;
                }
                // check diagonal case (to right and up)
                if (x >= (WIN_COUNT - 1) && y <= (FIELD_SIZE_Y - WIN_COUNT)) {
                    winFlag = 0;
                    int i = x;
                    for (int j = y; j <= WIN_COUNT + y - 1; j++) {
                        if (field[i][j] == mark) winFlag++;
                        i--;
                    }
                    if (winFlag == WIN_COUNT) return true;
                }
            }
        }
        return false;
    }
    /**
     * Проверка на ничью
     * @return
     */
    private static boolean checkDraw(){
        for (int x = 0; x < FIELD_SIZE_X; x++){
            for (int y = 0; y < FIELD_SIZE_Y; y++){
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }
}