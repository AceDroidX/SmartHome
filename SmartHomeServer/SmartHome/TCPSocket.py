# coding=utf-8
# 导入socket库:
import socket
import threading
import time

import Security
import Door
import SmartHome
import Furniture

port = 23333
thread = 5


def tcplink(sock, addr):
    print('Accept new connection from %s:%s...' % addr)
    while True:
        data = sock.recv(1024)
        time.sleep(1)
        echo = data.decode('utf-8')
        netcmd = echo.split(' ')
        if echo == '':
            break
        print('<---' + addr.__str__() + '>' + echo)

        # 发送别忘了\n
        if netcmd[0] == 'keepAlive':
            sock.send('keepAlive\n'.encode('utf-8'))
            print('--->' + addr.__str__() + '>' + 'keepAlive')
        elif netcmd[0] == 'verify':
            if Security.verify(netcmd[1]):
                sock.send('verify keycorrect\n'.encode('utf-8'))
                print('--->' + addr.__str__() + '>' + 'verify keycorrect')
            else:
                sock.send('verify keywrong\n'.encode('utf-8'))
                print('--->' + addr.__str__() + '>' + 'verify keywrong')
        elif netcmd[0] == 'isSmartLock':
            sock.send('SmartLock\n'.encode('utf-8'))
            print('--->' + addr.__str__() + '>' + 'SmartLock')
        elif netcmd[0] == 'setkey':
            if Security.isverify():
                sock.send(Security.setkey(echo.split(' ')[1]).encode('utf-8'))
        elif netcmd[0] == 'state':
            if Security.isverify():
                pass
        elif netcmd[0] == 'door_switch':
            if Security.isverify():
                Door.door_switch(SmartHome.pinList['door'])
                sock.send(('door_switch' + Door.lockstate + '\n').encode('utf-8'))
                print('--->' + addr.__str__() + '>' + 'door_switch' + Door.lockstate)
        elif netcmd[0] == 'whiteLight_switch':
            if Security.isverify():
                Furniture.whiteLight_switch(SmartHome.pinList['whiteLight'])
                sock.send(('whiteLight_switch' + Furniture.whiteLightState + '\n').encode('utf-8'))
                print('--->' + addr.__str__() + '>' + 'whiteLight_switch' + Furniture.whiteLightState)
        elif netcmd[0] == 'test':
            if Security.isverify():
                pass
        elif echo == 'exit':
            break
    sock.close()
    print('Connection from %s:%s closed.' % addr)


def server():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # 监听端口:
    s.bind(('0.0.0.0', port))

    s.listen(thread)
    print('Waiting for connection...')

    while True:
        # 接受一个新连接:
        sock, addr = s.accept()
        # 创建新线程来处理TCP连接:
        t = threading.Thread(target=tcplink, args=(sock, addr))
        t.start()


def startServer():
    serverThread = threading.Thread(target=server)
    serverThread.start()


if __name__ == '__main__':
    startServer()
