#!/usr/bin/python3
#-*- coding: UTF-8 -*-

"""
串口控制（占用GPIO0和GPIO1）
==================
修改时间：2017-4-9 15:22:15
作者：YaHei（zk）
联系方式：929391459@qq.com
"""

import serial
from .gpio import pin_mode

class Serial(object):
  "串口类"

  def __init__(self, baud = 9600):
    """
    初始化函数，将0、1口设为UART模式，并且初始化串口；
    参数：baud，波特率，默认为9600
    返回值：无
    """
    pin_mode(0, UART)
    pin_mode(1, UART)
    self.uart = serial.Serial('/dev/ttyS1', baud)
  
  def read_string(self, len = 1):
    """
    以字符串的形式读入缓冲区内特定长度的数据（默认为1）
    参数：len，数据长度（字节）
    返回值：字符串形式的数据
    """
    return self.uart.read(len)

  def read_bytes(self, len = 1):
    """
    以二进制的形式读入缓冲区内特定长度的数据（默认为1）
    参数：len，数据长度（字节）
    返回值：二进制形式的数据
    """
    data = 0
    for i in range(len):
      data += ord( uart.read(1) ) << ( (len - 1 - i) * 8 )
    return data 

  def read_all(self):
    """
    以字符串形式读入缓冲区的所有内容
    参数：无
    返回值：字符串形式的数据
    """
    return uart.readall()

  def write_string(self, s):
    """
    以字符串的形式向缓冲区写入内容
    参数：s，欲写入的字符串
    返回值：无
    """
    self.uart.write(s)
  
  def write_bytes(self, bs, len = 1):
    """
    以二进制的形式向缓冲区写入内容
    参数：bs，欲写入的二进制数据
        len，欲写入的长度
    返回值：无
    """
    for i in range(len):
      byte2write = 0xff & ( bs >> ( 8 * (len - 1 - i) ) )
      self.write(byte2write)

  def flush():
    """
    清空串口缓冲区
    参数：无
    返回值：无
    """
    uart.flush()
