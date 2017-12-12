#!/usr/bin/python
# -*- coding: utf-8 -*-

"""
####################原注释
本程序用于通过HTTP Server的方式与APP Inventor、Scratch等进行结合；
开机自动模拟按键以启动Scratch程序
===============================
camera指令：
  如果前边初始化摄像头成功，则直接返回一帧图像
  否则反馈错误信息
virtkey指令：
  如果参数为单字符，则模拟这个按键
  否则反馈错误信息
ctrl指令：
  如果参数的python函数是合法的，则尝试执行这条指令
  如果途中出错，反馈错误信息
===============================
修改时间：2017-4-9 17:21:35
作者：YaHei（zk）
联系方式：929391459@qq.com
"""

# 摄像头开关，如果不需要使用摄像头，则将True改为False即可
CAMERA = True
# 开机自动按键开关与指定按键
AUTO_VIRTKEY = True
VIRTKEYS = ''  # 字符串，字符串内的所有字符都会被触发

import http.server, socketserver, virtkey, cv2
from pcduino import *
from time import sleep

# 支持远程执行的python函数
PCDUINO_FUNC = (
    'digital_read', 'digital_write', 'analog_read', 'pwm_set',
    'pwm_set_level', 'pwm_set_freq', 'pwm_disable', 'pwm_enable',
    'pin_mode')
# 服务器监视端口号
PORT = 23233
# 虚拟按键对象
v = virtkey.virtkey()
# 开机自动模拟按键
if AUTO_VIRTKEY:
    sleep(10)
    for k in VIRTKEYS:
        v.press_keysym(ord(k))
        v.release_keysym(ord(k))
# 尝试初始化摄像头
if CAMERA:
    try:
        cam = cv2.VideoCapture(0)
    except:
        cam = None
else:
    cam = None


class Server(http.server.SimpleHTTPRequestHandler):
    def do_GET(self):
        # 发送空的响应头
        self.send_response(200)
        self.end_headers()
        # 从地址中分割出若干参数
        params = self.path.split('/')
        # 捕获所有错误，防止程序崩溃
        try:
            # 因为有根目录，分割后params[0]必定是空字符串
            # camera指令：
            #   如果前边初始化摄像头成功，则直接返回一帧图像
            #   否则反馈错误信息
            if params[1] == 'camera':
                if cam != None:
                    __, img = cam.read()
                    cv2.imwrite('/tmp/camera.jpg', img)
                    with open('/tmp/camera.jpg', 'rb') as f:
                        self.wfile.write(f.read())
                else:
                    self.wfile.write("没有检测到摄像头，请插入usb摄像头后重启系统")
            # virtkey指令：
            #   如果参数为单字符，则模拟这个按键
            #   否则反馈错误信息
            elif params[1] == 'virtkey':
                if len(params[2]) == 1:
                    ord_key = ord(params[2])
                    v.press_keysym(ord_key)
                    v.release_keysym(ord_key)
                else:
                    self.wfile.write("参数有误，virtkey指令仅支持单字符参数")
            # ctrl指令：
            #   如果参数的python函数是合法的，则尝试执行这条指令
            #   如果途中出错，反馈错误信息
            elif params[1] == 'ctrl':
                for func in PCDUINO_FUNC:
                    if params[2].startswith(func):
                        try:
                            exec(params[2])
                        except:
                            self.wfile.write(params[2] + '函数的参数有误')
                        break
                else:
                    self.wfile.write(params[2] + '不是一个已知的函数')
            # 其他指令：无效
            else:
                self.wfile.write('无效指令')
        except:
            self.wfile.write('指令或参数存在未知错误')


Handler = Server
httpd = socketserver.TCPServer(("", PORT), Handler)
httpd.serve_forever()
