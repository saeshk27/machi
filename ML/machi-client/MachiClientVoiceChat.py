# importing speech recognition package from google api
import speech_recognition as sr
import spacy
import requests
import time
import playsound  # to play saved mp3 file
from gtts import gTTS  # google text to speech
import os  # to save/open files
from flask import Flask, jsonify
from flask_cors import CORS, cross_origin
from flask import request
import datetime
app = Flask(__name__)
CORS(app)
app = Flask(__name__)


num = 1
MSG_WELCOME = "Welcome to today's Sprint meeting. Shall we proceed?"
MSG_PROCEED = ", Shall we proceed now?"
MSG_ARE_YOU_THERE = ", are you there?"
MSG_YESTERDAY = ", could you please explain about yesterday's task"
MSG_FUTURE = ", could you please explain about future tasks"
MSG_IMPEDIMENTS = "Do you have any impediments?"
MSG_ANYTHING_ELSE = "Anything else?"
MSG_EXIT = ", you have exceeded the maximum threshold time. Thanks for joining the meeting. Have a great day!"
MSG_CLOSE = "Thanks for joining the meeting. Have a great day. Bye!"
MSG_WELCOME_PLANNING = "Welcome to today's Sprint retrospective and planning meeting. Shall we proceed?"
MSG_INTRO_PLANNING = "Since we have come to the end of the current sprint and we are about to start a new sprint, It is time for us to retrospect how the previous sprint went."
MSG_WENT_WELL = ", What went well last sprint?"
MSG_DINDT_GO_WELL = "Okay, What did not go so well as per our plan?"
MSG_LEARNED = "Fine. What is the learning from what we faced during our last sprint?"
MSG_PUZZLE = "Cool. What is something that still puzzles you?"
MSG_END_PLANNING = ", Thank you for your feedback. All the best for the next sprint."
MSG_WELCOME_GROOMING = "Welcome to today's backlog grooming meeting. We have a list of tasks which has to be estimated and assigned. Shall we proceed?"
MSG_WHICH_USER = "Who is interested to work on the ticket "
MSG_ETA = "Fine. What is your estimate for completing the ticket "
MSG_WELCOME_COACH = "I am here to assist you to perform better. How can I help you?"
MSG_CHECKING_WAIT = ", Can you please hold on a minute. I am trying to retrieve your rating"
MSG_RATING = "Your rating percentage is "
MSG_BURDEN = "You seems to have been burdened. I am notifying this to your manager. Also, you have a discussion with your manager to get this sorted out."
MSG_DID_NOT_MEET_EXPECT = "Your rating is below that of the minimum expectation set for you. Do you need my assistance to improve yourself with any technologies"
MSG_CLOSE_COACH = "Fine then. I wish you the best to improve your rating. I am here to assist you any time. Have a great day. Bye!"
MSG_CLOSE_GENERAL_COACH = "Fine then. I am here to assist you any time. Have a great day. Bye!"
MSG_TRAINING="What training are you particularly looking for?"

import socket

host = '127.0.0.1'

# Define the port on which you want to connect
port = 12345

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# local host IP '127.0.0.1'


# connect to server on local computer
s.connect((host, port))

def summerarize(text):
    nlp = spacy.load("en_core_web_sm")

    # Process whole documents
    finalTxt = ''

    doc = nlp(text)

    # Token and Tag
    for token in doc:
        print(token.pos_)
        print(token)
        if (token.pos_ == 'VERB'):
            print("Value of token is")
            print(token)
            finalTxt = finalTxt + str(token) + " "
        if (token.pos_ == 'PART'):
            print("Value of token is")
            print(token)
            finalTxt = finalTxt + str(token) + " "
        if (token.pos_ == 'AUX'):
            print("Value of token is")
            print(token)
            finalTxt = finalTxt + str(token) + " "

        if (token.pos_ == 'DET'):
            print("Value of token is")
            print(token)
            finalTxt = finalTxt + str(token) + " "
        elif (token.pos_ == 'NOUN'):
            print("Value of token is")
            print(token)
            finalTxt = finalTxt + str(token) + " "
        elif (token.pos_ == 'PART'):
            print("Value of token is")
            print(token)
            finalTxt = finalTxt + str(token) + " "

        elif (token.pos_ == 'PROPN'):
            print("Value of token is")
            print(token)
            finalTxt = finalTxt + str(token) + " "
        elif (token.pos_ == 'NUM'):
            print("Value of token is")
            print(token)
            finalTxt = finalTxt + str(token) + " "
        elif (token.pos_ == 'ADP'):
            print("Value of token is")
            print(token)
            finalTxt = finalTxt + str(token) + " "
        elif (token.pos_ == 'PUNCT'):
            print("Value of token is")
            print(token)
            finalTxt = finalTxt + str(token) + " "

        print("entered the if loop ")
        # print(token, token.pos_, token.tag_)
    print("final text is " + finalTxt)
    return finalTxt


