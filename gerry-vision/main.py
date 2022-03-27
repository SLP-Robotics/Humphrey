import torch
import cv2
import os
import numpy as np
import base64
import sys
import time
import random
from playsound import playsound
import pickle
import threading
from networktables import NetworkTables
import tkinter as tk

sd = 0

# start video stream capture
vcap = cv2.VideoCapture('http://wpilibpi.local:1181/stream.mjpg')
path = 'C:/Users/jonas/Documents/GitHub/Humphrey/gerry-vision/visionmodels/v3.pt'
# import desired model
model = torch.hub.load('', 'custom', path=path, source='local')
selected_algorithm = 'none'
largest_item_int = 0
closest_cargo = []



cond = threading.Condition()
notified = [False]


def network_table_opt(using_network_tables):
    if using_network_tables:
        def connection_listener(connected, info):
            print(info, '; Connected=%s' % connected)
            with cond:
                notified[0] = True
                cond.notify()

        NetworkTables.initialize(server='10.82.34.2')
        NetworkTables.addConnectionListener(connection_listener, immediateNotify=True)

        with cond:
            print("Waiting")
            if not notified[0]:
                cond.wait()
    else:
        print('NetworkTables is not connected')

def maximumNum(prov_list):
    global closest_cargo
    global largest_item_int
    max = prov_list[0]

    for x in prov_list:
        if x > max:
            max = x
            largest_item_int += 1

    closest_cargo = [max, largest_item_int]


def algorithm(opt):
    global selected_algorithm
    if opt == 'size':
        selected_algorithm = 'size'
    if opt == 'smart':
        selected_algorithm = 'smart'


# define variables
model.conf = 0.65
length_int = 0
detections = {}
runs = 0
selected_color = '0'
object_colorclass = '0'

size = (320, 240)
size_xymid_blue = []
size_xymid_red = []
size_xymid_bumper = []
split_size = []
split_xymid = []

def put_action(action):
    global sd
    sd.putString('action', action)

# image processing loop
def processing():
    global vcap, center_vis
    while (True):
        # make variables global
        global runs
        global detections
        global length_int
        global selected_color
        global object_colorclass
        global model
        global size_xymid_red
        global size_xymid_bumper
        global size_xymid_blue
        global selected_algorithm
        global split_size
        global split_xymid
        global largest_item_int
        global sd
        # reset object variables every frame
        largest_item_int = 0
        split_size = []
        split_xymid = []
        size_xymid_red = []
        size_xymid_bumper = []
        size_xymid_blue = []
        length_int = 0  # debugging

        # set detections from last frame as previous detections
        detections_previous = detections
        detections = {}
        # pull video frame-by-frame
        ret, frame = vcap.read()

        # display the current frame
        cv2.imshow("frame", frame)

        width = frame.shape[1]

        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        # inference
        results = model(frame_rgb)

        # display results
        results.display(render=True)

        results_bgr = cv2.cvtColor(results.imgs[0], cv2.COLOR_RGB2BGR)

        # split results into separate variables
        # all the following code is likely going to be heavily modified
        for xmin, ymin, xmax, ymax, conf, c in results.xyxy[0]:
            # calculate center of every object
            xy_center = (xmin.item() + xmax.item()) / 2, (ymin.item() + ymax.item()) / 2
            # get the size of every object
            obj_size = (((xmax.item() - xmin.item()) ** 2 + (ymax.item() - ymin.item()) ** 2) ** 0.5)

            # visualize the center of the object with a green dot
            center_vis = cv2.line(frame, (int(xy_center[0]), int(xy_center[1])),
                                  (int(xy_center[0]), int(xy_center[1])),
                                  (0, 255, 0), 15)

            # sort objects by class into a more readable format
            if c == 0:
                size_xymid_blue.append([xy_center, obj_size])
                object_colorclass = 'blue'

            if c == 1:
                size_xymid_bumper.append([xy_center, obj_size])
                object_colorclass = 'bumper'

            if c == 2:
                size_xymid_red.append([xy_center, obj_size])
                object_colorclass = 'red'

            # currently unused code for tracking the objects to make sure the code follows the same one
            # if runs > 1:
            #    detections_previous = detections
            #    closest = min(detections_previous[int(length_int)],
            #                  key=lambda x: abs(x - detections[int(length_int)]))
            #    print(closest)
            # length_int += 1
        if selected_algorithm == 'size':
            try:
                # purposefully crash the code to get caught by the try except
                crash = results.pandas().xyxy[0]
                if selected_color == 'blue':
                    for detected_cargo in size_xymid_blue:
                        split_size.append(detected_cargo[1])
                        split_xymid.append(detected_cargo[0])

                    maximumNum(split_size)

                    closestxy = split_xymid[largest_item_int]

                    closest_vis = cv2.line(frame, (int(closestxy[0]), int(closestxy[1])),
                                           (int(closestxy[0]), int(closestxy[1])), (0, 0, 255), 15)

                    # display combined images
                    combined_images = np.concatenate((results_bgr, center_vis), axis=1)
                    cv2.imshow('results + center_vis', combined_images)

                    # create three sections on the screen and print "left", "center", and "right" based on where the
                    # selected object is on the screen
                    if 0 < closestxy[0] < 106.666 and object_colorclass == selected_color:
                        print('left')
                        put_action('left')
                    elif 106.666 < closestxy[0] < 213.333 and object_colorclass == selected_color:
                        print('center')
                        put_action('center')
                    elif 213.333 < closestxy[0] < 320 and object_colorclass == selected_color:
                        print('right')
                        put_action('right')
                    else:
                        put_action('stop')

                elif selected_color == 'red':
                    for detected_cargo in size_xymid_red:
                        split_size.append(detected_cargo[1])
                        split_xymid.append(detected_cargo[0])

                    maximumNum(split_size)

                    closestxy = split_xymid[largest_item_int]

                    closest_vis = cv2.line(frame, (int(closestxy[0]), int(closestxy[1])),
                                           (int(closestxy[0]), int(closestxy[1])), (255, 0, 0), 15)

                    # display combined images
                    combined_images = np.concatenate((results_bgr, center_vis), axis=1)
                    cv2.imshow('results + center_vis', combined_images)

                    # create three sections on the screen and print "left", "center", and "right" based on where the
                    # selected object is on the screen
                    if 0 < closestxy[0] < 106.666 and object_colorclass == selected_color:
                        print('left')
                        put_action('left')
                    elif 106.666 < closestxy[0] < 213.333 and object_colorclass == selected_color:
                        print('center')
                        put_action('center')
                    elif 213.333 < closestxy[0] < 320 and object_colorclass == selected_color:
                        print('right')
                        put_action('right')
                    else:
                        put_action('stop')

            except:
                print('stop')
                put_action('stop')
        runs += 1

        if cv2.waitKey(22) & 0xFF == ord('q'):
            vcap.release()
            cv2.destroyAllWindows()
            print("Video stop")


