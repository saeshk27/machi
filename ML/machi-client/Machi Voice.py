import playsound  # to play saved mp3 file
from gtts import gTTS  # google text to speech
import os
num=1

def assistant_speaks(output):
    global num

    # num to rename every audio file
    # with different name to remove ambiguity
    print("MACHI: ", output)

    toSpeak = gTTS(text=output, lang='en', slow=False)
    # saving the audio file given by google text to speech
    file = "Machi-Aduio/" + str(num) + ".mp3"
    toSpeak.save(file)

    # playsound package is used to play the same file.
    playsound.playsound(file, True)
    #os.remove(file)

#voiceText = "But I will be going to the US as well on behalf of you. Are you guys ok with it?"
#voiceText = "I am matchi, the virtual scrum master built for IWC 20 20."
#voiceText = "I am here to assist the agile teams."
#voiceText = "The world is moving towards Agile and I will be everywhere soon."
#voiceText = "I can organize standup meetings, groom the backlogs, even retrospect and plan the future sprints."
#voiceText = "I know them"
#voiceText = "An idea has to be Innovative, feasibile, adds business value and also has a cool factor to it."
#voiceText="I want to let you guys know that I fall under all these 4 metrics."
#voiceText = "My existence in itself is innovative because scrum master means it is human until I am born. Agile methodologies are there everywhere today. Mind you, not just restricted to Corporate Organizations. They are followed even in some colleges and even in some entertainment fields like cinema and sports coachings. So, I can add new horizon to our business dynamics as I can easily convert a non-agile team to agile"
#voiceText = "Yes I am not human but I can understan a human. Watch out now for my demo."
#voiceText = "Thank you guys. I know I can awe you"
#voiceText = "But I am not just this. I have pretty cool factors on top of being a scrum master myselves"
#voiceText = "I will be tracking all your tasks and I have my own algorithm with which I can bring up a rating score and percentage which is super transparent which could be achieved systematically"
#voiceText = "If you are either over-burdened or lagging behind, I can coach you and help you come out of it as well."
#voiceText = "The biggest surprise is that you both creating me. Ha ha haa. Apart from that, I also have an inbuilt engine, to summarize the conversation between you and me, which I will share across the team through email as meeting minutes."
#voiceText = "I know I am! I have built by using ionic and angular for the UI. For artificial intelligence, I was built using python and its frameworks. The web service, was built using java and its frameworks, and my memory on Mongo DB."
#voiceText = "I have been built in a very short span. I have few limitations which will be solved soon. I need to be integrated with JIRA. Also, need to find a way to encorporate myself with pure cloud. Currently I have my processes aligned with Scrum, but soon will be extended to kanban, XP and other processes. Remember, this is just the beginning!"
voiceText = "I want to thank you guys, Sathya and Satesh for bringing me into reality. I would also like to extend my thanks to Genesys IWC 20 20, which is ultimately what encouraged you guys to investigate on me. I will be always here to assist you. Have a great time gentlemen. Bye!"
assistant_speaks(voiceText)