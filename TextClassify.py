
# coding: utf-8

# In[167]:

import string
import sys
import math as m
import numpy as np
import os
import pandas as pd
from stop_words import get_stop_words
from collections import Counter


#Path='/Users/wazeed/Downloads/20news-bydate.tar/20news-bydate-train'
Path=sys.argv[1]
i=j=tot=0
lm=0
len_ftotal=0
len_fclass=[]
len_dclass=[]
data_class=[]
vocab=[]
t=[]
tt=[]
for root,subdir,nam in os.walk(Path):
    for sub in subdir:
        c=0
        fpath=os.path.join(root,sub)
        for rot,sudir,name in os.walk(fpath):
            a=[]
            len_fclass.append(len(name))
            len_ftotal+=len(name)
            for n in name:
                fupath=os.path.join(fpath,n)
                f=open(fupath,'r',encoding='iso-8859-15')
                for l in f:
                    l=str(l)
                    l.strip()
                    while l.startswith("Lines:") or c==0:
                        l= f.readline()
                        #l=str(next(f))
                        if l.startswith("Lines:"):
                            c=1
                    x=[w for w in l if w not in (string.punctuation)]
                    x=''.join(x)
                    x=x.split()
                    x= [a for a in x if a not in get_stop_words('english')] 
                    x=' '.join(x)
                    x=x.split()
                    a=a+x
            z=Counter(a)
            b = []
            for i in a:
                if i not in b:
                    b.append(i)
            a=b
            t=t+a
            len_dclass.append(len(a))
            data_class.append(dict(z))
            tot+=len(a)
    for i in t:
        if i not in tt:
            tt.append(i)
    t=tt
    sz_vocab=len(t)
    sum_vocab=[]
    for i in range(0,5):
        di=dict()
        total_c=0
        for each in t:
            dic=data_class[i]
            data=dic.get(each,10)
            di[each]=data
            total_c+=data
        vocab.append(di)
        sum_vocab.append(total_c)
    #print(sum_vocab[0])
    break
        

        
                            
j=0
acc=0
#Path='/Users/wazeed/Downloads/20news-bydate.tar/20news-bydate-test'
Path=sys.argv[2]
for root,subdir,nam in os.walk(Path):
    for sub in subdir:
        fpath=os.path.join(root,sub)
        for rot,sudir,name in os.walk(fpath):
            cz=v=0
            j=j+1
            for n in name:
                a=c=[]
                probf=[]
                arr=[]
                fupath=os.path.join(fpath,n)
                f=open(fupath,'r',encoding='iso-8859-15')
                for l in f:
                    l=str(l)
                    l.strip()
                    while l.startswith("Lines:") or c==0:
                        l= f.readline()
                        if l.startswith("Lines:"):
                            c=1
                    x=[w for w in l if w not in (string.punctuation)]
                    x=''.join(x)
                    x=x.split()
                    x= [a for a in x if a not in get_stop_words('english')] 
                    x=' '.join(x)
                    x=x.split()
                    a=a+x
                for i in range(0,5):
                    prob=0
                    prob_c=0
                    for e in a:
                        dic=data_class[i]
                        data_class[i][e]=dic.get(e,1)
                        prob+=m.log(data_class[i][e]/sum_vocab[i])
                    prob_c=m.log(len_fclass[i]/len_ftotal)
                    prob=prob+prob_c
                    probf.append(prob)
                v=v+1
                arr=np.array(probf)
                predict=np.argmax(arr)+1
                #print(predict)
                if predict==j:
                    cz=cz+1
                else:
                    cz=cz-1
            acc+=(cz/v)
acc=acc/5
acc=lm+acc
print('Accuracy is:')
print(acc*100)
            
            