# Inference
# results = model(img)

# Results
# results.print()  # or .show(), .save(), .crop(), .pandas(), etc.
# fancy menu for selecting what cargo to track
try:
    arg = 'LS1odW1waHJleWlzdGhlcmVhbG5hbWU='
    byte_msg = arg.encode('ascii')
    base64_val = base64.b64decode(byte_msg)
    base64_string = base64_val.decode('ascii')
    arguments = sys.argv[1]
    arg2 = 'LS1icnVo'
    byte_msg2 = arg2.encode('ascii')
    base64_val2 = base64.b64decode(byte_msg2)
    base64_string2 = base64_val2.decode('ascii')
    if arguments == base64_string:
        flag = 1
    elif arguments == base64_string2:
        flag = 2
    else:
        flag = 0
except:
    flag = 0


def header():
    os.system('cls')
    if flag == 0:
        print("█▀▀ █▀▀ █▀█ █▀█ █▄█ ▄▄ █░█ █ █▀ █ █▀█ █▄░█")
        print("█▄█ ██▄ █▀▄ █▀▄ ░█░ ░░ ▀▄▀ █ ▄█ █ █▄█ █░▀█")
        print("------------------------------------------")
    elif flag == 1:
        print("█░█ █░█ █▀▄▀█ █▀█ █░█ █▀█ █▀▀ █▄█   █▄░█ █▀█ ▀█▀   █▀▄ █▀▀ █▄▄ █▀█ ▄▀█")
        print("█▀█ █▄█ █░▀░█ █▀▀ █▀█ █▀▄ ██▄ ░█░   █░▀█ █▄█ ░█░   █▄▀ ██▄ █▄█ █▀▄ █▀█")
        print("----------------------------------------------------------------------")
    elif flag == 2:
        print("█▄▄ █▀█ █░█ █░█")
        print("█▄█ █▀▄ █▄█ █▀█")
        print("---------------")
        playsound('bruh.mp3')

