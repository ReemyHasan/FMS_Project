import joblib
import numpy as np
import pandas as pd
from flask import Flask, request
import py_eureka_client.eureka_client as eureka_client
from flask_cors import cross_origin
from sklearn.ensemble import GradientBoostingClassifier
from elasticsearch import Elasticsearch, helpers
from pandas import json_normalize
import atexit
from sklearn.metrics import cohen_kappa_score, accuracy_score, f1_score, recall_score, confusion_matrix
def cleanup():
    eureka_client.stop()
    print("Cleaning up resources...")
atexit.register(cleanup)
rest_port = 8050
eureka_client.init(
           eureka_server="http://192.168.27.227:8761/eureka",
           app_name="ML-MODEL-SERVICE",
           instance_host="192.168.24.47",
           instance_port=8050)
es = Elasticsearch("http://172.29.3.220:9200")
order = ['ICMP_ping', 'ICMP_loss', 'ICMP_response_time',
       'Device_uptime', 'SNMP_availability', 'range',
       'EX_Inbound_packets_discarded', 'IN_Inbound_packets_discarded',
       'P_Inbound_packets_discarded', 'EX_Outbound_packets_discarded',
       'IN_Outbound_packets_discarded', 'P_Outbound_packets_discarded',
       'EX_Inbound_packets_with_errors', 'IN_Inbound_packets_with_errors',
       'P_Inbound_packets_with_errors', 'EX_Outbound_packets_with_errors',
       'IN_Outbound_packets_with_errors', 'P_Outbound_packets_with_errors',
       'EX_Bits_sent', 'IN_Bits_sent', 'P_Bits_sent', 'EX_Bits_received',
       'IN_Bits_received', 'P_Bits_received', 'EX_Speed', 'IN_Speed',
       'P_Speed', 'EX_Interface_type', 'IN_Interface_type', 'P_Interface_type',
       'EX_Operational_status', 'IN_Operational_status',
       'P_Operational_status']

def getAllfromIndex(index_name):
    query = {"query": {"match_all": {}}}
    response = es.search(index=index_name, body=query, size=5)
    data = helpers.scan(es,
                        query=query,
                        index=index_name
                        )
    num = 0
    lst = []
    for doc in data:
        lst.append(doc['_source'])
        # if num == 10:
        #     break
        num += 1
        if num == 12000:
            break
    df2 = json_normalize(lst)
    return df2

filename = "my_model.joblib"
model = GradientBoostingClassifier()
# load model
model = joblib.load(filename)

app = Flask(__name__)

@app.route("/model/predict", methods=['POST'])
def predictClass():
    # time.sleep(60)
    print(request.json)
    data = request.json
    df = pd.DataFrame.from_dict(data,orient='index').T
    df = df.drop('timestamp',axis=1)
    df = df.reindex(order,axis="columns")
    response = model.predict(df)
    print(response)
    return response[0]

@app.route("/model/train", methods=['GET'])
@cross_origin()
def trainModel():
    index_name = "knowledge-base"
    df2 = getAllfromIndex(index_name)
    X = df2.drop('class', axis=1)
    X = X.drop('timestamp', axis=1)
    X = X.reindex(order,axis="columns")
    y = df2['class']
    model.fit(X,y)
    return "done"

def calcMetrics(model,X_test,y_test):
    # Initialize lists to store the metrics for each class
    f1_scores = []
    sensitivities = []
    g_means = []
    kappas = []
    accuracies = []
    y_pred = model.predict(X_test)
    # Calculate the metrics for each class
    f1_scores.append(f1_score(y_test, y_pred, average=None))
    sensitivities.append(recall_score(y_test, y_pred, average=None))
    g_means.append(
        np.sqrt(recall_score(y_test, y_pred, average=None) * recall_score(y_test, y_pred, average=None)).mean())
    kappas.append(cohen_kappa_score(y_test, y_pred))
    accuracies.append(accuracy_score(y_test, y_pred))
    # Calculate the average metrics across all folds
    avg_f1_scores = np.mean(f1_scores, axis=0)
    avg_sensitivities = np.mean(sensitivities, axis=0)
    avg_g_mean = np.mean(g_means)
    avg_kappa = np.mean(kappas)
    avg_accuracy = np.mean(accuracies)
    ret = ""
    # Print the metrics for each class
    for i, class_label in enumerate(model.classes_):
        ret += f"Metrics for class {class_label:}:" + ","
        print(f"Metrics for class {class_label}:")
        ret += f"F1-Score: {avg_f1_scores[i]:.3f}" + ","
        print(f"F1-Score: {avg_f1_scores[i]}:")
        ret += f"Sensitivity: {avg_sensitivities[i]:.3f}"
        ret += ","
        print(f"Sensitivity: {avg_sensitivities[i]}")
        print()

    # Print the average metrics
    ret += f"G-Mean: {avg_g_mean:.3f}" + ","
    print(f"G-Mean: {avg_g_mean}")
    ret += f"Kappa: {avg_kappa:.3f}" + ","
    print(f"Kappa: {avg_kappa}")
    ret += f"Accuracy: {avg_accuracy:.3f}" + ","
    print(f"Accuracy: {avg_accuracy}")
    labels = ["A","D","NE"]
    # Calculate confusion matrix
    cm = confusion_matrix(y_test, y_pred,labels=labels)
    # Get the number of classes
    num_classes = len(labels)
    # Create a string representation of the confusion matrix
    cm_string = "Confusion Matrix:,"
    for i in range(num_classes):
        cm_string += f"Class {labels[i]}:   "
        for j in range(num_classes):
            cm_string += f"{cm[i, j]}   "
        cm_string += ","
    # Print the confusion matrix
    print(cm_string)
    ret += "," + cm_string + ","
    return ret

@app.route("/model/metrics", methods=['GET'])
@cross_origin()
def returnMetrics():
    data = pd.read_csv('data_A1.csv')
    X = data.drop('class', axis=1)
    X = X.drop('timestamp', axis=1)
    y = data['class']
    ret = calcMetrics(model,X,y)
    return ret

@app.route("/model/accuracy", methods=['GET'])
@cross_origin()
def returnAcc():
    data = pd.read_csv('data_A1.csv')
    X = data.drop('class', axis=1)
    X = X.drop('timestamp', axis=1)
    y = data['class']
    y_pred = model.predict(X)
    accuracy = accuracy_score(y, y_pred)
    rounded_value = (round(accuracy, 3))*100
    return str(rounded_value)

if __name__ == "__main__":
    app.run(host='0.0.0.0',port=rest_port)

