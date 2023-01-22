import datetime
import time
from flask import Flask, jsonify
from flask_restful import Resource, Api
from flask_cors import CORS, cross_origin

app = Flask(__name__)
api = Api(app)
cors = CORS(app)

homePenalties = [{ "playerNo": 4, "duration": str(datetime.timedelta(seconds=93))}, {"playerNo": 87, "duration": str(datetime.timedelta(seconds=64))}]
visitorPenalties = [{"playerNo": 22, "duration": str(datetime.timedelta(seconds=37))}]

# homePenalties = []
# visitorPenalties = []

@app.route("/scoreboardData")
def countdown():
    event_time = datetime.datetime(2023, 1, 22, 8, 37, 0)
    current_time = datetime.datetime.now()
    time = (event_time - current_time)
    time = time.total_seconds() 
    period = 1
    homeScore = 2
    visitorScore = 1
    timeout = 40000
    time = str(datetime.timedelta(seconds=time))
    timeout = str(datetime.timedelta(milliseconds=timeout))


    return {'time': time, 'period': period, 'homeScore': homeScore, 'visitorScore': visitorScore, 'timeout': None, "homePenalties": homePenalties, "visitorPenalties": visitorPenalties }

if __name__ == '__main__':
    app.run(debug=True)