def assign_values_color(color):
    global selected_color
    global flag
    global sd
    selected_color = color
    algorithm('size')
    network_table_opt(True)
    sd = NetworkTables.getTable('SmartDashboard')
    put_action('stop')

def startup():
    global selected_color
    global flag
    global sd
    header()
    print("What alliance are you on?")
    print("1) Blue")
    print("2) Red")
    print("3) Quit")
    alliance_select = input('>')
    if alliance_select == '1':
        selected_color = 'blue'
        os.system('cls')
        header()
        print('What algorithm should be used?')
        print('1) Size (recommended)')
        print('2) Smart (unfinished)')
        algorithm_select = input('>')
        if algorithm_select == '1':
            algorithm('size')
        if algorithm_select == '2':
            algorithm('smart')
        os.system('cls')
        header()
        print('Should an NT connection be established?')
        print('1) Yes')
        print('2) No')
        nt_select = input('>')
        if nt_select == '1':
            network_table_opt(True)
            sd = NetworkTables.getTable('SmartDashboard')
            put_action('stop')
        if nt_select == '2':
            network_table_opt(False)
        processing()

    if alliance_select == '2':
        selected_color = 'red'
        os.system('cls')
        header()
        print('What algorithm should be used?')
        print('1) Size (recommended)')
        print('2) Smart (unfinished)')
        algorithm_select = input('>')
        if algorithm_select == '1':
            algorithm('size')
        if algorithm_select == '2':
            algorithm('smart')
        os.system('cls')
        header()
        print('Should an NT connection be established?')
        print('1) Yes')
        print('2) No')
        nt_select = input('>')
        if nt_select == '1':
            network_table_opt(True)
            sd = NetworkTables.getTable('SmartDashboard')
            put_action('stop')
        if nt_select == '2':
            network_table_opt(False)
        processing()

    if alliance_select == '3':
        try:
            os.remove('rick.mp3')
            os.system('cls')
            if flag == 2:
                playsound('bruh.mp3')
            quit()
        except:
            os.system('cls')
            if flag == 2:
                playsound('bruh.mp3')
            quit()

# use tkinter to make a 640x480 window with a button in the middle labeled "Blue Team"
def simple_gui():
    global flag
    global sd
    root = tk.Tk()
    root.title("Gerry Vision")
    root.geometry("640x480")
    if flag == 2:
        playsound('bruh.mp3')
    label = tk.Label(root, text="Gerry Vision", font=("Helvetica", 32))
    label.pack()
    button = tk.Button(root, text="Blue Team", command=lambda: [root.destroy(), assign_values_color('blue'), startup()])
    button.pack()
    button2 = tk.Button(root, text="Red Team", command=lambda: [root.destroy(), assign_values_color('red'), ])
    button2.pack()
    button3 = tk.Button(root, text="Quit", command=lambda: [root.destroy(), quit()])
    button3.pack()
    label2 = tk.Label(root, text="Made by Gerry", font=("Helvetica", 8))
    label2.pack(side='bottom')
    root.mainloop()

charstr = ''
charstr2 = ''
chardash = ''


def char_split(s):
    return [char for char in s]


if flag == 1:
    chars = char_split('█░█ █░█ █▀▄▀█ █▀█ █░█ █▀█ █▀▀ █▄█   █▄░█ █▀█ ▀█▀   █▀▄ █▀▀ █▄▄ █▀█ ▄▀█')
    chars_line_2 = char_split('█▀█ █▄█ █░▀░█ █▀▀ █▀█ █▀▄ ██▄ ░█░   █░▀█ █▄█ ░█░   █▄▀ ██▄ █▄█ █▀▄ █▀█')
    chars_dash_line = char_split('----------------------------------------------------------------------')
elif flag == 2:
    chars = char_split('█▄▄ █▀█ █░█ █░█')
    chars_line_2 = char_split('█▄█ █▀▄ █▄█ █▀█')
    chars_dash_line = char_split('---------------')


