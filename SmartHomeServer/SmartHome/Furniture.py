import pcduino.gpio
import pcduino.pwm

whiteLightState = 'off'


def fan_on():
    pass


def fan_stop():
    pass


def bigLight_on(pin):
    pcduino.pwm_enable(pin)
    pass


def bigLight_set(pin, level, freq):
    pcduino.pwm_set(pin, level, freq)
    pass


def bigLight_off(pin):
    pcduino.pwm_disable(pin)
    pass


def whiteLight_on(pin):
    global whiteLightState
    pcduino.gpio.pin_mode(pin, pcduino.OUTPUT)
    pcduino.gpio.digital_write(pin, pcduino.HIGH)
    whiteLightState = 'on'
    pass


def whiteLight_off(pin):
    global whiteLightState
    pcduino.gpio.digital_write(pin, pcduino.LOW)
    whiteLightState = 'off'
    pass


def whiteLight_switch(pin):
    global whiteLightState
    if whiteLightState == 'off':
        whiteLight_on(pin)
    elif whiteLightState == 'on':
        whiteLight_off(pin)
    else:
        whiteLight_off(pin)
        print('内存被非法更改 已重置')


def RGB_on():
    pass


def RGB_off():
    pass


def RGB_set():
    pass