def standup(user,projID,sprintId,userID):
    assistant_speaks("Hi " + user)
    time.sleep(1)
    assistant_speaks(MSG_WELCOME)
    welcomeAck = get_audio()
    time.sleep(1)
    print("welcomeAck printing area: ")
    print(welcomeAck)
    print("date: " + datetime.date.today().strftime("%Y%m%d"))
    while 1:
        if str(welcomeAck).lower().__contains__("proceed") or str(welcomeAck).lower().__contains__("yes"):
            while True:
                assistant_speaks(user + MSG_YESTERDAY)
                yesterdayTask = get_audio()
                if yesterdayTask:
                    summarizedYesterdayTask = summerarize(yesterdayTask)
                    print("value of yesterdayTask is ")
                    print(summarizedYesterdayTask)
                    data = {'meetingDate': datetime.date.today().strftime("%Y%m%d"),
                            'meetingMinutes': summarizedYesterdayTask,
                            'projectId': projID,
                            'sprintId': sprintId,
                            'userId': userID}
                    r = requests.post(url='http://localhost:8181/machi/standup/addMeetingNotesPast', data=data)
                    print("yesterdayTask: " + yesterdayTask)
                    break

                else:
                    time.sleep(1)
                    assistant_speaks(user + MSG_ARE_YOU_THERE)

                    yesterdayTask = get_audio()
                    time.sleep(1)
                    if not yesterdayTask:
                        assistant_speaks(user + MSG_EXIT)
                        return

            while True:
                assistant_speaks(user + MSG_FUTURE)
                futureTask = get_audio()
                if futureTask:
                    data = {'meetingDate': datetime.date.today().strftime("%Y%m%d"),
                            'meetingMinutes': futureTask,
                            'projectId': projID,
                            'sprintId': sprintId,
                            'userId': userID}
                    r = requests.post(url='http://localhost:8181/machi/standup/addMeetingNotesNext', data=data)
                    print("futureTask: " + futureTask)
                    break
                else:
                    time.sleep(1)
                    assistant_speaks(user + MSG_ARE_YOU_THERE)

                    futureTask = get_audio()
                    time.sleep(1)
                    if not futureTask:
                        assistant_speaks(user + MSG_EXIT)
                        return

            while True:
                assistant_speaks(MSG_IMPEDIMENTS)
                impediment = get_audio()
                print("message is " + impediment)
                if impediment:
                    data = {'meetingDate': datetime.date.today().strftime("%Y%m%d"),
                            'meetingMinutes': impediment,
                            'projectId': projID,
                            'sprintId': sprintId,
                            'userId': userID}
                    r = requests.post(url='http://localhost:8181/machi/standup/addMeetingNotesImpediment', data=data)
                    print("impediment: " + impediment)

                    #Sending meeting minutes
                    data = {'projectId': projID}
                    r = requests.post(url='http://localhost:8181/machi/standup/sendMeetingMinutesEmail', data=data)
                    break
                else:
                    time.sleep(1)
                    assistant_speaks(user + MSG_ARE_YOU_THERE)

                    impediment = get_audio()
                    time.sleep(1)
                    if not impediment:
                        assistant_speaks(MSG_ANYTHING_ELSE)
                        while True:
                            otherMessage = get_audio()

                            if not otherMessage:
                                assistant_speaks(user + MSG_EXIT)
                                return
                            # message sent to server
                            s.send(otherMessage.encode('ascii'))

                            # messaga received from servery
                            data = s.recv(1024)
                            assistant_speaks(str(data)[1:])

                            # print the received message
                            # here it would be a reverse of sent message
                            print('Received from the server :', str(data.decode('ascii')))
            time.sleep(1)

            while True:
                assistant_speaks(MSG_ANYTHING_ELSE)
                otherMessage = get_audio()
                if not otherMessage:
                    assistant_speaks(user + MSG_ARE_YOU_THERE)

                    otherMessage = get_audio()
                    time.sleep(1)
                    if not otherMessage:
                        assistant_speaks(user + MSG_EXIT)
                        return
                if otherMessage.lower().__contains__("no") or otherMessage.lower().__contains__(
                        "that's it") or otherMessage.lower().__contains__("that is it") or otherMessage.lower().__contains__("thank you"):
                    assistant_speaks(MSG_CLOSE)
                    return
                # message sent to server
                s.send(otherMessage.encode('ascii'))

                # messaga received from servery
                data = s.recv(1024)
                if str(data)[1:].lower().__contains__("bye"):
                    return
                else:
                   assistant_speaks(str(data)[1:])
                   print('Received from the server :', str(data.decode('ascii')))

                # print the received message
                # here it would be a reverse of sent message

        else:
            print ("enter the final else condition ")
            print ("valu of welcomeAck is "+str(welcomeAck))

            if not welcomeAck:
                assistant_speaks(user + MSG_ARE_YOU_THERE)

                welcomeAck = get_audio()
                time.sleep(1)
                if not welcomeAck:
                    assistant_speaks(user + MSG_EXIT)
                    return

            s.send(welcomeAck.encode('ascii'))
            s.send(welcomeAck.encode('ascii'))

            # messaga received from servery
            data = s.recv(1024)
            print ("data received from server "+str(data))
            assistant_speaks(str(data)[1:])

            assistant_speaks(user + MSG_PROCEED)
            welcomeAck = get_audio()


