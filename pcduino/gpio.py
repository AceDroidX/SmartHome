#!/usr/bin/python3
# -*- coding: UTF-8 -*-

"""
本模块用于GPIO的控制
==================
修改时间：2017-3-23 19:09:31
作者：YaHei（zk）
联系方式：929391459@qq.com
"""

from .pinmap import PinMap


__all__ = ['HIGH', 'LOW', 'INPUT', 'OUTPUT','PWM','UART' ,'digital_write', 'digital_read',
           "pin_mode"]

HIGH = 1
LOW = 0
INPUT = 0
OUTPUT = 1
PWM = 2
UART = 3

gpio_pins = PinMap(
    '/sys/devices/virtual/misc/gpio/pin',
    'gpio',
    20
)

gpio_mode_pins = PinMap(
    '/sys/devices/virtual/misc/gpio/mode/',
    'gpio',
    20
)

def digital_write(channel, value):
    """
    向数字口写入数据，高电平HIGH或低电平LOW
    """
    path = gpio_pins.get_path(channel)
    with open(path, 'w') as f:
        f.write('1' if value == HIGH else '0')

def digital_read(channel):
    """
    读取数字口数据，返回HIGH或LOW
    """
    path = gpio_pins.get_path(channel)
    with open(path, 'r') as f:
        return f.read(1) == '1'

def pin_mode(channel, mode):
    """
    设置数字口的引脚模式，
    对于D0-D13，可以设置为输入模式INPUT、输出模式OUTPUT；
    D3、D5、D6、D9、D10、D11还可以设置为PWM；
    """
    path = gpio_mode_pins.get_path(channel)
    with open(path, 'w+') as f:
        if mode == INPUT:
            f.write('0')
        if mode == OUTPUT:
            f.write('1')
        if mode == PWM:
            f.write('2' if channel in (5, 6) else '1')
        if mode == UART:
            f.write('3')
