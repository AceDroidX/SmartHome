#!/usr/bin/python3
#-*- coding: UTF-8 -*-

"""
本模块用于提供D2、D3引脚的外部中断功能
外部中断0对应D2口，外部中断1对应D3口
目前只有D2口实现了外部中断，此时D3的输入会影响D2口的外部中断
比如当设置D2口为上升沿模式时，D3口相当于高电平模式触发同一中断处理函数？？？？
建议在使用D2口外部中断时，不要使用D3口的输入模式
==================
修改时间：2017-3-23 19:09:31
作者：YaHei（zk）
联系方式：929391459@qq.com
"""

import os
import sys
import signal
import struct
import fcntl

# 外部中断的相关设备文件  
swirq_dev = "/dev/swirq"  

# 中断开关相关常量
SWIRQ_START   = 0x201   
SWIRQ_STOP    = 0x202      
SWIRQ_SETPID  = 0x203    
SWIRQ_ENABLE  = 0x204    
SWIRQ_DISABLE = 0x205  
   
# 中断类型相关常量
SWIRQ_RISING  = 0x00    
SWIRQ_FALLING = 0x01   
SWIRQ_HIGH    = 0x02    
SWIRQ_LOW     = 0x03      
SWIRQ_CHANGE  = 0x04  

# # 中断引脚编号
# SWIRQ_PIN1    = 0x0  
# SWIRQ_PIN2    = 0x1

# 中断口的对应信号
# 外部中断0（D2）使用信号USR1
# 外部中断1（D3）使用信号USR2
irq_signal = (signal.SIGUSR1, signal.SIGUSR2)

# 保存两个中断口的类型、处理函数
_irq_func_lst = [None, None]
_irq_type_lst = [None, None]

# 用于得到一个符合中断信号规则的函数
def _irq_func(func):
  def new_func(a, b):
    func()
  return new_func

def irq_init(swirq_num, swirq, irq_func):  
  "中断初始化，设定中断类型和中断处理函数"
  # 目前只有D2口能够使用外部中断
  if swirq != 0:
    raise ValueError("Only pin1 / gpio2 support interrupts!")
  # 保存中断口类型和处理函数
  _irq_type_lst[swirq_num] = swirq
  _irq_func_lst[swirq_num] = _irq_func(irq_func)
  # 设置中断
  signal.signal(irq_signal[swirq_num], _irq_func_lst[swirq_num])
  pid = os.getpid()
  fd = open(swirq_dev)
  fcntl.ioctl(fd, SWIRQ_STOP, struct.pack("@B",  swirq_num))  
  fcntl.ioctl(fd, SWIRQ_SETPID, struct.pack("@Bii", swirq_num, swirq , pid))
  fd.close()  

def irq_start(swirq_num):
  "中断使能"
  # 获取中断口类型和处理函数
  swirq = _irq_type_lst[swirq_num]
  irq_func = _irq_func_lst[swirq_num]
  # 打开中断
  signal.signal(irq_signal[swirq_num], _irq_func_lst[swirq_num])  
  pid = os.getpid()  
  fd = open(swirq_dev)
  fcntl.ioctl(fd, SWIRQ_START, struct.pack("@B", swirq_num))  
  fd.close()  

def irq_stop(swirq_num):
  "中断失能"
  # 获取中断口类型和处理函数
  swirq = _irq_type_lst[swirq_num]
  irq_func = _irq_func_lst[swirq_num]
  # 关闭中断
  pid = os.getpid()  
  fd = open(swirq_dev)
  fcntl.ioctl(fd, SWIRQ_STOP, struct.pack("@B",  swirq_num))  
  fd.close()  
