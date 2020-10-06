from urllib.request import urlopen
from urllib import parse
from selenium import webdriver
from bs4 import BeautifulSoup

from flask import Flask
from flask import request
from flask import jsonify

import time
import os
import json
import re
import sys, traceback
import threading

from library import *
from crawling import *

def makeSearchUrl(ISBN, title, sid, flag):
    if flag ==1:
        if sid == MJ_CODE or sid == SK_CODE :
            searchUrl = base_start_url_list[sid]+base_middle_url_list[sid] + base_end_url_list[sid].format(ISBN, ISBN, ISBN)
        else :
            searchUrl = base_start_url_list[sid]+base_middle_url_list[sid] + ISBN+ base_end_url_list[sid]
    else :
        # flag 2.
        searchUrl = base_start_url_list[sid]+base_middle_url_list[sid] + title+ base_end_url_list[sid]
    return searchUrl

def makeJsonDict(result):
    # result 의 타입은 LibraryLS
    jsonDict = {}
    jsonDict['sid'] = result.sid
    jsonDict['loanStatusList'] = []
    for item in result.loanStatusList:
        jsonDict['loanStatusList'].append(vars(item))
    jsonDict['errorMessage']=result.errorMessage
    return jsonDict

def findLoanStatus(ISBN, title, sid, resultList = None):

 
    if sid == KW_CODE or sid == SY_CODE or sid ==SW_CODE or sid == SS_CODE:
        searchUrl = makeSearchUrl(ISBN, title, sid, 2)
    else :
        searchUrl = makeSearchUrl(ISBN, title, sid, 1)
    result = crawling(ISBN, title, sid, searchUrl)
    # json string 으로 만들기.

    jsonDict = makeJsonDict(result)
    if resultList is not None:
        resultList[sid] = jsonDict
    else :
        return  jsonDict
    return
    #return json.dumps(jsonDict)

# 예시.
#ISBN = '9788972756194'
#koreaTitle = '나미야 잡화점'
#title = parse.quote(koreaTitle)
#print(findLoanStatus(ISBN, title, SK_CODE))


#print(findLoanStatus(isbn, title, DS_CODE))
#방법 1


#from datetime import datetime
#start = datetime.now()
#resultList = [i for i in range(12)]
#threads = []
#for i in range(12):
#    t = threading.Thread(target=findLoanStatus, args=(ISBN, title, i, resultList))
#    threads.append(t)

#for t in threads:
#    t.start()
#for t in threads:
#    t.join()
##jsonString =  json.dumps(resultList)
##print(jsonString)

#resultsizeStr = ''
#for item in resultList:
#    resultsizeStr += ' '+ str(len(item['loanStatusList']))

#print(resultsizeStr)
#print('')
#for item in resultList:
#    print(item)
#    print('')

#finish = datetime.now() - start
#print('실행 시간 : ',finish)
## # 방법 2