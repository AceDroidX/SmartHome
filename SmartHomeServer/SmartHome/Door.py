import pcduino.gpio
import pcduino.pwm
# import threading

doorpwmlevel = {'lock': 1, 'unlock': 100}
doorpwmfreq = 520

lockstate = 'lock'  # lock锁 unlock没锁


def door_switch(pindoor):
    if lockstate == 'lock':
        door_open(pindoor)
    elif lockstate == 'unlock':
        door_close(pindoor)
    else:
        door_close(pindoor)
        print('内存被非法更改 已重置')


def door_open(pindoor):
    lockstate = 'unlock'
    pcduino.pwm_set(pindoor, doorpwmlevel[lockstate], doorpwmfreq)
    pcduino.pwm_enable(pindoor)
    pass


def door_close(pindoor):
    lockstate = 'lock'
    pcduino.pwm_set(pindoor, doorpwmlevel[lockstate], doorpwmfreq)
    pcduino.pwm_enable(pindoor)
    pass

def door_test1(pindoor,level,freq):
    pcduino.pwm_set(pindoor, level, freq)
    pcduino.pwm_enable(pindoor)
    pass

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
