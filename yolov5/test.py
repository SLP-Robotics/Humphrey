import cv2

vcap = cv2.VideoCapture('http://wpilibpi.local:1181/stream.mjpg')

while(True):
    ret, frame = vcap.read()

    cv2.imshow('', frame)

    if cv2.waitKey(22) & 0xFF == ord('q'):
        vcap.release()
        cv2.destroyAllWindows()
        print("Video stop")
