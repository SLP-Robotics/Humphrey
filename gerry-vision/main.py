import torch
import cv2
import os
import numpy as np
from models.common import DetectMultiBackend
from utils.datasets import IMG_FORMATS, VID_FORMATS, LoadImages, LoadStreams
from utils.general import (LOGGER, check_file, check_img_size, check_imshow, check_requirements, colorstr,
                           increment_path, non_max_suppression, print_args, scale_coords, strip_optimizer, xyxy2xywh)
from utils.plots import Annotator, colors, save_one_box
from utils.torch_utils import select_device, time_sync
from PIL import Image, ImageColor

# start video stream capture
vcap = cv2.VideoCapture('humphreyvisionscaled.mp4')
path = 'C:/Users/jonas/Documents/GitHub/Humphrey/gerry-vision/visionmodels/v3.pt'
# import desired model
model = torch.hub.load('', 'custom', path=path, source='local')

# define variables
model.conf = 0.65
length_int = 0
detections = {}
runs = 0
selected_color = '0'
object_colorclass = '0'

size = (320, 240)

# image processing loop
def processing():
    global vcap
    while (True):
        # make variables global
        global runs
        global detections
        global length_int
        global selected_color
        global object_colorclass
        global model
        # reset object count every frame
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

        length = len(results.xyxy[0])

        # get results
        if results.xyxy[0].size()[0] == 0:
            print('')
        else:
            # split results into separate variables
            # all of the following code is likely going to be heavily modified
            for xmin, ymin, xmax, ymax, conf, c in results.xyxy[0]:
                # calculate center of every object
                xy_center = (xmin.item() + xmax.item()) / 2, (ymin.item() + ymax.item()) / 2
                # visualize the center of the object with a green dot
                center_vis = cv2.line(frame, (int(xy_center[0]), int(xy_center[1])), (int(xy_center[0]), int(xy_center[1])),
                                      (0, 255, 0), 15)
                # display combined images
                combined_images = np.concatenate((results_bgr, center_vis), axis=1)
                cv2.imshow('results + center_vis', combined_images)

                # sort objects by class into a more readable format
                if c == 0:
                    object_colorclass = 'blue'

                if c == 1:
                    object_colorclass = 'bumper'

                if c == 2:
                    object_colorclass = 'red'

                # create three sections on the screen and print "left", "center", and "right" based on where the selected object is on the screen
                if xy_center[0] > 0 and xy_center[0] < 80 and object_colorclass == selected_color:
                    print('left')
                elif xy_center[0] > 80 and xy_center[0] < 160 and object_colorclass == selected_color:
                    print('center')
                elif xy_center[0] > 160 and xy_center[0] < 240 and object_colorclass == selected_color:
                    print('right')

                # currently unused code for tracking the objects to make sure the code follows the same one
                #if runs > 1:
                #    detections_previous = detections
                #    closest = min(detections_previous[int(length_int)],
                #                  key=lambda x: abs(x - detections[int(length_int)]))
                #    print(closest)
                #length_int += 1
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
def startup():
    global selected_color
    os.system('cls')
    print("█▀▀ █▀▀ █▀█ █▀█ █▄█ ▄▄ █░█ █ █▀ █ █▀█ █▄░█")
    print("█▄█ ██▄ █▀▄ █▀▄ ░█░ ░░ ▀▄▀ █ ▄█ █ █▄█ █░▀█")
    print("------------------------------------------")
    print("What alliance are you on?")
    print("1) Blue")
    print("2) Red")
    print("3) Quit")
    alliance_select = input('>')
    if alliance_select == '1':
        selected_color = 'blue'
        os.system('cls')
        processing()

    if alliance_select == '2':
        selected_color = 'red'
        os.system('cls')
        processing()

    if alliance_select == '3':
        os.system('cls')
        quit()

startup()
