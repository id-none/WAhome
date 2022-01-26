base = input("请输入要转换的字符串：")
by = bytes(base,'UTF-8')    #先将输入的字符串转化成字节码
hexstring = by.hex()    #得到16进制字符串，不带0x
 
print(hexstring)
#输出如下：
#请输入要转换的字符串：大多数
#e5a4a7e5a49ae695b0