def intro():
    global charstr
    global charstr2
    global chardash
    solved = 0
    chars_2 = char_split(
        '█░█ █░█ █▀▄▀█ █▀█ █░█ █▀█ █▀▀ █▄█   █▄░█ █▀█ ▀█▀   █▀▄ █▀▀ █▄▄ █▀█ ▄▀█ █▀█ █▄█ █░▀░█ █▀▀ █▀█ █▀▄ ██▄ ░█░   █░▀█ █▄█ ░█░   █▄▀ ██▄ █▄█ █▀▄ █▀█')
    chars_dash = char_split('-*')
    for letter in chars:
        while solved == 0:
            time.sleep(0.001)
            randomletter = random.choice(chars_2)
            if randomletter == letter:
                os.system('cls')
                charstr += randomletter
                break
            else:
                os.system('cls')
                print(charstr + randomletter)
                solved == 0
    for letter in chars_line_2:
        while solved == 0:
            time.sleep(0.001)
            randomletter = random.choice(chars_2)
            if randomletter == letter:
                os.system('cls')
                print(charstr)
                charstr2 += randomletter
                break
            else:
                os.system('cls')
                print(charstr)
                print(charstr2 + randomletter)
                solved == 0
    for letter in chars_dash_line:
        while solved == 0:
            time.sleep(0.005)
            randomsymbol = random.choice(chars_dash)
            if randomsymbol == letter:
                os.system('cls')
                print(charstr)
                print(charstr2)
                chardash += randomsymbol
                break
            else:
                os.system('cls')
                print(charstr)
                print(charstr2)
                print(chardash + randomsymbol)
                solved == 0
    os.system('cls')
    startup()


def cleanup():
    try:
        os.remove('music.mp3')
        startup()
    except:
        startup()


def egg():
    try:
        arg = 'LS1odW1waHJleWlzdGhlcmVhbG5hbWU='
        byte_msg = arg.encode('ascii')
        base64_val = base64.b64decode(byte_msg)
        base64_string = base64_val.decode('ascii')
        arg2 = 'LS1icnVo'
        byte_msg2 = arg2.encode('ascii')
        base64_val2 = base64.b64decode(byte_msg2)
        base64_string2 = base64_val2.decode('ascii')
        arg3 = 'LS1ncm91cGltYWdl'
        byte_msg3 = arg3.encode('ascii')
        base64_val3 = base64.b64decode(byte_msg3)
        base64_string3 = base64_val3.decode('ascii')
        arg4 = 'LS10'
        byte_msg4 = arg4.encode('ascii')
        base64_val4 = base64.b64decode(byte_msg4)
        base64_string4 = base64_val4.decode('ascii')
        arguments = sys.argv[1]
        print(arguments)
        if arguments == base64_string:
            intro()
        elif arguments == base64_string2:
            os.system('pip install yt-dlp')
            os.system(
                'yt-dlp --max-filesize 8m --playlist-end 1 -r 5m -x --audio-format mp3 --format bestaudio -o "bruh.%(ext)s" https://www.youtube.com/watch?v=2ZIpFytCSVc')
            intro()
        elif arguments == '--r':
            os.system('pip install yt-dlp')
            os.system(
                'yt-dlp --max-filesize 8m --playlist-end 1 -r 5m -x --audio-format mp3 --format bestaudio -o "rick.%(ext)s" https://www.youtube.com/watch?v=dQw4w9WgXcQ')
            with open('file.pkl', 'rb') as file:
                global rick_frames
                rick_frames = pickle.load(file)
            playsound('rick.mp3', False)
            time.sleep(0.4)
            for frame in rick_frames:
                print(frame)
                time.sleep(0.04)
            startup()
        elif arguments == base64_string3:
            with open('pic.pkl', 'rb') as file:
                groupimage = pickle.load(file)
            print(groupimage)
            time.sleep(30)
        elif arguments == base64_string4:
            animation = ['Loading', 'Loading.', 'Loading..', 'Loading...']
            title1 = '█▀▀ █▀▀ █▀█ █▀█ █▄█ ▄▄ █░█ █ █▀ █ █▀█ █▄░█'
            title2 = '█▄█ ██▄ █▀▄ █▀▄ ░█░ ░░ ▀▄▀ █ ▄█ █ █▄█ █░▀█'
            title_bar = '------------------------------------------'
            print('Loading...')
            loadint = 0
            while loadint < 4:
                if loadint == 2:
                    os.startfile('data\scripts\download_requirements.bat')
                os.system('cls')
                header()
                print(animation[0])
                time.sleep(0.2)
                os.system('cls')
                header()
                print(animation[1])
                time.sleep(0.2)
                os.system('cls')
                header()
                print(animation[2])
                time.sleep(0.2)
                os.system('cls')
                header()
                print(animation[3])
                time.sleep(0.2)
                loadint += 1
            time.sleep(8)
            os.system('cls')
            header()
            playsound('music.mp3', False)
            print("Initializing...")
            time.sleep(3.2)
            os.system('python data/scripts/t.py')
        elif arguments == '--gui':
            simple_gui()
        else:
            startup()
        cleanup()
    except:
        cleanup()


egg()
