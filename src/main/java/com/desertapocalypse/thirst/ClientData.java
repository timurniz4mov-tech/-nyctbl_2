package com.desertapocalypse.thirst;

/** Данные, присланные сервером клиенту (только статические поля, без клиентских классов). */
public final class ClientData {
    public static volatile float thirst = -1.0F; // < 0 = не показывать
    public static volatile int day = 0;          // 0 = апокалипсис не активен / не в Верхнем мире
    public static volatile float edge = 0.0F;    // расстояние до границы пустыни (<= 0 внутри)
    public static volatile boolean storm = false; // идёт буря и игрок в зоне пустыни

    private ClientData() {}
}
