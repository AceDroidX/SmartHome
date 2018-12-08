from pcduino.gpio import *
from pcduino.pwm import *
from SmartHome import pinList

state={'bigLight': 'off', 'fanB': 'off', 'door': 'off', 'whiteLight': 'off',
           'RGBLightR': 'off', 'RGBLightB': 'off', 'RGBLightG': 'off','RGB':'off'}

def fanSwitch():
    global state
    if state['fanB']=="on":
        fan_stop(pinList['fanB'])
        return 'off'
    else:
        fan_on(pinList['fanB'])
        return 'on'

def fan_on(pin):
    global state
    pin_mode(pin, OUTPUT)
    digital_write(pin, LOW)
    state['fanB']='on'


def fan_stop(pin):
    global state
    pin_mode(pin, OUTPUT)
    digital_write(pin, HIGH)
    state['fanB']='off'


def bigLightSwitch():
    global state
    if state['bigLight']=='off':
        pwm_enable(pinList['bigLight'])
        state['bigLight']='on'
        return 'on'
    else:
        pwm_disable(pinList['bigLight'])
        state['bigLight']='off'
        return 'off'

def bigLightSet(freq,level):#freq=1000,maxlevel=32
    global state
    f=int(freq)
    l=int(level)
    if f>pwm_get_freq_range(pinList['bigLight'])[1]:
        return 'freq max'+str(pwm_get_freq_range(pinList['bigLight']))
    if l>pwm_get_max_level(pinList['bigLight']):
        return 'level max'+str(pwm_get_max_level(pinList['bigLight']))
    pwm_set(pinList['bigLight'],l,f)
    if state['bigLight']=='on':
        pwm_enable(pinList['bigLight'])
    return 'ok'


def whiteLight_on(pin):
    global state
    pin_mode(pin, OUTPUT)
    digital_write(pin, HIGH)
    state['whiteLight'] = 'on'


def whiteLight_off(pin):
    global state
    digital_write(pin, LOW)
    state['whiteLight'] = 'off'

def whiteLightSwitch():
    global state
    if state['whiteLight'] == 'off':
        whiteLight_on(pinList['whiteLight'])
        return 'on'
    elif state['whiteLight'] == 'on':
        whiteLight_off(pinList['whiteLight'])
        return 'off'
    else:
        whiteLight_off(pinList['whiteLight'])
        print('内存被非法更改 已重置')
        return 'error-whitelight'


def RGB_on():
    pass


def RGB_off():
    pass


def RGB_set(fr,lr,fb,lb,fg,lg):
    pass
    # global state
    # if fr>pwm_get_freq_range(pinList['RGBLightR']):
    #     return 'freq max'+str(pwm_get_freq_range(pinList['bigLight']))
    # if lr>pwm_get_max_level(pinList['RGBLightR']):
    #     return 'level max'+str(pwm_get_max_level(pinList['bigLight']))
    # pwm_set(pinList['RGBLightR'],level,freq)
    # if state['RGB']=='on':
    #     pwm_enable(pinList['bigLight'])
    # return 'ok'


################################################
#####       debug部分
################################################

def setPWM(pin,freq,level):
    pwm_set(pin, level, freq)

def enPWM(pin):
    pwm_enable(pin)

def disPWM(pin):
    pwm_disable(pin)

def getMax(pin):
    f=pwm_get_freq_range(pin)
    l=pwm_get_max_level(pin)
    s = '%s----%s\n' % (f,l) 
    return s

def getNow(pin):
    f=pwm_get_freq(pin)
    l=pwm_get_level(pin)
    s = '%s----%s\n' % (f,l) 
    return s

def setGPIO(pin,value):
    digital_write(pin,value)

def pwm_get_level(pin):
    """
    获取对应PWM口的最大占空比（取决于PWM口和所设置的频率）
    参数：pin，PWM口号
    返回值：整数，pin口的最大占空比
    """
    # 参数检查
    if pin not in (3, 5, 6, 9, 10, 11):
        raise ValueError("Only pwm3, 5, 6, 9, 10, 11 support pwm")
    # 读取最大占空比并返回
    with open("/sys/devices/virtual/misc/pwmtimer/level/pwm%d" % pin, 'r') as f:
        level = int(f.read())
    return level


def pwm_get_freq(pin):
    # 参数检查
    if pin not in (3, 5, 6, 9, 10, 11):
        raise ValueError("Only pwm3, 5, 6, 9, 10, 11 support pwm")
    # 读取freq并返回
    with open("/sys/devices/virtual/misc/pwmtimer/freq/pwm%d" % pin, 'r') as f:
        freq = int(f.read())
    return freq