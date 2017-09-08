#!/usr/bin/python3
# -*- coding: UTF-8 -*-

import pcduino.gpio
import pcduino.pwm

pcduino.pwm_set(6, 50, 50)
pcduino.pwm_enable(6)

