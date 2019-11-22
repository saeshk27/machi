from PyDictionary import PyDictionary
dictionary=PyDictionary()
if dictionary.meaning("can"):
    print('sucess')
wordList = str("can you please remind satish about this meeting").split()
for w in wordList:
                print (w)
                if dictionary.meaning(w):
                    print("if "+w)
                else:
                    print("else "+w)
resp = "i have sent mail to "+w

print (resp)
