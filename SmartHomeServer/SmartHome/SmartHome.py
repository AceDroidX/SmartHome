#!/usr/bin/python3
# -*- coding: UTF-8 -*-
# noinspection PyUnresolvedReferences
import sys
import threading

import os

import Door
import TCPSocket

isdebug = True
pinList = {'bigLight': 3, 'fanB': 5, 'door': 6, 'whiteLight': 7,
           'RGBLightR': 9, 'RGBLightB': 10, 'RGBLightG': 11}

cmd = ''


# functions
def stop():
    print("退出程序中")
    os._exit(0)


def about():
    print("----------------------------")
    print('https://wangxuxin.github.io')
    print("By wxx")
    print("----------------------------")


def pinSet():
    print("设置引脚号")


def doortest1():
    Door.door_test1(input('pin'), input('level'), input('freq'))


def updateTime():
    os.system('sudo ntpdate -u ntp.api.bz')

# -----------------------------

if __name__ == '__main__':
    # setup
    if isdebug:
        print("命令行参数:%s" % sys.argv)
    TCPSocket.startServer()
    about()
    timeThread = threading.Thread(target=updateTime())
    timeThread.start()
    print("SmartHome已启动\n控制台帮助请输入help")
    # ----------------------------

    # loop
    while True:
        cmd = input("wxx>")
        if cmd == "help":
            print("SmartHome控制台帮助")
            print("about或version-----显示版本信息")
            print("help-----显示此帮助")
            print("stop或exit-----退出程序")
            print("")
        elif cmd == "stop" or cmd == "exit":
            stop()
        elif cmd == "about" or cmd == "version":
            about()
        elif cmd == "doortest1":
            doortest1()
        elif cmd == 'doorswitch':
            Door.door_switch(pinList['door'])
        elif cmd=='updatetime':
            timeThread = threading.Thread(target=updateTime())
            timeThread.start()
        elif cmd == '':
            pass
        else:
            print("未知命令 输入help查看帮助")
        cmd = ""
        # --------------------------------------
