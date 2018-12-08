import time

import pcduino.gpio
import pcduino.pwm
from SmartHome import pinList

# import threading

doorpwmlevel = {'lock': 3, 'unlock': 5}
doorpwmfreq = 50

lockstate = 'lock'  # lock锁 unlock没锁

def doorSwitch():
    global lockstate
    if lockstate == 'lock':
        door_open(pinList['door'])
        return 'on'
    elif lockstate == 'unlock':
        door_close(pinList['door'])
        return 'off'
    else:
        door_close(pinList['door'])
        print('内存被非法更改 已重置')
        return 'error-door'


def door_open(pindoor):
    global lockstate
    lockstate = 'unlock'
    pcduino.pwm_set(pindoor, doorpwmlevel[lockstate], doorpwmfreq)
    pcduino.pwm_enable(pindoor)
    time.sleep(1)
    pcduino.pwm_disable(pindoor)
    #print('door on')


def door_close(pindoor):
    global lockstate
    lockstate = 'lock'
    pcduino.pwm_set(pindoor, doorpwmlevel[lockstate], doorpwmfreq)
    pcduino.pwm_enable(pindoor)
    time.sleep(1)
    pcduino.pwm_disable(pindoor)
    #print('door off')


def door_test1(pindoor, level, freq):
    pcduino.pwm_set(pindoor, level, freq)
    pcduino.pwm_enable(pindoor)
    pcduino.pwm_disable(pindoor)
    pass


if __name__ == '__main__':
    while True:
        #door_switch(6)
        time.sleep(5)
"""


class DoorTest1(threading.Thread):  # 测试
    def __init__(self, threadID, name, counter, pindoor):
        threading.Thread.__init__(self)
        self.threadID = threadID
        self.name = name
        self.counter = counter
        self.pindoor = pindoor

    def run(self):
        pwmlevel = 1
        pwmstate = 0
        print("开始线程：" + self.name)
        while True:
            if pwmstate == 0:
                pcduino.pwm_set(self.pindoor, pwmlevel, 520)
                pcduino.pwm_enable(self.pindoor)
                pwmlevel += 1
            if pwmstate == 1:
                pcduino.pwm_set(self.pindoor, pwmlevel, 520)
                pcduino.pwm_enable(self.pindoor)
                pwmlevel -= 1
            if pwmlevel >= 256:
                pwmlevel -= 1
                pwmstate = 1
                print("pwmstate = 1")
            if pwmlevel <= 0:
                pwmlevel -= 1
                pwmstate = 0
                print("pwmstate = 0")


"""
