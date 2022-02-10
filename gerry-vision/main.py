import torch
import cv2
from models.common import DetectMultiBackend
from utils.datasets import IMG_FORMATS, VID_FORMATS, LoadImages, LoadStreams
from utils.general import (LOGGER, check_file, check_img_size, check_imshow, check_requirements, colorstr,
                           increment_path, non_max_suppression, print_args, scale_coords, strip_optimizer, xyxy2xywh)
from utils.plots import Annotator, colors, save_one_box
from utils.torch_utils import select_device, time_sync
from PIL import Image, ImageColor

# start video stream capture
vcap = cv2.VideoCapture(0)

# import desired model
model = torch.hub.load('', 'custom', path='C:/Users/jonas/yolov5/runs/train/exp10/weights/best.pt', source='local')
model.conf = 0.6
def processing():
    while (True):
        global model
        # pull video frame-by-frame
        ret, frame = vcap.read()

        # display the current frame
        cv2.imshow("frame", frame)
        frame_rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
        # inference
        results = model(frame_rgb)

        # display results
        results.display(render=True)

        results_bgr = cv2.cvtColor(results.imgs[0], cv2.COLOR_RGB2BGR)

        cv2.imshow('', results_bgr)

        length = len(results.xyxy[0])

        # get results
        if results.xyxy[0].size()[0] == 0:
            print('no results')
        else:
            detections = {}
            # split results into separate variables
            for xmin, ymin, xmax, ymax, conf, c in results.xyxy[0]:
                xycenter = (xmin.item() + xmax.item())/2, (ymin.item() + ymax.item())/2
                centervis = cv2.line(frame, (int(xycenter[0]), int(xycenter[1])), (int(xycenter[0]), int(xycenter[1])), (0, 255, 0), 15)
                cv2.imshow('vis', centervis)

                detections.update({'class' : c})


        #except:
        #    print('no results')

        if cv2.waitKey(22) & 0xFF == ord('q'):
            vcap.release()
            cv2.destroyAllWindows()
            print("Video stop")

# Inference
# results = model(img)

# Results
# results.print()  # or .show(), .save(), .crop(), .pandas(), etc.

processing()
