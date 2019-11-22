# import socket programming library
import socket
import requests
import re
from PyDictionary import PyDictionary
dictionary=PyDictionary()




# import thread module
from _thread import *
import threading

print_lock = threading.Lock()

import nltk
from nltk.stem.lancaster import LancasterStemmer
stemmer = LancasterStemmer()

# things we need for Tensorflow
import numpy as np
import tflearn
import tensorflow as tf
import random
import wikipediaapi

# restore all of our data structures
import pickle
data = pickle.load( open( "training_data", "rb" ) )
words = data['words']
classes = data['classes']
train_x = data['train_x']
train_y = data['train_y']

# import our chat-bot intents file
import json
with open('C:\\Users\\sarunach\\Desktop\\machi.json') as json_data:
    intents = json.load(json_data)

# Build neural network
net = tflearn.input_data(shape=[None, len(train_x[0])])
net = tflearn.fully_connected(net, 8)
net = tflearn.fully_connected(net, 8)
net = tflearn.fully_connected(net, len(train_y[0]), activation='softmax')
net = tflearn.regression(net)


def techChecker(req):
    flag=False
    wiki_wiki = wikipediaapi.Wikipedia('en')
    wordList = req.split()
    for w in wordList:
        print ("value of the word is "+w)
        if (wiki_wiki.page(w.capitalize() + '_(programming_language)').exists() or wiki_wiki.page(
                    'Apache_' + w.capitalize()).exists() or
                wiki_wiki.page(w.capitalize() + '_Software').exists() or wiki_wiki.page(
                w.capitalize() + '_(framework)').exists() or wiki_wiki.page(
                'Microsoft_' + w.capitalize()).exists() ):
            print("sucessfull" +w)
            flag=True
            break
    if flag:

        return  "yes, I have  booked and sent mail about the course "+w +" on behalf of you"
    else:
        return "sorry,it seems to be non technical training, i can book only technical training. please ask me about technical training "

# Define model and setup tensorboard
model = tflearn.DNN(net, tensorboard_dir='tflearn_logs')
def clean_up_sentence(sentence):
    # tokenize the pattern
    sentence_words = nltk.word_tokenize(sentence)
    # stem each word
    sentence_words = [stemmer.stem(word.lower()) for word in sentence_words]
    return sentence_words

# return bag of words array: 0 or 1 for each word in the bag that exists in the sentence
def bow(sentence, words, show_details=False):
    # tokenize the pattern
    sentence_words = clean_up_sentence(sentence)
    # bag of words
    bag = [0]*len(words)
    for s in sentence_words:
        for i,w in enumerate(words):
            if w == s:
                bag[i] = 1
                if show_details:
                    print ("found in bag: %s" % w)

    return(np.array(bag))


print (classes)


# load our saved model
model.load('./model.tflearn')

context = {}

ERROR_THRESHOLD = 0.25
def classify(sentence):
    # generate probabilities from the model
    results = model.predict([bow(sentence, words)])[0]
    # filter out predictions below a threshold
    results = [[i,r] for i,r in enumerate(results) if r>ERROR_THRESHOLD]
    # sort by strength of probability
    results.sort(key=lambda x: x[1], reverse=True)
    return_list = []
    for r in results:
        return_list.append((classes[r[0]], r[1]))
    # return tuple of intent and probability
    return return_list

def response(sentence, userID='123', show_details=False):
    results = classify(sentence)
    # if we have a classification then find the matching intent tag
    if results:
        # loop as long as there are matches to process
        while results:
            for i in intents['intents']:
                # find a tag matching the first result
                if i['tag'] == results[0][0]:
                    # set context for this intent if necessary
                    if 'context_set' in i:
                        if show_details: print ('context:', i['context_set'])
                        context[userID] = i['context_set']

                    # check if this intent is contextual and applies to this user's conversation
                    if not 'context_filter' in i or \
                        (userID in context and 'context_filter' in i and i['context_filter'] == context[userID]):
                        if show_details: print ('tag:', i['tag'])
                        # a random response from the intent
                        return (random.choice(i['responses']))

            results.pop(0)



def send_mail():
    return ""

# thread fuction
def threaded(c):
    while True:

        # data received from client
        clientdata = c.recv(1024)
        print("client recived data is ")
        print(clientdata)
        resp=str(response(str(clientdata)))
        if resp=='TechTraining':
            resp=techChecker(str(clientdata))
            print ("value sent from server "+resp)

            #c.send(resp.encode('ascii'))
            send_mail()


        if resp=='Reminder':
            wordList = str(clientdata).split()
            for w in wordList:
                if dictionary.meaning(w):
                    print("if "+w)
                else:
                    print("else "+w)
            resp = "i have sent mail to "+w
            data = {'userName': w}
            r = requests.post(url='http://localhost:8181/machi/standup/sendReminderEmail', data=data)



       


        print(resp)
        if not clientdata:
            print('Bye')


            # lock released on exit
            print_lock.release()
            break

        # reverse the given string from client
        #data = data[::-1]
        #print(response(clientdata))
                # send back reversed string to client
        c.send(resp.encode('ascii'))

        # connection closed
    c.close()


def Main():
    host = ""

    # reverse a port on your computer
    # in our case it is 12345 but it
    # can be anything
    port = 12345
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((host, port))
    print("socket binded to port", port)

    # put the socket into listening mode
    s.listen(5)
    print("socket is listening")

    # a forever loop until client wants to exit
    while True:
        # establish connection with client
        c, addr = s.accept()

        # lock acquired by client
        print_lock.acquire()
        print('Connected to :', addr[0], ':', addr[1])

        # Start a new thread and return its identifier
        start_new_thread(threaded, (c,))
    s.close()


if __name__ == '__main__':

    Main()