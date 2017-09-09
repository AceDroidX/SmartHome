# coding=utf-8
# 导入socket库:
import socket
import threading
import time

import Security
import Door
import SmartHome

port = 23333
thread = 5


def tcplink(sock, addr):
    print('Accept new connection from %s:%s...' % addr)
    while True:
        data = sock.recv(1024)
        time.sleep(1)
        echo = data.decode('utf-8')
        netcmd = echo.split(' ')
        print(addr.__str__() + '>' + echo)
        if netcmd[0] == 'verify':
            Security.verify(netcmd[1])
        elif netcmd[0] == 'setkey':
            if Security.isverify():
                sock.send(Security.setkey(echo.split(' ')[1]).encode('utf-8'))
        elif netcmd[0] == 'state':
            if Security.isverify():
                pass
        elif netcmd[0] == 'switch':
            if Security.isverify():
                Door.door_switch(SmartHome.pinList['door'])
                sock.send(Door.lockstate.encode('utf-8'))
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
