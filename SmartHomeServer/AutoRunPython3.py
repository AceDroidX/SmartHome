#-*- coding:utf-8 -*-

"""
自动启动程序

源程序已修改
使用python3启动程序
修改时间：2017-09-08 19:00
By wxx
"""

import os
Auto_dir = '/home/ubuntu/wxx/SmartHome'

#AutoStart_List_File = open(Auto_folder +'astlist')
#AutoStart_List= AutoStart_List_File.read()


SBList=[]
PYList=[]

Dir_filelist=os.listdir(Auto_dir)
for file_name in Dir_filelist:

        Full_Path_Name = os.path.join(Auto_dir , file_name)
        print Full_Path_Name
        if file_name.endswith('.sb') & (~os.path.isdir(Full_Path_Name)):
                SBList.append(file_name)
        if file_name.endswith('.py') & (~os.path.isdir(Full_Path_Name)):
                PYList.append(file_name)


for fname in PYList:
        os.system("python3 "+Auto_dir + fname + " &")
if len(SBList)>0:
        os.system("scratch "+Auto_dir +SBList[0])





#print "scratch "+Auto_folder +SBList[0]
