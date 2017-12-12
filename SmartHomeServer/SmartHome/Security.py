import hashlib

import os
from Crypto.Cipher import AES
from Crypto import Random

import time

timeout = 600  # 10分钟

path = os.path.split(os.path.realpath(__file__))[0]
file = path + '/config.txt'

verifiedDevices = []
verifiedIPs = []


def getkey():
    with open(file, 'r') as f:
        config = f.read().split('\n')
        return config[2]


def iskey(key):
    with open(file, 'r') as f:
        config = f.read().split('\n')
        if config[1] == 'alreadysetkey':
            if key == config[2]:
                return 'keycorrect'
            else:
                return 'keywrong'
        else:
            return 'notInitialize'


def setkey(key):
    with open(file, 'w') as f:
        config = f.read().split('\n')
        if config[1] != 'alreadysetkey':
            with open(file, 'w') as f:
                f.write('配置文件 请勿删除\nalreadysetkey\n' + key)


def isverify(ip):
    print(verifiedIPs)
    for i in verifiedIPs:
        if time.time() - float(i.split(' ')[1]) > timeout:
            verifiedIPs.remove(i)
        if i.split(' ')[0] == ip:
            return True
    else:
        return False


def verify(key, ip):
    tmp = iskey(key)
    if tmp == 'keycorrect':
        verifiedIPs.append(ip + ' ' + str(time.time()))
        return tmp
    else:
        return tmp


def AESencrypt(data, password):
    bs = AES.block_size
    pad = lambda s: s + (bs - len(s) % bs) * chr(bs - len(s) % bs)
    iv = Random.new().read(bs)
    cipher = AES.new(password, AES.MODE_CBC, iv)
    data = cipher.encrypt(pad(data))
    data = iv + data
    return data


def AESdecrypt(data, password):
    bs = AES.block_size
    if len(data) <= bs:
        return data
    unpad = lambda s: s[0:-ord(s[-1])]
    iv = data[:bs]
    cipher = AES.new(password, AES.MODE_CBC, iv)
    data = unpad(cipher.decrypt(data[bs:]))
    return data


def sha256(str):
    sh = hashlib.sha256()
    sh.update(str)
    return sh.hesdigest()


def b64decode():
    pass
