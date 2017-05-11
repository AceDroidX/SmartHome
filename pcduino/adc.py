#!/usr/bin/python3
# -*- coding: UTF-8 -*-

"""
本模块用于ADC的读值
==================
修改时间：2017-3-23 19:09:31
作者：YaHei（zk）
联系方式：929391459@qq.com
"""

from .pinmap import PinMap

pins = PinMap('/proc', 'adc', 6)

def analog_read(channel):
    """
    返回模拟口的ADC读值，
    A0、A1为6位ADC，返回值范围为0-63；
    A2、A3、A4、A5为12位ADC，返回值范围为0-4095
    """
    with open(pins.get_path(channel), 'r') as f:
        return int(f.read(32).split(':')[1].strip())
