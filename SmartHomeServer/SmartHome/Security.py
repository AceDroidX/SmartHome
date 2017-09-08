file = '/home/ubuntu/SmartHomeServer/SmartHome/config.txt'


def iskey(key):
    with open(file, 'r') as f:
        config = f.read().split('\n')
        config2 = config[2].split(' ')
        if config[1] == 'alreadysetkey':
            if key == config2[1]:
                return 'keycorrect'
            else:
                return 'keywrong'
        else:
            return 'notInitialize'


def setkey(key):
    with open(file, 'r') as f:
        config = f.read().split('\n')
        if config[1] != 'alreadysetkey':
            with open(file, 'w') as f:
                f.write('配置文件 请勿删除\nalreadysetkey\nkeyis ' + key)
