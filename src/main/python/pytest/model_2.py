#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import model_1

def add(a,b):
    return a + b

if __name__ == '__main__':
    if (len(sys.argv) > 1):
        a = []
        for i in range(1, len(sys.argv)):
            a.append((int(sys.argv[i])))

        print(model_1.model(a[0],a[1]))

#a = sys.argv[1]
#b = sys.argv[2]

#print("a+b = %s" % model_1.model(a, b))