def sprintPlanning(user,projID,userID,sprintName):
    assistant_speaks("Hi " + user)
    time.sleep(1)
    assistant_speaks(MSG_WELCOME_PLANNING)
    welcomeRes = get_audio()
    while 1:
        if welcomeRes:
            assistant_speaks(MSG_INTRO_PLANNING)
            time.sleep(1)

            while 1:
                assistant_speaks(user + MSG_WENT_WELL)
                wentWell = get_audio()
                time.sleep(1)
                if wentWell:
                    data = {'wentWell': wentWell,
                            'projectId': projID,
                            'sprintName': sprintName,
                            'userId': userID}
                    r = requests.post(url='http://localhost:8181/machi/sprintretro/addWhatWentWell', data=data)
                    break
                else:
                    assistant_speaks(user + MSG_ARE_YOU_THERE)

                    wentWell = get_audio()
                    time.sleep(1)
                    if not wentWell:
                        assistant_speaks(user + MSG_EXIT)
                        return

            while 1:
                assistant_speaks(MSG_DINDT_GO_WELL)
                didNotGoWell = get_audio()
                time.sleep(1)
                if didNotGoWell:
                    data = {'didNotGoWell': didNotGoWell,
                            'projectId': projID,
                            'sprintName': sprintName,
                            'userId': userID}
                    r = requests.post(url='http://localhost:8181/machi/sprintretro/addWhatDidNotGoWell', data=data)
                    break
                else:
                    assistant_speaks(MSG_ARE_YOU_THERE)

                    didNotGoWell = get_audio()
                    time.sleep(1)
                    if not didNotGoWell:
                        assistant_speaks(user + MSG_EXIT)
                        return

            while 1:
                assistant_speaks(MSG_LEARNED)
                learned = get_audio()
                time.sleep(1)
                if learned:
                    data = {'learned': learned,
                            'projectId': projID,
                            'sprintName': sprintName,
                            'userId': userID}
                    r = requests.post(url='http://localhost:8181/machi/sprintretro/addWhatLearned', data=data)
                    break
                else:
                    assistant_speaks(user + MSG_ARE_YOU_THERE)

                    learned = get_audio()
                    time.sleep(1)
                    if not learned:
                        assistant_speaks(user + MSG_EXIT)
                        return

            while 1:
                assistant_speaks(MSG_PUZZLE)
                puzzle = get_audio()
                time.sleep(1)
                if puzzle:
                    data = {'puzzle': puzzle,
                            'projectId': projID,
                            'sprintName': sprintName,
                            'userId': userID}
                    r = requests.post(url='http://localhost:8181/machi/sprintretro/addWhatPuzzles', data=data)
                    assistant_speaks(user + MSG_END_PLANNING)
                    return
                else:
                    assistant_speaks(user + MSG_ARE_YOU_THERE)

                    puzzle = get_audio()
                    time.sleep(1)
                    if not puzzle:
                        assistant_speaks(user + MSG_EXIT)
                        return

        else:
            assistant_speaks(user + MSG_ARE_YOU_THERE)

            welcomeRes = get_audio()
            time.sleep(1)
            if not welcomeRes:
                assistant_speaks(user + MSG_EXIT)
                return



def backlogGrooming(user, userID, backlogID, taskNo):
    assistant_speaks("Hi " + user)
    time.sleep(1)
    assistant_speaks(MSG_WELCOME_GROOMING)
    welcomeRes = get_audio()

    while 1:
        if welcomeRes:
            while 1:
                assistant_speaks(MSG_WHICH_USER + taskNo)
                whichUser = get_audio()
                time.sleep(1)
                if whichUser:
                    data = {'userId': userID,
                            'id': backlogID}
                    r = requests.post(url='http://localhost:8181/machi/backlog/updateUser', data=data)
                    break
                else:
                    assistant_speaks(user + MSG_ARE_YOU_THERE)

                    whichUser = get_audio()
                    time.sleep(1)
                    if not whichUser:
                        assistant_speaks(user + MSG_EXIT)
                        return

            while 1:
                assistant_speaks(MSG_ETA + taskNo)
                eta = get_audio()
                time.sleep(1)
                if eta:
                    data = {'eta': eta,
                            'id': backlogID}
                    r = requests.post(url='http://localhost:8181/machi/backlog/updateUser', data=data)
                    assistant_speaks(user + " , that is it for now. " + MSG_CLOSE)
                    return
                else:
                    assistant_speaks(user + MSG_ARE_YOU_THERE)

                    eta = get_audio()
                    time.sleep(1)
                    if not eta:
                        assistant_speaks(user + MSG_EXIT)
                        return

        else:
            assistant_speaks(user + MSG_ARE_YOU_THERE)

            welcomeRes = get_audio()
            time.sleep(1)
            if not welcomeRes:
                assistant_speaks(user + MSG_EXIT)
                return
