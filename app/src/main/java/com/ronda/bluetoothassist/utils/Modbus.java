package com.ronda.bluetoothassist.utils;

/*
 *
 *类描述：
 *创建人：R
 *创建时间：${DATA}16:58
 *
 */public class Modbus {
    public static final int MODBUS_FIRST = 1;
    public static final int MODBUS_SECCOND = 2;
    public static final int MODBUS_THIRD = 3;
    private static boolean classerror = false;


    /**
     * 作用：封装消息体
     * 参数：start数据的起始地址  num读取的寄存器数  返回消息体字节数组
     * 重要笔记：
     */
    public static final byte[] modbus_body(int dataclass, int start, int num) {

            int n = 12;//消息体字节数
            byte[] command = new byte[n];
            byte i = 0;
        try {
            //事务标志 2 {0x00,0x19,0x00,0x00,0x00,0x06,0x01,0x04,0x00,0x4E,0x00,0x04}
            if (dataclass == MODBUS_FIRST) {
                command[0] = 0x00;
                command[1] = 0x19;
            } else if (dataclass == MODBUS_SECCOND) {

                command[0] = 0x00;
                command[1] = 0x20;
            }
         else if (dataclass == MODBUS_THIRD) {

            command[0] = 0x00;
            command[1] = 0x20;
        }
            else {

                Modbus.classerror = true;
                throw new Exception("类型错误");
            }
            //协议标识 2字节  0x0000代表TCPIP协议
            command[2] = 0x00;
            command[3] = 0x00;
            //长度  2字节 代表这两个字节后面的字节数
            command[4] = 0x00;
            command[5] = 0x06;
            //单元标志 1
            command[6] = 0x01;
            //功能码 1  0x04读取数据
            command[7] = 0x04;
            //功能数据 4  前两个字节代表读取寄存器开始的地址    后两个字节代表读取的寄存器数
            command[8] = (byte) ((start & 0x0000ff00) >> 8);
            command[9] = (byte) ((start & 0x000000ff) >> 0);
            command[10] = (byte) ((num & 0x0000ff00) >> 8);
            command[11] = (byte) ((num & 0x000000ff) >> 0);

            return command;

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return command;
        }


    }
}