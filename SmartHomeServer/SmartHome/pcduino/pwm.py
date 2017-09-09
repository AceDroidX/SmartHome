#!/usr/bin/python3
# -*- coding: UTF-8 -*-


"""
本模块用于实现PWM的设置与使用
==================
修改时间：2017-4-9 14:54:02
作者：YaHei（zk）
联系方式：929391459@qq.com

文件已修改
修改时间：2017-9-9 15:07
"""


def pwm_get_max_level(pin):
    """
    获取对应PWM口的最大占空比（取决于PWM口和所设置的频率）
    参数：pin，PWM口号
    返回值：整数，pin口的最大占空比
    """
    # 参数检查
    if pin not in (3, 5, 6, 9, 10, 11):
        raise ValueError("Only pwm3, 5, 6, 9, 10, 11 support pwm")
    # 读取最大占空比并返回
    with open("/sys/devices/virtual/misc/pwmtimer/max_level/pwm%d" % pin, 'r') as f:
        max_level = int(f.read())
    return max_level


def pwm_get_freq_range(pin):
    """
    获取对应PWM口的频率返回
    参数：pin，PWM口号
    返回值：元组，第一个元素为最小值，第二个元素为最大值
    """
    if pin not in (3, 5, 6, 9, 10, 11):
        raise ValueError("Only pwm3, 5, 6, 9, 10, 11 support pwm")
    with open("/sys/devices/virtual/misc/pwmtimer/freq_range/pwm%d" % pin, 'r') as f:
        str_freq = f.read().replace('\n', ':').split(':')
        max_freq = int(str_freq[1])
        min_freq = int(str_freq[3])
    return (min_freq, max_freq)


def pwm_duty2level(pin, duty):
    """
    将占空比百分比转换为占空比等级
    参数：pin，PWM口号
         duty，占空比百分比
    返回值：占空比等级（作为pwm_set_level）的参数
    """
    return duty * pwm_get_max_level(pin) / 100


def pwm_set_level(pin, level):
    """
    设置PWM的占空比（占空比设置的精度取决于max_level）
    参数：pin，PWM口号
         level，占空比等级（如果要按百分比计算，可以借助函数pwm_duty2level)
    返回值：无
    """
    if pin not in (3, 5, 6, 9, 10, 11):
        raise ValueError("Only pwm3, 5, 6, 9, 10, 11 support pwm")
    # 关闭pwm
    pwm_disable(pin)
    # 计算实际的level并设置占空比
    with open("/sys/devices/virtual/misc/pwmtimer/level/pwm%d" % pin, 'w') as f:
        f.write("%d\n" % level)


def pwm_set_freq(pin, freq):
    """
    设置PWM的频率
    参数：pin，PWM口号
         freq，所要设置的频率值
    返回值：无
    """
    # 参数检查
    if pin not in (3, 5, 6, 9, 10, 11):
        raise ValueError("Only pwm3, 5, 6, 9, 10, 11 support pwm")
    min, max = pwm_get_freq_range(pin)
    if not min <= freq <= max:
        raise ValueError("For pwm%d, freq must be between %d and %d" % (pin, min, max))
    # 关闭PWM
    pwm_disable(pin)
    # 写入频率值
    with open("/sys/devices/virtual/misc/pwmtimer/freq/pwm%d" % pin, 'w') as f:
        f.write("%d\n" % freq)


def pwm_set(pin, level, freq):
    """
    同时设置PWM的占空比和频率
    参数：pin，PWM口号
         level，占空比（%）
         freq，频率
    返回值：无
    """
    pwm_set_freq(pin, freq)
    pwm_set_level(pin, level)


def pwm_enable(pin):
    """
    使能PWM
    参数：pin，PWM口号
    返回值：无
    """
    # 参数检查
    if pin not in (3, 5, 6, 9, 10, 11):
        raise ValueError("Only digital3, 5, 6, 9, 10, 11 support pwm")
    # 使能
    with open("/sys/devices/virtual/misc/pwmtimer/enable/pwm%d" % pin, 'w') as f:
        f.write("1")


def pwm_disable(pin):
    """
    失能PWM
    参数：pin，PWM口号
    返回值：无
    """
    # 参数检查
    if pin not in (3, 5, 6, 9, 10, 11):
        raise ValueError("Only digital3, 5, 6, 9, 10, 11 support pwm")
    # 失能
    with open("/sys/devices/virtual/misc/pwmtimer/enable/pwm%d" % pin, 'w') as f:
        f.write("0")