def scrumCoach(user, userID):
    print("userid")
    print(userID)
    assistant_speaks("Hi " + user)
    assistant_speaks(MSG_WELCOME_COACH)
    while 1:
        userRequest = get_audio()
        if userRequest.__contains__("rating"):
            assistant_speaks(user + MSG_CHECKING_WAIT)
            data = {'userId': userID}
            r = requests.get(url='http://localhost:8181/machi/metrics/getMetricsForUser?userId=' + userID)
            print(r.content)
            score=r.json()['userScorePercentage']
            print(score)
            assistant_speaks(MSG_RATING + str(score))
            if score > 100:
                assistant_speaks(MSG_BURDEN)
                assistant_speaks(MSG_ANYTHING_ELSE)
                time.sleep(1)
                assistant_speaks(MSG_CLOSE_GENERAL_COACH)
                return
            else:
                assistant_speaks(MSG_DID_NOT_MEET_EXPECT)
                userRequest = get_audio()
                if userRequest.__contains__("no"):
                    assistant_speaks(MSG_CLOSE_COACH)
                    return
                assistant_speaks(MSG_TRAINING)
        else:
            s.send(userRequest.encode('ascii'))

            # messaga received from servery
            data = s.recv(1024)
            assistant_speaks(str(data)[1:])
            time.sleep(1)
            assistant_speaks(MSG_ANYTHING_ELSE)
            userRequest = get_audio()
            if not userRequest or not userRequest.__contains__("yes"):
                assistant_speaks(MSG_CLOSE_GENERAL_COACH)
                return
            assistant_speaks(MSG_TRAINING)


def assistant_speaks(output):
    global num

    # num to rename every audio file
    # with different name to remove ambiguity
    num += 1
    print("MACHI: ", output)

    toSpeak = gTTS(text=output, lang='en', slow=False)
    # saving the audio file given by google text to speech
    file = str(num) + ".mp3"
    toSpeak.save(file)

    # playsound package is used to play the same file.
    playsound.playsound(file, True)
    os.remove(file)


def get_audio():
    r = sr.Recognizer()

    speech = sr.Microphone(device_index=0)

    # for recognizing speech
    with speech as source:
        print("say something!…")
        audio = r.adjust_for_ambient_noise(source)
        audio = r.listen(source)
    # Speech recognition using Google Speech Recognition
    try:
        recog = r.recognize_google(audio, language='en-US')
        # for testing purposes, we're just using the default API key
        # to use another API key, use r.recognize_google(audio)
        # instead of r.recognize_google(audio)
        print("User: " + recog)
        return recog
    except:
        return ""


@app.route('/dailysprint/<user>', methods=['GET'])
def dailysprint(user):
    print("value of daily sprint user")
    print(user)
    userID = request.args.get('userID', None)
    projID = request.args.get('projID', None)
    sprintId = request.args.get('sprintId', None)
    print("value of projID")
    print(projID)
    print("value of sprintId")
    print(sprintId)
    standup(user,projID,sprintId,userID)


@app.route('/backloggrooming/<user>', methods=['GET'])
def backloggrooming(user):
    print("value of backloggromming user")
    userID = request.args.get('userID', None)
    backlogID = request.args.get('backlogID', None)
    taskNo = request.args.get('taskNo', "Task 11")
    print("value of backlogID: ")
    print(backlogID)
    print("value of taskNo: ")
    print(taskNo)

    backlogGrooming(user, userID, backlogID, taskNo)

@app.route('/sprintplanning/<user>', methods=['GET'])
def sprintplanning(user):
    print("value of sprintplanning user")
    userID = request.args.get('userID', None)
    projID = request.args.get('projID', None)
    sprintName = request.args.get('currentSprint', None)
    print("value of sprintName ")
    print(sprintName)

    sprintPlanning(user, projID, userID, sprintName)

@app.route('/scrumcoach/<user>', methods=['GET'])
def scrumcoach(user):
    print("value of sprintplanning user")
    userID = request.args.get('userID', None)
    print("value of userID inside scrumcoach"+userID)

    scrumCoach(user,userID)

# Driver Code
if __name__ == "__main__":
    app.run(port=3009)

